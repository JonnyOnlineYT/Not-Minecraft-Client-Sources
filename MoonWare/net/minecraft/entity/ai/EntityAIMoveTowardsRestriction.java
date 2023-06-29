package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveTowardsRestriction extends EntityAIBase
{
    private final EntityCreature theEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;

    public EntityAIMoveTowardsRestriction(EntityCreature creatureIn, double speedIn)
    {
        theEntity = creatureIn;
        movementSpeed = speedIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (theEntity.isWithinHomeDistanceCurrentPosition())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = theEntity.getHomePosition();
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(theEntity, 16, 7, new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ()));

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
        return !theEntity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ, movementSpeed);
    }
}
