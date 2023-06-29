package net.minecraft.block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockEndRod extends BlockDirectional
{
    protected static final AxisAlignedBB END_ROD_VERTICAL_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB END_ROD_NS_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB END_ROD_EW_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    protected BlockEndRod()
    {
        super(Material.CIRCUITS);
        setDefaultState(blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockDirectional.FACING, rot.rotate(state.getValue(BlockDirectional.FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withProperty(BlockDirectional.FACING, mirrorIn.mirror(state.getValue(BlockDirectional.FACING)));
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(BlockDirectional.FACING).getAxis())
        {
            case X:
            default:
                return END_ROD_EW_AABB;

            case Z:
                return END_ROD_NS_AABB;

            case Y:
                return END_ROD_VERTICAL_AABB;
        }
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing.getOpposite()));

        if (iblockstate.getBlock() == Blocks.END_ROD)
        {
            EnumFacing enumfacing = iblockstate.getValue(BlockDirectional.FACING);

            if (enumfacing == facing)
            {
                return getDefaultState().withProperty(BlockDirectional.FACING, facing.getOpposite());
            }
        }

        return getDefaultState().withProperty(BlockDirectional.FACING, facing);
    }

    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = stateIn.getValue(BlockDirectional.FACING);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = 0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F;

        if (rand.nextInt(5) == 0)
        {
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0 + (double)enumfacing.getFrontOffsetX() * d3, d1 + (double)enumfacing.getFrontOffsetY() * d3, d2 + (double)enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
        }
    }

    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = getDefaultState();
        iblockstate = iblockstate.withProperty(BlockDirectional.FACING, EnumFacing.getFront(meta));
        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockDirectional.FACING).getIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockDirectional.FACING);
    }

    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.NORMAL;
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
