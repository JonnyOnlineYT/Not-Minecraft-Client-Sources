package net.minecraft.entity.ai;

import java.util.Iterator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {
   private final boolean entityCallsForHelp;
   private int revengeTimerOld;
   private final Class<?>[] targetClasses;

   public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>... targetClassesIn) {
      super(creatureIn, true);
      this.entityCallsForHelp = entityCallsForHelpIn;
      this.targetClasses = targetClassesIn;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      int i = this.taskOwner.getRevengeTimer();
      EntityLivingBase entitylivingbase = this.taskOwner.getAITarget();
      return i != this.revengeTimerOld && entitylivingbase != null && this.isSuitableTarget(entitylivingbase, false);
   }

   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
      this.target = this.taskOwner.getAttackTarget();
      this.revengeTimerOld = this.taskOwner.getRevengeTimer();
      this.unseenMemoryTicks = 300;
      if (this.entityCallsForHelp) {
         this.alertOthers();
      }

      super.startExecuting();
   }

   protected void alertOthers() {
      double d0 = this.getTargetDistance();
      Iterator var3 = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0)).iterator();

      while(true) {
         EntityCreature entitycreature;
         do {
            do {
               do {
                  do {
                     if (!var3.hasNext()) {
                        return;
                     }

                     entitycreature = (EntityCreature)var3.next();
                  } while(this.taskOwner == entitycreature);
               } while(entitycreature.getAttackTarget() != null);
            } while(this.taskOwner instanceof EntityTameable && ((EntityTameable)this.taskOwner).getOwner() != ((EntityTameable)entitycreature).getOwner());
         } while(entitycreature.isOnSameTeam(this.taskOwner.getAITarget()));

         boolean flag = false;
         Class[] var6 = this.targetClasses;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Class<?> oclass = var6[var8];
            if (entitycreature.getClass() == oclass) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            this.setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
         }
      }
   }

   protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
      creatureIn.setAttackTarget(entityLivingBaseIn);
   }
}
