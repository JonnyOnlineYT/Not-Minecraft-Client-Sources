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

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;

    /** The entity we are attached to */
    protected EntityCreature theEntity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;

    /** The PathEntity of our entity */
    private Path entityPathEntity;

    /** The PathNavigate of our entity */
    private final PathNavigate entityPathNavigate;
    private final Class<T> classToAvoid;
    private final Predicate <? super T > avoidTargetSelector;

    public EntityAIAvoidEntity(EntityCreature theEntityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        this(theEntityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public EntityAIAvoidEntity(EntityCreature theEntityIn, Class<T> classToAvoidIn, Predicate <? super T > avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        canBeSeenSelector = new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_.isEntityAlive() && theEntity.getEntitySenses().canSee(p_apply_1_) && !theEntity.isOnSameTeam(p_apply_1_);
            }
        };
        theEntity = theEntityIn;
        classToAvoid = classToAvoidIn;
        avoidTargetSelector = avoidTargetSelectorIn;
        avoidDistance = avoidDistanceIn;
        farSpeed = farSpeedIn;
        nearSpeed = nearSpeedIn;
        entityPathNavigate = theEntityIn.getNavigator();
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List<T> list = theEntity.world.getEntitiesWithinAABB(classToAvoid, theEntity.getEntityBoundingBox().expand(avoidDistance, 3.0D, avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, canBeSeenSelector, avoidTargetSelector));

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theEntity, 16, 7, new Vec3d(closestLivingEntity.posX, closestLivingEntity.posY, closestLivingEntity.posZ));

            if (vec3d == null)
            {
                return false;
            }
            else if (closestLivingEntity.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < closestLivingEntity.getDistanceSqToEntity(theEntity))
            {
                return false;
            }
            else
            {
                entityPathEntity = entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                return entityPathEntity != null;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !entityPathNavigate.noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        entityPathNavigate.setPath(entityPathEntity, farSpeed);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        closestLivingEntity = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (theEntity.getDistanceSqToEntity(closestLivingEntity) < 49.0D)
        {
            theEntity.getNavigator().setSpeed(nearSpeed);
        }
        else
        {
            theEntity.getNavigator().setSpeed(farSpeed);
        }
    }
}
