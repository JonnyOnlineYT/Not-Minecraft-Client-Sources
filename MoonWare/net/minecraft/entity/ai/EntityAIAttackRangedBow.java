package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRangedBow<T extends EntityMob & IRangedAttackMob> extends EntityAIBase
{
    private final T entity;
    private final double moveSpeedAmp;
    private int attackCooldown;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityAIAttackRangedBow(T p_i47515_1_, double p_i47515_2_, int p_i47515_4_, float p_i47515_5_)
    {
        entity = p_i47515_1_;
        moveSpeedAmp = p_i47515_2_;
        attackCooldown = p_i47515_4_;
        maxAttackDistance = p_i47515_5_ * p_i47515_5_;
        setMutexBits(3);
    }

    public void setAttackCooldown(int p_189428_1_)
    {
        attackCooldown = p_189428_1_;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return entity.getAttackTarget() != null && isBowInMainhand();
    }

    protected boolean isBowInMainhand()
    {
        return !entity.getHeldItemMainhand().isEmpty() && entity.getHeldItemMainhand().getItem() == Items.BOW;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return (shouldExecute() || !entity.getNavigator().noPath()) && isBowInMainhand();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        entity.setSwingingArms(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        super.resetTask();
        entity.setSwingingArms(false);
        seeTime = 0;
        attackTime = -1;
        entity.resetActiveHand();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        EntityLivingBase entitylivingbase = entity.getAttackTarget();

        if (entitylivingbase != null)
        {
            double d0 = entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            boolean flag = entity.getEntitySenses().canSee(entitylivingbase);
            boolean flag1 = seeTime > 0;

            if (flag != flag1)
            {
                seeTime = 0;
            }

            if (flag)
            {
                ++seeTime;
            }
            else
            {
                --seeTime;
            }

            if (d0 <= (double) maxAttackDistance && seeTime >= 20)
            {
                entity.getNavigator().clearPathEntity();
                ++strafingTime;
            }
            else
            {
                entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, moveSpeedAmp);
                strafingTime = -1;
            }

            if (strafingTime >= 20)
            {
                if ((double) entity.getRNG().nextFloat() < 0.3D)
                {
                    strafingClockwise = !strafingClockwise;
                }

                if ((double) entity.getRNG().nextFloat() < 0.3D)
                {
                    strafingBackwards = !strafingBackwards;
                }

                strafingTime = 0;
            }

            if (strafingTime > -1)
            {
                if (d0 > (double)(maxAttackDistance * 0.75F))
                {
                    strafingBackwards = false;
                }
                else if (d0 < (double)(maxAttackDistance * 0.25F))
                {
                    strafingBackwards = true;
                }

                entity.getMoveHelper().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.5F : -0.5F);
                entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
            }
            else
            {
                entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            }

            if (entity.isHandActive())
            {
                if (!flag && seeTime < -60)
                {
                    entity.resetActiveHand();
                }
                else if (flag)
                {
                    int i = entity.getItemInUseMaxCount();

                    if (i >= 20)
                    {
                        entity.resetActiveHand();
                        entity.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                        attackTime = attackCooldown;
                    }
                }
            }
            else if (--attackTime <= 0 && seeTime >= -60)
            {
                entity.setActiveHand(EnumHand.MAIN_HAND);
            }
        }
    }
}
