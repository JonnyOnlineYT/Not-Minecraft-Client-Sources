package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityVelocity implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    public int motionX;
    public int motionY;
    public int motionZ;

    public SPacketEntityVelocity()
    {
    }

    public SPacketEntityVelocity(Entity entityIn)
    {
        this(entityIn.getEntityId(), entityIn.motionX, entityIn.motionY, entityIn.motionZ);
    }

    public SPacketEntityVelocity(int entityIdIn, double motionXIn, double motionYIn, double motionZIn)
    {
        entityID = entityIdIn;
        double d0 = 3.9D;

        if (motionXIn < -3.9D)
        {
            motionXIn = -3.9D;
        }

        if (motionYIn < -3.9D)
        {
            motionYIn = -3.9D;
        }

        if (motionZIn < -3.9D)
        {
            motionZIn = -3.9D;
        }

        if (motionXIn > 3.9D)
        {
            motionXIn = 3.9D;
        }

        if (motionYIn > 3.9D)
        {
            motionYIn = 3.9D;
        }

        if (motionZIn > 3.9D)
        {
            motionZIn = 3.9D;
        }

        motionX = (int)(motionXIn * 8000.0D);
        motionY = (int)(motionYIn * 8000.0D);
        motionZ = (int)(motionZIn * 8000.0D);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityID = buf.readVarIntFromBuffer();
        motionX = buf.readShort();
        motionY = buf.readShort();
        motionZ = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityID);
        buf.writeShort(motionX);
        buf.writeShort(motionY);
        buf.writeShort(motionZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityVelocity(this);
    }

    public int getEntityID()
    {
        return entityID;
    }

    public int getMotionX()
    {
        return motionX;
    }

    public int getMotionY()
    {
        return motionY;
    }

    public int getMotionZ()
    {
        return motionZ;
    }
}
