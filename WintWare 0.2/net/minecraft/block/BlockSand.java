package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSand extends BlockFalling {
   public static final PropertyEnum<BlockSand.EnumType> VARIANT = PropertyEnum.create("variant", BlockSand.EnumType.class);

   public BlockSand() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSand.EnumType.SAND));
   }

   public int damageDropped(IBlockState state) {
      return ((BlockSand.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      BlockSand.EnumType[] var3 = BlockSand.EnumType.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockSand.EnumType blocksand$enumtype = var3[var5];
         tab.add(new ItemStack(this, 1, blocksand$enumtype.getMetadata()));
      }

   }

   public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
      return ((BlockSand.EnumType)state.getValue(VARIANT)).getMapColor();
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockSand.EnumType.byMetadata(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockSand.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{VARIANT});
   }

   public int getDustColor(IBlockState p_189876_1_) {
      BlockSand.EnumType blocksand$enumtype = (BlockSand.EnumType)p_189876_1_.getValue(VARIANT);
      return blocksand$enumtype.getDustColor();
   }

   public static enum EnumType implements IStringSerializable {
      SAND(0, "sand", "default", MapColor.SAND, -2370656),
      RED_SAND(1, "red_sand", "red", MapColor.ADOBE, -5679071);

      private static final BlockSand.EnumType[] META_LOOKUP = new BlockSand.EnumType[values().length];
      private final int meta;
      private final String name;
      private final MapColor mapColor;
      private final String unlocalizedName;
      private final int dustColor;

      private EnumType(int p_i47157_3_, String p_i47157_4_, String p_i47157_5_, MapColor p_i47157_6_, int p_i47157_7_) {
         this.meta = p_i47157_3_;
         this.name = p_i47157_4_;
         this.mapColor = p_i47157_6_;
         this.unlocalizedName = p_i47157_5_;
         this.dustColor = p_i47157_7_;
      }

      public int getDustColor() {
         return this.dustColor;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String toString() {
         return this.name;
      }

      public MapColor getMapColor() {
         return this.mapColor;
      }

      public static BlockSand.EnumType byMetadata(int meta) {
         if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }

      public String getName() {
         return this.name;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      static {
         BlockSand.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockSand.EnumType blocksand$enumtype = var0[var2];
            META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
         }

      }
   }
}
