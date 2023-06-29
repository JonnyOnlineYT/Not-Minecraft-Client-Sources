package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnExperienceOrb implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private double posX;
    private double posY;
    private double posZ;
    private int xpValue;

    public SPacketSpawnExperienceOrb()
    {
    }

    public SPacketSpawnExperienceOrb(EntityXPOrb orb)
    {
        entityID = orb.getEntityId();
        posX = orb.posX;
        posY = orb.posY;
        posZ = orb.posZ;
        xpValue = orb.getXpValue();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityID = buf.readVarIntFromBuffer();
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        xpValue = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityID);
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeShort(xpValue);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnExperienceOrb(this);
    }

    public int getEntityID()
    {
        return entityID;
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

    public int getXPValue()
    {
        return xpValue;
    }
}
