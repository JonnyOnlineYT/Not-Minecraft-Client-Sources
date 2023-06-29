package net.minecraft.network;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.PacketEvent;
import baritone.api.event.events.type.EventState;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.CryptManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.qol.via.MWVia;
import org.moonware.client.qol.via.handlers.CommonTransformer;
import org.moonware.client.qol.via.handlers.VRDecodeHandler;
import org.moonware.client.qol.via.handlers.VREncodeHandler;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetworkManager extends SimpleChannelInboundHandler<Packet<?>> {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Marker NETWORK_MARKER = MarkerManager.getMarker("NETWORK");
    public static final Marker NETWORK_PACKETS_MARKER = MarkerManager.getMarker("NETWORK_PACKETS", NETWORK_MARKER);
    public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>() {
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>() {
        protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
        }
    };
    public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>() {
        protected LocalEventLoopGroup load() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
        }
    };
    private final EnumPacketDirection direction;
    private final Queue<NetworkManager.InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * The active channel
     */
    private Channel channel;

    /**
     * The address of the remote party
     */
    private SocketAddress socketAddress;

    /**
     * The INetHandler instance responsible for processing received packets
     */
    private INetHandler packetListener;

    /**
     * A String indicating why the network has shutdown.
     */
    private Component terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;

    public NetworkManager(EnumPacketDirection packetDirection) {
        direction = packetDirection;
    }

    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        channel = p_channelActive_1_.channel();
        socketAddress = channel.remoteAddress();

        try {
            setConnectionState(EnumConnectionState.HANDSHAKING);
        } catch (Throwable throwable) {
            LOGGER.fatal(throwable);
        }
    }

    /**
     * Sets the new connection state and registers which packets this channel may send and receive
     */
    public void setConnectionState(EnumConnectionState newState) {
        channel.attr(PROTOCOL_ATTRIBUTE_KEY).set(newState);
        channel.config().setAutoRead(true);
        LOGGER.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {
        closeChannel(new TranslatableComponent("disconnect.endOfStream"));
    }

    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
        TranslatableComponent textcomponenttranslation;

        if (p_exceptionCaught_2_ instanceof TimeoutException) {
            textcomponenttranslation = new TranslatableComponent("disconnect.timeout");
        } else {
            textcomponenttranslation = new TranslatableComponent("disconnect.genericReason", "Internal Exception: " + p_exceptionCaught_2_);
        }

        LOGGER.debug(textcomponenttranslation.asString(), p_exceptionCaught_2_);
        closeChannel(textcomponenttranslation);
        p_exceptionCaught_2_.printStackTrace();
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> packetIn) throws Exception {

        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() != null && ibaritone.getPlayerContext().player().connection.getNetworkManager() == this) {
                ibaritone.getGameEventHandler().onReceivePacket(new PacketEvent(this, EventState.PRE, packetIn));
            }
        }

        EventReceivePacket event = new EventReceivePacket(packetIn);
        EventManager.call(event);

        if (event.isCancelled())
            return;

        if (channel.isOpen()) {
            try {
                ((Packet<INetHandler>) packetIn).processPacket(packetListener);
            } catch (ThreadQuickExitException var4) {
            }
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() != null && ibaritone.getPlayerContext().player().connection.getNetworkManager() == this) {
                ibaritone.getGameEventHandler().onReceivePacket(new PacketEvent(this, EventState.POST, packetIn));
            }
        }
    }

    /**
     * Sets the NetHandler for this NetworkManager, no checks are made if this handler is suitable for the particular
     * connection state (protocol)
     */
    public void setNetHandler(INetHandler handler) {
        Preconditions.checkNotNull(handler, "packetListener");
        LOGGER.debug("Set listener of {} to {}", this, handler);
        packetListener = handler;
    }

    public void sendPacket(Packet<?> packetIn) {
        EventSendPacket event = new EventSendPacket(packetIn);
        EventManager.call(event);

        if (event.isCancelled())
            return;

        if (isChannelOpen()) {
            flushOutboundQueue();
            dispatchPacket(packetIn, null);
        } else {
            readWriteLock.writeLock().lock();

            try {
                outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn));
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }
    }

    public void sendPacket(Packet<?> packetIn, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>>... listeners) {
        if (isChannelOpen()) {
            flushOutboundQueue();
            dispatchPacket(packetIn, ArrayUtils.add(listeners, 0, listener));
        } else {
            readWriteLock.writeLock().lock();

            try {
                outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, ArrayUtils.add(listeners, 0, listener)));
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }
    }

    /**
     * Will commit the packet to the channel. If the current thread 'owns' the channel it will write and flush the
     * packet, otherwise it will add a task for the channel eventloop thread to do that.
     */
    private void dispatchPacket(Packet<?> inPacket, @Nullable GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {

        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() != null && ibaritone.getPlayerContext().player().connection.getNetworkManager() == this) {
                ibaritone.getGameEventHandler().onSendPacket(new PacketEvent(this, EventState.PRE, inPacket));
            }
        }

        EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
        EnumConnectionState enumconnectionstate1 = channel.attr(PROTOCOL_ATTRIBUTE_KEY).get();

        if (enumconnectionstate1 != enumconnectionstate) {
            LOGGER.debug("Disabled auto read");
            channel.config().setAutoRead(false);
        }

        if (channel.eventLoop().inEventLoop()) {
            if (enumconnectionstate != enumconnectionstate1) {
                setConnectionState(enumconnectionstate);
            }

            ChannelFuture channelfuture = channel.writeAndFlush(inPacket);

            if (futureListeners != null) {
                channelfuture.addListeners(futureListeners);
            }

            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            channel.eventLoop().execute(new Runnable() {
                public void run() {
                    if (enumconnectionstate != enumconnectionstate1) {
                        setConnectionState(enumconnectionstate);
                    }

                    ChannelFuture channelfuture1 = channel.writeAndFlush(inPacket);

                    if (futureListeners != null) {
                        channelfuture1.addListeners(futureListeners);
                    }

                    channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() != null && ibaritone.getPlayerContext().player().connection.getNetworkManager() == this) {
                ibaritone.getGameEventHandler().onSendPacket(new PacketEvent(this, EventState.POST, inPacket));
            }
        }
    }

    /**
     * Will iterate through the outboundPacketQueue and dispatch all Packets
     */
    private void flushOutboundQueue() {
        if (channel != null && channel.isOpen()) {
            readWriteLock.readLock().lock();

            try {
                while (!outboundPacketsQueue.isEmpty()) {
                    NetworkManager.InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = outboundPacketsQueue.poll();
                    dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
                }
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
    }

    /**
     * Checks timeouts and processes all packets received
     */
    public void processReceivedPackets() {
        flushOutboundQueue();

        if (packetListener instanceof ITickable) {
            ((ITickable) packetListener).update();
        }

        if (channel != null) {
            channel.flush();
        }
    }

    /**
     * Returns the socket address of the remote side. Server-only.
     */
    public SocketAddress getRemoteAddress() {
        return socketAddress;
    }

    /**
     * Closes the channel, the parameter can be used for an exit message (not certain how it gets sent)
     */
    public void closeChannel(Component message) {
        if (channel.isOpen()) {
            channel.close().awaitUninterruptibly();
            terminationReason = message;
        }
    }

    /**
     * True if this NetworkManager uses a memory connection (single player game). False may imply both an active TCP
     * connection or simply no active connection at all
     */
    public boolean isLocalChannel() {
        return channel instanceof LocalChannel || channel instanceof LocalServerChannel;
    }

    /**
     * Create a new NetworkManager from the server host and connect it to the server
     */
    public static NetworkManager remote(InetAddress address, int port) {
        NetworkManager manager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        Class<? extends SocketChannel> oclass;
        LazyLoadBase<? extends EventLoopGroup> lazyloadbase;
        if (Epoll.isAvailable()) {
            oclass = EpollSocketChannel.class;
            lazyloadbase = CLIENT_EPOLL_EVENTLOOP;
        } else {
            oclass = NioSocketChannel.class;
            lazyloadbase = CLIENT_NIO_EVENTLOOP;
        }
        new Bootstrap().group(lazyloadbase.getValue()).handler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel channel) {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (Exception ignored) {}
                channel.pipeline()
                        .addLast("timeout", new ReadTimeoutHandler(30))
                        .addLast("splitter", new NettyVarint21FrameDecoder())
                        .addLast("decoder", new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND))
                        .addLast("prepender", new NettyVarint21FrameEncoder())
                        .addLast("encoder", new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND))
                        .addLast("packet_handler", manager);
                if (channel instanceof SocketChannel && MWVia.version.isKnown()) {
                    UserConnection user = new UserConnectionImpl(channel, true);
                    new ProtocolPipelineImpl(user);
                    channel.pipeline().addBefore("encoder", CommonTransformer.HANDLER_ENCODER_NAME, new VREncodeHandler(user)).addBefore("decoder", CommonTransformer.HANDLER_DECODER_NAME, new VRDecodeHandler(user));
                }
            }
        }).channel(oclass).connect(address, port).syncUninterruptibly();
        return manager;
    }

    /**
     * Prepares a clientside NetworkManager: establishes a connection to the socket supplied and configures the channel
     * pipeline. Returns the newly created instance.
     */
    public static NetworkManager local(SocketAddress address) {
        NetworkManager manager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        new Bootstrap().group(CLIENT_LOCAL_EVENTLOOP.getValue()).handler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast("packet_handler", manager);
            }
        }).channel(LocalChannel.class).connect(address).syncUninterruptibly();
        return manager;
    }

    /**
     * Adds an encoder+decoder to the channel pipeline. The parameter is the secret key used for encrypted communication
     */
    public void enableEncryption(SecretKey key) {
        isEncrypted = true;
        channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     * Returns true if this NetworkManager has an active channel, false otherwise
     */
    public boolean isChannelOpen() {
        return channel != null && channel.isOpen();
    }

    public boolean hasNoChannel() {
        return channel == null;
    }

    /**
     * Gets the current handler for processing packets
     */
    public INetHandler getNetHandler() {
        return packetListener;
    }

    /**
     * If this channel is closed, returns the exit message, null otherwise.
     */
    public Component getExitMessage() {
        return terminationReason;
    }

    /**
     * Switches the channel to manual reading modus
     */
    public void disableAutoRead() {
        channel.config().setAutoRead(false);
    }

    public void setCompressionThreshold(int threshold) {
        if (threshold >= 0) {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder) channel.pipeline().get("decompress")).setCompressionThreshold(threshold);
            } else {
                CommonTransformer.decodeEncodePlacement(channel.pipeline(), "decoder", "decompress", new NettyCompressionDecoder(threshold));

                //    this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(threshold));
            }

            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder) channel.pipeline().get("compress")).setCompressionThreshold(threshold);
            } else {
                CommonTransformer.decodeEncodePlacement(channel.pipeline(), "encoder", "compress", new NettyCompressionEncoder(threshold));

                //  this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(threshold));
            }
        } else {
            if (channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                channel.pipeline().remove("decompress");
            }

            if (channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                channel.pipeline().remove("compress");
            }
        }
    }

    public void checkDisconnected() {
        if (channel != null && !channel.isOpen()) {
            if (disconnected) {
                LOGGER.warn("handleDisconnection() called twice");
            } else {
                disconnected = true;

                if (getExitMessage() != null) {
                    getNetHandler().onDisconnect(getExitMessage());
                } else if (getNetHandler() != null) {
                    getNetHandler().onDisconnect(new TranslatableComponent("multiplayer.disconnect.generic"));
                }
            }
        }
    }

    public static class InboundHandlerTuplePacketListener {
        private final Packet<?> packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

        @SafeVarargs
        public InboundHandlerTuplePacketListener(Packet<?> inPacket, GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
            packet = inPacket;
            futureListeners = inFutureListeners;
        }
    }
}
