package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockStoneSlab extends BlockSlab
{
    public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
    public static final PropertyEnum<BlockStoneSlab.EnumType> VARIANT = PropertyEnum.create("variant", BlockStoneSlab.EnumType.class);

    public BlockStoneSlab()
    {
        super(Material.ROCK);
        IBlockState iblockstate = blockState.getBaseState();

        if (isDouble())
        {
            iblockstate = iblockstate.withProperty(SEAMLESS, Boolean.valueOf(false));
        }
        else
        {
            iblockstate = iblockstate.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        setDefaultState(iblockstate.withProperty(VARIANT, BlockStoneSlab.EnumType.STONE));
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.STONE_SLAB);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Blocks.STONE_SLAB, 1, state.getValue(VARIANT).getMetadata());
    }

    /**
     * Returns the slab block name with the type associated with it
     */
    public String getUnlocalizedName(int meta)
    {
        return getUnlocalizedName() + "." + BlockStoneSlab.EnumType.byMetadata(meta).getUnlocalizedName();
    }

    public IProperty<?> getVariantProperty()
    {
        return VARIANT;
    }

    public Comparable<?> getTypeForItem(ItemStack stack)
    {
        return BlockStoneSlab.EnumType.byMetadata(stack.getMetadata() & 7);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab)
    {
        for (BlockStoneSlab.EnumType blockstoneslab$enumtype : BlockStoneSlab.EnumType.values())
        {
            if (blockstoneslab$enumtype != BlockStoneSlab.EnumType.WOOD)
            {
                tab.add(new ItemStack(this, 1, blockstoneslab$enumtype.getMetadata()));
            }
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = getDefaultState().withProperty(VARIANT, BlockStoneSlab.EnumType.byMetadata(meta & 7));

        if (isDouble())
        {
            iblockstate = iblockstate.withProperty(SEAMLESS, Boolean.valueOf((meta & 8) != 0));
        }
        else
        {
            iblockstate = iblockstate.withProperty(BlockSlab.HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(VARIANT).getMetadata();

        if (isDouble())
        {
            if (state.getValue(SEAMLESS).booleanValue())
            {
                i |= 8;
            }
        }
        else if (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP)
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return isDouble() ? new BlockStateContainer(this, SEAMLESS, VARIANT) : new BlockStateContainer(this, BlockSlab.HALF, VARIANT);
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT).getMetadata();
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_)
    {
        return state.getValue(VARIANT).getMapColor();
    }

    public enum EnumType implements IStringSerializable
    {
        STONE(0, MapColor.STONE, "stone"),
        SAND(1, MapColor.SAND, "sandstone", "sand"),
        WOOD(2, MapColor.WOOD, "wood_old", "wood"),
        COBBLESTONE(3, MapColor.STONE, "cobblestone", "cobble"),
        BRICK(4, MapColor.RED, "brick"),
        SMOOTHBRICK(5, MapColor.STONE, "stone_brick", "smoothStoneBrick"),
        NETHERBRICK(6, MapColor.NETHERRACK, "nether_brick", "netherBrick"),
        QUARTZ(7, MapColor.QUARTZ, "quartz");

        private static final BlockStoneSlab.EnumType[] META_LOOKUP = new BlockStoneSlab.EnumType[values().length];
        private final int meta;
        private final MapColor mapColor;
        private final String name;
        private final String unlocalizedName;

        EnumType(int p_i46381_3_, MapColor p_i46381_4_, String p_i46381_5_)
        {
            this(p_i46381_3_, p_i46381_4_, p_i46381_5_, p_i46381_5_);
        }

        EnumType(int p_i46382_3_, MapColor p_i46382_4_, String p_i46382_5_, String p_i46382_6_)
        {
            meta = p_i46382_3_;
            mapColor = p_i46382_4_;
            name = p_i46382_5_;
            unlocalizedName = p_i46382_6_;
        }

        public int getMetadata()
        {
            return meta;
        }

        public MapColor getMapColor()
        {
            return mapColor;
        }

        public String toString()
        {
            return name;
        }

        public static BlockStoneSlab.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return name;
        }

        public String getUnlocalizedName()
        {
            return unlocalizedName;
        }

        static {
            for (BlockStoneSlab.EnumType blockstoneslab$enumtype : values())
            {
                META_LOOKUP[blockstoneslab$enumtype.getMetadata()] = blockstoneslab$enumtype;
            }
        }
    }
}
