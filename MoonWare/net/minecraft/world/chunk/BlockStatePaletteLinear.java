package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public class BlockStatePaletteLinear implements IBlockStatePalette
{
    private final IBlockState[] states;
    private final IBlockStatePaletteResizer resizeHandler;
    private final int bits;
    private int arraySize;

    public BlockStatePaletteLinear(int p_i47088_1_, IBlockStatePaletteResizer p_i47088_2_)
    {
        states = new IBlockState[1 << p_i47088_1_];
        bits = p_i47088_1_;
        resizeHandler = p_i47088_2_;
    }

    public int idFor(IBlockState state)
    {
        for (int i = 0; i < arraySize; ++i)
        {
            if (states[i] == state)
            {
                return i;
            }
        }

        int j = arraySize;

        if (j < states.length)
        {
            states[j] = state;
            ++arraySize;
            return j;
        }
        else
        {
            return resizeHandler.onResize(bits + 1, state);
        }
    }

    @Nullable

    /**
     * Gets the block state by the palette id.
     */
    public IBlockState getBlockState(int indexKey)
    {
        return indexKey >= 0 && indexKey < arraySize ? states[indexKey] : null;
    }

    public void read(PacketBuffer buf)
    {
        arraySize = buf.readVarIntFromBuffer();

        for (int i = 0; i < arraySize; ++i)
        {
            states[i] = Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer());
        }
    }

    public void write(PacketBuffer buf)
    {
        buf.writeVarIntToBuffer(arraySize);

        for (int i = 0; i < arraySize; ++i)
        {
            buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(states[i]));
        }
    }

    public int getSerializedState()
    {
        int i = PacketBuffer.getVarIntSize(arraySize);

        for (int j = 0; j < arraySize; ++j)
        {
            i += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(states[j]));
        }

        return i;
    }
}
