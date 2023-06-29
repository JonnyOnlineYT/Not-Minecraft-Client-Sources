package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    EntityTameable theDefendingTameable;
    EntityLivingBase theOwnerAttacker;
    private int timestamp;

    public EntityAIOwnerHurtByTarget(EntityTameable theDefendingTameableIn)
    {
        super(theDefendingTameableIn, false);
        theDefendingTameable = theDefendingTameableIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!theDefendingTameable.isTamed())
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = theDefendingTameable.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                theOwnerAttacker = entitylivingbase.getAITarget();
                int i = entitylivingbase.getRevengeTimer();
                return i != timestamp && isSuitableTarget(theOwnerAttacker, false) && theDefendingTameable.shouldAttackEntity(theOwnerAttacker, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        taskOwner.setAttackTarget(theOwnerAttacker);
        EntityLivingBase entitylivingbase = theDefendingTameable.getOwner();

        if (entitylivingbase != null)
        {
            timestamp = entitylivingbase.getRevengeTimer();
        }

        super.startExecuting();
    }
}
