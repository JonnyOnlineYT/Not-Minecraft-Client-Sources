package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetExperience implements Packet<INetHandlerPlayClient>
{
    private float experienceBar;
    private int totalExperience;
    private int level;

    public SPacketSetExperience()
    {
    }

    public SPacketSetExperience(float experienceBarIn, int totalExperienceIn, int levelIn)
    {
        experienceBar = experienceBarIn;
        totalExperience = totalExperienceIn;
        level = levelIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        experienceBar = buf.readFloat();
        level = buf.readVarIntFromBuffer();
        totalExperience = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeFloat(experienceBar);
        buf.writeVarIntToBuffer(level);
        buf.writeVarIntToBuffer(totalExperience);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSetExperience(this);
    }

    public float getExperienceBar()
    {
        return experienceBar;
    }

    public int getTotalExperience()
    {
        return totalExperience;
    }

    public int getLevel()
    {
        return level;
    }
}
