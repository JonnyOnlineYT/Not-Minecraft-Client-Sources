package net.minecraft.block;

import com.google.common.base.Predicates;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndPortalFrame extends Block {
   public static final PropertyDirection FACING;
   public static final PropertyBool EYE;
   protected static final AxisAlignedBB AABB_BLOCK;
   protected static final AxisAlignedBB AABB_EYE;
   private static BlockPattern portalShape;

   public BlockEndPortalFrame() {
      super(Material.ROCK, MapColor.GREEN);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EYE, false));
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      return AABB_BLOCK;
   }

   public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BLOCK);
      if ((Boolean)worldIn.getBlockState(pos).getValue(EYE)) {
         addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EYE);
      }

   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.field_190931_a;
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(EYE, false);
   }

   public boolean hasComparatorInputOverride(IBlockState state) {
      return true;
   }

   public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
      return (Boolean)blockState.getValue(EYE) ? 15 : 0;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(EYE, (meta & 4) != 0).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
      if ((Boolean)state.getValue(EYE)) {
         i |= 4;
      }

      return i;
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{FACING, EYE});
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public static BlockPattern getOrCreatePortalShape() {
      if (portalShape == null) {
         portalShape = FactoryBlockPattern.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?").where('?', BlockWorldState.hasState(BlockStateMatcher.ANY)).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(EnumFacing.SOUTH)))).where('>', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(EnumFacing.WEST)))).where('v', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(EnumFacing.NORTH)))).where('<', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_PORTAL_FRAME).where(EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(EnumFacing.EAST)))).build();
      }

      return portalShape;
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return p_193383_4_ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }

   static {
      FACING = BlockHorizontal.FACING;
      EYE = PropertyBool.create("eye");
      AABB_BLOCK = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
      AABB_EYE = new AxisAlignedBB(0.3125D, 0.8125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
   }
}
