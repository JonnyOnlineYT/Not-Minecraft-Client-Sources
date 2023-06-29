package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenBush extends WorldGenerator
{
    private final BlockBush block;

    public WorldGenBush(BlockBush blockIn)
    {
        block = blockIn;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255) && block.canBlockStay(worldIn, blockpos, block.getDefaultState()))
            {
                worldIn.setBlockState(blockpos, block.getDefaultState(), 2);
            }
        }

        return true;
    }
}
