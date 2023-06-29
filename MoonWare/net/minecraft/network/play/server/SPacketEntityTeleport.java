package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityTeleport implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private double posX;
    private double posY;
    private double posZ;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public SPacketEntityTeleport()
    {
    }

    public SPacketEntityTeleport(Entity entityIn)
    {
        entityId = entityIn.getEntityId();
        posX = entityIn.posX;
        posY = entityIn.posY;
        posZ = entityIn.posZ;
        yaw = (byte)((int)(entityIn.rotationYaw * 256.0F / 360.0F));
        pitch = (byte)((int)(entityIn.rotationPitch * 256.0F / 360.0F));
        onGround = entityIn.onGround;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        yaw = buf.readByte();
        pitch = buf.readByte();
        onGround = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeByte(yaw);
        buf.writeByte(pitch);
        buf.writeBoolean(onGround);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityTeleport(this);
    }

    public int getEntityId()
    {
        return entityId;
    }

    public double getX()
    {
        return posX;
    }

    public double getY()
    {
        return posY;
    }

    public double getZ()
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

    public boolean getOnGround()
    {
        return onGround;
    }
}
