package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketRecipeBook implements Packet<INetHandlerPlayClient>
{
    private SPacketRecipeBook.State field_193646_a;
    private List<IRecipe> field_192596_a;
    private List<IRecipe> field_193647_c;
    private boolean field_192598_c;
    private boolean field_192599_d;

    public SPacketRecipeBook()
    {
    }

    public SPacketRecipeBook(SPacketRecipeBook.State p_i47597_1_, List<IRecipe> p_i47597_2_, List<IRecipe> p_i47597_3_, boolean p_i47597_4_, boolean p_i47597_5_)
    {
        field_193646_a = p_i47597_1_;
        field_192596_a = p_i47597_2_;
        field_193647_c = p_i47597_3_;
        field_192598_c = p_i47597_4_;
        field_192599_d = p_i47597_5_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_191980_a(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        field_193646_a = buf.readEnumValue(State.class);
        field_192598_c = buf.readBoolean();
        field_192599_d = buf.readBoolean();
        int i = buf.readVarIntFromBuffer();
        field_192596_a = Lists.newArrayList();

        for (int j = 0; j < i; ++j)
        {
            field_192596_a.add(CraftingManager.func_193374_a(buf.readVarIntFromBuffer()));
        }

        if (field_193646_a == SPacketRecipeBook.State.INIT)
        {
            i = buf.readVarIntFromBuffer();
            field_193647_c = Lists.newArrayList();

            for (int k = 0; k < i; ++k)
            {
                field_193647_c.add(CraftingManager.func_193374_a(buf.readVarIntFromBuffer()));
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(field_193646_a);
        buf.writeBoolean(field_192598_c);
        buf.writeBoolean(field_192599_d);
        buf.writeVarIntToBuffer(field_192596_a.size());

        for (IRecipe irecipe : field_192596_a)
        {
            buf.writeVarIntToBuffer(CraftingManager.func_193375_a(irecipe));
        }

        if (field_193646_a == SPacketRecipeBook.State.INIT)
        {
            buf.writeVarIntToBuffer(field_193647_c.size());

            for (IRecipe irecipe1 : field_193647_c)
            {
                buf.writeVarIntToBuffer(CraftingManager.func_193375_a(irecipe1));
            }
        }
    }

    public List<IRecipe> func_192595_a()
    {
        return field_192596_a;
    }

    public List<IRecipe> func_193644_b()
    {
        return field_193647_c;
    }

    public boolean func_192593_c()
    {
        return field_192598_c;
    }

    public boolean func_192594_d()
    {
        return field_192599_d;
    }

    public SPacketRecipeBook.State func_194151_e()
    {
        return field_193646_a;
    }

    public enum State
    {
        INIT,
        ADD,
        REMOVE
    }
}
