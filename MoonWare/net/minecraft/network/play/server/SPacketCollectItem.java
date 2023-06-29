package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCollectItem implements Packet<INetHandlerPlayClient>
{
    private int collectedItemEntityId;
    private int entityId;
    private int field_191209_c;

    public SPacketCollectItem()
    {
    }

    public SPacketCollectItem(int p_i47316_1_, int p_i47316_2_, int p_i47316_3_)
    {
        collectedItemEntityId = p_i47316_1_;
        entityId = p_i47316_2_;
        field_191209_c = p_i47316_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        collectedItemEntityId = buf.readVarIntFromBuffer();
        entityId = buf.readVarIntFromBuffer();
        field_191209_c = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(collectedItemEntityId);
        buf.writeVarIntToBuffer(entityId);
        buf.writeVarIntToBuffer(field_191209_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCollectItem(this);
    }

    public int getCollectedItemEntityID()
    {
        return collectedItemEntityId;
    }

    public int getEntityID()
    {
        return entityId;
    }

    public int func_191208_c()
    {
        return field_191209_c;
    }
}
