package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIMate extends EntityAIBase {
   private final EntityAnimal theAnimal;
   private final Class<? extends EntityAnimal> field_190857_e;
   World theWorld;
   private EntityAnimal targetMate;
   int spawnBabyDelay;
   double moveSpeed;

   public EntityAIMate(EntityAnimal animal, double speedIn) {
      this(animal, speedIn, animal.getClass());
   }

   public EntityAIMate(EntityAnimal p_i47306_1_, double p_i47306_2_, Class<? extends EntityAnimal> p_i47306_4_) {
      this.theAnimal = p_i47306_1_;
      this.theWorld = p_i47306_1_.world;
      this.field_190857_e = p_i47306_4_;
      this.moveSpeed = p_i47306_2_;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (!this.theAnimal.isInLove()) {
         return false;
      } else {
         this.targetMate = this.getNearbyMate();
         return this.targetMate != null;
      }
   }

   public boolean continueExecuting() {
      return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
   }

   public void resetTask() {
      this.targetMate = null;
      this.spawnBabyDelay = 0;
   }

   public void updateTask() {
      this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.theAnimal.getVerticalFaceSpeed());
      this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
      ++this.spawnBabyDelay;
      if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D) {
         this.spawnBaby();
      }

   }

   private EntityAnimal getNearbyMate() {
      List<EntityAnimal> list = this.theWorld.getEntitiesWithinAABB(this.field_190857_e, this.theAnimal.getEntityBoundingBox().expandXyz(8.0D));
      double d0 = Double.MAX_VALUE;
      EntityAnimal entityanimal = null;
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         EntityAnimal entityanimal1 = (EntityAnimal)var5.next();
         if (this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0) {
            entityanimal = entityanimal1;
            d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
         }
      }

      return entityanimal;
   }

   private void spawnBaby() {
      EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);
      if (entityageable != null) {
         EntityPlayerMP entityplayermp = this.theAnimal.func_191993_do();
         if (entityplayermp == null && this.targetMate.func_191993_do() != null) {
            entityplayermp = this.targetMate.func_191993_do();
         }

         if (entityplayermp != null) {
            entityplayermp.addStat(StatList.ANIMALS_BRED);
            CriteriaTriggers.field_192134_n.func_192168_a(entityplayermp, this.theAnimal, this.targetMate, entityageable);
         }

         this.theAnimal.setGrowingAge(6000);
         this.targetMate.setGrowingAge(6000);
         this.theAnimal.resetInLove();
         this.targetMate.resetInLove();
         entityageable.setGrowingAge(-24000);
         entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
         this.theWorld.spawnEntityInWorld(entityageable);
         Random random = this.theAnimal.getRNG();

         for(int i = 0; i < 7; ++i) {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            double d3 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
            double d4 = 0.5D + random.nextDouble() * (double)this.theAnimal.height;
            double d5 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
            this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4, this.theAnimal.posZ + d5, d0, d1, d2);
         }

         if (this.theWorld.getGameRules().getBoolean("doMobLoot")) {
            this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
         }
      }

   }
}
