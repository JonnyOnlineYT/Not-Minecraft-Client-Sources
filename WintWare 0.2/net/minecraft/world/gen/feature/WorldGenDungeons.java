package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation[] SPAWNERTYPES = new ResourceLocation[]{EntityList.func_191306_a(EntitySkeleton.class), EntityList.func_191306_a(EntityZombie.class), EntityList.func_191306_a(EntityZombie.class), EntityList.func_191306_a(EntitySpider.class)};

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      int i = true;
      int j = rand.nextInt(2) + 2;
      int k = -j - 1;
      int l = j + 1;
      int i1 = true;
      int j1 = true;
      int k1 = rand.nextInt(2) + 2;
      int l1 = -k1 - 1;
      int i2 = k1 + 1;
      int j2 = 0;

      int k3;
      int i4;
      int k4;
      BlockPos blockpos1;
      for(k3 = k; k3 <= l; ++k3) {
         for(i4 = -1; i4 <= 4; ++i4) {
            for(k4 = l1; k4 <= i2; ++k4) {
               blockpos1 = position.add(k3, i4, k4);
               Material material = worldIn.getBlockState(blockpos1).getMaterial();
               boolean flag = material.isSolid();
               if (i4 == -1 && !flag) {
                  return false;
               }

               if (i4 == 4 && !flag) {
                  return false;
               }

               if ((k3 == k || k3 == l || k4 == l1 || k4 == i2) && i4 == 0 && worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.up())) {
                  ++j2;
               }
            }
         }
      }

      if (j2 >= 1 && j2 <= 5) {
         for(k3 = k; k3 <= l; ++k3) {
            for(i4 = 3; i4 >= -1; --i4) {
               for(k4 = l1; k4 <= i2; ++k4) {
                  blockpos1 = position.add(k3, i4, k4);
                  if (k3 != k && i4 != -1 && k4 != l1 && k3 != l && i4 != 4 && k4 != i2) {
                     if (worldIn.getBlockState(blockpos1).getBlock() != Blocks.CHEST) {
                        worldIn.setBlockToAir(blockpos1);
                     }
                  } else if (blockpos1.getY() >= 0 && !worldIn.getBlockState(blockpos1.down()).getMaterial().isSolid()) {
                     worldIn.setBlockToAir(blockpos1);
                  } else if (worldIn.getBlockState(blockpos1).getMaterial().isSolid() && worldIn.getBlockState(blockpos1).getBlock() != Blocks.CHEST) {
                     if (i4 == -1 && rand.nextInt(4) != 0) {
                        worldIn.setBlockState(blockpos1, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
                     } else {
                        worldIn.setBlockState(blockpos1, Blocks.COBBLESTONE.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         for(k3 = 0; k3 < 2; ++k3) {
            for(i4 = 0; i4 < 3; ++i4) {
               k4 = position.getX() + rand.nextInt(j * 2 + 1) - j;
               int i5 = position.getY();
               int j5 = position.getZ() + rand.nextInt(k1 * 2 + 1) - k1;
               BlockPos blockpos2 = new BlockPos(k4, i5, j5);
               if (worldIn.isAirBlock(blockpos2)) {
                  int j3 = 0;
                  Iterator var21 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var21.hasNext()) {
                     EnumFacing enumfacing = (EnumFacing)var21.next();
                     if (worldIn.getBlockState(blockpos2.offset(enumfacing)).getMaterial().isSolid()) {
                        ++j3;
                     }
                  }

                  if (j3 == 1) {
                     worldIn.setBlockState(blockpos2, Blocks.CHEST.correctFacing(worldIn, blockpos2, Blocks.CHEST.getDefaultState()), 2);
                     TileEntity tileentity1 = worldIn.getTileEntity(blockpos2);
                     if (tileentity1 instanceof TileEntityChest) {
                        ((TileEntityChest)tileentity1).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
                     }
                     break;
                  }
               }
            }
         }

         worldIn.setBlockState(position, Blocks.MOB_SPAWNER.getDefaultState(), 2);
         TileEntity tileentity = worldIn.getTileEntity(position);
         if (tileentity instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().func_190894_a(this.pickMobSpawner(rand));
         } else {
            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", position.getX(), position.getY(), position.getZ());
         }

         return true;
      } else {
         return false;
      }
   }

   private ResourceLocation pickMobSpawner(Random rand) {
      return SPAWNERTYPES[rand.nextInt(SPAWNERTYPES.length)];
   }
}
