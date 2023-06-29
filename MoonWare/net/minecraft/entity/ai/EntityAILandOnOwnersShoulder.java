package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILandOnOwnersShoulder extends EntityAIBase
{
    private final EntityShoulderRiding field_192382_a;
    private EntityPlayer field_192383_b;
    private boolean field_192384_c;

    public EntityAILandOnOwnersShoulder(EntityShoulderRiding p_i47415_1_)
    {
        field_192382_a = p_i47415_1_;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = field_192382_a.getOwner();
        boolean flag = entitylivingbase != null && !((EntityPlayer)entitylivingbase).isSpectator() && !((EntityPlayer)entitylivingbase).capabilities.isFlying && !entitylivingbase.isInWater();
        return !field_192382_a.isSitting() && flag && field_192382_a.func_191995_du();
    }

    /**
     * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITask have
     * this value set to true.
     */
    public boolean isInterruptible()
    {
        return !field_192384_c;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_192383_b = (EntityPlayer) field_192382_a.getOwner();
        field_192384_c = false;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (!field_192384_c && !field_192382_a.isSitting() && !field_192382_a.getLeashed())
        {
            if (field_192382_a.getEntityBoundingBox().intersectsWith(field_192383_b.getEntityBoundingBox()))
            {
                field_192384_c = field_192382_a.func_191994_f(field_192383_b);
            }
        }
    }
}
