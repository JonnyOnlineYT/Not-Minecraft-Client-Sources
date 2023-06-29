package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBirchTree extends WorldGenAbstractTree {
   private static final IBlockState LOG;
   private static final IBlockState LEAF;
   private final boolean useExtraRandomHeight;

   public WorldGenBirchTree(boolean notify, boolean useExtraRandomHeightIn) {
      super(notify);
      this.useExtraRandomHeight = useExtraRandomHeightIn;
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      int i = rand.nextInt(3) + 5;
      if (this.useExtraRandomHeight) {
         i += rand.nextInt(7);
      }

      boolean flag = true;
      if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
         int l2;
         int i3;
         for(int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            int k = 1;
            if (j == position.getY()) {
               k = 0;
            }

            if (j >= position.getY() + 1 + i - 2) {
               k = 2;
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(l2 = position.getX() - k; l2 <= position.getX() + k && flag; ++l2) {
               for(i3 = position.getZ() - k; i3 <= position.getZ() + k && flag; ++i3) {
                  if (j >= 0 && j < 256) {
                     if (!this.canGrowInto(worldIn.getBlockState(blockpos$mutableblockpos.setPos(l2, j, i3)).getBlock())) {
                        flag = false;
                     }
                  } else {
                     flag = false;
                  }
               }
            }
         }

         if (!flag) {
            return false;
         } else {
            Block block = worldIn.getBlockState(position.down()).getBlock();
            if ((block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.FARMLAND) && position.getY() < 256 - i - 1) {
               this.setDirtAt(worldIn, position.down());

               int i2;
               for(i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2) {
                  int k2 = i2 - (position.getY() + i);
                  l2 = 1 - k2 / 2;

                  for(i3 = position.getX() - l2; i3 <= position.getX() + l2; ++i3) {
                     int j1 = i3 - position.getX();

                     for(int k1 = position.getZ() - l2; k1 <= position.getZ() + l2; ++k1) {
                        int l1 = k1 - position.getZ();
                        if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                           BlockPos blockpos = new BlockPos(i3, i2, k1);
                           Material material = worldIn.getBlockState(blockpos).getMaterial();
                           if (material == Material.AIR || material == Material.LEAVES) {
                              this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
                           }
                        }
                     }
                  }
               }

               for(i2 = 0; i2 < i; ++i2) {
                  Material material1 = worldIn.getBlockState(position.up(i2)).getMaterial();
                  if (material1 == Material.AIR || material1 == Material.LEAVES) {
                     this.setBlockAndNotifyAdequately(worldIn, position.up(i2), LOG);
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   static {
      LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
      LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLeaf.CHECK_DECAY, false);
   }
}
