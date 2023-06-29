package net.minecraft.client.network;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ServerPingData;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerPinger {
    public static final Executor EXECUTOR = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, "Server Ping Thread"));
    private static final Splitter PING_RESPONSE_SPLITTER = Splitter.on('\u0000').limit(6);
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<NetworkManager> pingDestinations = Collections.synchronizedList(Lists.newArrayList());

    public void ping(ServerData server) throws UnknownHostException {
        ServerAddress addr = ServerAddress.fromString(server.ip);
        NetworkManager manager = NetworkManager.remote(InetAddress.getByName(addr.getIP()), addr.getPort());
        pingDestinations.add(manager);
        server.motd = I18n.format("multiplayer.status.pinging");
        server.pingToServer = -1L;
        server.players = null;
        manager.setNetHandler(new INetHandlerStatusClient() {
            private boolean successful;
            private boolean receivedStatus;
            private long pingSent;
            @Override
            public void handleServerInfo(SPacketServerInfo packetIn) {
                if (receivedStatus) {
                    manager.closeChannel(new TranslatableComponent("multiplayer.status.unrequested"));
                    return;
                }
                receivedStatus = true;
                ServerPingData ping = packetIn.getResponse();
                server.motd = ping.getDescription() != null ? ping.getDescription().asFormattedString() : "";
                if (ping.getVersion() != null) {
                    server.version = ping.getVersion().getVersion();
                    server.protocol = ping.getVersion().getProtocol();
                } else {
                    server.version = I18n.format("multiplayer.status.old");
                    server.protocol = 0;
                }
                if (ping.getPlayers() != null) {
                    server.onlineMax = Formatting.GRAY.toString() + ping.getPlayers().getOnline() + Formatting.DARK_GRAY + "/" + Formatting.GRAY + ping.getPlayers().getMax();
                    if (ping.getPlayers().getPlayers() != null && ping.getPlayers().getPlayers().length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (GameProfile gameprofile : ping.getPlayers().getPlayers()) {
                            if (sb.length() > 0) sb.append("\n");
                            sb.append(gameprofile.getName());
                        }
                        if (ping.getPlayers().getPlayers().length < ping.getPlayers().getOnline()) {
                            if (sb.length() > 0) sb.append("\n");
                            sb.append(I18n.format("multiplayer.status.and_more", ping.getPlayers().getOnline() - ping.getPlayers().getPlayers().length));
                        }
                        server.players = sb.toString();
                    }
                } else {
                    server.onlineMax = Formatting.DARK_GRAY + I18n.format("multiplayer.status.unknown");
                }
                if (ping.getFavicon() != null) {
                    String s = ping.getFavicon();
                    if (s.startsWith("data:image/png;base64,")) {
                        server.setFavicon(s.substring("data:image/png;base64,".length()));
                    }
                } else {
                    server.setFavicon(null);
                }
                pingSent = Minecraft.getSystemTime();
                manager.sendPacket(new CPacketPing(pingSent));
                successful = true;
            }

            @Override
            public void handlePong(SPacketPong packetIn) {
                server.pingToServer = Minecraft.getSystemTime() - pingSent;
                manager.closeChannel(new TextComponent("Finished"));
            }

            @Override
            public void onDisconnect(Component reason) {
                if (!successful) {
                    LOGGER.error("Can't ping {}: {}", server.ip, reason.asString());
                    server.motd = Formatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect");
                    server.onlineMax = "";
                    tryCompatibilityPing(server);
                }
            }
        });
        try {
            manager.sendPacket(new C00Handshake(addr.getIP(), addr.getPort(), EnumConnectionState.STATUS));
            manager.sendPacket(new CPacketServerQuery());
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
        }
    }

    private void tryCompatibilityPing(ServerData server) {
        ServerAddress serveraddress = ServerAddress.fromString(server.ip);
        new Bootstrap().group(NetworkManager.CLIENT_NIO_EVENTLOOP.getValue()).handler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel channel) {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (Exception ignored) {}
                channel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        super.channelActive(ctx);
                        ByteBuf buf = Unpooled.buffer();
                        try {
                            buf.writeByte(254);
                            buf.writeByte(1);
                            buf.writeByte(250);
                            char[] achar = "MC|PingHost".toCharArray();
                            buf.writeShort(achar.length);
                            for (char c0 : achar) {
                                buf.writeChar(c0);
                            }
                            buf.writeShort(7 + 2 * serveraddress.getIP().length());
                            buf.writeByte(127);
                            achar = serveraddress.getIP().toCharArray();
                            buf.writeShort(achar.length);
                            for (char c1 : achar) {
                                buf.writeChar(c1);
                            }
                            buf.writeInt(serveraddress.getPort());
                            ctx.channel().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        } finally {
                            buf.release();
                        }
                    }

                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
                        short short1 = buf.readUnsignedByte();
                        if (short1 == 255) {
                            String s = new String(buf.readBytes(buf.readShort() * 2).array(), StandardCharsets.UTF_16BE);
                            String[] astring = Iterables.toArray(PING_RESPONSE_SPLITTER.split(s), String.class);
                            if ("\u00a71".equals(astring[0])) {
                                int i = MathHelper.getInt(astring[1], 0);
                                String s1 = astring[2];
                                String s2 = astring[3];
                                int j = MathHelper.getInt(astring[4], -1);
                                int k = MathHelper.getInt(astring[5], -1);
                                server.protocol = -1;
                                server.version = s1;
                                server.motd = s2;
                                server.onlineMax = Formatting.GRAY + "" + j + "" + Formatting.DARK_GRAY + "/" + Formatting.GRAY + k;
                            }
                        }
                        ctx.close();
                    }

                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex) {
                        ctx.close();
                    }
                });
            }
        }).channel(NioSocketChannel.class).connect(serveraddress.getIP(), serveraddress.getPort());
    }

    public void pingPendingNetworks() {
        synchronized (pingDestinations) {
            Iterator<NetworkManager> iterator = pingDestinations.iterator();

            while (iterator.hasNext()) {
                NetworkManager networkmanager = iterator.next();

                if (networkmanager.isChannelOpen()) {
                    networkmanager.processReceivedPackets();
                } else {
                    iterator.remove();
                    networkmanager.checkDisconnected();
                }
            }
        }
    }

    public void clearPendingNetworks() {
        synchronized (pingDestinations) {
            Iterator<NetworkManager> iterator = pingDestinations.iterator();

            while (iterator.hasNext()) {
                NetworkManager networkmanager = iterator.next();

                if (networkmanager.isChannelOpen()) {
                    iterator.remove();
                    networkmanager.closeChannel(new TranslatableComponent("multiplayer.status.cancelled"));
                }
            }
        }
    }
}
