package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnMob implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private UUID uniqueId;
    private int type;
    private double x;
    private double y;
    private double z;
    private int velocityX;
    private int velocityY;
    private int velocityZ;
    private byte yaw;
    private byte pitch;
    private byte headPitch;
    private EntityDataManager dataManager;
    private List < EntityDataManager.DataEntry<? >> dataManagerEntries;

    public SPacketSpawnMob()
    {
    }

    public SPacketSpawnMob(EntityLivingBase entityIn)
    {
        entityId = entityIn.getEntityId();
        uniqueId = entityIn.getUniqueID();
        type = EntityList.REGISTRY.getIDForObject(entityIn.getClass());
        x = entityIn.posX;
        y = entityIn.posY;
        z = entityIn.posZ;
        yaw = (byte)((int)(entityIn.rotationYaw * 256.0F / 360.0F));
        pitch = (byte)((int)(entityIn.rotationPitch * 256.0F / 360.0F));
        headPitch = (byte)((int)(entityIn.rotationYawHead * 256.0F / 360.0F));
        double d0 = 3.9D;
        double d1 = entityIn.motionX;
        double d2 = entityIn.motionY;
        double d3 = entityIn.motionZ;

        if (d1 < -3.9D)
        {
            d1 = -3.9D;
        }

        if (d2 < -3.9D)
        {
            d2 = -3.9D;
        }

        if (d3 < -3.9D)
        {
            d3 = -3.9D;
        }

        if (d1 > 3.9D)
        {
            d1 = 3.9D;
        }

        if (d2 > 3.9D)
        {
            d2 = 3.9D;
        }

        if (d3 > 3.9D)
        {
            d3 = 3.9D;
        }

        velocityX = (int)(d1 * 8000.0D);
        velocityY = (int)(d2 * 8000.0D);
        velocityZ = (int)(d3 * 8000.0D);
        dataManager = entityIn.getDataManager();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        uniqueId = buf.readUuid();
        type = buf.readVarIntFromBuffer();
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        yaw = buf.readByte();
        pitch = buf.readByte();
        headPitch = buf.readByte();
        velocityX = buf.readShort();
        velocityY = buf.readShort();
        velocityZ = buf.readShort();
        dataManagerEntries = EntityDataManager.readEntries(buf);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        buf.writeUuid(uniqueId);
        buf.writeVarIntToBuffer(type);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeByte(yaw);
        buf.writeByte(pitch);
        buf.writeByte(headPitch);
        buf.writeShort(velocityX);
        buf.writeShort(velocityY);
        buf.writeShort(velocityZ);
        dataManager.writeEntries(buf);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnMob(this);
    }

    @Nullable
    public List < EntityDataManager.DataEntry<? >> getDataManagerEntries()
    {
        return dataManagerEntries;
    }

    public int getEntityID()
    {
        return entityId;
    }

    public UUID getUniqueId()
    {
        return uniqueId;
    }

    public int getEntityType()
    {
        return type;
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

    public int getVelocityX()
    {
        return velocityX;
    }

    public int getVelocityY()
    {
        return velocityY;
    }

    public int getVelocityZ()
    {
        return velocityZ;
    }

    public byte getYaw()
    {
        return yaw;
    }

    public byte getPitch()
    {
        return pitch;
    }

    public byte getHeadPitch()
    {
        return headPitch;
    }
}
