package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class SPacketServerDifficulty implements Packet<INetHandlerPlayClient>
{
    private EnumDifficulty difficulty;
    private boolean difficultyLocked;

    public SPacketServerDifficulty()
    {
    }

    public SPacketServerDifficulty(EnumDifficulty difficultyIn, boolean difficultyLockedIn)
    {
        difficulty = difficultyIn;
        difficultyLocked = difficultyLockedIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleServerDifficulty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(difficulty.getDifficultyId());
    }

    public boolean isDifficultyLocked()
    {
        return difficultyLocked;
    }

    public EnumDifficulty getDifficulty()
    {
        return difficulty;
    }
}
