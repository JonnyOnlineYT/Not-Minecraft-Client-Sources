package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketWindowProperty implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private int property;
    private int value;

    public SPacketWindowProperty()
    {
    }

    public SPacketWindowProperty(int windowIdIn, int propertyIn, int valueIn)
    {
        windowId = windowIdIn;
        property = propertyIn;
        value = valueIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleWindowProperty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        windowId = buf.readUnsignedByte();
        property = buf.readShort();
        value = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(windowId);
        buf.writeShort(property);
        buf.writeShort(value);
    }

    public int getWindowId()
    {
        return windowId;
    }

    public int getProperty()
    {
        return property;
    }

    public int getValue()
    {
        return value;
    }
}
