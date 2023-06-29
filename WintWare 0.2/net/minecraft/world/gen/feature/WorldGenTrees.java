package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree {
   private static final IBlockState DEFAULT_TRUNK;
   private static final IBlockState DEFAULT_LEAF;
   private final int minTreeHeight;
   private final boolean vinesGrow;
   private final IBlockState metaWood;
   private final IBlockState metaLeaves;

   public WorldGenTrees(boolean p_i2027_1_) {
      this(p_i2027_1_, 4, DEFAULT_TRUNK, DEFAULT_LEAF, false);
   }

   public WorldGenTrees(boolean p_i46446_1_, int p_i46446_2_, IBlockState p_i46446_3_, IBlockState p_i46446_4_, boolean p_i46446_5_) {
      super(p_i46446_1_);
      this.minTreeHeight = p_i46446_2_;
      this.metaWood = p_i46446_3_;
      this.metaLeaves = p_i46446_4_;
      this.vinesGrow = p_i46446_5_;
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      int i = rand.nextInt(3) + this.minTreeHeight;
      boolean flag = true;
      if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
         int l3;
         int i4;
         for(int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            int k = 1;
            if (j == position.getY()) {
               k = 0;
            }

            if (j >= position.getY() + 1 + i - 2) {
               k = 2;
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(l3 = position.getX() - k; l3 <= position.getX() + k && flag; ++l3) {
               for(i4 = position.getZ() - k; i4 <= position.getZ() + k && flag; ++i4) {
                  if (j >= 0 && j < 256) {
                     if (!this.canGrowInto(worldIn.getBlockState(blockpos$mutableblockpos.setPos(l3, j, i4)).getBlock())) {
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
               int k2 = true;
               int l2 = false;

               int k4;
               int l4;
               int i5;
               BlockPos blockpos3;
               for(l3 = position.getY() - 3 + i; l3 <= position.getY() + i; ++l3) {
                  i4 = l3 - (position.getY() + i);
                  k4 = 1 - i4 / 2;

                  for(int k1 = position.getX() - k4; k1 <= position.getX() + k4; ++k1) {
                     l4 = k1 - position.getX();

                     for(i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5) {
                        int j2 = i5 - position.getZ();
                        if (Math.abs(l4) != k4 || Math.abs(j2) != k4 || rand.nextInt(2) != 0 && i4 != 0) {
                           blockpos3 = new BlockPos(k1, l3, i5);
                           Material material = worldIn.getBlockState(blockpos3).getMaterial();
                           if (material == Material.AIR || material == Material.LEAVES || material == Material.VINE) {
                              this.setBlockAndNotifyAdequately(worldIn, blockpos3, this.metaLeaves);
                           }
                        }
                     }
                  }
               }

               for(l3 = 0; l3 < i; ++l3) {
                  Material material1 = worldIn.getBlockState(position.up(l3)).getMaterial();
                  if (material1 == Material.AIR || material1 == Material.LEAVES || material1 == Material.VINE) {
                     this.setBlockAndNotifyAdequately(worldIn, position.up(l3), this.metaWood);
                     if (this.vinesGrow && l3 > 0) {
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(-1, l3, 0))) {
                           this.addVine(worldIn, position.add(-1, l3, 0), BlockVine.EAST);
                        }

                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(1, l3, 0))) {
                           this.addVine(worldIn, position.add(1, l3, 0), BlockVine.WEST);
                        }

                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, l3, -1))) {
                           this.addVine(worldIn, position.add(0, l3, -1), BlockVine.SOUTH);
                        }

                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, l3, 1))) {
                           this.addVine(worldIn, position.add(0, l3, 1), BlockVine.NORTH);
                        }
                     }
                  }
               }

               if (this.vinesGrow) {
                  for(l3 = position.getY() - 3 + i; l3 <= position.getY() + i; ++l3) {
                     i4 = l3 - (position.getY() + i);
                     k4 = 2 - i4 / 2;
                     BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

                     for(l4 = position.getX() - k4; l4 <= position.getX() + k4; ++l4) {
                        for(i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5) {
                           blockpos$mutableblockpos1.setPos(l4, l3, i5);
                           if (worldIn.getBlockState(blockpos$mutableblockpos1).getMaterial() == Material.LEAVES) {
                              BlockPos blockpos2 = blockpos$mutableblockpos1.west();
                              blockpos3 = blockpos$mutableblockpos1.east();
                              BlockPos blockpos4 = blockpos$mutableblockpos1.north();
                              BlockPos blockpos1 = blockpos$mutableblockpos1.south();
                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getMaterial() == Material.AIR) {
                                 this.addHangingVine(worldIn, blockpos2, BlockVine.EAST);
                              }

                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getMaterial() == Material.AIR) {
                                 this.addHangingVine(worldIn, blockpos3, BlockVine.WEST);
                              }

                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getMaterial() == Material.AIR) {
                                 this.addHangingVine(worldIn, blockpos4, BlockVine.SOUTH);
                              }

                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos1).getMaterial() == Material.AIR) {
                                 this.addHangingVine(worldIn, blockpos1, BlockVine.NORTH);
                              }
                           }
                        }
                     }
                  }

                  if (rand.nextInt(5) == 0 && i > 5) {
                     for(l3 = 0; l3 < 2; ++l3) {
                        Iterator var23 = EnumFacing.Plane.HORIZONTAL.iterator();

                        while(var23.hasNext()) {
                           EnumFacing enumfacing = (EnumFacing)var23.next();
                           if (rand.nextInt(4 - l3) == 0) {
                              EnumFacing enumfacing1 = enumfacing.getOpposite();
                              this.placeCocoa(worldIn, rand.nextInt(3), position.add(enumfacing1.getFrontOffsetX(), i - 5 + l3, enumfacing1.getFrontOffsetZ()), enumfacing);
                           }
                        }
                     }
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

   private void placeCocoa(World worldIn, int p_181652_2_, BlockPos pos, EnumFacing side) {
      this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.AGE, p_181652_2_).withProperty(BlockCocoa.FACING, side));
   }

   private void addVine(World worldIn, BlockPos pos, PropertyBool prop) {
      this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.VINE.getDefaultState().withProperty(prop, true));
   }

   private void addHangingVine(World worldIn, BlockPos pos, PropertyBool prop) {
      this.addVine(worldIn, pos, prop);
      int i = 4;

      for(BlockPos blockpos = pos.down(); worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && i > 0; --i) {
         this.addVine(worldIn, blockpos, prop);
         blockpos = blockpos.down();
      }

   }

   static {
      DEFAULT_TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
      DEFAULT_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false);
   }
}
