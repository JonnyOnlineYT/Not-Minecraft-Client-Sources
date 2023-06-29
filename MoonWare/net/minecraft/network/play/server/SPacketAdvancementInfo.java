package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Namespaced;

public class SPacketAdvancementInfo implements Packet<INetHandlerPlayClient>
{
    private boolean field_192605_a;
    private Map<Namespaced, Advancement.Builder> field_192606_b;
    private Set<Namespaced> field_192607_c;
    private Map<Namespaced, AdvancementProgress> field_192608_d;

    public SPacketAdvancementInfo()
    {
    }

    public SPacketAdvancementInfo(boolean p_i47519_1_, Collection<Advancement> p_i47519_2_, Set<Namespaced> p_i47519_3_, Map<Namespaced, AdvancementProgress> p_i47519_4_)
    {
        field_192605_a = p_i47519_1_;
        field_192606_b = Maps.newHashMap();

        for (Advancement advancement : p_i47519_2_)
        {
            field_192606_b.put(advancement.getId(), advancement.asBuilder());
        }

        field_192607_c = p_i47519_3_;
        field_192608_d = Maps.newHashMap(p_i47519_4_);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_191981_a(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        field_192605_a = buf.readBoolean();
        field_192606_b = Maps.newHashMap();
        field_192607_c = Sets.newLinkedHashSet();
        field_192608_d = Maps.newHashMap();
        int i = buf.readVarIntFromBuffer();

        for (int j = 0; j < i; ++j)
        {
            Namespaced resourcelocation = buf.func_192575_l();
            Advancement.Builder advancement$builder = Advancement.Builder.func_192060_b(buf);
            field_192606_b.put(resourcelocation, advancement$builder);
        }

        i = buf.readVarIntFromBuffer();

        for (int k = 0; k < i; ++k)
        {
            Namespaced resourcelocation1 = buf.func_192575_l();
            field_192607_c.add(resourcelocation1);
        }

        i = buf.readVarIntFromBuffer();

        for (int l = 0; l < i; ++l)
        {
            Namespaced resourcelocation2 = buf.func_192575_l();
            field_192608_d.put(resourcelocation2, AdvancementProgress.func_192100_b(buf));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBoolean(field_192605_a);
        buf.writeVarIntToBuffer(field_192606_b.size());

        for (Map.Entry<Namespaced, Advancement.Builder> entry : field_192606_b.entrySet())
        {
            Namespaced resourcelocation = entry.getKey();
            Advancement.Builder advancement$builder = entry.getValue();
            buf.func_192572_a(resourcelocation);
            advancement$builder.func_192057_a(buf);
        }

        buf.writeVarIntToBuffer(field_192607_c.size());

        for (Namespaced resourcelocation1 : field_192607_c)
        {
            buf.func_192572_a(resourcelocation1);
        }

        buf.writeVarIntToBuffer(field_192608_d.size());

        for (Map.Entry<Namespaced, AdvancementProgress> entry1 : field_192608_d.entrySet())
        {
            buf.func_192572_a(entry1.getKey());
            entry1.getValue().func_192104_a(buf);
        }
    }

    public Map<Namespaced, Advancement.Builder> func_192603_a()
    {
        return field_192606_b;
    }

    public Set<Namespaced> func_192600_b()
    {
        return field_192607_c;
    }

    public Map<Namespaced, AdvancementProgress> func_192604_c()
    {
        return field_192608_d;
    }

    public boolean func_192602_d()
    {
        return field_192605_a;
    }
}
