package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.text.Component;

public class SPacketDisconnect implements Packet<INetHandlerLoginClient>
{
    private Component reason;

    public SPacketDisconnect()
    {
    }

    public SPacketDisconnect(Component p_i46853_1_)
    {
        reason = p_i46853_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        reason = Component.Serializer.fromJsonLenient(buf.readStringFromBuffer(32767));
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeTextComponent(reason);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleDisconnect(this);
    }

    public Component getReason()
    {
        return reason;
    }
}
