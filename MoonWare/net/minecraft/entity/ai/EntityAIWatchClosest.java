package net.minecraft.entity.ai;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class EntityAIWatchClosest extends EntityAIBase
{
    protected EntityLiving theWatcher;

    /** The closest entity which is being watched by this one. */
    protected Entity closestEntity;

    /** This is the Maximum distance that the AI will look for the Entity */
    protected float maxDistanceForPlayer;
    private int lookTime;
    private final float chance;
    protected Class <? extends Entity > watchedClass;

    public EntityAIWatchClosest(EntityLiving entitylivingIn, Class <? extends Entity > watchTargetClass, float maxDistance)
    {
        theWatcher = entitylivingIn;
        watchedClass = watchTargetClass;
        maxDistanceForPlayer = maxDistance;
        chance = 0.02F;
        setMutexBits(2);
    }

    public EntityAIWatchClosest(EntityLiving entitylivingIn, Class <? extends Entity > watchTargetClass, float maxDistance, float chanceIn)
    {
        theWatcher = entitylivingIn;
        watchedClass = watchTargetClass;
        maxDistanceForPlayer = maxDistance;
        chance = chanceIn;
        setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (theWatcher.getRNG().nextFloat() >= chance)
        {
            return false;
        }
        else
        {
            if (theWatcher.getAttackTarget() != null)
            {
                closestEntity = theWatcher.getAttackTarget();
            }

            if (watchedClass == EntityPlayer.class)
            {
                closestEntity = theWatcher.world.func_190525_a(theWatcher.posX, theWatcher.posY, theWatcher.posZ, maxDistanceForPlayer, Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.func_191324_b(theWatcher)));
            }
            else
            {
                closestEntity = theWatcher.world.findNearestEntityWithinAABB(watchedClass, theWatcher.getEntityBoundingBox().expand(maxDistanceForPlayer, 3.0D, maxDistanceForPlayer), theWatcher);
            }

            return closestEntity != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (!closestEntity.isEntityAlive())
        {
            return false;
        }
        else if (theWatcher.getDistanceSqToEntity(closestEntity) > (double)(maxDistanceForPlayer * maxDistanceForPlayer))
        {
            return false;
        }
        else
        {
            return lookTime > 0;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        lookTime = 40 + theWatcher.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        closestEntity = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        theWatcher.getLookHelper().setLookPosition(closestEntity.posX, closestEntity.posY + (double) closestEntity.getEyeHeight(), closestEntity.posZ, (float) theWatcher.getHorizontalFaceSpeed(), (float) theWatcher.getVerticalFaceSpeed());
        --lookTime;
    }
}
