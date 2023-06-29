package net.minecraft.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Random;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

public class Teleporter {
   private final WorldServer worldServerInstance;
   private final Random random;
   private final Long2ObjectMap<Teleporter.PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap(4096);

   public Teleporter(WorldServer worldIn) {
      this.worldServerInstance = worldIn;
      this.random = new Random(worldIn.getSeed());
   }

   public void placeInPortal(Entity entityIn, float rotationYaw) {
      if (this.worldServerInstance.provider.getDimensionType().getId() != 1) {
         if (!this.placeInExistingPortal(entityIn, rotationYaw)) {
            this.makePortal(entityIn);
            this.placeInExistingPortal(entityIn, rotationYaw);
         }
      } else {
         int i = MathHelper.floor(entityIn.posX);
         int j = MathHelper.floor(entityIn.posY) - 1;
         int k = MathHelper.floor(entityIn.posZ);
         int l = true;
         int i1 = false;

         for(int j1 = -2; j1 <= 2; ++j1) {
            for(int k1 = -2; k1 <= 2; ++k1) {
               for(int l1 = -1; l1 < 3; ++l1) {
                  int i2 = i + k1 * 1 + j1 * 0;
                  int j2 = j + l1;
                  int k2 = k + k1 * 0 - j1 * 1;
                  boolean flag = l1 < 0;
                  this.worldServerInstance.setBlockState(new BlockPos(i2, j2, k2), flag ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
               }
            }
         }

         entityIn.setLocationAndAngles((double)i, (double)j, (double)k, entityIn.rotationYaw, 0.0F);
         entityIn.motionX = 0.0D;
         entityIn.motionY = 0.0D;
         entityIn.motionZ = 0.0D;
      }

   }

   public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
      int i = true;
      double d0 = -1.0D;
      int j = MathHelper.floor(entityIn.posX);
      int k = MathHelper.floor(entityIn.posZ);
      boolean flag = true;
      BlockPos blockpos = BlockPos.ORIGIN;
      long l = ChunkPos.asLong(j, k);
      if (this.destinationCoordinateCache.containsKey(l)) {
         Teleporter.PortalPosition teleporter$portalposition = (Teleporter.PortalPosition)this.destinationCoordinateCache.get(l);
         d0 = 0.0D;
         blockpos = teleporter$portalposition;
         teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
         flag = false;
      } else {
         BlockPos blockpos3 = new BlockPos(entityIn);

         for(int i1 = -128; i1 <= 128; ++i1) {
            BlockPos blockpos2;
            for(int j1 = -128; j1 <= 128; ++j1) {
               for(BlockPos blockpos1 = blockpos3.add(i1, this.worldServerInstance.getActualHeight() - 1 - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
                  blockpos2 = blockpos1.down();
                  if (this.worldServerInstance.getBlockState(blockpos1).getBlock() == Blocks.PORTAL) {
                     for(blockpos2 = blockpos1.down(); this.worldServerInstance.getBlockState(blockpos2).getBlock() == Blocks.PORTAL; blockpos2 = blockpos2.down()) {
                        blockpos1 = blockpos2;
                     }

                     double d1 = blockpos1.distanceSq(blockpos3);
                     if (d0 < 0.0D || d1 < d0) {
                        d0 = d1;
                        blockpos = blockpos1;
                     }
                  }
               }
            }
         }
      }

      if (d0 >= 0.0D) {
         if (flag) {
            this.destinationCoordinateCache.put(l, new Teleporter.PortalPosition((BlockPos)blockpos, this.worldServerInstance.getTotalWorldTime()));
         }

         double d5 = (double)((BlockPos)blockpos).getX() + 0.5D;
         double d7 = (double)((BlockPos)blockpos).getZ() + 0.5D;
         BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.PORTAL.createPatternHelper(this.worldServerInstance, (BlockPos)blockpos);
         boolean flag1 = blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
         double d2 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? (double)blockpattern$patternhelper.getFrontTopLeft().getZ() : (double)blockpattern$patternhelper.getFrontTopLeft().getX();
         double d6 = (double)(blockpattern$patternhelper.getFrontTopLeft().getY() + 1) - entityIn.getLastPortalVec().yCoord * (double)blockpattern$patternhelper.getHeight();
         if (flag1) {
            ++d2;
         }

         if (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) {
            d7 = d2 + (1.0D - entityIn.getLastPortalVec().xCoord) * (double)blockpattern$patternhelper.getWidth() * (double)blockpattern$patternhelper.getForwards().rotateY().getAxisDirection().getOffset();
         } else {
            d5 = d2 + (1.0D - entityIn.getLastPortalVec().xCoord) * (double)blockpattern$patternhelper.getWidth() * (double)blockpattern$patternhelper.getForwards().rotateY().getAxisDirection().getOffset();
         }

         float f = 0.0F;
         float f1 = 0.0F;
         float f2 = 0.0F;
         float f3 = 0.0F;
         if (blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection()) {
            f = 1.0F;
            f1 = 1.0F;
         } else if (blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection().getOpposite()) {
            f = -1.0F;
            f1 = -1.0F;
         } else if (blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection().rotateY()) {
            f2 = 1.0F;
            f3 = -1.0F;
         } else {
            f2 = -1.0F;
            f3 = 1.0F;
         }

         double d3 = entityIn.motionX;
         double d4 = entityIn.motionZ;
         entityIn.motionX = d3 * (double)f + d4 * (double)f3;
         entityIn.motionZ = d3 * (double)f2 + d4 * (double)f1;
         entityIn.rotationYaw = rotationYaw - (float)(entityIn.getTeleportDirection().getOpposite().getHorizontalIndex() * 90) + (float)(blockpattern$patternhelper.getForwards().getHorizontalIndex() * 90);
         if (entityIn instanceof EntityPlayerMP) {
            ((EntityPlayerMP)entityIn).connection.setPlayerLocation(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
         } else {
            entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean makePortal(Entity entityIn) {
      int i = true;
      double d0 = -1.0D;
      int j = MathHelper.floor(entityIn.posX);
      int k = MathHelper.floor(entityIn.posY);
      int l = MathHelper.floor(entityIn.posZ);
      int i1 = j;
      int j1 = k;
      int k1 = l;
      int l1 = 0;
      int i2 = this.random.nextInt(4);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      int l5;
      double d3;
      int j6;
      double d4;
      int i7;
      int k7;
      int j8;
      int j9;
      int j10;
      int j11;
      int j12;
      int i13;
      int j13;
      double d6;
      double d8;
      for(l5 = j - 16; l5 <= j + 16; ++l5) {
         d3 = (double)l5 + 0.5D - entityIn.posX;

         for(j6 = l - 16; j6 <= l + 16; ++j6) {
            d4 = (double)j6 + 0.5D - entityIn.posZ;

            label291:
            for(i7 = this.worldServerInstance.getActualHeight() - 1; i7 >= 0; --i7) {
               if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.setPos(l5, i7, j6))) {
                  while(i7 > 0 && this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.setPos(l5, i7 - 1, j6))) {
                     --i7;
                  }

                  for(k7 = i2; k7 < i2 + 4; ++k7) {
                     j8 = k7 % 2;
                     j9 = 1 - j8;
                     if (k7 % 4 >= 2) {
                        j8 = -j8;
                        j9 = -j9;
                     }

                     for(j10 = 0; j10 < 3; ++j10) {
                        for(j11 = 0; j11 < 4; ++j11) {
                           for(j12 = -1; j12 < 4; ++j12) {
                              i13 = l5 + (j11 - 1) * j8 + j10 * j9;
                              j13 = i7 + j12;
                              int k5 = j6 + (j11 - 1) * j9 - j10 * j8;
                              blockpos$mutableblockpos.setPos(i13, j13, k5);
                              if (j12 < 0 && !this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getMaterial().isSolid() || j12 >= 0 && !this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)) {
                                 continue label291;
                              }
                           }
                        }
                     }

                     d6 = (double)i7 + 0.5D - entityIn.posY;
                     d8 = d3 * d3 + d6 * d6 + d4 * d4;
                     if (d0 < 0.0D || d8 < d0) {
                        d0 = d8;
                        i1 = l5;
                        j1 = i7;
                        k1 = j6;
                        l1 = k7 % 4;
                     }
                  }
               }
            }
         }
      }

      if (d0 < 0.0D) {
         for(l5 = j - 16; l5 <= j + 16; ++l5) {
            d3 = (double)l5 + 0.5D - entityIn.posX;

            for(j6 = l - 16; j6 <= l + 16; ++j6) {
               d4 = (double)j6 + 0.5D - entityIn.posZ;

               label229:
               for(i7 = this.worldServerInstance.getActualHeight() - 1; i7 >= 0; --i7) {
                  if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.setPos(l5, i7, j6))) {
                     while(i7 > 0 && this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.setPos(l5, i7 - 1, j6))) {
                        --i7;
                     }

                     for(k7 = i2; k7 < i2 + 2; ++k7) {
                        j8 = k7 % 2;
                        j9 = 1 - j8;

                        for(j10 = 0; j10 < 4; ++j10) {
                           for(j11 = -1; j11 < 4; ++j11) {
                              j12 = l5 + (j10 - 1) * j8;
                              i13 = i7 + j11;
                              j13 = j6 + (j10 - 1) * j9;
                              blockpos$mutableblockpos.setPos(j12, i13, j13);
                              if (j11 < 0 && !this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getMaterial().isSolid() || j11 >= 0 && !this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)) {
                                 continue label229;
                              }
                           }
                        }

                        d6 = (double)i7 + 0.5D - entityIn.posY;
                        d8 = d3 * d3 + d6 * d6 + d4 * d4;
                        if (d0 < 0.0D || d8 < d0) {
                           d0 = d8;
                           i1 = l5;
                           j1 = i7;
                           k1 = j6;
                           l1 = k7 % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      l5 = i1;
      int k2 = j1;
      int k6 = k1;
      j6 = l1 % 2;
      int i3 = 1 - j6;
      if (l1 % 4 >= 2) {
         j6 = -j6;
         i3 = -i3;
      }

      if (d0 < 0.0D) {
         j1 = MathHelper.clamp(j1, 70, this.worldServerInstance.getActualHeight() - 10);
         k2 = j1;

         for(int j7 = -1; j7 <= 1; ++j7) {
            for(i7 = 1; i7 < 3; ++i7) {
               for(k7 = -1; k7 < 3; ++k7) {
                  j8 = l5 + (i7 - 1) * j6 + j7 * i3;
                  j9 = k2 + k7;
                  j10 = k6 + (i7 - 1) * i3 - j7 * j6;
                  boolean flag = k7 < 0;
                  this.worldServerInstance.setBlockState(new BlockPos(j8, j9, j10), flag ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
               }
            }
         }
      }

      IBlockState iblockstate = Blocks.PORTAL.getDefaultState().withProperty(BlockPortal.AXIS, j6 == 0 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);

      for(i7 = 0; i7 < 4; ++i7) {
         for(k7 = 0; k7 < 4; ++k7) {
            for(j8 = -1; j8 < 4; ++j8) {
               j9 = l5 + (k7 - 1) * j6;
               j10 = k2 + j8;
               j11 = k6 + (k7 - 1) * i3;
               boolean flag1 = k7 == 0 || k7 == 3 || j8 == -1 || j8 == 3;
               this.worldServerInstance.setBlockState(new BlockPos(j9, j10, j11), flag1 ? Blocks.OBSIDIAN.getDefaultState() : iblockstate, 2);
            }
         }

         for(k7 = 0; k7 < 4; ++k7) {
            for(j8 = -1; j8 < 4; ++j8) {
               j9 = l5 + (k7 - 1) * j6;
               j10 = k2 + j8;
               j11 = k6 + (k7 - 1) * i3;
               BlockPos blockpos = new BlockPos(j9, j10, j11);
               this.worldServerInstance.notifyNeighborsOfStateChange(blockpos, this.worldServerInstance.getBlockState(blockpos).getBlock(), false);
            }
         }
      }

      return true;
   }

   public void removeStalePortalLocations(long worldTime) {
      if (worldTime % 100L == 0L) {
         long i = worldTime - 300L;
         ObjectIterator objectiterator = this.destinationCoordinateCache.values().iterator();

         while(true) {
            Teleporter.PortalPosition teleporter$portalposition;
            do {
               if (!objectiterator.hasNext()) {
                  return;
               }

               teleporter$portalposition = (Teleporter.PortalPosition)objectiterator.next();
            } while(teleporter$portalposition != null && teleporter$portalposition.lastUpdateTime >= i);

            objectiterator.remove();
         }
      }
   }

   public class PortalPosition extends BlockPos {
      public long lastUpdateTime;

      public PortalPosition(BlockPos pos, long lastUpdate) {
         super(pos.getX(), pos.getY(), pos.getZ());
         this.lastUpdateTime = lastUpdate;
      }
   }
}
