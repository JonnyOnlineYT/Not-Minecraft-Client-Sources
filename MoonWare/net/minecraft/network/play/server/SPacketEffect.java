package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketEffect implements Packet<INetHandlerPlayClient>
{
    private int soundType;
    private BlockPos soundPos;

    /** can be a block/item id or other depending on the soundtype */
    private int soundData;

    /** If true the sound is played across the server */
    private boolean serverWide;

    public SPacketEffect()
    {
    }

    public SPacketEffect(int soundTypeIn, BlockPos soundPosIn, int soundDataIn, boolean serverWideIn)
    {
        soundType = soundTypeIn;
        soundPos = soundPosIn;
        soundData = soundDataIn;
        serverWide = serverWideIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        soundType = buf.readInt();
        soundPos = buf.readBlockPos();
        soundData = buf.readInt();
        serverWide = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(soundType);
        buf.writeBlockPos(soundPos);
        buf.writeInt(soundData);
        buf.writeBoolean(serverWide);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEffect(this);
    }

    public boolean isSoundServerwide()
    {
        return serverWide;
    }

    public int getSoundType()
    {
        return soundType;
    }

    public int getSoundData()
    {
        return soundData;
    }

    public BlockPos getSoundPos()
    {
        return soundPos;
    }
}
