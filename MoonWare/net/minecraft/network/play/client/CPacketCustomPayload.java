package net.minecraft.network.play.client;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketCustomPayload implements Packet<INetHandlerPlayServer>
{
    private String channel;
    private PacketBuffer data;

    public CPacketCustomPayload()
    {
    }

    public CPacketCustomPayload(String channelIn, PacketBuffer bufIn)
    {
        channel = channelIn;
        data = bufIn;

        if (bufIn.writerIndex() > 32767)
        {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        channel = buf.readStringFromBuffer(20);
        int i = buf.readableBytes();

        if (i >= 0 && i <= 32767)
        {
            data = new PacketBuffer(buf.readBytes(i));
        }
        else
        {
            throw new IOException("Payload may not be larger than 32767 bytes");
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(channel);
        buf.writeBytes(data);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processCustomPayload(this);

        if (data != null)
        {
            data.release();
        }
    }

    public String getChannelName()
    {
        return channel;
    }

    public PacketBuffer getBufferData()
    {
        return data;
    }
}
