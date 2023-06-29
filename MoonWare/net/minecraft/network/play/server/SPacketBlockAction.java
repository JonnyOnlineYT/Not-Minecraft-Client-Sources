package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;

public class SPacketBlockAction implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPosition;
    private int instrument;
    private int pitch;
    private Block block;

    public SPacketBlockAction()
    {
    }

    public SPacketBlockAction(BlockPos pos, Block blockIn, int instrumentIn, int pitchIn)
    {
        blockPosition = pos;
        instrument = instrumentIn;
        pitch = pitchIn;
        block = blockIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        blockPosition = buf.readBlockPos();
        instrument = buf.readUnsignedByte();
        pitch = buf.readUnsignedByte();
        block = Block.getBlockById(buf.readVarIntFromBuffer() & 4095);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(blockPosition);
        buf.writeByte(instrument);
        buf.writeByte(pitch);
        buf.writeVarIntToBuffer(Block.getIdFromBlock(block) & 4095);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleBlockAction(this);
    }

    public BlockPos getBlockPosition()
    {
        return blockPosition;
    }

    /**
     * instrument data for noteblocks
     */
    public int getData1()
    {
        return instrument;
    }

    /**
     * pitch data for noteblocks
     */
    public int getData2()
    {
        return pitch;
    }

    public Block getBlockType()
    {
        return block;
    }
}
