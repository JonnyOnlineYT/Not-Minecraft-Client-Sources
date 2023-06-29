package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.NibbleArray;
import optifine.Reflector;

public class ExtendedBlockStorage
{
    /**
     * Contains the bottom-most Y block represented by this ExtendedBlockStorage. Typically a multiple of 16.
     */
    private final int yBase;

    /**
     * A total count of the number of non-air blocks in this block storage's Chunk.
     */
    private int blockRefCount;

    /**
     * Contains the number of blocks in this block storage's parent chunk that require random ticking. Used to cull the
     * Chunk from random tick updates for performance reasons.
     */
    private int tickRefCount;
    private final BlockStateContainer data;

    /** The NibbleArray containing a block of Block-light data. */
    private NibbleArray blocklightArray;

    /** The NibbleArray containing a block of Sky-light data. */
    private NibbleArray skylightArray;

    public ExtendedBlockStorage(int y, boolean storeSkylight)
    {
        yBase = y;
        data = new BlockStateContainer();
        blocklightArray = new NibbleArray();

        if (storeSkylight)
        {
            skylightArray = new NibbleArray();
        }
    }

    public IBlockState get(int x, int y, int z)
    {
        return data.get(x, y, z);
    }

    public void set(int x, int y, int z, IBlockState state)
    {
        if (Reflector.IExtendedBlockState.isInstance(state))
        {
            state = (IBlockState)Reflector.call(state, Reflector.IExtendedBlockState_getClean);
        }

        IBlockState iblockstate = get(x, y, z);
        Block block = iblockstate.getBlock();
        Block block1 = state.getBlock();

        if (block != Blocks.AIR)
        {
            --blockRefCount;

            if (block.getTickRandomly())
            {
                --tickRefCount;
            }
        }

        if (block1 != Blocks.AIR)
        {
            ++blockRefCount;

            if (block1.getTickRandomly())
            {
                ++tickRefCount;
            }
        }

        data.set(x, y, z, state);
    }

    /**
     * Returns whether or not this block storage's Chunk is fully empty, based on its internal reference count.
     */
    public boolean isEmpty()
    {
        return blockRefCount == 0;
    }

    /**
     * Returns whether or not this block storage's Chunk will require random ticking, used to avoid looping through
     * random block ticks when there are no blocks that would randomly tick.
     */
    public boolean getNeedsRandomTick()
    {
        return tickRefCount > 0;
    }

    /**
     * Returns the Y location of this ExtendedBlockStorage.
     */
    public int getYLocation()
    {
        return yBase;
    }

    /**
     * Sets the saved Sky-light value in the extended block storage structure.
     */
    public void setExtSkylightValue(int x, int y, int z, int value)
    {
        skylightArray.set(x, y, z, value);
    }

    /**
     * Gets the saved Sky-light value in the extended block storage structure.
     */
    public int getExtSkylightValue(int x, int y, int z)
    {
        return skylightArray.get(x, y, z);
    }

    /**
     * Sets the saved Block-light value in the extended block storage structure.
     */
    public void setExtBlocklightValue(int x, int y, int z, int value)
    {
        blocklightArray.set(x, y, z, value);
    }

    /**
     * Gets the saved Block-light value in the extended block storage structure.
     */
    public int getExtBlocklightValue(int x, int y, int z)
    {
        return blocklightArray.get(x, y, z);
    }

    public void removeInvalidBlocks()
    {
        IBlockState iblockstate = Blocks.AIR.getDefaultState();
        int i = 0;
        int j = 0;

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                for (int i1 = 0; i1 < 16; ++i1)
                {
                    IBlockState iblockstate1 = data.get(i1, k, l);

                    if (iblockstate1 != iblockstate)
                    {
                        ++i;
                        Block block = iblockstate1.getBlock();

                        if (block.getTickRandomly())
                        {
                            ++j;
                        }
                    }
                }
            }
        }

        blockRefCount = i;
        tickRefCount = j;
    }

    public BlockStateContainer getData()
    {
        return data;
    }

    /**
     * Returns the NibbleArray instance containing Block-light data.
     */
    public NibbleArray getBlocklightArray()
    {
        return blocklightArray;
    }

    /**
     * Returns the NibbleArray instance containing Sky-light data.
     */
    public NibbleArray getSkylightArray()
    {
        return skylightArray;
    }

    /**
     * Sets the NibbleArray instance used for Block-light values in this particular storage block.
     */
    public void setBlocklightArray(NibbleArray newBlocklightArray)
    {
        blocklightArray = newBlocklightArray;
    }

    /**
     * Sets the NibbleArray instance used for Sky-light values in this particular storage block.
     */
    public void setSkylightArray(NibbleArray newSkylightArray)
    {
        skylightArray = newSkylightArray;
    }
}
