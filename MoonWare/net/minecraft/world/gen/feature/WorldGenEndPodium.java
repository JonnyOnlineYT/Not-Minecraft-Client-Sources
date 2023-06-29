package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndPodium extends WorldGenerator
{
    public static final BlockPos END_PODIUM_LOCATION = BlockPos.ORIGIN;
    public static final BlockPos END_PODIUM_CHUNK_POS = new BlockPos(END_PODIUM_LOCATION.getX() - 4 & -16, 0, END_PODIUM_LOCATION.getZ() - 4 & -16);
    private final boolean activePortal;

    public WorldGenEndPodium(boolean activePortalIn)
    {
        activePortal = activePortalIn;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(new BlockPos(position.getX() - 4, position.getY() - 1, position.getZ() - 4), new BlockPos(position.getX() + 4, position.getY() + 32, position.getZ() + 4)))
        {
            double d0 = blockpos$mutableblockpos.getDistance(position.getX(), blockpos$mutableblockpos.getY(), position.getZ());

            if (d0 <= 3.5D)
            {
                if (blockpos$mutableblockpos.getY() < position.getY())
                {
                    if (d0 <= 2.5D)
                    {
                        setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                    }
                    else if (blockpos$mutableblockpos.getY() < position.getY())
                    {
                        setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.END_STONE.getDefaultState());
                    }
                }
                else if (blockpos$mutableblockpos.getY() > position.getY())
                {
                    setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
                }
                else if (d0 > 2.5D)
                {
                    setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                }
                else if (activePortal)
                {
                    setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.END_PORTAL.getDefaultState());
                }
                else
                {
                    setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.AIR.getDefaultState());
                }
            }
        }

        for (int i = 0; i < 4; ++i)
        {
            setBlockAndNotifyAdequately(worldIn, position.up(i), Blocks.BEDROCK.getDefaultState());
        }

        BlockPos blockpos = position.up(2);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            setBlockAndNotifyAdequately(worldIn, blockpos.offset(enumfacing), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, enumfacing));
        }

        return true;
    }
}
