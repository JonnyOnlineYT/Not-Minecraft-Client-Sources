package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFrostedIce extends BlockIce {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

   public BlockFrostedIce() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
   }

   public int getMetaFromState(IBlockState state) {
      return (Integer)state.getValue(AGE);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(AGE, MathHelper.clamp(meta, 0, 3));
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11 - (Integer)state.getValue(AGE) - state.getLightOpacity()) {
         this.slightlyMelt(worldIn, pos, state, rand, true);
      } else {
         worldIn.scheduleUpdate(pos, this, MathHelper.getInt((Random)rand, 20, 40));
      }

   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (blockIn == this) {
         int i = this.countNeighbors(worldIn, pos);
         if (i < 2) {
            this.turnIntoWater(worldIn, pos);
         }
      }

   }

   private int countNeighbors(World p_185680_1_, BlockPos p_185680_2_) {
      int i = 0;
      EnumFacing[] var4 = EnumFacing.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumFacing enumfacing = var4[var6];
         if (p_185680_1_.getBlockState(p_185680_2_.offset(enumfacing)).getBlock() == this) {
            ++i;
            if (i >= 4) {
               return i;
            }
         }
      }

      return i;
   }

   protected void slightlyMelt(World p_185681_1_, BlockPos p_185681_2_, IBlockState p_185681_3_, Random p_185681_4_, boolean p_185681_5_) {
      int i = (Integer)p_185681_3_.getValue(AGE);
      if (i < 3) {
         p_185681_1_.setBlockState(p_185681_2_, p_185681_3_.withProperty(AGE, i + 1), 2);
         p_185681_1_.scheduleUpdate(p_185681_2_, this, MathHelper.getInt((Random)p_185681_4_, 20, 40));
      } else {
         this.turnIntoWater(p_185681_1_, p_185681_2_);
         if (p_185681_5_) {
            EnumFacing[] var7 = EnumFacing.values();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               EnumFacing enumfacing = var7[var9];
               BlockPos blockpos = p_185681_2_.offset(enumfacing);
               IBlockState iblockstate = p_185681_1_.getBlockState(blockpos);
               if (iblockstate.getBlock() == this) {
                  this.slightlyMelt(p_185681_1_, blockpos, iblockstate, p_185681_4_, false);
               }
            }
         }
      }

   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{AGE});
   }

   public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
      return ItemStack.field_190927_a;
   }
}
