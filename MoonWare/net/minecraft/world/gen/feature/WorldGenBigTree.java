package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class WorldGenBigTree extends WorldGenAbstractTree
{
    private Random rand;
    private World world;
    private BlockPos basePos = BlockPos.ORIGIN;
    int heightLimit;
    int height;
    double heightAttenuation = 0.618D;
    double branchSlope = 0.381D;
    double scaleWidth = 1.0D;
    double leafDensity = 1.0D;
    int trunkSize = 1;
    int heightLimitLimit = 12;

    /**
     * Sets the distance limit for how far away the generator will populate leaves from the base leaf node.
     */
    int leafDistanceLimit = 4;
    List<WorldGenBigTree.FoliageCoordinates> foliageCoords;

    public WorldGenBigTree(boolean notify)
    {
        super(notify);
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList()
    {
        height = (int)((double) heightLimit * heightAttenuation);

        if (height >= heightLimit)
        {
            height = heightLimit - 1;
        }

        int i = (int)(1.382D + Math.pow(leafDensity * (double) heightLimit / 13.0D, 2.0D));

        if (i < 1)
        {
            i = 1;
        }

        int j = basePos.getY() + height;
        int k = heightLimit - leafDistanceLimit;
        foliageCoords = Lists.newArrayList();
        foliageCoords.add(new WorldGenBigTree.FoliageCoordinates(basePos.up(k), j));

        for (; k >= 0; --k)
        {
            float f = layerSize(k);

            if (f >= 0.0F)
            {
                for (int l = 0; l < i; ++l)
                {
                    double d0 = scaleWidth * (double)f * ((double) rand.nextFloat() + 0.328D);
                    double d1 = (double)(rand.nextFloat() * 2.0F) * Math.PI;
                    double d2 = d0 * Math.sin(d1) + 0.5D;
                    double d3 = d0 * Math.cos(d1) + 0.5D;
                    BlockPos blockpos = basePos.add(d2, k - 1, d3);
                    BlockPos blockpos1 = blockpos.up(leafDistanceLimit);

                    if (checkBlockLine(blockpos, blockpos1) == -1)
                    {
                        int i1 = basePos.getX() - blockpos.getX();
                        int j1 = basePos.getZ() - blockpos.getZ();
                        double d4 = (double)blockpos.getY() - Math.sqrt(i1 * i1 + j1 * j1) * branchSlope;
                        int k1 = d4 > (double)j ? j : (int)d4;
                        BlockPos blockpos2 = new BlockPos(basePos.getX(), k1, basePos.getZ());

                        if (checkBlockLine(blockpos2, blockpos) == -1)
                        {
                            foliageCoords.add(new WorldGenBigTree.FoliageCoordinates(blockpos, blockpos2.getY()));
                        }
                    }
                }
            }
        }
    }

    void crosSection(BlockPos pos, float p_181631_2_, IBlockState p_181631_3_)
    {
        int i = (int)((double)p_181631_2_ + 0.618D);

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                if (Math.pow((double)Math.abs(j) + 0.5D, 2.0D) + Math.pow((double)Math.abs(k) + 0.5D, 2.0D) <= (double)(p_181631_2_ * p_181631_2_))
                {
                    BlockPos blockpos = pos.add(j, 0, k);
                    Material material = world.getBlockState(blockpos).getMaterial();

                    if (material == Material.AIR || material == Material.LEAVES)
                    {
                        setBlockAndNotifyAdequately(world, blockpos, p_181631_3_);
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    float layerSize(int y)
    {
        if ((float)y < (float) heightLimit * 0.3F)
        {
            return -1.0F;
        }
        else
        {
            float f = (float) heightLimit / 2.0F;
            float f1 = f - (float)y;
            float f2 = MathHelper.sqrt(f * f - f1 * f1);

            if (f1 == 0.0F)
            {
                f2 = f;
            }
            else if (Math.abs(f1) >= f)
            {
                return 0.0F;
            }

            return f2 * 0.5F;
        }
    }

    float leafSize(int y)
    {
        if (y >= 0 && y < leafDistanceLimit)
        {
            return y != 0 && y != leafDistanceLimit - 1 ? 3.0F : 2.0F;
        }
        else
        {
            return -1.0F;
        }
    }

    /**
     * Generates the leaves surrounding an individual entry in the leafNodes list.
     */
    void generateLeafNode(BlockPos pos)
    {
        for (int i = 0; i < leafDistanceLimit; ++i)
        {
            crosSection(pos.up(i), leafSize(i), Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
        }
    }

    void limb(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_)
    {
        BlockPos blockpos = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int i = getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;

        for (int j = 0; j <= i; ++j)
        {
            BlockPos blockpos1 = p_175937_1_.add(0.5F + (float)j * f, 0.5F + (float)j * f1, 0.5F + (float)j * f2);
            BlockLog.EnumAxis blocklog$enumaxis = getLogAxis(p_175937_1_, blockpos1);
            setBlockAndNotifyAdequately(world, blockpos1, p_175937_3_.getDefaultState().withProperty(BlockLog.LOG_AXIS, blocklog$enumaxis));
        }
    }

    /**
     * Returns the absolute greatest distance in the BlockPos object.
     */
    private int getGreatestDistance(BlockPos posIn)
    {
        int i = MathHelper.abs(posIn.getX());
        int j = MathHelper.abs(posIn.getY());
        int k = MathHelper.abs(posIn.getZ());

        if (k > i && k > j)
        {
            return k;
        }
        else
        {
            return j > i ? j : i;
        }
    }

    private BlockLog.EnumAxis getLogAxis(BlockPos p_175938_1_, BlockPos p_175938_2_)
    {
        BlockLog.EnumAxis blocklog$enumaxis = BlockLog.EnumAxis.Y;
        int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
        int k = Math.max(i, j);

        if (k > 0)
        {
            if (i == k)
            {
                blocklog$enumaxis = BlockLog.EnumAxis.X;
            }
            else if (j == k)
            {
                blocklog$enumaxis = BlockLog.EnumAxis.Z;
            }
        }

        return blocklog$enumaxis;
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    void generateLeaves()
    {
        for (WorldGenBigTree.FoliageCoordinates worldgenbigtree$foliagecoordinates : foliageCoords)
        {
            generateLeafNode(worldgenbigtree$foliagecoordinates);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    boolean leafNodeNeedsBase(int p_76493_1_)
    {
        return (double)p_76493_1_ >= (double) heightLimit * 0.2D;
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    void generateTrunk()
    {
        BlockPos blockpos = basePos;
        BlockPos blockpos1 = basePos.up(height);
        Block block = Blocks.LOG;
        limb(blockpos, blockpos1, block);

        if (trunkSize == 2)
        {
            limb(blockpos.east(), blockpos1.east(), block);
            limb(blockpos.east().south(), blockpos1.east().south(), block);
            limb(blockpos.south(), blockpos1.south(), block);
        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    void generateLeafNodeBases()
    {
        for (WorldGenBigTree.FoliageCoordinates worldgenbigtree$foliagecoordinates : foliageCoords)
        {
            int i = worldgenbigtree$foliagecoordinates.getBranchBase();
            BlockPos blockpos = new BlockPos(basePos.getX(), i, basePos.getZ());

            if (!blockpos.equals(worldgenbigtree$foliagecoordinates) && leafNodeNeedsBase(i - basePos.getY()))
            {
                limb(blockpos, worldgenbigtree$foliagecoordinates, Blocks.LOG);
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     */
    int checkBlockLine(BlockPos posOne, BlockPos posTwo)
    {
        BlockPos blockpos = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
        int i = getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;

        if (i == 0)
        {
            return -1;
        }
        else
        {
            for (int j = 0; j <= i; ++j)
            {
                BlockPos blockpos1 = posOne.add(0.5F + (float)j * f, 0.5F + (float)j * f1, 0.5F + (float)j * f2);

                if (!canGrowInto(world.getBlockState(blockpos1).getBlock()))
                {
                    return j;
                }
            }

            return -1;
        }
    }

    public void setDecorationDefaults()
    {
        leafDistanceLimit = 5;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        world = worldIn;
        basePos = position;
        this.rand = new Random(rand.nextLong());

        if (heightLimit == 0)
        {
            heightLimit = 5 + this.rand.nextInt(heightLimitLimit);
        }

        if (!validTreeLocation())
        {
            return false;
        }
        else
        {
            generateLeafNodeList();
            generateLeaves();
            generateTrunk();
            generateLeafNodeBases();
            return true;
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    private boolean validTreeLocation()
    {
        Block block = world.getBlockState(basePos.down()).getBlock();

        if (block != Blocks.DIRT && block != Blocks.GRASS && block != Blocks.FARMLAND)
        {
            return false;
        }
        else
        {
            int i = checkBlockLine(basePos, basePos.up(heightLimit - 1));

            if (i == -1)
            {
                return true;
            }
            else if (i < 6)
            {
                return false;
            }
            else
            {
                heightLimit = i;
                return true;
            }
        }
    }

    static class FoliageCoordinates extends BlockPos
    {
        private final int branchBase;

        public FoliageCoordinates(BlockPos pos, int p_i45635_2_)
        {
            super(pos.getX(), pos.getY(), pos.getZ());
            branchBase = p_i45635_2_;
        }

        public int getBranchBase()
        {
            return branchBase;
        }
    }
}
