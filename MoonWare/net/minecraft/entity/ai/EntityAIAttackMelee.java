package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIAttackMelee extends EntityAIBase
{
    World worldObj;
    protected EntityCreature attacker;

    /**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     */
    protected int attackTick;

    /** The speed with which the mob will approach the target */
    double speedTowardsTarget;

    /**
     * When true, the mob will continue chasing its target, even if it can't find a path to them right now.
     */
    boolean longMemory;

    /** The PathEntity of our entity. */
    Path entityPathEntity;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    protected final int attackInterval = 20;

    public EntityAIAttackMelee(EntityCreature creature, double speedIn, boolean useLongMemory)
    {
        attacker = creature;
        worldObj = creature.world;
        speedTowardsTarget = speedIn;
        longMemory = useLongMemory;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else
        {
            entityPathEntity = attacker.getNavigator().getPathToEntityLiving(entitylivingbase);

            if (entityPathEntity != null)
            {
                return true;
            }
            else
            {
                return getAttackReachSqr(entitylivingbase) >= attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLivingBase entitylivingbase = attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (!longMemory)
        {
            return !attacker.getNavigator().noPath();
        }
        else if (!attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)))
        {
            return false;
        }
        else
        {
            return !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).isSpectator() && !((EntityPlayer)entitylivingbase).isCreative();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        attacker.getNavigator().setPath(entityPathEntity, speedTowardsTarget);
        delayCounter = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        EntityLivingBase entitylivingbase = attacker.getAttackTarget();

        if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() || ((EntityPlayer)entitylivingbase).isCreative()))
        {
            attacker.setAttackTarget(null);
        }

        attacker.getNavigator().clearPathEntity();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        EntityLivingBase entitylivingbase = attacker.getAttackTarget();
        attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        double d0 = attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        --delayCounter;

        if ((longMemory || attacker.getEntitySenses().canSee(entitylivingbase)) && delayCounter <= 0 && (targetX == 0.0D && targetY == 0.0D && targetZ == 0.0D || entitylivingbase.getDistanceSq(targetX, targetY, targetZ) >= 1.0D || attacker.getRNG().nextFloat() < 0.05F))
        {
            targetX = entitylivingbase.posX;
            targetY = entitylivingbase.getEntityBoundingBox().minY;
            targetZ = entitylivingbase.posZ;
            delayCounter = 4 + attacker.getRNG().nextInt(7);

            if (d0 > 1024.0D)
            {
                delayCounter += 10;
            }
            else if (d0 > 256.0D)
            {
                delayCounter += 5;
            }

            if (!attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, speedTowardsTarget))
            {
                delayCounter += 15;
            }
        }

        attackTick = Math.max(attackTick - 1, 0);
        checkAndPerformAttack(entitylivingbase, d0);
    }

    protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_)
    {
        double d0 = getAttackReachSqr(p_190102_1_);

        if (p_190102_2_ <= d0 && attackTick <= 0)
        {
            attackTick = 20;
            attacker.swingArm(EnumHand.MAIN_HAND);
            attacker.attackEntityAsMob(p_190102_1_);
        }
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return attacker.width * 2.0F * attacker.width * 2.0F + attackTarget.width;
    }
}
