package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyEnum<BlockWall.EnumType> VARIANT = PropertyEnum.create("variant", BlockWall.EnumType.class);
   protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[]{new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
   protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX;

   public BlockWall(Block modelBlock) {
      super(modelBlock.blockMaterial);
      this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(VARIANT, BlockWall.EnumType.NORMAL));
      this.setHardness(modelBlock.blockHardness);
      this.setResistance(modelBlock.blockResistance / 3.0F);
      this.setSoundType(modelBlock.blockSoundType);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      state = this.getActualState(state, source, pos);
      return AABB_BY_INDEX[getAABBIndex(state)];
   }

   public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
      if (!p_185477_7_) {
         state = this.getActualState(state, worldIn, pos);
      }

      addCollisionBoxToList(pos, entityBox, collidingBoxes, CLIP_AABB_BY_INDEX[getAABBIndex(state)]);
   }

   @Nullable
   public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      blockState = this.getActualState(blockState, worldIn, pos);
      return CLIP_AABB_BY_INDEX[getAABBIndex(blockState)];
   }

   private static int getAABBIndex(IBlockState state) {
      int i = 0;
      if ((Boolean)state.getValue(NORTH)) {
         i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
      }

      if ((Boolean)state.getValue(EAST)) {
         i |= 1 << EnumFacing.EAST.getHorizontalIndex();
      }

      if ((Boolean)state.getValue(SOUTH)) {
         i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
      }

      if ((Boolean)state.getValue(WEST)) {
         i |= 1 << EnumFacing.WEST.getHorizontalIndex();
      }

      return i;
   }

   public String getLocalizedName() {
      return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockWall.EnumType.NORMAL.getUnlocalizedName() + ".name");
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
      return false;
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   private boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing p_176253_3_) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      Block block = iblockstate.getBlock();
      BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, pos, p_176253_3_);
      boolean flag = blockfaceshape == BlockFaceShape.MIDDLE_POLE_THICK || blockfaceshape == BlockFaceShape.MIDDLE_POLE && block instanceof BlockFenceGate;
      return !func_194143_e(block) && blockfaceshape == BlockFaceShape.SOLID || flag;
   }

   protected static boolean func_194143_e(Block p_194143_0_) {
      return Block.func_193382_c(p_194143_0_) || p_194143_0_ == Blocks.BARRIER || p_194143_0_ == Blocks.MELON_BLOCK || p_194143_0_ == Blocks.PUMPKIN || p_194143_0_ == Blocks.LIT_PUMPKIN;
   }

   public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      BlockWall.EnumType[] var3 = BlockWall.EnumType.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockWall.EnumType blockwall$enumtype = var3[var5];
         tab.add(new ItemStack(this, 1, blockwall$enumtype.getMetadata()));
      }

   }

   public int damageDropped(IBlockState state) {
      return ((BlockWall.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return side == EnumFacing.DOWN ? super.shouldSideBeRendered(blockState, blockAccess, pos, side) : true;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(VARIANT, BlockWall.EnumType.byMetadata(meta));
   }

   public int getMetaFromState(IBlockState state) {
      return ((BlockWall.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      boolean flag = this.canConnectTo(worldIn, pos.north(), EnumFacing.SOUTH);
      boolean flag1 = this.canConnectTo(worldIn, pos.east(), EnumFacing.WEST);
      boolean flag2 = this.canConnectTo(worldIn, pos.south(), EnumFacing.NORTH);
      boolean flag3 = this.canConnectTo(worldIn, pos.west(), EnumFacing.EAST);
      boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;
      return state.withProperty(UP, !flag4 || !worldIn.isAirBlock(pos.up())).withProperty(NORTH, flag).withProperty(EAST, flag1).withProperty(SOUTH, flag2).withProperty(WEST, flag3);
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{UP, NORTH, EAST, WEST, SOUTH, VARIANT});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
   }

   static {
      CLIP_AABB_BY_INDEX = new AxisAlignedBB[]{AABB_BY_INDEX[0].setMaxY(1.5D), AABB_BY_INDEX[1].setMaxY(1.5D), AABB_BY_INDEX[2].setMaxY(1.5D), AABB_BY_INDEX[3].setMaxY(1.5D), AABB_BY_INDEX[4].setMaxY(1.5D), AABB_BY_INDEX[5].setMaxY(1.5D), AABB_BY_INDEX[6].setMaxY(1.5D), AABB_BY_INDEX[7].setMaxY(1.5D), AABB_BY_INDEX[8].setMaxY(1.5D), AABB_BY_INDEX[9].setMaxY(1.5D), AABB_BY_INDEX[10].setMaxY(1.5D), AABB_BY_INDEX[11].setMaxY(1.5D), AABB_BY_INDEX[12].setMaxY(1.5D), AABB_BY_INDEX[13].setMaxY(1.5D), AABB_BY_INDEX[14].setMaxY(1.5D), AABB_BY_INDEX[15].setMaxY(1.5D)};
   }

   public static enum EnumType implements IStringSerializable {
      NORMAL(0, "cobblestone", "normal"),
      MOSSY(1, "mossy_cobblestone", "mossy");

      private static final BlockWall.EnumType[] META_LOOKUP = new BlockWall.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String unlocalizedName;

      private EnumType(int meta, String name, String unlocalizedName) {
         this.meta = meta;
         this.name = name;
         this.unlocalizedName = unlocalizedName;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String toString() {
         return this.name;
      }

      public static BlockWall.EnumType byMetadata(int meta) {
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
         BlockWall.EnumType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockWall.EnumType blockwall$enumtype = var0[var2];
            META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype;
         }

      }
   }
}
