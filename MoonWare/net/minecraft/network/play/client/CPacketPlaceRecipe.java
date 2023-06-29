package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlaceRecipe implements Packet<INetHandlerPlayServer>
{
    private int field_194320_a;
    private IRecipe field_194321_b;
    private boolean field_194322_c;

    public CPacketPlaceRecipe()
    {
    }

    public CPacketPlaceRecipe(int p_i47614_1_, IRecipe p_i47614_2_, boolean p_i47614_3_)
    {
        field_194320_a = p_i47614_1_;
        field_194321_b = p_i47614_2_;
        field_194322_c = p_i47614_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        field_194320_a = buf.readByte();
        field_194321_b = CraftingManager.func_193374_a(buf.readVarIntFromBuffer());
        field_194322_c = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(field_194320_a);
        buf.writeVarIntToBuffer(CraftingManager.func_193375_a(field_194321_b));
        buf.writeBoolean(field_194322_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.func_194308_a(this);
    }

    public int func_194318_a()
    {
        return field_194320_a;
    }

    public IRecipe func_194317_b()
    {
        return field_194321_b;
    }

    public boolean func_194319_c()
    {
        return field_194322_c;
    }
}
