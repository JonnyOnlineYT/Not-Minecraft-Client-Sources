package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenDoublePlant extends WorldGenerator
{
    private BlockDoublePlant.EnumPlantType plantType;

    public void setPlantType(BlockDoublePlant.EnumPlantType plantTypeIn)
    {
        plantType = plantTypeIn;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        boolean flag = false;

        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 254) && Blocks.DOUBLE_PLANT.canPlaceBlockAt(worldIn, blockpos))
            {
                Blocks.DOUBLE_PLANT.placeAt(worldIn, blockpos, plantType, 2);
                flag = true;
            }
        }

        return flag;
    }
}
