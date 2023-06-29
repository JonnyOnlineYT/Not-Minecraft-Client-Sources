package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class WorldGenAbstractTree extends WorldGenerator
{
    public WorldGenAbstractTree(boolean notify)
    {
        super(notify);
    }

    /**
     * returns whether or not a tree can grow into a block
     * For example, a tree will not grow into stone
     */
    protected boolean canGrowInto(Block blockType)
    {
        Material material = blockType.getDefaultState().getMaterial();
        return material == Material.AIR || material == Material.LEAVES || blockType == Blocks.GRASS || blockType == Blocks.DIRT || blockType == Blocks.LOG || blockType == Blocks.LOG2 || blockType == Blocks.SAPLING || blockType == Blocks.VINE;
    }

    public void generateSaplings(World worldIn, Random random, BlockPos pos)
    {
    }

    /**
     * sets dirt at a specific location if it isn't already dirt
     */
    protected void setDirtAt(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() != Blocks.DIRT)
        {
            setBlockAndNotifyAdequately(worldIn, pos, Blocks.DIRT.getDefaultState());
        }
    }
}
