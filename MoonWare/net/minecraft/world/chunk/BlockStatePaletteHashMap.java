package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntIdentityHashBiMap;

public class BlockStatePaletteHashMap implements IBlockStatePalette
{
    private final IntIdentityHashBiMap<IBlockState> statePaletteMap;
    private final IBlockStatePaletteResizer paletteResizer;
    private final int bits;

    public BlockStatePaletteHashMap(int bitsIn, IBlockStatePaletteResizer p_i47089_2_)
    {
        bits = bitsIn;
        paletteResizer = p_i47089_2_;
        statePaletteMap = new IntIdentityHashBiMap<IBlockState>(1 << bitsIn);
    }

    public int idFor(IBlockState state)
    {
        int i = statePaletteMap.getId(state);

        if (i == -1)
        {
            i = statePaletteMap.add(state);

            if (i >= 1 << bits)
            {
                i = paletteResizer.onResize(bits + 1, state);
            }
        }

        return i;
    }

    @Nullable

    /**
     * Gets the block state by the palette id.
     */
    public IBlockState getBlockState(int indexKey)
    {
        return statePaletteMap.get(indexKey);
    }

    public void read(PacketBuffer buf)
    {
        statePaletteMap.clear();
        int i = buf.readVarIntFromBuffer();

        for (int j = 0; j < i; ++j)
        {
            statePaletteMap.add(Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer()));
        }
    }

    public void write(PacketBuffer buf)
    {
        int i = statePaletteMap.size();
        buf.writeVarIntToBuffer(i);

        for (int j = 0; j < i; ++j)
        {
            buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(statePaletteMap.get(j)));
        }
    }

    public int getSerializedState()
    {
        int i = PacketBuffer.getVarIntSize(statePaletteMap.size());

        for (int j = 0; j < statePaletteMap.size(); ++j)
        {
            i += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(statePaletteMap.get(j)));
        }

        return i;
    }
}
