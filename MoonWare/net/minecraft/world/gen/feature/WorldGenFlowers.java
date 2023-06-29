package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenFlowers extends WorldGenerator
{
    private BlockFlower flower;
    private IBlockState state;

    public WorldGenFlowers(BlockFlower flowerIn, BlockFlower.EnumFlowerType type)
    {
        setGeneratedBlock(flowerIn, type);
    }

    public void setGeneratedBlock(BlockFlower flowerIn, BlockFlower.EnumFlowerType typeIn)
    {
        flower = flowerIn;
        state = flowerIn.getDefaultState().withProperty(flowerIn.getTypeProperty(), typeIn);
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255) && flower.canBlockStay(worldIn, blockpos, state))
            {
                worldIn.setBlockState(blockpos, state, 2);
            }
        }

        return true;
    }
}
