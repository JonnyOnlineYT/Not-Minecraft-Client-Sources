package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCooldown implements Packet<INetHandlerPlayClient>
{
    private Item item;
    private int ticks;

    public SPacketCooldown()
    {
    }

    public SPacketCooldown(Item itemIn, int ticksIn)
    {
        item = itemIn;
        ticks = ticksIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        item = Item.getItemById(buf.readVarIntFromBuffer());
        ticks = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(Item.getIdFromItem(item));
        buf.writeVarIntToBuffer(ticks);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCooldown(this);
    }

    public Item getItem()
    {
        return item;
    }

    public int getTicks()
    {
        return ticks;
    }
}
