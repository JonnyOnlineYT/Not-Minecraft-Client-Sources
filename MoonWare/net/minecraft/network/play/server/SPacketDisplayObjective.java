package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketDisplayObjective implements Packet<INetHandlerPlayClient>
{
    private int position;
    private String scoreName;

    public SPacketDisplayObjective()
    {
    }

    public SPacketDisplayObjective(int positionIn, ScoreObjective objective)
    {
        position = positionIn;

        if (objective == null)
        {
            scoreName = "";
        }
        else
        {
            scoreName = objective.getName();
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        position = buf.readByte();
        scoreName = buf.readStringFromBuffer(16);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(position);
        buf.writeString(scoreName);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleDisplayObjective(this);
    }

    public int getPosition()
    {
        return position;
    }

    public String getName()
    {
        return scoreName;
    }
}
