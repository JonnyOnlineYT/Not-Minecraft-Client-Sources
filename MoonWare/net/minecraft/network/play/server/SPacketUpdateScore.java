package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketUpdateScore implements Packet<INetHandlerPlayClient>
{
    private String name = "";
    private String objective = "";
    private int value;
    private SPacketUpdateScore.Action action;

    public SPacketUpdateScore()
    {
    }

    public SPacketUpdateScore(Score scoreIn)
    {
        name = scoreIn.getPlayerName();
        objective = scoreIn.getObjective().getName();
        value = scoreIn.getScorePoints();
        action = SPacketUpdateScore.Action.CHANGE;
    }

    public SPacketUpdateScore(String nameIn)
    {
        name = nameIn;
        objective = "";
        value = 0;
        action = SPacketUpdateScore.Action.REMOVE;
    }

    public SPacketUpdateScore(String nameIn, ScoreObjective objectiveIn)
    {
        name = nameIn;
        objective = objectiveIn.getName();
        value = 0;
        action = SPacketUpdateScore.Action.REMOVE;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        name = buf.readStringFromBuffer(40);
        action = buf.readEnumValue(Action.class);
        objective = buf.readStringFromBuffer(16);

        if (action != SPacketUpdateScore.Action.REMOVE)
        {
            value = buf.readVarIntFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(name);
        buf.writeEnumValue(action);
        buf.writeString(objective);

        if (action != SPacketUpdateScore.Action.REMOVE)
        {
            buf.writeVarIntToBuffer(value);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateScore(this);
    }

    public String getPlayerName()
    {
        return name;
    }

    public String getObjectiveName()
    {
        return objective;
    }

    public int getScoreValue()
    {
        return value;
    }

    public SPacketUpdateScore.Action getScoreAction()
    {
        return action;
    }

    public enum Action
    {
        CHANGE,
        REMOVE
    }
}
