package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketBlockChange implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPosition;
    private IBlockState blockState;

    public SPacketBlockChange()
    {
    }

    public SPacketBlockChange(World worldIn, BlockPos posIn)
    {
        blockPosition = posIn;
        blockState = worldIn.getBlockState(posIn);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        blockPosition = buf.readBlockPos();
        blockState = Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(blockPosition);
        buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(blockState));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleBlockChange(this);
    }

    public IBlockState getBlockState()
    {
        return blockState;
    }

    public BlockPos getBlockPosition()
    {
        return blockPosition;
    }
}
