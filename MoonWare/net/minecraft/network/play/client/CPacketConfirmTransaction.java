package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketConfirmTransaction implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    private short uid;
    private boolean accepted;

    public CPacketConfirmTransaction()
    {
    }

    public CPacketConfirmTransaction(int windowIdIn, short uidIn, boolean acceptedIn)
    {
        windowId = windowIdIn;
        uid = uidIn;
        accepted = acceptedIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        windowId = buf.readByte();
        uid = buf.readShort();
        accepted = buf.readByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(windowId);
        buf.writeShort(uid);
        buf.writeByte(accepted ? 1 : 0);
    }

    public int getWindowId()
    {
        return windowId;
    }

    public short getUid()
    {
        return uid;
    }
}
