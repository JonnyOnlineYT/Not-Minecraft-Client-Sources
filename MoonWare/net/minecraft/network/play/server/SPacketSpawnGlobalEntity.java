package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnGlobalEntity implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private double x;
    private double y;
    private double z;
    private int type;

    public SPacketSpawnGlobalEntity()
    {
    }

    public SPacketSpawnGlobalEntity(Entity entityIn)
    {
        entityId = entityIn.getEntityId();
        x = entityIn.posX;
        y = entityIn.posY;
        z = entityIn.posZ;

        if (entityIn instanceof EntityLightningBolt)
        {
            type = 1;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        type = buf.readByte();
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        buf.writeByte(type);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnGlobalEntity(this);
    }

    public int getEntityId()
    {
        return entityId;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public int getType()
    {
        return type;
    }
}
