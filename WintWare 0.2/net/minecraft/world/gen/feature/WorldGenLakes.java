package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGenLakes extends WorldGenerator {
   private final Block block;

   public WorldGenLakes(Block blockIn) {
      this.block = blockIn;
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      for(position = position.add(-8, 0, -8); position.getY() > 5 && worldIn.isAirBlock(position); position = position.down()) {
      }

      if (position.getY() <= 4) {
         return false;
      } else {
         position = position.down(4);
         boolean[] aboolean = new boolean[2048];
         int i = rand.nextInt(4) + 4;

         int k2;
         for(k2 = 0; k2 < i; ++k2) {
            double d0 = rand.nextDouble() * 6.0D + 3.0D;
            double d1 = rand.nextDouble() * 4.0D + 2.0D;
            double d2 = rand.nextDouble() * 6.0D + 3.0D;
            double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
            double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
            double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

            for(int l = 1; l < 15; ++l) {
               for(int i1 = 1; i1 < 15; ++i1) {
                  for(int j1 = 1; j1 < 7; ++j1) {
                     double d6 = ((double)l - d3) / (d0 / 2.0D);
                     double d7 = ((double)j1 - d4) / (d1 / 2.0D);
                     double d8 = ((double)i1 - d5) / (d2 / 2.0D);
                     double d9 = d6 * d6 + d7 * d7 + d8 * d8;
                     if (d9 < 1.0D) {
                        aboolean[(l * 16 + i1) * 8 + j1] = true;
                     }
                  }
               }
            }
         }

         int k4;
         int l3;
         boolean flag1;
         for(k2 = 0; k2 < 16; ++k2) {
            for(l3 = 0; l3 < 16; ++l3) {
               for(k4 = 0; k4 < 8; ++k4) {
                  flag1 = !aboolean[(k2 * 16 + l3) * 8 + k4] && (k2 < 15 && aboolean[((k2 + 1) * 16 + l3) * 8 + k4] || k2 > 0 && aboolean[((k2 - 1) * 16 + l3) * 8 + k4] || l3 < 15 && aboolean[(k2 * 16 + l3 + 1) * 8 + k4] || l3 > 0 && aboolean[(k2 * 16 + (l3 - 1)) * 8 + k4] || k4 < 7 && aboolean[(k2 * 16 + l3) * 8 + k4 + 1] || k4 > 0 && aboolean[(k2 * 16 + l3) * 8 + (k4 - 1)]);
                  if (flag1) {
                     Material material = worldIn.getBlockState(position.add(k2, k4, l3)).getMaterial();
                     if (k4 >= 4 && material.isLiquid()) {
                        return false;
                     }

                     if (k4 < 4 && !material.isSolid() && worldIn.getBlockState(position.add(k2, k4, l3)).getBlock() != this.block) {
                        return false;
                     }
                  }
               }
            }
         }

         for(k2 = 0; k2 < 16; ++k2) {
            for(l3 = 0; l3 < 16; ++l3) {
               for(k4 = 0; k4 < 8; ++k4) {
                  if (aboolean[(k2 * 16 + l3) * 8 + k4]) {
                     worldIn.setBlockState(position.add(k2, k4, l3), k4 >= 4 ? Blocks.AIR.getDefaultState() : this.block.getDefaultState(), 2);
                  }
               }
            }
         }

         for(k2 = 0; k2 < 16; ++k2) {
            for(l3 = 0; l3 < 16; ++l3) {
               for(k4 = 4; k4 < 8; ++k4) {
                  if (aboolean[(k2 * 16 + l3) * 8 + k4]) {
                     BlockPos blockpos = position.add(k2, k4 - 1, l3);
                     if (worldIn.getBlockState(blockpos).getBlock() == Blocks.DIRT && worldIn.getLightFor(EnumSkyBlock.SKY, position.add(k2, k4, l3)) > 0) {
                        Biome biome = worldIn.getBiome(blockpos);
                        if (biome.topBlock.getBlock() == Blocks.MYCELIUM) {
                           worldIn.setBlockState(blockpos, Blocks.MYCELIUM.getDefaultState(), 2);
                        } else {
                           worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState(), 2);
                        }
                     }
                  }
               }
            }
         }

         if (this.block.getDefaultState().getMaterial() == Material.LAVA) {
            for(k2 = 0; k2 < 16; ++k2) {
               for(l3 = 0; l3 < 16; ++l3) {
                  for(k4 = 0; k4 < 8; ++k4) {
                     flag1 = !aboolean[(k2 * 16 + l3) * 8 + k4] && (k2 < 15 && aboolean[((k2 + 1) * 16 + l3) * 8 + k4] || k2 > 0 && aboolean[((k2 - 1) * 16 + l3) * 8 + k4] || l3 < 15 && aboolean[(k2 * 16 + l3 + 1) * 8 + k4] || l3 > 0 && aboolean[(k2 * 16 + (l3 - 1)) * 8 + k4] || k4 < 7 && aboolean[(k2 * 16 + l3) * 8 + k4 + 1] || k4 > 0 && aboolean[(k2 * 16 + l3) * 8 + (k4 - 1)]);
                     if (flag1 && (k4 < 4 || rand.nextInt(2) != 0) && worldIn.getBlockState(position.add(k2, k4, l3)).getMaterial().isSolid()) {
                        worldIn.setBlockState(position.add(k2, k4, l3), Blocks.STONE.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         if (this.block.getDefaultState().getMaterial() == Material.WATER) {
            for(k2 = 0; k2 < 16; ++k2) {
               for(l3 = 0; l3 < 16; ++l3) {
                  int l4 = true;
                  if (worldIn.canBlockFreezeWater(position.add(k2, 4, l3))) {
                     worldIn.setBlockState(position.add(k2, 4, l3), Blocks.ICE.getDefaultState(), 2);
                  }
               }
            }
         }

         return true;
      }
   }
}
