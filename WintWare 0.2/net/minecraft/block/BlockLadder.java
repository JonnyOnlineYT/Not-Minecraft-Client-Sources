package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block {
   public static final PropertyDirection FACING;
   protected static final AxisAlignedBB LADDER_EAST_AABB;
   protected static final AxisAlignedBB LADDER_WEST_AABB;
   protected static final AxisAlignedBB LADDER_SOUTH_AABB;
   protected static final AxisAlignedBB LADDER_NORTH_AABB;

   protected BlockLadder() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      switch((EnumFacing)state.getValue(FACING)) {
      case NORTH:
         return LADDER_NORTH_AABB;
      case SOUTH:
         return LADDER_SOUTH_AABB;
      case WEST:
         return LADDER_WEST_AABB;
      case EAST:
      default:
         return LADDER_EAST_AABB;
      }
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      if (this.func_193392_c(worldIn, pos.west(), side)) {
         return true;
      } else if (this.func_193392_c(worldIn, pos.east(), side)) {
         return true;
      } else {
         return this.func_193392_c(worldIn, pos.north(), side) ? true : this.func_193392_c(worldIn, pos.south(), side);
      }
   }

   private boolean func_193392_c(World p_193392_1_, BlockPos p_193392_2_, EnumFacing p_193392_3_) {
      IBlockState iblockstate = p_193392_1_.getBlockState(p_193392_2_);
      boolean flag = func_193382_c(iblockstate.getBlock());
      return !flag && iblockstate.func_193401_d(p_193392_1_, p_193392_2_, p_193392_3_) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      if (facing.getAxis().isHorizontal() && this.func_193392_c(worldIn, pos.offset(facing.getOpposite()), facing)) {
         return this.getDefaultState().withProperty(FACING, facing);
      } else {
         Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator();

         EnumFacing enumfacing;
         do {
            if (!var9.hasNext()) {
               return this.getDefaultState();
            }

            enumfacing = (EnumFacing)var9.next();
         } while(!this.func_193392_c(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing));

         return this.getDefaultState().withProperty(FACING, enumfacing);
      }
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      if (!this.func_193392_c(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

      super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
   }

   public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   public IBlockState getStateFromMeta(int meta) {
      EnumFacing enumfacing = EnumFacing.getFront(meta);
      if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
         enumfacing = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, enumfacing);
   }

   public int getMetaFromState(IBlockState state) {
      return ((EnumFacing)state.getValue(FACING)).getIndex();
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{FACING});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }

   static {
      FACING = BlockHorizontal.FACING;
      LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
      LADDER_WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
      LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
      LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
   }
}
