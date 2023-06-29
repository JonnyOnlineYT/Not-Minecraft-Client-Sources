package net.minecraft.village;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

public class VillageSiege {
   private final World worldObj;
   private boolean hasSetupSiege;
   private int siegeState = -1;
   private int siegeCount;
   private int nextSpawnTime;
   private Village theVillage;
   private int spawnX;
   private int spawnY;
   private int spawnZ;

   public VillageSiege(World worldIn) {
      this.worldObj = worldIn;
   }

   public void tick() {
      if (this.worldObj.isDaytime()) {
         this.siegeState = 0;
      } else if (this.siegeState != 2) {
         if (this.siegeState == 0) {
            float f = this.worldObj.getCelestialAngle(0.0F);
            if ((double)f < 0.5D || (double)f > 0.501D) {
               return;
            }

            this.siegeState = this.worldObj.rand.nextInt(10) == 0 ? 1 : 2;
            this.hasSetupSiege = false;
            if (this.siegeState == 2) {
               return;
            }
         }

         if (this.siegeState != -1) {
            if (!this.hasSetupSiege) {
               if (!this.trySetupSiege()) {
                  return;
               }

               this.hasSetupSiege = true;
            }

            if (this.nextSpawnTime > 0) {
               --this.nextSpawnTime;
            } else {
               this.nextSpawnTime = 2;
               if (this.siegeCount > 0) {
                  this.spawnZombie();
                  --this.siegeCount;
               } else {
                  this.siegeState = 2;
               }
            }
         }
      }

   }

   private boolean trySetupSiege() {
      List<EntityPlayer> list = this.worldObj.playerEntities;
      Iterator iterator = list.iterator();

      Vec3d vec3d;
      do {
         do {
            do {
               do {
                  do {
                     EntityPlayer entityplayer;
                     do {
                        if (!iterator.hasNext()) {
                           return false;
                        }

                        entityplayer = (EntityPlayer)iterator.next();
                     } while(entityplayer.isSpectator());

                     this.theVillage = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(entityplayer), 1);
                  } while(this.theVillage == null);
               } while(this.theVillage.getNumVillageDoors() < 10);
            } while(this.theVillage.getTicksSinceLastDoorAdding() < 20);
         } while(this.theVillage.getNumVillagers() < 20);

         BlockPos blockpos = this.theVillage.getCenter();
         float f = (float)this.theVillage.getVillageRadius();
         boolean flag = false;

         for(int i = 0; i < 10; ++i) {
            float f1 = this.worldObj.rand.nextFloat() * 6.2831855F;
            this.spawnX = blockpos.getX() + (int)((double)(MathHelper.cos(f1) * f) * 0.9D);
            this.spawnY = blockpos.getY();
            this.spawnZ = blockpos.getZ() + (int)((double)(MathHelper.sin(f1) * f) * 0.9D);
            flag = false;
            Iterator var9 = this.worldObj.getVillageCollection().getVillageList().iterator();

            while(var9.hasNext()) {
               Village village = (Village)var9.next();
               if (village != this.theVillage && village.isBlockPosWithinSqVillageRadius(new BlockPos(this.spawnX, this.spawnY, this.spawnZ))) {
                  flag = true;
                  break;
               }
            }

            if (!flag) {
               break;
            }
         }

         if (flag) {
            return false;
         }

         vec3d = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
      } while(vec3d == null);

      this.nextSpawnTime = 0;
      this.siegeCount = 20;
      return true;
   }

   private boolean spawnZombie() {
      Vec3d vec3d = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
      if (vec3d == null) {
         return false;
      } else {
         EntityZombie entityzombie;
         try {
            entityzombie = new EntityZombie(this.worldObj);
            entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
         } catch (Exception var4) {
            var4.printStackTrace();
            return false;
         }

         entityzombie.setLocationAndAngles(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
         this.worldObj.spawnEntityInWorld(entityzombie);
         BlockPos blockpos = this.theVillage.getCenter();
         entityzombie.setHomePosAndDistance(blockpos, this.theVillage.getVillageRadius());
         return true;
      }
   }

   @Nullable
   private Vec3d findRandomSpawnPos(BlockPos pos) {
      for(int i = 0; i < 10; ++i) {
         BlockPos blockpos = pos.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
         if (this.theVillage.isBlockPosWithinSqVillageRadius(blockpos) && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, this.worldObj, blockpos)) {
            return new Vec3d((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
         }
      }

      return null;
   }
}
