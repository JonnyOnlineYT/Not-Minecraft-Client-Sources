package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Namespaced;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient>
{
    @Nullable
    private Namespaced field_194155_a;

    public SPacketSelectAdvancementsTab()
    {
    }

    public SPacketSelectAdvancementsTab(@Nullable Namespaced p_i47596_1_)
    {
        field_194155_a = p_i47596_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_194022_a(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        if (buf.readBoolean())
        {
            field_194155_a = buf.func_192575_l();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBoolean(field_194155_a != null);

        if (field_194155_a != null)
        {
            buf.func_192572_a(field_194155_a);
        }
    }

    @Nullable
    public Namespaced func_194154_a()
    {
        return field_194155_a;
    }
}
