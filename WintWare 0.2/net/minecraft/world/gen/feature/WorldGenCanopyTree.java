package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCanopyTree extends WorldGenAbstractTree {
   private static final IBlockState DARK_OAK_LOG;
   private static final IBlockState DARK_OAK_LEAVES;

   public WorldGenCanopyTree(boolean notify) {
      super(notify);
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      int i = rand.nextInt(3) + rand.nextInt(2) + 6;
      int j = position.getX();
      int k = position.getY();
      int l = position.getZ();
      if (k >= 1 && k + i + 1 < 256) {
         BlockPos blockpos = position.down();
         Block block = worldIn.getBlockState(blockpos).getBlock();
         if (block != Blocks.GRASS && block != Blocks.DIRT) {
            return false;
         } else if (!this.placeTreeOfHeight(worldIn, position, i)) {
            return false;
         } else {
            this.setDirtAt(worldIn, blockpos);
            this.setDirtAt(worldIn, blockpos.east());
            this.setDirtAt(worldIn, blockpos.south());
            this.setDirtAt(worldIn, blockpos.south().east());
            EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
            int i1 = i - rand.nextInt(4);
            int j1 = 2 - rand.nextInt(3);
            int k1 = j;
            int l1 = l;
            int i2 = k + i - 1;

            int k3;
            int j4;
            for(k3 = 0; k3 < i; ++k3) {
               if (k3 >= i1 && j1 > 0) {
                  k1 += enumfacing.getFrontOffsetX();
                  l1 += enumfacing.getFrontOffsetZ();
                  --j1;
               }

               j4 = k + k3;
               BlockPos blockpos1 = new BlockPos(k1, j4, l1);
               Material material = worldIn.getBlockState(blockpos1).getMaterial();
               if (material == Material.AIR || material == Material.LEAVES) {
                  this.placeLogAt(worldIn, blockpos1);
                  this.placeLogAt(worldIn, blockpos1.east());
                  this.placeLogAt(worldIn, blockpos1.south());
                  this.placeLogAt(worldIn, blockpos1.east().south());
               }
            }

            for(k3 = -2; k3 <= 0; ++k3) {
               for(j4 = -2; j4 <= 0; ++j4) {
                  int k4 = -1;
                  this.placeLeafAt(worldIn, k1 + k3, i2 + k4, l1 + j4);
                  this.placeLeafAt(worldIn, 1 + k1 - k3, i2 + k4, l1 + j4);
                  this.placeLeafAt(worldIn, k1 + k3, i2 + k4, 1 + l1 - j4);
                  this.placeLeafAt(worldIn, 1 + k1 - k3, i2 + k4, 1 + l1 - j4);
                  if ((k3 > -2 || j4 > -1) && (k3 != -1 || j4 != -2)) {
                     int k4 = 1;
                     this.placeLeafAt(worldIn, k1 + k3, i2 + k4, l1 + j4);
                     this.placeLeafAt(worldIn, 1 + k1 - k3, i2 + k4, l1 + j4);
                     this.placeLeafAt(worldIn, k1 + k3, i2 + k4, 1 + l1 - j4);
                     this.placeLeafAt(worldIn, 1 + k1 - k3, i2 + k4, 1 + l1 - j4);
                  }
               }
            }

            if (rand.nextBoolean()) {
               this.placeLeafAt(worldIn, k1, i2 + 2, l1);
               this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1);
               this.placeLeafAt(worldIn, k1 + 1, i2 + 2, l1 + 1);
               this.placeLeafAt(worldIn, k1, i2 + 2, l1 + 1);
            }

            for(k3 = -3; k3 <= 4; ++k3) {
               for(j4 = -3; j4 <= 4; ++j4) {
                  if ((k3 != -3 || j4 != -3) && (k3 != -3 || j4 != 4) && (k3 != 4 || j4 != -3) && (k3 != 4 || j4 != 4) && (Math.abs(k3) < 3 || Math.abs(j4) < 3)) {
                     this.placeLeafAt(worldIn, k1 + k3, i2, l1 + j4);
                  }
               }
            }

            for(k3 = -1; k3 <= 2; ++k3) {
               for(j4 = -1; j4 <= 2; ++j4) {
                  if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && rand.nextInt(3) <= 0) {
                     int l4 = rand.nextInt(3) + 2;

                     int k5;
                     for(k5 = 0; k5 < l4; ++k5) {
                        this.placeLogAt(worldIn, new BlockPos(j + k3, i2 - k5 - 1, l + j4));
                     }

                     int l5;
                     for(k5 = -1; k5 <= 1; ++k5) {
                        for(l5 = -1; l5 <= 1; ++l5) {
                           this.placeLeafAt(worldIn, k1 + k3 + k5, i2, l1 + j4 + l5);
                        }
                     }

                     for(k5 = -2; k5 <= 2; ++k5) {
                        for(l5 = -2; l5 <= 2; ++l5) {
                           if (Math.abs(k5) != 2 || Math.abs(l5) != 2) {
                              this.placeLeafAt(worldIn, k1 + k3 + k5, i2 - 1, l1 + j4 + l5);
                           }
                        }
                     }
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private boolean placeTreeOfHeight(World worldIn, BlockPos pos, int height) {
      int i = pos.getX();
      int j = pos.getY();
      int k = pos.getZ();
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int l = 0; l <= height + 1; ++l) {
         int i1 = 1;
         if (l == 0) {
            i1 = 0;
         }

         if (l >= height - 1) {
            i1 = 2;
         }

         for(int j1 = -i1; j1 <= i1; ++j1) {
            for(int k1 = -i1; k1 <= i1; ++k1) {
               if (!this.canGrowInto(worldIn.getBlockState(blockpos$mutableblockpos.setPos(i + j1, j + l, k + k1)).getBlock())) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private void placeLogAt(World worldIn, BlockPos pos) {
      if (this.canGrowInto(worldIn.getBlockState(pos).getBlock())) {
         this.setBlockAndNotifyAdequately(worldIn, pos, DARK_OAK_LOG);
      }

   }

   private void placeLeafAt(World worldIn, int x, int y, int z) {
      BlockPos blockpos = new BlockPos(x, y, z);
      Material material = worldIn.getBlockState(blockpos).getMaterial();
      if (material == Material.AIR) {
         this.setBlockAndNotifyAdequately(worldIn, blockpos, DARK_OAK_LEAVES);
      }

   }

   static {
      DARK_OAK_LOG = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
      DARK_OAK_LEAVES = Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, false);
   }
}
