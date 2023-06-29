package net.minecraft.world.gen.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class WorldGenerator
{
    /**
     * Sets wither or not the generator should notify blocks of blocks it changes. When the world is first generated,
     * this is false, when saplings grow, this is true.
     */
    private final boolean doBlockNotify;

    public WorldGenerator()
    {
        this(false);
    }

    public WorldGenerator(boolean notify)
    {
        doBlockNotify = notify;
    }

    public abstract boolean generate(World worldIn, Random rand, BlockPos position);

    public void setDecorationDefaults()
    {
    }

    protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state)
    {
        if (doBlockNotify)
        {
            worldIn.setBlockState(pos, state, 3);
        }
        else
        {
            worldIn.setBlockState(pos, state, 2);
        }
    }
}
