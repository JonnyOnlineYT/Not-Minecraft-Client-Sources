package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockNewLeaf extends BlockLeaves
{
    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>()
    {
        public boolean apply(@Nullable BlockPlanks.EnumType p_apply_1_)
        {
            return p_apply_1_.getMetadata() >= 4;
        }
    });

    public BlockNewLeaf()
    {
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf(true)));
    }

    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
    {
        if (state.getValue(VARIANT) == BlockPlanks.EnumType.DARK_OAK && worldIn.rand.nextInt(chance) == 0)
        {
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.APPLE));
        }
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT).getMetadata();
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state) & 3);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab)
    {
        tab.add(new ItemStack(this, 1, 0));
        tab.add(new ItemStack(this, 1, 1));
    }

    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).getMetadata() - 4);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(VARIANT, getWoodType(meta)).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(VARIANT).getMetadata() - 4;

        if (!state.getValue(BlockLeaves.DECAYABLE).booleanValue())
        {
            i |= 4;
        }

        if (state.getValue(BlockLeaves.CHECK_DECAY).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    public BlockPlanks.EnumType getWoodType(int meta)
    {
        return BlockPlanks.EnumType.byMetadata((meta & 3) + 4);
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT, BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).getMetadata() - 4));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
}
