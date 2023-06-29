package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient>
{
    private String[] matches;

    public SPacketTabComplete()
    {
    }

    public SPacketTabComplete(String[] matchesIn)
    {
        matches = matchesIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        matches = new String[buf.readVarIntFromBuffer()];

        for (int i = 0; i < matches.length; ++i)
        {
            matches[i] = buf.readStringFromBuffer(32767);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(matches.length);

        for (String s : matches)
        {
            buf.writeString(s);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleTabComplete(this);
    }

    public String[] getMatches()
    {
        return matches;
    }
}
