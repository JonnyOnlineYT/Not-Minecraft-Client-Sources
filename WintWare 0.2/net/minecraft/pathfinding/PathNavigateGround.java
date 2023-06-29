package net.minecraft.pathfinding;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateGround extends PathNavigate {
   private boolean shouldAvoidSun;

   public PathNavigateGround(EntityLiving entitylivingIn, World worldIn) {
      super(entitylivingIn, worldIn);
   }

   protected PathFinder getPathFinder() {
      this.nodeProcessor = new WalkNodeProcessor();
      this.nodeProcessor.setCanEnterDoors(true);
      return new PathFinder(this.nodeProcessor);
   }

   protected boolean canNavigate() {
      return this.theEntity.onGround || this.getCanSwim() && this.isInLiquid() || this.theEntity.isRiding();
   }

   protected Vec3d getEntityPosition() {
      return new Vec3d(this.theEntity.posX, (double)this.getPathablePosY(), this.theEntity.posZ);
   }

   public Path getPathToPos(BlockPos pos) {
      BlockPos blockpos1;
      if (this.worldObj.getBlockState(pos).getMaterial() == Material.AIR) {
         for(blockpos1 = pos.down(); blockpos1.getY() > 0 && this.worldObj.getBlockState(blockpos1).getMaterial() == Material.AIR; blockpos1 = blockpos1.down()) {
         }

         if (blockpos1.getY() > 0) {
            return super.getPathToPos(blockpos1.up());
         }

         while(blockpos1.getY() < this.worldObj.getHeight() && this.worldObj.getBlockState(blockpos1).getMaterial() == Material.AIR) {
            blockpos1 = blockpos1.up();
         }

         pos = blockpos1;
      }

      if (!this.worldObj.getBlockState(pos).getMaterial().isSolid()) {
         return super.getPathToPos(pos);
      } else {
         for(blockpos1 = pos.up(); blockpos1.getY() < this.worldObj.getHeight() && this.worldObj.getBlockState(blockpos1).getMaterial().isSolid(); blockpos1 = blockpos1.up()) {
         }

         return super.getPathToPos(blockpos1);
      }
   }

   public Path getPathToEntityLiving(Entity entityIn) {
      return this.getPathToPos(new BlockPos(entityIn));
   }

   private int getPathablePosY() {
      if (this.theEntity.isInWater() && this.getCanSwim()) {
         int i = (int)this.theEntity.getEntityBoundingBox().minY;
         Block block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor(this.theEntity.posX), i, MathHelper.floor(this.theEntity.posZ))).getBlock();
         int j = 0;

         do {
            if (block != Blocks.FLOWING_WATER && block != Blocks.WATER) {
               return i;
            }

            ++i;
            block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor(this.theEntity.posX), i, MathHelper.floor(this.theEntity.posZ))).getBlock();
            ++j;
         } while(j <= 16);

         return (int)this.theEntity.getEntityBoundingBox().minY;
      } else {
         return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D);
      }
   }

   protected void removeSunnyPath() {
      super.removeSunnyPath();
      if (this.shouldAvoidSun) {
         if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(this.theEntity.posZ)))) {
            return;
         }

         for(int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
            PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
            if (this.worldObj.canSeeSky(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord))) {
               this.currentPath.setCurrentPathLength(i - 1);
               return;
            }
         }
      }

   }

   protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
      int i = MathHelper.floor(posVec31.xCoord);
      int j = MathHelper.floor(posVec31.zCoord);
      double d0 = posVec32.xCoord - posVec31.xCoord;
      double d1 = posVec32.zCoord - posVec31.zCoord;
      double d2 = d0 * d0 + d1 * d1;
      if (d2 < 1.0E-8D) {
         return false;
      } else {
         double d3 = 1.0D / Math.sqrt(d2);
         d0 *= d3;
         d1 *= d3;
         sizeX += 2;
         sizeZ += 2;
         if (!this.isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
            return false;
         } else {
            sizeX -= 2;
            sizeZ -= 2;
            double d4 = 1.0D / Math.abs(d0);
            double d5 = 1.0D / Math.abs(d1);
            double d6 = (double)i - posVec31.xCoord;
            double d7 = (double)j - posVec31.zCoord;
            if (d0 >= 0.0D) {
               ++d6;
            }

            if (d1 >= 0.0D) {
               ++d7;
            }

            d6 /= d0;
            d7 /= d1;
            int k = d0 < 0.0D ? -1 : 1;
            int l = d1 < 0.0D ? -1 : 1;
            int i1 = MathHelper.floor(posVec32.xCoord);
            int j1 = MathHelper.floor(posVec32.zCoord);
            int k1 = i1 - i;
            int l1 = j1 - j;

            do {
               if (k1 * k <= 0 && l1 * l <= 0) {
                  return true;
               }

               if (d6 < d7) {
                  d6 += d4;
                  i += k;
                  k1 = i1 - i;
               } else {
                  d7 += d5;
                  j += l;
                  l1 = j1 - j;
               }
            } while(this.isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1));

            return false;
         }
      }
   }

   private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d vec31, double p_179683_8_, double p_179683_10_) {
      int i = x - sizeX / 2;
      int j = z - sizeZ / 2;
      if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
         return false;
      } else {
         for(int k = i; k < i + sizeX; ++k) {
            for(int l = j; l < j + sizeZ; ++l) {
               double d0 = (double)k + 0.5D - vec31.xCoord;
               double d1 = (double)l + 0.5D - vec31.zCoord;
               if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D) {
                  PathNodeType pathnodetype = this.nodeProcessor.getPathNodeType(this.worldObj, k, y - 1, l, this.theEntity, sizeX, sizeY, sizeZ, true, true);
                  if (pathnodetype == PathNodeType.WATER) {
                     return false;
                  }

                  if (pathnodetype == PathNodeType.LAVA) {
                     return false;
                  }

                  if (pathnodetype == PathNodeType.OPEN) {
                     return false;
                  }

                  pathnodetype = this.nodeProcessor.getPathNodeType(this.worldObj, k, y, l, this.theEntity, sizeX, sizeY, sizeZ, true, true);
                  float f = this.theEntity.getPathPriority(pathnodetype);
                  if (f < 0.0F || f >= 8.0F) {
                     return false;
                  }

                  if (pathnodetype == PathNodeType.DAMAGE_FIRE || pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_OTHER) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private boolean isPositionClear(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3d p_179692_7_, double p_179692_8_, double p_179692_10_) {
      Iterator var12 = BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)).iterator();

      while(var12.hasNext()) {
         BlockPos blockpos = (BlockPos)var12.next();
         double d0 = (double)blockpos.getX() + 0.5D - p_179692_7_.xCoord;
         double d1 = (double)blockpos.getZ() + 0.5D - p_179692_7_.zCoord;
         if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D) {
            Block block = this.worldObj.getBlockState(blockpos).getBlock();
            if (!block.isPassable(this.worldObj, blockpos)) {
               return false;
            }
         }
      }

      return true;
   }

   public void setBreakDoors(boolean canBreakDoors) {
      this.nodeProcessor.setCanBreakDoors(canBreakDoors);
   }

   public void setEnterDoors(boolean enterDoors) {
      this.nodeProcessor.setCanEnterDoors(enterDoors);
   }

   public boolean getEnterDoors() {
      return this.nodeProcessor.getCanEnterDoors();
   }

   public void setCanSwim(boolean canSwim) {
      this.nodeProcessor.setCanSwim(canSwim);
   }

   public boolean getCanSwim() {
      return this.nodeProcessor.getCanSwim();
   }

   public void setAvoidSun(boolean avoidSun) {
      this.shouldAvoidSun = avoidSun;
   }
}
