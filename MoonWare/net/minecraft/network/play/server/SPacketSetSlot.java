package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetSlot implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private int slot;
    private ItemStack item = ItemStack.EMPTY;

    public SPacketSetSlot()
    {
    }

    public SPacketSetSlot(int windowIdIn, int slotIn, ItemStack itemIn)
    {
        windowId = windowIdIn;
        slot = slotIn;
        item = itemIn.copy();
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSetSlot(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        windowId = buf.readByte();
        slot = buf.readShort();
        item = buf.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(windowId);
        buf.writeShort(slot);
        buf.writeItemStackToBuffer(item);
    }

    public int getWindowId()
    {
        return windowId;
    }

    public int getSlot()
    {
        return slot;
    }

    public ItemStack getStack()
    {
        return item;
    }
}
