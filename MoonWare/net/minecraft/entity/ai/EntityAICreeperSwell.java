package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;

public class EntityAICreeperSwell extends EntityAIBase
{
    /** The creeper that is swelling. */
    EntityCreeper swellingCreeper;

    /**
     * The creeper's attack target. This is used for the changing of the creeper's state.
     */
    EntityLivingBase creeperAttackTarget;

    public EntityAICreeperSwell(EntityCreeper entitycreeperIn)
    {
        swellingCreeper = entitycreeperIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = swellingCreeper.getAttackTarget();
        return swellingCreeper.getCreeperState() > 0 || entitylivingbase != null && swellingCreeper.getDistanceSqToEntity(entitylivingbase) < 9.0D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        swellingCreeper.getNavigator().clearPathEntity();
        creeperAttackTarget = swellingCreeper.getAttackTarget();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        creeperAttackTarget = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (creeperAttackTarget == null)
        {
            swellingCreeper.setCreeperState(-1);
        }
        else if (swellingCreeper.getDistanceSqToEntity(creeperAttackTarget) > 49.0D)
        {
            swellingCreeper.setCreeperState(-1);
        }
        else if (!swellingCreeper.getEntitySenses().canSee(creeperAttackTarget))
        {
            swellingCreeper.setCreeperState(-1);
        }
        else
        {
            swellingCreeper.setCreeperState(1);
        }
    }
}
