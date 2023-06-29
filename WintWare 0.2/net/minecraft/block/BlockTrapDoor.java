package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block {
   public static final PropertyDirection FACING;
   public static final PropertyBool OPEN;
   public static final PropertyEnum<BlockTrapDoor.DoorHalf> HALF;
   protected static final AxisAlignedBB EAST_OPEN_AABB;
   protected static final AxisAlignedBB WEST_OPEN_AABB;
   protected static final AxisAlignedBB SOUTH_OPEN_AABB;
   protected static final AxisAlignedBB NORTH_OPEN_AABB;
   protected static final AxisAlignedBB BOTTOM_AABB;
   protected static final AxisAlignedBB TOP_AABB;

   protected BlockTrapDoor(Material materialIn) {
      super(materialIn);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false).withProperty(HALF, BlockTrapDoor.DoorHalf.BOTTOM));
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      AxisAlignedBB axisalignedbb;
      if ((Boolean)state.getValue(OPEN)) {
         switch((EnumFacing)state.getValue(FACING)) {
         case NORTH:
         default:
            axisalignedbb = NORTH_OPEN_AABB;
            break;
         case SOUTH:
            axisalignedbb = SOUTH_OPEN_AABB;
            break;
         case WEST:
            axisalignedbb = WEST_OPEN_AABB;
            break;
         case EAST:
            axisalignedbb = EAST_OPEN_AABB;
         }
      } else if (state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP) {
         axisalignedbb = TOP_AABB;
      } else {
         axisalignedbb = BOTTOM_AABB;
      }

      return axisalignedbb;
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
      return !(Boolean)worldIn.getBlockState(pos).getValue(OPEN);
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
      if (this.blockMaterial == Material.IRON) {
         return false;
      } else {
         state = state.cycleProperty(OPEN);
         worldIn.setBlockState(pos, state, 2);
         this.playSound(playerIn, worldIn, pos, (Boolean)state.getValue(OPEN));
         return true;
      }
   }

   protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_) {
      int i;
      if (p_185731_4_) {
         i = this.blockMaterial == Material.IRON ? 1037 : 1007;
         worldIn.playEvent(player, i, pos, 0);
      } else {
         i = this.blockMaterial == Material.IRON ? 1036 : 1013;
         worldIn.playEvent(player, i, pos, 0);
      }

   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (!worldIn.isRemote) {
         boolean flag = worldIn.isBlockPowered(pos);
         if (flag || blockIn.getDefaultState().canProvidePower()) {
            boolean flag1 = (Boolean)state.getValue(OPEN);
            if (flag1 != flag) {
               worldIn.setBlockState(pos, state.withProperty(OPEN, flag), 2);
               this.playSound((EntityPlayer)null, worldIn, pos, flag);
            }
         }
      }

   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.getDefaultState();
      if (facing.getAxis().isHorizontal()) {
         iblockstate = iblockstate.withProperty(FACING, facing).withProperty(OPEN, false);
         iblockstate = iblockstate.withProperty(HALF, hitY > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
      } else {
         iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OPEN, false);
         iblockstate = iblockstate.withProperty(HALF, facing == EnumFacing.UP ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
      }

      if (worldIn.isBlockPowered(pos)) {
         iblockstate = iblockstate.withProperty(OPEN, true);
      }

      return iblockstate;
   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      return true;
   }

   protected static EnumFacing getFacing(int meta) {
      switch(meta & 3) {
      case 0:
         return EnumFacing.NORTH;
      case 1:
         return EnumFacing.SOUTH;
      case 2:
         return EnumFacing.WEST;
      case 3:
      default:
         return EnumFacing.EAST;
      }
   }

   protected static int getMetaForFacing(EnumFacing facing) {
      switch(facing) {
      case NORTH:
         return 0;
      case SOUTH:
         return 1;
      case WEST:
         return 2;
      case EAST:
      default:
         return 3;
      }
   }

   public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(OPEN, (meta & 4) != 0).withProperty(HALF, (meta & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | getMetaForFacing((EnumFacing)state.getValue(FACING));
      if ((Boolean)state.getValue(OPEN)) {
         i |= 4;
      }

      if (state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP) {
         i |= 8;
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
      return new BlockStateContainer(this, new IProperty[]{FACING, OPEN, HALF});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return (p_193383_4_ == EnumFacing.UP && p_193383_2_.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP || p_193383_4_ == EnumFacing.DOWN && p_193383_2_.getValue(HALF) == BlockTrapDoor.DoorHalf.BOTTOM) && !(Boolean)p_193383_2_.getValue(OPEN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }

   static {
      FACING = BlockHorizontal.FACING;
      OPEN = PropertyBool.create("open");
      HALF = PropertyEnum.create("half", BlockTrapDoor.DoorHalf.class);
      EAST_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
      WEST_OPEN_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
      SOUTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
      NORTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
      BOTTOM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
      TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);
   }

   public static enum DoorHalf implements IStringSerializable {
      TOP("top"),
      BOTTOM("bottom");

      private final String name;

      private DoorHalf(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }
   }
}
