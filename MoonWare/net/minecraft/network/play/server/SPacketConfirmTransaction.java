package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketConfirmTransaction implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private short actionNumber;
    private boolean accepted;

    public SPacketConfirmTransaction()
    {
    }

    public SPacketConfirmTransaction(int windowIdIn, short actionNumberIn, boolean acceptedIn)
    {
        windowId = windowIdIn;
        actionNumber = actionNumberIn;
        accepted = acceptedIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        windowId = buf.readUnsignedByte();
        actionNumber = buf.readShort();
        accepted = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(windowId);
        buf.writeShort(actionNumber);
        buf.writeBoolean(accepted);
    }

    public int getWindowId()
    {
        return windowId;
    }

    public short getActionNumber()
    {
        return actionNumber;
    }

    public boolean wasAccepted()
    {
        return accepted;
    }
}
