package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveTowardsTarget extends EntityAIBase
{
    private final EntityCreature theEntity;
    private EntityLivingBase targetEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double speed;

    /**
     * If the distance to the target entity is further than this, this AI task will not run.
     */
    private final float maxTargetDistance;

    public EntityAIMoveTowardsTarget(EntityCreature creature, double speedIn, float targetMaxDistance)
    {
        theEntity = creature;
        speed = speedIn;
        maxTargetDistance = targetMaxDistance;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        targetEntity = theEntity.getAttackTarget();

        if (targetEntity == null)
        {
            return false;
        }
        else if (targetEntity.getDistanceSqToEntity(theEntity) > (double)(maxTargetDistance * maxTargetDistance))
        {
            return false;
        }
        else
        {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(theEntity, 16, 7, new Vec3d(targetEntity.posX, targetEntity.posY, targetEntity.posZ));

            if (vec3d == null)
            {
                return false;
            }
            else
            {
                movePosX = vec3d.xCoord;
                movePosY = vec3d.yCoord;
                movePosZ = vec3d.zCoord;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !theEntity.getNavigator().noPath() && targetEntity.isEntityAlive() && targetEntity.getDistanceSqToEntity(theEntity) < (double)(maxTargetDistance * maxTargetDistance);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        targetEntity = null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, speed);
    }
}
