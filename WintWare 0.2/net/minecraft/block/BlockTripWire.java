package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyBool ATTACHED = PropertyBool.create("attached");
   public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0625D, 0.0D, 1.0D, 0.15625D, 1.0D);
   protected static final AxisAlignedBB TRIP_WRITE_ATTACHED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

   public BlockTripWire() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(ATTACHED, false).withProperty(DISARMED, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
      this.setTickRandomly(true);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      return !(Boolean)state.getValue(ATTACHED) ? TRIP_WRITE_ATTACHED_AABB : AABB;
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state.withProperty(NORTH, isConnectedTo(worldIn, pos, state, EnumFacing.NORTH)).withProperty(EAST, isConnectedTo(worldIn, pos, state, EnumFacing.EAST)).withProperty(SOUTH, isConnectedTo(worldIn, pos, state, EnumFacing.SOUTH)).withProperty(WEST, isConnectedTo(worldIn, pos, state, EnumFacing.WEST));
   }

   @Nullable
   public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return NULL_AABB;
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.STRING;
   }

   public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
      return new ItemStack(Items.STRING);
   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      worldIn.setBlockState(pos, state, 3);
      this.notifyHook(worldIn, pos, state);
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      this.notifyHook(worldIn, pos, state.withProperty(POWERED, true));
   }

   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
      if (!worldIn.isRemote && !player.getHeldItemMainhand().func_190926_b() && player.getHeldItemMainhand().getItem() == Items.SHEARS) {
         worldIn.setBlockState(pos, state.withProperty(DISARMED, true), 4);
      }

   }

   private void notifyHook(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing[] var4 = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST};
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumFacing enumfacing = var4[var6];

         for(int i = 1; i < 42; ++i) {
            BlockPos blockpos = pos.offset(enumfacing, i);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK) {
               if (iblockstate.getValue(BlockTripWireHook.FACING) == enumfacing.getOpposite()) {
                  Blocks.TRIPWIRE_HOOK.calculateState(worldIn, blockpos, iblockstate, false, true, i, state);
               }
               break;
            }

            if (iblockstate.getBlock() != Blocks.TRIPWIRE) {
               break;
            }
         }
      }

   }

   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      if (!worldIn.isRemote && !(Boolean)state.getValue(POWERED)) {
         this.updateState(worldIn, pos);
      }

   }

   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.isRemote && (Boolean)worldIn.getBlockState(pos).getValue(POWERED)) {
         this.updateState(worldIn, pos);
      }

   }

   private void updateState(World worldIn, BlockPos pos) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      boolean flag = (Boolean)iblockstate.getValue(POWERED);
      boolean flag1 = false;
      List<? extends Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, iblockstate.getBoundingBox(worldIn, pos).offset(pos));
      if (!list.isEmpty()) {
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            Entity entity = (Entity)var7.next();
            if (!entity.doesEntityNotTriggerPressurePlate()) {
               flag1 = true;
               break;
            }
         }
      }

      if (flag1 != flag) {
         iblockstate = iblockstate.withProperty(POWERED, flag1);
         worldIn.setBlockState(pos, iblockstate, 3);
         this.notifyHook(worldIn, pos, iblockstate);
      }

      if (flag1) {
         worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
      }

   }

   public static boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing direction) {
      BlockPos blockpos = pos.offset(direction);
      IBlockState iblockstate = worldIn.getBlockState(blockpos);
      Block block = iblockstate.getBlock();
      if (block == Blocks.TRIPWIRE_HOOK) {
         EnumFacing enumfacing = direction.getOpposite();
         return iblockstate.getValue(BlockTripWireHook.FACING) == enumfacing;
      } else {
         return block == Blocks.TRIPWIRE;
      }
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(POWERED, (meta & 1) > 0).withProperty(ATTACHED, (meta & 4) > 0).withProperty(DISARMED, (meta & 8) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      if ((Boolean)state.getValue(POWERED)) {
         i |= 1;
      }

      if ((Boolean)state.getValue(ATTACHED)) {
         i |= 4;
      }

      if ((Boolean)state.getValue(DISARMED)) {
         i |= 8;
      }

      return i;
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      switch(rot) {
      case CLOCKWISE_180:
         return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
      case COUNTERCLOCKWISE_90:
         return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
      case CLOCKWISE_90:
         return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
      default:
         return state;
      }
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      switch(mirrorIn) {
      case LEFT_RIGHT:
         return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
      case FRONT_BACK:
         return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
      default:
         return super.withMirror(state, mirrorIn);
      }
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{POWERED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }
}
