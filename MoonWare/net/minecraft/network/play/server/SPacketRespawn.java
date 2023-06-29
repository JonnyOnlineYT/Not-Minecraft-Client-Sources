package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketRespawn implements Packet<INetHandlerPlayClient>
{
    private int dimensionID;
    private EnumDifficulty difficulty;
    private GameType gameType;
    private WorldType worldType;

    public SPacketRespawn()
    {
    }

    public SPacketRespawn(int dimensionIdIn, EnumDifficulty difficultyIn, WorldType worldTypeIn, GameType gameModeIn)
    {
        dimensionID = dimensionIdIn;
        difficulty = difficultyIn;
        gameType = gameModeIn;
        worldType = worldTypeIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleRespawn(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        dimensionID = buf.readInt();
        difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
        gameType = GameType.getByID(buf.readUnsignedByte());
        worldType = WorldType.parseWorldType(buf.readStringFromBuffer(16));

        if (worldType == null)
        {
            worldType = WorldType.DEFAULT;
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(dimensionID);
        buf.writeByte(difficulty.getDifficultyId());
        buf.writeByte(gameType.getID());
        buf.writeString(worldType.getWorldTypeName());
    }

    public int getDimensionID()
    {
        return dimensionID;
    }

    public EnumDifficulty getDifficulty()
    {
        return difficulty;
    }

    public GameType getGameType()
    {
        return gameType;
    }

    public WorldType getWorldType()
    {
        return worldType;
    }
}
