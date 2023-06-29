package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase
{
    /** The entity that is leaping. */
    EntityLiving leaper;

    /** The entity that the leaper is leaping towards. */
    EntityLivingBase leapTarget;

    /** The entity's motionY after leaping. */
    float leapMotionY;

    public EntityAILeapAtTarget(EntityLiving leapingEntity, float leapMotionYIn)
    {
        leaper = leapingEntity;
        leapMotionY = leapMotionYIn;
        setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        leapTarget = leaper.getAttackTarget();

        if (leapTarget == null)
        {
            return false;
        }
        else
        {
            double d0 = leaper.getDistanceSqToEntity(leapTarget);

            if (d0 >= 4.0D && d0 <= 16.0D)
            {
                if (!leaper.onGround)
                {
                    return false;
                }
                else
                {
                    return leaper.getRNG().nextInt(5) == 0;
                }
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !leaper.onGround;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        double d0 = leapTarget.posX - leaper.posX;
        double d1 = leapTarget.posZ - leaper.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

        if ((double)f >= 1.0E-4D)
        {
            leaper.motionX += d0 / (double)f * 0.5D * 0.800000011920929D + leaper.motionX * 0.20000000298023224D;
            leaper.motionZ += d1 / (double)f * 0.5D * 0.800000011920929D + leaper.motionZ * 0.20000000298023224D;
        }

        leaper.motionY = leapMotionY;
    }
}
