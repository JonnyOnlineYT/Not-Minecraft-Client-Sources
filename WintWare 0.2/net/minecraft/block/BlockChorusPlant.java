package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusPlant extends Block {
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool DOWN = PropertyBool.create("down");

   protected BlockChorusPlant() {
      super(Material.PLANTS, MapColor.PURPLE);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      Block block = worldIn.getBlockState(pos.down()).getBlock();
      Block block1 = worldIn.getBlockState(pos.up()).getBlock();
      Block block2 = worldIn.getBlockState(pos.north()).getBlock();
      Block block3 = worldIn.getBlockState(pos.east()).getBlock();
      Block block4 = worldIn.getBlockState(pos.south()).getBlock();
      Block block5 = worldIn.getBlockState(pos.west()).getBlock();
      return state.withProperty(DOWN, block == this || block == Blocks.CHORUS_FLOWER || block == Blocks.END_STONE).withProperty(UP, block1 == this || block1 == Blocks.CHORUS_FLOWER).withProperty(NORTH, block2 == this || block2 == Blocks.CHORUS_FLOWER).withProperty(EAST, block3 == this || block3 == Blocks.CHORUS_FLOWER).withProperty(SOUTH, block4 == this || block4 == Blocks.CHORUS_FLOWER).withProperty(WEST, block5 == this || block5 == Blocks.CHORUS_FLOWER);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      state = state.getActualState(source, pos);
      float f = 0.1875F;
      float f1 = (Boolean)state.getValue(WEST) ? 0.0F : 0.1875F;
      float f2 = (Boolean)state.getValue(DOWN) ? 0.0F : 0.1875F;
      float f3 = (Boolean)state.getValue(NORTH) ? 0.0F : 0.1875F;
      float f4 = (Boolean)state.getValue(EAST) ? 1.0F : 0.8125F;
      float f5 = (Boolean)state.getValue(UP) ? 1.0F : 0.8125F;
      float f6 = (Boolean)state.getValue(SOUTH) ? 1.0F : 0.8125F;
      return new AxisAlignedBB((double)f1, (double)f2, (double)f3, (double)f4, (double)f5, (double)f6);
   }

   public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
      if (!p_185477_7_) {
         state = state.getActualState(worldIn, pos);
      }

      float f = 0.1875F;
      float f1 = 0.8125F;
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D));
      if ((Boolean)state.getValue(WEST)) {
         addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0D, 0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D));
      }

      if ((Boolean)state.getValue(EAST)) {
         addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.8125D, 0.1875D, 0.1875D, 1.0D, 0.8125D, 0.8125D));
      }

      if ((Boolean)state.getValue(UP)) {
         addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.8125D, 0.1875D, 0.8125D, 1.0D, 0.8125D));
      }

      if ((Boolean)state.getValue(DOWN)) {
         addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.1875D, 0.8125D));
      }

      if ((Boolean)state.getValue(NORTH)) {
         addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D));
      }

      if ((Boolean)state.getValue(SOUTH)) {
         addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D));
      }

   }

   public int getMetaFromState(IBlockState state) {
      return 0;
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!this.canSurviveAt(worldIn, pos)) {
         worldIn.destroyBlock(pos, true);
      }

   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.CHORUS_FRUIT;
   }

   public int quantityDropped(Random random) {
      return random.nextInt(2);
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return super.canPlaceBlockAt(worldIn, pos) ? this.canSurviveAt(worldIn, pos) : false;
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (!this.canSurviveAt(worldIn, pos)) {
         worldIn.scheduleUpdate(pos, this, 1);
      }

   }

   public boolean canSurviveAt(World wordIn, BlockPos pos) {
      boolean flag = wordIn.isAirBlock(pos.up());
      boolean flag1 = wordIn.isAirBlock(pos.down());
      Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

      Block block1;
      do {
         BlockPos blockpos;
         Block block;
         do {
            if (!var5.hasNext()) {
               Block block2 = wordIn.getBlockState(pos.down()).getBlock();
               return block2 == this || block2 == Blocks.END_STONE;
            }

            EnumFacing enumfacing = (EnumFacing)var5.next();
            blockpos = pos.offset(enumfacing);
            block = wordIn.getBlockState(blockpos).getBlock();
         } while(block != this);

         if (!flag && !flag1) {
            return false;
         }

         block1 = wordIn.getBlockState(blockpos.down()).getBlock();
      } while(block1 != this && block1 != Blocks.END_STONE);

      return true;
   }

   public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();
      return block != this && block != Blocks.CHORUS_FLOWER && (side != EnumFacing.DOWN || block != Blocks.END_STONE);
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{NORTH, EAST, SOUTH, WEST, UP, DOWN});
   }

   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }
}
