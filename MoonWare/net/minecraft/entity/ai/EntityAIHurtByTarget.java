package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget
{
    private final boolean entityCallsForHelp;

    /** Store the previous revengeTimer value */
    private int revengeTimerOld;
    private final Class<?>[] targetClasses;

    public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>... targetClassesIn)
    {
        super(creatureIn, true);
        entityCallsForHelp = entityCallsForHelpIn;
        targetClasses = targetClassesIn;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        int i = taskOwner.getRevengeTimer();
        EntityLivingBase entitylivingbase = taskOwner.getAITarget();
        return i != revengeTimerOld && entitylivingbase != null && isSuitableTarget(entitylivingbase, false);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        taskOwner.setAttackTarget(taskOwner.getAITarget());
        target = taskOwner.getAttackTarget();
        revengeTimerOld = taskOwner.getRevengeTimer();
        unseenMemoryTicks = 300;

        if (entityCallsForHelp)
        {
            alertOthers();
        }

        super.startExecuting();
    }

    protected void alertOthers()
    {
        double d0 = getTargetDistance();

        for (EntityCreature entitycreature : taskOwner.world.getEntitiesWithinAABB(taskOwner.getClass(), (new AxisAlignedBB(taskOwner.posX, taskOwner.posY, taskOwner.posZ, taskOwner.posX + 1.0D, taskOwner.posY + 1.0D, taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0)))
        {
            if (taskOwner != entitycreature && entitycreature.getAttackTarget() == null && (!(taskOwner instanceof EntityTameable) || ((EntityTameable) taskOwner).getOwner() == ((EntityTameable)entitycreature).getOwner()) && !entitycreature.isOnSameTeam(taskOwner.getAITarget()))
            {
                boolean flag = false;

                for (Class<?> oclass : targetClasses)
                {
                    if (entitycreature.getClass() == oclass)
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    setEntityAttackTarget(entitycreature, taskOwner.getAITarget());
                }
            }
        }
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
    {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}
