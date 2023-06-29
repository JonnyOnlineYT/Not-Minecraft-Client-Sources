package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketEntity implements Packet<INetHandlerPlayClient>
{
    protected int entityId;
    protected int posX;
    protected int posY;
    protected int posZ;
    protected byte yaw;
    protected byte pitch;
    protected boolean onGround;
    protected boolean rotating;

    public SPacketEntity()
    {
    }

    public SPacketEntity(int entityIdIn)
    {
        entityId = entityIdIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityMovement(this);
    }

    public String toString()
    {
        return "Entity_" + super.toString();
    }

    public Entity getEntity(World worldIn)
    {
        return worldIn.getEntityByID(entityId);
    }

    public int getX()
    {
        return posX;
    }

    public int getY()
    {
        return posY;
    }

    public int getZ()
    {
        return posZ;
    }

    public byte getYaw()
    {
        return yaw;
    }

    public byte getPitch()
    {
        return pitch;
    }

    public boolean isRotating()
    {
        return rotating;
    }

    public boolean getOnGround()
    {
        return onGround;
    }

    public static class S15PacketEntityRelMove extends SPacketEntity
    {
        public S15PacketEntityRelMove()
        {
        }

        public S15PacketEntityRelMove(int entityIdIn, long xIn, long yIn, long zIn, boolean onGroundIn)
        {
            super(entityIdIn);
            posX = (int)xIn;
            posY = (int)yIn;
            posZ = (int)zIn;
            onGround = onGroundIn;
        }

        public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            posX = buf.readShort();
            posY = buf.readShort();
            posZ = buf.readShort();
            onGround = buf.readBoolean();
        }

        public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeShort(posX);
            buf.writeShort(posY);
            buf.writeShort(posZ);
            buf.writeBoolean(onGround);
        }
    }

    public static class S16PacketEntityLook extends SPacketEntity
    {
        public S16PacketEntityLook()
        {
            rotating = true;
        }

        public S16PacketEntityLook(int entityIdIn, byte yawIn, byte pitchIn, boolean onGroundIn)
        {
            super(entityIdIn);
            yaw = yawIn;
            pitch = pitchIn;
            rotating = true;
            onGround = onGroundIn;
        }

        public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            yaw = buf.readByte();
            pitch = buf.readByte();
            onGround = buf.readBoolean();
        }

        public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeByte(yaw);
            buf.writeByte(pitch);
            buf.writeBoolean(onGround);
        }
    }

    public static class S17PacketEntityLookMove extends SPacketEntity
    {
        public S17PacketEntityLookMove()
        {
            rotating = true;
        }

        public S17PacketEntityLookMove(int entityIdIn, long xIn, long yIn, long zIn, byte yawIn, byte pitchIn, boolean onGroundIn)
        {
            super(entityIdIn);
            posX = (int)xIn;
            posY = (int)yIn;
            posZ = (int)zIn;
            yaw = yawIn;
            pitch = pitchIn;
            onGround = onGroundIn;
            rotating = true;
        }

        public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            posX = buf.readShort();
            posY = buf.readShort();
            posZ = buf.readShort();
            yaw = buf.readByte();
            pitch = buf.readByte();
            onGround = buf.readBoolean();
        }

        public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeShort(posX);
            buf.writeShort(posY);
            buf.writeShort(posZ);
            buf.writeByte(yaw);
            buf.writeByte(pitch);
            buf.writeBoolean(onGround);
        }
    }
}
