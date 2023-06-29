package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<BlockRedstoneComparator.Mode> MODE = PropertyEnum.create("mode", BlockRedstoneComparator.Mode.class);

    public BlockRedstoneComparator(boolean powered)
    {
        super(powered);
        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)).withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE));
        isBlockContainer = true;
    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return I18n.translateToLocal("item.comparator.name");
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.COMPARATOR;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.COMPARATOR);
    }

    protected int getDelay(IBlockState state)
    {
        return 2;
    }

    protected IBlockState getPoweredState(IBlockState unpoweredState)
    {
        Boolean obool = unpoweredState.getValue(POWERED);
        BlockRedstoneComparator.Mode blockredstonecomparator$mode = unpoweredState.getValue(MODE);
        EnumFacing enumfacing = unpoweredState.getValue(BlockHorizontal.FACING);
        return Blocks.POWERED_COMPARATOR.getDefaultState().withProperty(BlockHorizontal.FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
    }

    protected IBlockState getUnpoweredState(IBlockState poweredState)
    {
        Boolean obool = poweredState.getValue(POWERED);
        BlockRedstoneComparator.Mode blockredstonecomparator$mode = poweredState.getValue(MODE);
        EnumFacing enumfacing = poweredState.getValue(BlockHorizontal.FACING);
        return Blocks.UNPOWERED_COMPARATOR.getDefaultState().withProperty(BlockHorizontal.FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
    }

    protected boolean isPowered(IBlockState state)
    {
        return isRepeaterPowered || state.getValue(POWERED).booleanValue();
    }

    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityComparator ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
    }

    private int calculateOutput(World worldIn, BlockPos pos, IBlockState state)
    {
        return state.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? Math.max(calculateInputStrength(worldIn, pos, state) - getPowerOnSides(worldIn, pos, state), 0) : calculateInputStrength(worldIn, pos, state);
    }

    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = calculateInputStrength(worldIn, pos, state);

        if (i >= 15)
        {
            return true;
        }
        else if (i == 0)
        {
            return false;
        }
        else
        {
            int j = getPowerOnSides(worldIn, pos, state);

            if (j == 0)
            {
                return true;
            }
            else
            {
                return i >= j;
            }
        }
    }

    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = super.calculateInputStrength(worldIn, pos, state);
        EnumFacing enumfacing = state.getValue(BlockHorizontal.FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.hasComparatorInputOverride())
        {
            i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
        }
        else if (i < 15 && iblockstate.isNormalCube())
        {
            blockpos = blockpos.offset(enumfacing);
            iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.hasComparatorInputOverride())
            {
                i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
            }
            else if (iblockstate.getMaterial() == Material.AIR)
            {
                EntityItemFrame entityitemframe = findItemFrame(worldIn, enumfacing, blockpos);

                if (entityitemframe != null)
                {
                    i = entityitemframe.getAnalogOutput();
                }
            }
        }

        return i;
    }

    @Nullable
    private EntityItemFrame findItemFrame(World worldIn, EnumFacing facing, BlockPos pos)
    {
        List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing;
            }
        });
        return list.size() == 1 ? list.get(0) : null;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY)
    {
        if (!playerIn.capabilities.allowEdit)
        {
            return false;
        }
        else
        {
            state = state.cycleProperty(MODE);
            float f = state.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F;
            worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            worldIn.setBlockState(pos, state, 2);
            onStateChange(worldIn, pos, state);
            return true;
        }
    }

    protected void updateState(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isBlockTickPending(pos, this))
        {
            int i = calculateOutput(worldIn, pos, state);
            TileEntity tileentity = worldIn.getTileEntity(pos);
            int j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;

            if (i != j || isPowered(state) != shouldBePowered(worldIn, pos, state))
            {
                if (isFacingTowardsRepeater(worldIn, pos, state))
                {
                    worldIn.updateBlockTick(pos, this, 2, -1);
                }
                else
                {
                    worldIn.updateBlockTick(pos, this, 2, 0);
                }
            }
        }
    }

    private void onStateChange(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = calculateOutput(worldIn, pos, state);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        int j = 0;

        if (tileentity instanceof TileEntityComparator)
        {
            TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
            j = tileentitycomparator.getOutputSignal();
            tileentitycomparator.setOutputSignal(i);
        }

        if (j != i || state.getValue(MODE) == BlockRedstoneComparator.Mode.COMPARE)
        {
            boolean flag1 = shouldBePowered(worldIn, pos, state);
            boolean flag = isPowered(state);

            if (flag && !flag1)
            {
                worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 2);
            }
            else if (!flag && flag1)
            {
                worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 2);
            }

            notifyNeighbors(worldIn, pos, state);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (isRepeaterPowered)
        {
            worldIn.setBlockState(pos, getUnpoweredState(state).withProperty(POWERED, Boolean.valueOf(true)), 4);
        }

        onStateChange(worldIn, pos, state);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, createNewTileEntity(worldIn, 0));
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
        notifyNeighbors(worldIn, pos, state);
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called. On the Server, this may perform additional
     * changes to the world, like pistons replacing the block with an extended base. On the client, the update may
     * involve replacing tile entities, playing sounds, or performing other visual actions to reflect the server side
     * changes.
     */
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityComparator();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0)).withProperty(MODE, (meta & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(BlockHorizontal.FACING).getHorizontalIndex();

        if (state.getValue(POWERED).booleanValue())
        {
            i |= 8;
        }

        if (state.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT)
        {
            i |= 4;
        }

        return i;
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

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockHorizontal.FACING, MODE, POWERED);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing().getOpposite()).withProperty(POWERED, Boolean.valueOf(false)).withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE);
    }

    public enum Mode implements IStringSerializable
    {
        COMPARE("compare"),
        SUBTRACT("subtract");

        private final String name;

        Mode(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return name;
        }

        public String getName()
        {
            return name;
        }
    }
}
