package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable {
   public static final PropertyEnum<BlockTallGrass.EnumType> TYPE = PropertyEnum.create("type", BlockTallGrass.EnumType.class);
   protected static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

   protected BlockTallGrass() {
      super(Material.VINE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      return TALL_GRASS_AABB;
   }

   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
      return this.canSustainBush(worldIn.getBlockState(pos.down()));
   }

   public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return rand.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.field_190931_a;
   }

   public int quantityDroppedWithBonus(int fortune, Random random) {
      return 1 + random.nextInt(fortune * 2 + 1);
   }

   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
      if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
         player.addStat(StatList.getBlockStats(this));
         spawnAsEntity(worldIn, pos, new ItemStack(Blocks.TALLGRASS, 1, ((BlockTallGrass.EnumType)state.getValue(TYPE)).getMeta()));
      } else {
         super.harvestBlock(worldIn, player, pos, state, te, stack);
      }

   }

   public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
      return new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
   }

   public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      for(int i = 1; i < 3; ++i) {
         tab.add(new ItemStack(this, 1, i));
      }

   }

   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
      return state.getValue(TYPE) != BlockTallGrass.EnumType.DEAD_BUSH;
   }

   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      return true;
   }

   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;
      if (state.getValue(TYPE) == BlockTallGrass.EnumType.FERN) {
         blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN;
      }

      if (Blocks.DOUBLE_PLANT.canPlaceBlockAt(worldIn, pos)) {
         Blocks.DOUBLE_PLANT.placeAt(worldIn, pos, blockdoubleplant$enumplanttype, 2);
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(TYPE, BlockTallGrass.EnumType.byMetadata(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockTallGrass.EnumType)state.getValue(TYPE)).getMeta();
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{TYPE});
   }

   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.XYZ;
   }

   public static enum EnumType implements IStringSerializable {
      DEAD_BUSH(0, "dead_bush"),
      GRASS(1, "tall_grass"),
      FERN(2, "fern");

      private static final BlockTallGrass.EnumType[] META_LOOKUP = new BlockTallGrass.EnumType[values().length];
      private final int meta;
      private final String name;

      private EnumType(int meta, String name) {
         this.meta = meta;
         this.name = name;
      }

      public int getMeta() {
         return this.meta;
      }

      public String toString() {
         return this.name;
      }

      public static BlockTallGrass.EnumType byMetadata(int meta) {
         if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }

      public String getName() {
         return this.name;
      }

      static {
         BlockTallGrass.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockTallGrass.EnumType blocktallgrass$enumtype = var0[var2];
            META_LOOKUP[blocktallgrass$enumtype.getMeta()] = blocktallgrass$enumtype;
         }

      }
   }
}
