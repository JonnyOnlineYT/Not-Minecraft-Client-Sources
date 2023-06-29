package net.minecraft.server.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;

public class NetHandlerStatusServer implements INetHandlerStatusServer
{
    private static final Component EXIT_MESSAGE = new TextComponent("Status request has been handled.");
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    private boolean handled;

    public NetHandlerStatusServer(MinecraftServer serverIn, NetworkManager netManager)
    {
        server = serverIn;
        networkManager = netManager;
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(Component reason)
    {
    }

    public void processServerQuery(CPacketServerQuery packetIn)
    {
        if (handled)
        {
            networkManager.closeChannel(EXIT_MESSAGE);
        }
        else
        {
            handled = true;
            networkManager.sendPacket(new SPacketServerInfo(server.getServerStatusResponse()));
        }
    }

    public void processPing(CPacketPing packetIn)
    {
        networkManager.sendPacket(new SPacketPong(packetIn.getClientTime()));
        networkManager.closeChannel(EXIT_MESSAGE);
    }
}
