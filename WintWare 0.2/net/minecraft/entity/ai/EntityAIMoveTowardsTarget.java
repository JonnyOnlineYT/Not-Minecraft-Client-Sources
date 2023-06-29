package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveTowardsTarget extends EntityAIBase {
   private final EntityCreature theEntity;
   private EntityLivingBase targetEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private final double speed;
   private final float maxTargetDistance;

   public EntityAIMoveTowardsTarget(EntityCreature creature, double speedIn, float targetMaxDistance) {
      this.theEntity = creature;
      this.speed = speedIn;
      this.maxTargetDistance = targetMaxDistance;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      this.targetEntity = this.theEntity.getAttackTarget();
      if (this.targetEntity == null) {
         return false;
      } else if (this.targetEntity.getDistanceSqToEntity(this.theEntity) > (double)(this.maxTargetDistance * this.maxTargetDistance)) {
         return false;
      } else {
         Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3d(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
         if (vec3d == null) {
            return false;
         } else {
            this.movePosX = vec3d.xCoord;
            this.movePosY = vec3d.yCoord;
            this.movePosZ = vec3d.zCoord;
            return true;
         }
      }
   }

   public boolean continueExecuting() {
      return !this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity(this.theEntity) < (double)(this.maxTargetDistance * this.maxTargetDistance);
   }

   public void resetTask() {
      this.targetEntity = null;
   }

   public void startExecuting() {
      this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
   }
}
