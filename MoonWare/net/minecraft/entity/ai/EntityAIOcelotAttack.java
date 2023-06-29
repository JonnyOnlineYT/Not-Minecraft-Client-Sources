package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack extends EntityAIBase
{
    World theWorld;
    EntityLiving theEntity;
    EntityLivingBase theVictim;
    int attackCountdown;

    public EntityAIOcelotAttack(EntityLiving theEntityIn)
    {
        theEntity = theEntityIn;
        theWorld = theEntityIn.world;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = theEntity.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            theVictim = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (!theVictim.isEntityAlive())
        {
            return false;
        }
        else if (theEntity.getDistanceSqToEntity(theVictim) > 225.0D)
        {
            return false;
        }
        else
        {
            return !theEntity.getNavigator().noPath() || shouldExecute();
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        theVictim = null;
        theEntity.getNavigator().clearPathEntity();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        theEntity.getLookHelper().setLookPositionWithEntity(theVictim, 30.0F, 30.0F);
        double d0 = theEntity.width * 2.0F * theEntity.width * 2.0F;
        double d1 = theEntity.getDistanceSq(theVictim.posX, theVictim.getEntityBoundingBox().minY, theVictim.posZ);
        double d2 = 0.8D;

        if (d1 > d0 && d1 < 16.0D)
        {
            d2 = 1.33D;
        }
        else if (d1 < 225.0D)
        {
            d2 = 0.6D;
        }

        theEntity.getNavigator().tryMoveToEntityLiving(theVictim, d2);
        attackCountdown = Math.max(attackCountdown - 1, 0);

        if (d1 <= d0)
        {
            if (attackCountdown <= 0)
            {
                attackCountdown = 20;
                theEntity.attackEntityAsMob(theVictim);
            }
        }
    }
}
