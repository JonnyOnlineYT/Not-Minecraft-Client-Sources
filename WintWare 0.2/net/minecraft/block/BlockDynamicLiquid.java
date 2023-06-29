package net.minecraft.block;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDynamicLiquid extends BlockLiquid {
   int adjacentSourceBlocks;

   protected BlockDynamicLiquid(Material materialIn) {
      super(materialIn);
   }

   private void placeStaticBlock(World worldIn, BlockPos pos, IBlockState currentState) {
      worldIn.setBlockState(pos, getStaticBlock(this.blockMaterial).getDefaultState().withProperty(LEVEL, currentState.getValue(LEVEL)), 2);
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      int i = (Integer)state.getValue(LEVEL);
      int j = 1;
      if (this.blockMaterial == Material.LAVA && !worldIn.provider.doesWaterVaporize()) {
         j = 2;
      }

      int k = this.tickRate(worldIn);
      int k1;
      if (i > 0) {
         int l = -100;
         this.adjacentSourceBlocks = 0;

         EnumFacing enumfacing;
         for(Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator(); var9.hasNext(); l = this.checkAdjacentBlock(worldIn, pos.offset(enumfacing), l)) {
            enumfacing = (EnumFacing)var9.next();
         }

         int i1 = l + j;
         if (i1 >= 8 || l < 0) {
            i1 = -1;
         }

         k1 = this.getDepth(worldIn.getBlockState(pos.up()));
         if (k1 >= 0) {
            if (k1 >= 8) {
               i1 = k1;
            } else {
               i1 = k1 + 8;
            }
         }

         if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.WATER) {
            IBlockState iblockstate = worldIn.getBlockState(pos.down());
            if (iblockstate.getMaterial().isSolid()) {
               i1 = 0;
            } else if (iblockstate.getMaterial() == this.blockMaterial && (Integer)iblockstate.getValue(LEVEL) == 0) {
               i1 = 0;
            }
         }

         if (this.blockMaterial == Material.LAVA && i < 8 && i1 < 8 && i1 > i && rand.nextInt(4) != 0) {
            k *= 4;
         }

         if (i1 == i) {
            this.placeStaticBlock(worldIn, pos, state);
         } else {
            i = i1;
            if (i1 < 0) {
               worldIn.setBlockToAir(pos);
            } else {
               state = state.withProperty(LEVEL, i1);
               worldIn.setBlockState(pos, state, 2);
               worldIn.scheduleUpdate(pos, this, k);
               worldIn.notifyNeighborsOfStateChange(pos, this, false);
            }
         }
      } else {
         this.placeStaticBlock(worldIn, pos, state);
      }

      IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
      if (this.canFlowInto(worldIn, pos.down(), iblockstate1)) {
         if (this.blockMaterial == Material.LAVA && worldIn.getBlockState(pos.down()).getMaterial() == Material.WATER) {
            worldIn.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
            this.triggerMixEffects(worldIn, pos.down());
            return;
         }

         if (i >= 8) {
            this.tryFlowInto(worldIn, pos.down(), iblockstate1, i);
         } else {
            this.tryFlowInto(worldIn, pos.down(), iblockstate1, i + 8);
         }
      } else if (i >= 0 && (i == 0 || this.isBlocked(worldIn, pos.down(), iblockstate1))) {
         Set<EnumFacing> set = this.getPossibleFlowDirections(worldIn, pos);
         k1 = i + j;
         if (i >= 8) {
            k1 = 1;
         }

         if (k1 >= 8) {
            return;
         }

         Iterator var16 = set.iterator();

         while(var16.hasNext()) {
            EnumFacing enumfacing1 = (EnumFacing)var16.next();
            this.tryFlowInto(worldIn, pos.offset(enumfacing1), worldIn.getBlockState(pos.offset(enumfacing1)), k1);
         }
      }

   }

   private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level) {
      if (this.canFlowInto(worldIn, pos, state)) {
         if (state.getMaterial() != Material.AIR) {
            if (this.blockMaterial == Material.LAVA) {
               this.triggerMixEffects(worldIn, pos);
            } else {
               state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
            }
         }

         worldIn.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, level), 3);
      }

   }

   private int getSlopeDistance(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost) {
      int i = 1000;
      Iterator var6 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(true) {
         EnumFacing enumfacing;
         BlockPos blockpos;
         IBlockState iblockstate;
         do {
            do {
               do {
                  if (!var6.hasNext()) {
                     return i;
                  }

                  enumfacing = (EnumFacing)var6.next();
               } while(enumfacing == calculateFlowCost);

               blockpos = pos.offset(enumfacing);
               iblockstate = worldIn.getBlockState(blockpos);
            } while(this.isBlocked(worldIn, blockpos, iblockstate));
         } while(iblockstate.getMaterial() == this.blockMaterial && (Integer)iblockstate.getValue(LEVEL) <= 0);

         if (!this.isBlocked(worldIn, blockpos.down(), iblockstate)) {
            return distance;
         }

         if (distance < this.getSlopeFindDistance(worldIn)) {
            int j = this.getSlopeDistance(worldIn, blockpos, distance + 1, enumfacing.getOpposite());
            if (j < i) {
               i = j;
            }
         }
      }
   }

   private int getSlopeFindDistance(World worldIn) {
      return this.blockMaterial == Material.LAVA && !worldIn.provider.doesWaterVaporize() ? 2 : 4;
   }

   private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos) {
      int i = 1000;
      Set<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);
      Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(true) {
         EnumFacing enumfacing;
         BlockPos blockpos;
         IBlockState iblockstate;
         do {
            do {
               if (!var5.hasNext()) {
                  return set;
               }

               enumfacing = (EnumFacing)var5.next();
               blockpos = pos.offset(enumfacing);
               iblockstate = worldIn.getBlockState(blockpos);
            } while(this.isBlocked(worldIn, blockpos, iblockstate));
         } while(iblockstate.getMaterial() == this.blockMaterial && (Integer)iblockstate.getValue(LEVEL) <= 0);

         int j;
         if (this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down()))) {
            j = this.getSlopeDistance(worldIn, blockpos, 1, enumfacing.getOpposite());
         } else {
            j = 0;
         }

         if (j < i) {
            set.clear();
         }

         if (j <= i) {
            set.add(enumfacing);
            i = j;
         }
      }
   }

   private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
      Block block = worldIn.getBlockState(pos).getBlock();
      if (!(block instanceof BlockDoor) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER && block != Blocks.REEDS) {
         return block.blockMaterial != Material.PORTAL && block.blockMaterial != Material.STRUCTURE_VOID ? block.blockMaterial.blocksMovement() : true;
      } else {
         return true;
      }
   }

   protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel) {
      int i = this.getDepth(worldIn.getBlockState(pos));
      if (i < 0) {
         return currentMinLevel;
      } else {
         if (i == 0) {
            ++this.adjacentSourceBlocks;
         }

         if (i >= 8) {
            i = 0;
         }

         return currentMinLevel >= 0 && i >= currentMinLevel ? currentMinLevel : i;
      }
   }

   private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state) {
      Material material = state.getMaterial();
      return material != this.blockMaterial && material != Material.LAVA && !this.isBlocked(worldIn, pos, state);
   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      if (!this.checkForMixing(worldIn, pos, state)) {
         worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
      }

   }
}
