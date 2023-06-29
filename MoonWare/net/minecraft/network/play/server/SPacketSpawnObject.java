package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SPacketSpawnObject implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private int speedX;
    private int speedY;
    private int speedZ;
    private int pitch;
    private int yaw;
    private int type;
    private int data;

    public SPacketSpawnObject()
    {
    }

    public SPacketSpawnObject(Entity entityIn, int typeIn)
    {
        this(entityIn, typeIn, 0);
    }

    public SPacketSpawnObject(Entity entityIn, int typeIn, int dataIn)
    {
        entityId = entityIn.getEntityId();
        uniqueId = entityIn.getUniqueID();
        x = entityIn.posX;
        y = entityIn.posY;
        z = entityIn.posZ;
        pitch = MathHelper.floor(entityIn.rotationPitch * 256.0F / 360.0F);
        yaw = MathHelper.floor(entityIn.rotationYaw * 256.0F / 360.0F);
        type = typeIn;
        data = dataIn;
        double d0 = 3.9D;
        speedX = (int)(MathHelper.clamp(entityIn.motionX, -3.9D, 3.9D) * 8000.0D);
        speedY = (int)(MathHelper.clamp(entityIn.motionY, -3.9D, 3.9D) * 8000.0D);
        speedZ = (int)(MathHelper.clamp(entityIn.motionZ, -3.9D, 3.9D) * 8000.0D);
    }

    public SPacketSpawnObject(Entity entityIn, int typeIn, int dataIn, BlockPos pos)
    {
        this(entityIn, typeIn, dataIn);
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        uniqueId = buf.readUuid();
        type = buf.readByte();
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        pitch = buf.readByte();
        yaw = buf.readByte();
        data = buf.readInt();
        speedX = buf.readShort();
        speedY = buf.readShort();
        speedZ = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        buf.writeUuid(uniqueId);
        buf.writeByte(type);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeByte(pitch);
        buf.writeByte(yaw);
        buf.writeInt(data);
        buf.writeShort(speedX);
        buf.writeShort(speedY);
        buf.writeShort(speedZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnObject(this);
    }

    public int getEntityID()
    {
        return entityId;
    }

    public UUID getUniqueId()
    {
        return uniqueId;
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

    public int getSpeedX()
    {
        return speedX;
    }

    public int getSpeedY()
    {
        return speedY;
    }

    public int getSpeedZ()
    {
        return speedZ;
    }

    public int getPitch()
    {
        return pitch;
    }

    public int getYaw()
    {
        return yaw;
    }

    public int getType()
    {
        return type;
    }

    public int getData()
    {
        return data;
    }

    public void setSpeedX(int newSpeedX)
    {
        speedX = newSpeedX;
    }

    public void setSpeedY(int newSpeedY)
    {
        speedY = newSpeedY;
    }

    public void setSpeedZ(int newSpeedZ)
    {
        speedZ = newSpeedZ;
    }

    public void setData(int dataIn)
    {
        data = dataIn;
    }
}
