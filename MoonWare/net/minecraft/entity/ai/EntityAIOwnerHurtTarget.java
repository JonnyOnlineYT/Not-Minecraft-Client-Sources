package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    EntityTameable theEntityTameable;
    EntityLivingBase theTarget;
    private int timestamp;

    public EntityAIOwnerHurtTarget(EntityTameable theEntityTameableIn)
    {
        super(theEntityTameableIn, false);
        theEntityTameable = theEntityTameableIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!theEntityTameable.isTamed())
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = theEntityTameable.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                theTarget = entitylivingbase.getLastAttacker();
                int i = entitylivingbase.getLastAttackerTime();
                return i != timestamp && isSuitableTarget(theTarget, false) && theEntityTameable.shouldAttackEntity(theTarget, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        taskOwner.setAttackTarget(theTarget);
        EntityLivingBase entitylivingbase = theEntityTameable.getOwner();

        if (entitylivingbase != null)
        {
            timestamp = entitylivingbase.getLastAttackerTime();
        }

        super.startExecuting();
    }
}
