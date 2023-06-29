package org.moonware.client.qol.via;

import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.bootstrap.Bootstrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import org.moonware.client.qol.via.platform.*;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MWVia {
    public static final ProtocolVersion NATIVE_PROTOCOL = ProtocolVersion.v1_12_2;
    public static final File VIA_FOLDER = new File(Minecraft.gameDir, "config/ViaVersion");
    public static final File VIA_BACKWARDS_FOLDER = new File(Minecraft.gameDir, "config/ViaBackwards");
    public static final MWViaPlatform PLATFORM = new MWViaPlatform();
    public static final MWViaBackwardsPlatform BACKWARDS_PLATFORM = new MWViaBackwardsPlatform();
    public static final MWViaPlatformLoader PLATFORM_LOADER = new MWViaPlatformLoader();
    public static final MWViaInjector INJECTOR = new MWViaInjector();
    public static final ViaManagerImpl MANAGER = ViaManagerImpl.builder().injector(INJECTOR).loader(PLATFORM_LOADER).platform(PLATFORM).build();
    public static ProtocolVersion version = ProtocolVersion.unknown;
    public static void load() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        MWViaPlatformTask.EVENT_LOOP.submit(future::join);
        VIA_FOLDER.mkdirs();
        VIA_BACKWARDS_FOLDER.mkdirs();
        MappingDataLoader.enableMappingsCache();
        Via.init(MANAGER);
        MANAGER.init();
        BACKWARDS_PLATFORM.init(VIA_BACKWARDS_FOLDER);
        future.complete(null);
    }

    public static ProtocolVersion getProtocol(InetAddress address, int port) {
        try {
            System.out.println(String.format("[MWVia] Getting %s:%s server protocol.", address, port));
            CompletableFuture<ProtocolVersion> future = new CompletableFuture<>();
            NetworkManager manager = NetworkManager.remote(address, port);
            manager.setNetHandler(new INetHandlerStatusClient() {
                @Override
                public void handleServerInfo(SPacketServerInfo packetIn) {
                    if (packetIn.getResponse().getVersion() == null) {
                        future.complete(ProtocolVersion.unknown);
                        System.err.println(String.format("[MWVia] Server %s:%s didn't sent protocol in S2CStatusResponse.", address, port));
                    } else {
                        ProtocolVersion protocol = ProtocolVersion.getProtocol(packetIn.getResponse().getVersion().getProtocol());
                        System.out.println(String.format("[MWVia] Server %s:%s protocol is %s.", address, port, protocol));
                        future.complete(protocol);
                    }
                    manager.closeChannel(new TextComponent("Finished"));
                }

                @Override
                public void handlePong(SPacketPong packetIn) {}

                @Override
                public void onDisconnect(Component reason) {
                    if (!future.isDone()) {
                        System.err.println(String.format("[MWVia] Server %s:%s closed connection without sending protocol.", address, port));
                        future.complete(ProtocolVersion.unknown);
                    }
                }
            });
            try {
                manager.sendPacket(new C00Handshake(-1, address.getHostAddress(), port, EnumConnectionState.STATUS));
                manager.sendPacket(new CPacketServerQuery());
            } catch (Exception ignored) {}
            return future.get();
        } catch (Exception ignored) {
            return ProtocolVersion.unknown;
        }
    }
}
