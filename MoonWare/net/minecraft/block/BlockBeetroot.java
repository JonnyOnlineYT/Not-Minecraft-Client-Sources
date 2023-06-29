package net.minecraft.block;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBeetroot extends BlockCrops
{
    public static final PropertyInteger BEETROOT_AGE = PropertyInteger.create("age", 0, 3);
    private static final AxisAlignedBB[] BEETROOT_AABB = {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)};

    protected PropertyInteger getAgeProperty()
    {
        return BEETROOT_AGE;
    }

    public int getMaxAge()
    {
        return 3;
    }

    protected Item getSeed()
    {
        return Items.BEETROOT_SEEDS;
    }

    protected Item getCrop()
    {
        return Items.BEETROOT;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (rand.nextInt(3) == 0)
        {
            checkAndDropBlock(worldIn, pos, state);
        }
        else
        {
            super.updateTick(worldIn, pos, state, rand);
        }
    }

    protected int getBonemealAgeIncrease(World worldIn)
    {
        return super.getBonemealAgeIncrease(worldIn) / 3;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BEETROOT_AGE);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BEETROOT_AABB[state.getValue(getAgeProperty()).intValue()];
    }
}
