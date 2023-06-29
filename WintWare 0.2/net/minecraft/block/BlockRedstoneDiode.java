package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode extends BlockHorizontal {
   protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
   protected final boolean isRepeaterPowered;

   protected BlockRedstoneDiode(boolean powered) {
      super(Material.CIRCUITS);
      this.isRepeaterPowered = powered;
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      return REDSTONE_DIODE_AABB;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.down()).isFullyOpaque() ? super.canPlaceBlockAt(worldIn, pos) : false;
   }

   public boolean canBlockStay(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.down()).isFullyOpaque();
   }

   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!this.isLocked(worldIn, pos, state)) {
         boolean flag = this.shouldBePowered(worldIn, pos, state);
         if (this.isRepeaterPowered && !flag) {
            worldIn.setBlockState(pos, this.getUnpoweredState(state), 2);
         } else if (!this.isRepeaterPowered) {
            worldIn.setBlockState(pos, this.getPoweredState(state), 2);
            if (!flag) {
               worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
            }
         }
      }

   }

   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return side.getAxis() != EnumFacing.Axis.Y;
   }

   protected boolean isPowered(IBlockState state) {
      return this.isRepeaterPowered;
   }

   public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return blockState.getWeakPower(blockAccess, pos, side);
   }

   public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      if (!this.isPowered(blockState)) {
         return 0;
      } else {
         return blockState.getValue(FACING) == side ? this.getActiveSignal(blockAccess, pos, blockState) : 0;
      }
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (this.canBlockStay(worldIn, pos)) {
         this.updateState(worldIn, pos, state);
      } else {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
         EnumFacing[] var6 = EnumFacing.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EnumFacing enumfacing = var6[var8];
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
         }
      }

   }

   protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
      if (!this.isLocked(worldIn, pos, state)) {
         boolean flag = this.shouldBePowered(worldIn, pos, state);
         if (this.isRepeaterPowered != flag && !worldIn.isBlockTickPending(pos, this)) {
            int i = -1;
            if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
               i = -3;
            } else if (this.isRepeaterPowered) {
               i = -2;
            }

            worldIn.updateBlockTick(pos, this, this.getDelay(state), i);
         }
      }

   }

   public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
      return false;
   }

   protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
      return this.calculateInputStrength(worldIn, pos, state) > 0;
   }

   protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      BlockPos blockpos = pos.offset(enumfacing);
      int i = worldIn.getRedstonePower(blockpos, enumfacing);
      if (i >= 15) {
         return i;
      } else {
         IBlockState iblockstate = worldIn.getBlockState(blockpos);
         return Math.max(i, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? (Integer)iblockstate.getValue(BlockRedstoneWire.POWER) : 0);
      }
   }

   protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      EnumFacing enumfacing1 = enumfacing.rotateY();
      EnumFacing enumfacing2 = enumfacing.rotateYCCW();
      return Math.max(this.getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
   }

   protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      Block block = iblockstate.getBlock();
      if (this.isAlternateInput(iblockstate)) {
         if (block == Blocks.REDSTONE_BLOCK) {
            return 15;
         } else {
            return block == Blocks.REDSTONE_WIRE ? (Integer)iblockstate.getValue(BlockRedstoneWire.POWER) : worldIn.getStrongPower(pos, side);
         }
      } else {
         return 0;
      }
   }

   public boolean canProvidePower(IBlockState state) {
      return true;
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
   }

   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      if (this.shouldBePowered(worldIn, pos, state)) {
         worldIn.scheduleUpdate(pos, this, 1);
      }

   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      this.notifyNeighbors(worldIn, pos, state);
   }

   protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      BlockPos blockpos = pos.offset(enumfacing.getOpposite());
      worldIn.func_190524_a(blockpos, this, pos);
      worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
   }

   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
      if (this.isRepeaterPowered) {
         EnumFacing[] var4 = EnumFacing.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumFacing enumfacing = var4[var6];
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
         }
      }

      super.onBlockDestroyedByPlayer(worldIn, pos, state);
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   protected boolean isAlternateInput(IBlockState state) {
      return state.canProvidePower();
   }

   protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
      return 15;
   }

   public static boolean isDiode(IBlockState state) {
      return Blocks.UNPOWERED_REPEATER.isSameDiode(state) || Blocks.UNPOWERED_COMPARATOR.isSameDiode(state);
   }

   public boolean isSameDiode(IBlockState state) {
      Block block = state.getBlock();
      return block == this.getPoweredState(this.getDefaultState()).getBlock() || block == this.getUnpoweredState(this.getDefaultState()).getBlock();
   }

   public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = ((EnumFacing)state.getValue(FACING)).getOpposite();
      BlockPos blockpos = pos.offset(enumfacing);
      if (isDiode(worldIn.getBlockState(blockpos))) {
         return worldIn.getBlockState(blockpos).getValue(FACING) != enumfacing;
      } else {
         return false;
      }
   }

   protected int getTickDelay(IBlockState state) {
      return this.getDelay(state);
   }

   protected abstract int getDelay(IBlockState var1);

   protected abstract IBlockState getPoweredState(IBlockState var1);

   protected abstract IBlockState getUnpoweredState(IBlockState var1);

   public boolean isAssociatedBlock(Block other) {
      return this.isSameDiode(other.getDefaultState());
   }

   public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return p_193383_4_ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
