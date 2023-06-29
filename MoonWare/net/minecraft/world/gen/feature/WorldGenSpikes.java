package net.minecraft.world.gen.feature;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class WorldGenSpikes extends WorldGenerator
{
    private boolean crystalInvulnerable;
    private WorldGenSpikes.EndSpike spike;
    private BlockPos beamTarget;

    public void setSpike(WorldGenSpikes.EndSpike p_186143_1_)
    {
        spike = p_186143_1_;
    }

    public void setCrystalInvulnerable(boolean p_186144_1_)
    {
        crystalInvulnerable = p_186144_1_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        if (spike == null)
        {
            throw new IllegalStateException("Decoration requires priming with a spike");
        }
        else
        {
            int i = spike.getRadius();

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(new BlockPos(position.getX() - i, 0, position.getZ() - i), new BlockPos(position.getX() + i, spike.getHeight() + 10, position.getZ() + i)))
            {
                if (blockpos$mutableblockpos.distanceSq(position.getX(), blockpos$mutableblockpos.getY(), position.getZ()) <= (double)(i * i + 1) && blockpos$mutableblockpos.getY() < spike.getHeight())
                {
                    setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.OBSIDIAN.getDefaultState());
                }
                else if (blockpos$mutableblockpos.getY() > 65)
                {
                    setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
                }
            }

            if (spike.isGuarded())
            {
                for (int j = -2; j <= 2; ++j)
                {
                    for (int k = -2; k <= 2; ++k)
                    {
                        if (MathHelper.abs(j) == 2 || MathHelper.abs(k) == 2)
                        {
                            setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, spike.getHeight(), position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, spike.getHeight() + 1, position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, spike.getHeight() + 2, position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                        }

                        setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, spike.getHeight() + 3, position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                    }
                }
            }

            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(worldIn);
            entityendercrystal.setBeamTarget(beamTarget);
            entityendercrystal.setEntityInvulnerable(crystalInvulnerable);
            entityendercrystal.setLocationAndAngles((float)position.getX() + 0.5F, spike.getHeight() + 1, (float)position.getZ() + 0.5F, rand.nextFloat() * 360.0F, 0.0F);
            worldIn.spawnEntityInWorld(entityendercrystal);
            setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX(), spike.getHeight(), position.getZ()), Blocks.BEDROCK.getDefaultState());
            return true;
        }
    }

    /**
     * Sets the value that will be used in a call to entitycrystal.setBeamTarget.
     * At the moment, WorldGenSpikes.setBeamTarget is only ever called with a value of (0, 128, 0)
     */
    public void setBeamTarget(@Nullable BlockPos pos)
    {
        beamTarget = pos;
    }

    public static class EndSpike
    {
        private final int centerX;
        private final int centerZ;
        private final int radius;
        private final int height;
        private final boolean guarded;
        private final AxisAlignedBB topBoundingBox;

        public EndSpike(int p_i47020_1_, int p_i47020_2_, int p_i47020_3_, int p_i47020_4_, boolean p_i47020_5_)
        {
            centerX = p_i47020_1_;
            centerZ = p_i47020_2_;
            radius = p_i47020_3_;
            height = p_i47020_4_;
            guarded = p_i47020_5_;
            topBoundingBox = new AxisAlignedBB(p_i47020_1_ - p_i47020_3_, 0.0D, p_i47020_2_ - p_i47020_3_, p_i47020_1_ + p_i47020_3_, 256.0D, p_i47020_2_ + p_i47020_3_);
        }

        public boolean doesStartInChunk(BlockPos p_186154_1_)
        {
            int i = centerX - radius;
            int j = centerZ - radius;
            return p_186154_1_.getX() == (i & -16) && p_186154_1_.getZ() == (j & -16);
        }

        public int getCenterX()
        {
            return centerX;
        }

        public int getCenterZ()
        {
            return centerZ;
        }

        public int getRadius()
        {
            return radius;
        }

        public int getHeight()
        {
            return height;
        }

        public boolean isGuarded()
        {
            return guarded;
        }

        public AxisAlignedBB getTopBoundingBox()
        {
            return topBoundingBox;
        }
    }
}
