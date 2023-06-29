package net.minecraft.block;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRedstoneRepeater extends BlockRedstoneDiode
{
    public static final PropertyBool LOCKED = PropertyBool.create("locked");
    public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);

    protected BlockRedstoneRepeater(boolean powered)
    {
        super(powered);
        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH).withProperty(DELAY, Integer.valueOf(1)).withProperty(LOCKED, Boolean.valueOf(false)));
    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return I18n.translateToLocal("item.diode.name");
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(LOCKED, Boolean.valueOf(isLocked(worldIn, pos, state)));
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY)
    {
        if (!playerIn.capabilities.allowEdit)
        {
            return false;
        }
        else
        {
            worldIn.setBlockState(pos, state.cycleProperty(DELAY), 3);
            return true;
        }
    }

    protected int getDelay(IBlockState state)
    {
        return state.getValue(DELAY).intValue() * 2;
    }

    protected IBlockState getPoweredState(IBlockState unpoweredState)
    {
        Integer integer = unpoweredState.getValue(DELAY);
        Boolean obool = unpoweredState.getValue(LOCKED);
        EnumFacing enumfacing = unpoweredState.getValue(BlockHorizontal.FACING);
        return Blocks.POWERED_REPEATER.getDefaultState().withProperty(BlockHorizontal.FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    protected IBlockState getUnpoweredState(IBlockState poweredState)
    {
        Integer integer = poweredState.getValue(DELAY);
        Boolean obool = poweredState.getValue(LOCKED);
        EnumFacing enumfacing = poweredState.getValue(BlockHorizontal.FACING);
        return Blocks.UNPOWERED_REPEATER.getDefaultState().withProperty(BlockHorizontal.FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.REPEATER;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.REPEATER);
    }

    public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        return getPowerOnSides(worldIn, pos, state) > 0;
    }

    protected boolean isAlternateInput(IBlockState state)
    {
        return BlockRedstoneDiode.isDiode(state);
    }

    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (isRepeaterPowered)
        {
            EnumFacing enumfacing = stateIn.getValue(BlockHorizontal.FACING);
            double d0 = (double)((float)pos.getX() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.4F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double d2 = (double)((float)pos.getZ() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            float f = -5.0F;

            if (rand.nextBoolean())
            {
                f = (float)(stateIn.getValue(DELAY).intValue() * 2 - 1);
            }

            f = f / 16.0F;
            double d3 = f * (float)enumfacing.getFrontOffsetX();
            double d4 = f * (float)enumfacing.getFrontOffsetZ();
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        notifyNeighbors(worldIn, pos, state);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta)).withProperty(LOCKED, Boolean.valueOf(false)).withProperty(DELAY, Integer.valueOf(1 + (meta >> 2)));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
        i = i | state.getValue(DELAY).intValue() - 1 << 2;
        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING, DELAY, LOCKED);
    }
}
