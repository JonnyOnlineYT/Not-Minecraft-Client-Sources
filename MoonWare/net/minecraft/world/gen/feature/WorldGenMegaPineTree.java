package net.minecraft.world.gen.feature;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState PODZOL = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
    private final boolean useBaseHeight;

    public WorldGenMegaPineTree(boolean notify, boolean p_i45457_2_)
    {
        super(notify, 13, 15, TRUNK, LEAF);
        useBaseHeight = p_i45457_2_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int i = getHeight(rand);

        if (!ensureGrowable(worldIn, rand, position, i))
        {
            return false;
        }
        else
        {
            createCrown(worldIn, position.getX(), position.getZ(), position.getY() + i, 0, rand);

            for (int j = 0; j < i; ++j)
            {
                IBlockState iblockstate = worldIn.getBlockState(position.up(j));

                if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                {
                    setBlockAndNotifyAdequately(worldIn, position.up(j), woodMetadata);
                }

                if (j < i - 1)
                {
                    iblockstate = worldIn.getBlockState(position.add(1, j, 0));

                    if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                    {
                        setBlockAndNotifyAdequately(worldIn, position.add(1, j, 0), woodMetadata);
                    }

                    iblockstate = worldIn.getBlockState(position.add(1, j, 1));

                    if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                    {
                        setBlockAndNotifyAdequately(worldIn, position.add(1, j, 1), woodMetadata);
                    }

                    iblockstate = worldIn.getBlockState(position.add(0, j, 1));

                    if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                    {
                        setBlockAndNotifyAdequately(worldIn, position.add(0, j, 1), woodMetadata);
                    }
                }
            }

            return true;
        }
    }

    private void createCrown(World worldIn, int x, int z, int y, int p_150541_5_, Random rand)
    {
        int i = rand.nextInt(5) + (useBaseHeight ? baseHeight : 3);
        int j = 0;

        for (int k = y - i; k <= y; ++k)
        {
            int l = y - k;
            int i1 = p_150541_5_ + MathHelper.floor((float)l / (float)i * 3.5F);
            growLeavesLayerStrict(worldIn, new BlockPos(x, k, z), i1 + (l > 0 && i1 == j && (k & 1) == 0 ? 1 : 0));
            j = i1;
        }
    }

    public void generateSaplings(World worldIn, Random random, BlockPos pos)
    {
        placePodzolCircle(worldIn, pos.west().north());
        placePodzolCircle(worldIn, pos.east(2).north());
        placePodzolCircle(worldIn, pos.west().south(2));
        placePodzolCircle(worldIn, pos.east(2).south(2));

        for (int i = 0; i < 5; ++i)
        {
            int j = random.nextInt(64);
            int k = j % 8;
            int l = j / 8;

            if (k == 0 || k == 7 || l == 0 || l == 7)
            {
                placePodzolCircle(worldIn, pos.add(-3 + k, 0, -3 + l));
            }
        }
    }

    private void placePodzolCircle(World worldIn, BlockPos center)
    {
        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                if (Math.abs(i) != 2 || Math.abs(j) != 2)
                {
                    placePodzolAt(worldIn, center.add(i, 0, j));
                }
            }
        }
    }

    private void placePodzolAt(World worldIn, BlockPos pos)
    {
        for (int i = 2; i >= -3; --i)
        {
            BlockPos blockpos = pos.up(i);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (block == Blocks.GRASS || block == Blocks.DIRT)
            {
                setBlockAndNotifyAdequately(worldIn, blockpos, PODZOL);
                break;
            }

            if (iblockstate.getMaterial() != Material.AIR && i < 0)
            {
                break;
            }
        }
    }
}
