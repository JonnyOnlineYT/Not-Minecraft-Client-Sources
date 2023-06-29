package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockHorizontal {
   public static final PropertyBool OPEN = PropertyBool.create("open");
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
   protected static final AxisAlignedBB AABB_COLLIDE_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
   protected static final AxisAlignedBB AABB_COLLIDE_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
   protected static final AxisAlignedBB AABB_COLLIDE_ZAXIS_INWALL = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
   protected static final AxisAlignedBB AABB_COLLIDE_XAXIS_INWALL = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);
   protected static final AxisAlignedBB AABB_CLOSED_SELECTED_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
   protected static final AxisAlignedBB AABB_CLOSED_SELECTED_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

   public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_) {
      super(Material.WOOD, p_i46394_1_.getMapColor());
      this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, false).withProperty(POWERED, false).withProperty(IN_WALL, false));
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      state = this.getActualState(state, source, pos);
      if ((Boolean)state.getValue(IN_WALL)) {
         return ((EnumFacing)state.getValue(FACING)).getAxis() == EnumFacing.Axis.X ? AABB_COLLIDE_XAXIS_INWALL : AABB_COLLIDE_ZAXIS_INWALL;
      } else {
         return ((EnumFacing)state.getValue(FACING)).getAxis() == EnumFacing.Axis.X ? AABB_COLLIDE_XAXIS : AABB_COLLIDE_ZAXIS;
      }
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue(FACING)).getAxis();
      if (enumfacing$axis == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.west()).getBlock() == Blocks.COBBLESTONE_WALL || worldIn.getBlockState(pos.east()).getBlock() == Blocks.COBBLESTONE_WALL) || enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north()).getBlock() == Blocks.COBBLESTONE_WALL || worldIn.getBlockState(pos.south()).getBlock() == Blocks.COBBLESTONE_WALL)) {
         state = state.withProperty(IN_WALL, true);
      }

      return state;
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.down()).getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
   }

   @Nullable
   public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      if ((Boolean)blockState.getValue(OPEN)) {
         return NULL_AABB;
      } else {
         return ((EnumFacing)blockState.getValue(FACING)).getAxis() == EnumFacing.Axis.Z ? AABB_CLOSED_SELECTED_ZAXIS : AABB_CLOSED_SELECTED_XAXIS;
      }
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
      return (Boolean)worldIn.getBlockState(pos).getValue(OPEN);
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      boolean flag = worldIn.isBlockPowered(pos);
      return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(OPEN, flag).withProperty(POWERED, flag).withProperty(IN_WALL, false);
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
      if ((Boolean)state.getValue(OPEN)) {
         state = state.withProperty(OPEN, false);
         worldIn.setBlockState(pos, state, 10);
      } else {
         EnumFacing enumfacing = EnumFacing.fromAngle((double)playerIn.rotationYaw);
         if (state.getValue(FACING) == enumfacing.getOpposite()) {
            state = state.withProperty(FACING, enumfacing);
         }

         state = state.withProperty(OPEN, true);
         worldIn.setBlockState(pos, state, 10);
      }

      worldIn.playEvent(playerIn, (Boolean)state.getValue(OPEN) ? 1008 : 1014, pos, 0);
      return true;
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (!worldIn.isRemote) {
         boolean flag = worldIn.isBlockPowered(pos);
         if ((Boolean)state.getValue(POWERED) != flag) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, flag).withProperty(OPEN, flag), 2);
            if ((Boolean)state.getValue(OPEN) != flag) {
               worldIn.playEvent((EntityPlayer)null, flag ? 1008 : 1014, pos, 0);
            }
         }
      }

   }

   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return true;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(OPEN, (meta & 4) != 0).withProperty(POWERED, (meta & 8) != 0);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
      if ((Boolean)state.getValue(POWERED)) {
         i |= 8;
      }

      if ((Boolean)state.getValue(OPEN)) {
         i |= 4;
      }

      return i;
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{FACING, OPEN, POWERED, IN_WALL});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      if (p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN) {
         return ((EnumFacing)p_193383_2_.getValue(FACING)).getAxis() == p_193383_4_.rotateY().getAxis() ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED;
      } else {
         return BlockFaceShape.UNDEFINED;
      }
   }
}
