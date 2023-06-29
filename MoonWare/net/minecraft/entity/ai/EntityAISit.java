package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase
{
    private final EntityTameable theEntity;

    /** If the EntityTameable is sitting. */
    private boolean isSitting;

    public EntityAISit(EntityTameable entityIn)
    {
        theEntity = entityIn;
        setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!theEntity.isTamed())
        {
            return false;
        }
        else if (theEntity.isInWater())
        {
            return false;
        }
        else if (!theEntity.onGround)
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = theEntity.getOwner();

            if (entitylivingbase == null)
            {
                return true;
            }
            else
            {
                return (!(theEntity.getDistanceSqToEntity(entitylivingbase) < 144.0D) || entitylivingbase.getAITarget() == null) && isSitting;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        theEntity.getNavigator().clearPathEntity();
        theEntity.setSitting(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        theEntity.setSitting(false);
    }

    /**
     * Sets the sitting flag.
     */
    public void setSitting(boolean sitting)
    {
        isSitting = sitting;
    }
}
