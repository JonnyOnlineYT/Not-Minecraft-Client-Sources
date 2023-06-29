package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketJoinGame implements Packet<INetHandlerPlayClient>
{
    private int playerId;
    private boolean hardcoreMode;
    private GameType gameType;
    private int dimension;
    private EnumDifficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;
    private boolean reducedDebugInfo;

    public SPacketJoinGame()
    {
    }

    public SPacketJoinGame(int playerIdIn, GameType gameTypeIn, boolean hardcoreModeIn, int dimensionIn, EnumDifficulty difficultyIn, int maxPlayersIn, WorldType worldTypeIn, boolean reducedDebugInfoIn)
    {
        playerId = playerIdIn;
        dimension = dimensionIn;
        difficulty = difficultyIn;
        gameType = gameTypeIn;
        maxPlayers = maxPlayersIn;
        hardcoreMode = hardcoreModeIn;
        worldType = worldTypeIn;
        reducedDebugInfo = reducedDebugInfoIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        playerId = buf.readInt();
        int i = buf.readUnsignedByte();
        hardcoreMode = (i & 8) == 8;
        i = i & -9;
        gameType = GameType.getByID(i);
        dimension = buf.readInt();
        difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
        maxPlayers = buf.readUnsignedByte();
        worldType = WorldType.parseWorldType(buf.readStringFromBuffer(16));

        if (worldType == null)
        {
            worldType = WorldType.DEFAULT;
        }

        reducedDebugInfo = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(playerId);
        int i = gameType.getID();

        if (hardcoreMode)
        {
            i |= 8;
        }

        buf.writeByte(i);
        buf.writeInt(dimension);
        buf.writeByte(difficulty.getDifficultyId());
        buf.writeByte(maxPlayers);
        buf.writeString(worldType.getWorldTypeName());
        buf.writeBoolean(reducedDebugInfo);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleJoinGame(this);
    }

    public int getPlayerId()
    {
        return playerId;
    }

    public boolean isHardcoreMode()
    {
        return hardcoreMode;
    }

    public GameType getGameType()
    {
        return gameType;
    }

    public int getDimension()
    {
        return dimension;
    }

    public EnumDifficulty getDifficulty()
    {
        return difficulty;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public WorldType getWorldType()
    {
        return worldType;
    }

    public boolean isReducedDebugInfo()
    {
        return reducedDebugInfo;
    }
}
