package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase {
   private final Predicate<Entity> canBeSeenSelector;
   protected EntityCreature theEntity;
   private final double farSpeed;
   private final double nearSpeed;
   protected T closestLivingEntity;
   private final float avoidDistance;
   private Path entityPathEntity;
   private final PathNavigate entityPathNavigate;
   private final Class<T> classToAvoid;
   private final Predicate<? super T> avoidTargetSelector;

   public EntityAIAvoidEntity(EntityCreature theEntityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
      this(theEntityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
   }

   public EntityAIAvoidEntity(EntityCreature theEntityIn, Class<T> classToAvoidIn, Predicate<? super T> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
      this.canBeSeenSelector = new Predicate<Entity>() {
         public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(p_apply_1_) && !EntityAIAvoidEntity.this.theEntity.isOnSameTeam(p_apply_1_);
         }
      };
      this.theEntity = theEntityIn;
      this.classToAvoid = classToAvoidIn;
      this.avoidTargetSelector = avoidTargetSelectorIn;
      this.avoidDistance = avoidDistanceIn;
      this.farSpeed = farSpeedIn;
      this.nearSpeed = nearSpeedIn;
      this.entityPathNavigate = theEntityIn.getNavigator();
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      List<T> list = this.theEntity.world.getEntitiesWithinAABB(this.classToAvoid, this.theEntity.getEntityBoundingBox().expand((double)this.avoidDistance, 3.0D, (double)this.avoidDistance), Predicates.and(new Predicate[]{EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector}));
      if (list.isEmpty()) {
         return false;
      } else {
         this.closestLivingEntity = (Entity)list.get(0);
         Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
         if (vec3d == null) {
            return false;
         } else if (this.closestLivingEntity.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)) {
            return false;
         } else {
            this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            return this.entityPathEntity != null;
         }
      }
   }

   public boolean continueExecuting() {
      return !this.entityPathNavigate.noPath();
   }

   public void startExecuting() {
      this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
   }

   public void resetTask() {
      this.closestLivingEntity = null;
   }

   public void updateTask() {
      if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0D) {
         this.theEntity.getNavigator().setSpeed(this.nearSpeed);
      } else {
         this.theEntity.getNavigator().setSpeed(this.farSpeed);
      }

   }
}
