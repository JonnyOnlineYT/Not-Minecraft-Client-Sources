package net.minecraft.network.status.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerPingData;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Style;

public class SPacketServerInfo implements Packet<INetHandlerStatusClient>
{
    private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(ServerPingData.Version.class, new ServerPingData.Version.Serializer()).registerTypeAdapter(ServerPingData.Players.class, new ServerPingData.Players.Serializer()).registerTypeAdapter(ServerPingData.class, new ServerPingData.Serializer()).registerTypeHierarchyAdapter(Component.class, new Component.Serializer()).registerTypeHierarchyAdapter(Style.class, new Style.Serializer()).registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
    private ServerPingData response;

    public SPacketServerInfo()
    {
    }

    public SPacketServerInfo(ServerPingData responseIn)
    {
        response = responseIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        response = JsonUtils.gsonDeserialize(GSON, buf.readStringFromBuffer(32767), ServerPingData.class);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(GSON.toJson(response));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerStatusClient handler)
    {
        handler.handleServerInfo(this);
    }

    public ServerPingData getResponse()
    {
        return response;
    }
}
