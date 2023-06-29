package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class EntityAITarget extends EntityAIBase
{
    /** The entity that this task belongs to */
    protected final EntityCreature taskOwner;

    /**
     * If true, EntityAI targets must be able to be seen (cannot be blocked by walls) to be suitable targets.
     */
    protected boolean shouldCheckSight;

    /**
     * When true, only entities that can be reached with minimal effort will be targetted.
     */
    private final boolean nearbyOnly;

    /**
     * When nearbyOnly is true: 0 -> No target, but OK to search; 1 -> Nearby target found; 2 -> Target too far.
     */
    private int targetSearchStatus;

    /**
     * When nearbyOnly is true, this throttles target searching to avoid excessive pathfinding.
     */
    private int targetSearchDelay;

    /**
     * If  @shouldCheckSight is true, the number of ticks before the interuption of this AITastk when the entity does't
     * see the target
     */
    private int targetUnseenTicks;
    protected EntityLivingBase target;
    protected int unseenMemoryTicks;

    public EntityAITarget(EntityCreature creature, boolean checkSight)
    {
        this(creature, checkSight, false);
    }

    public EntityAITarget(EntityCreature creature, boolean checkSight, boolean onlyNearby)
    {
        unseenMemoryTicks = 60;
        taskOwner = creature;
        shouldCheckSight = checkSight;
        nearbyOnly = onlyNearby;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLivingBase entitylivingbase = taskOwner.getAttackTarget();

        if (entitylivingbase == null)
        {
            entitylivingbase = target;
        }

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
            Team team = taskOwner.getTeam();
            Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                double d0 = getTargetDistance();

                if (taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
                {
                    return false;
                }
                else
                {
                    if (shouldCheckSight)
                    {
                        if (taskOwner.getEntitySenses().canSee(entitylivingbase))
                        {
                            targetUnseenTicks = 0;
                        }
                        else if (++targetUnseenTicks > unseenMemoryTicks)
                        {
                            return false;
                        }
                    }

                    if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage)
                    {
                        return false;
                    }
                    else
                    {
                        taskOwner.setAttackTarget(entitylivingbase);
                        return true;
                    }
                }
            }
        }
    }

    protected double getTargetDistance()
    {
        IAttributeInstance iattributeinstance = taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        targetSearchStatus = 0;
        targetSearchDelay = 0;
        targetUnseenTicks = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        taskOwner.setAttackTarget(null);
        target = null;
    }

    /**
     * A static method used to see if an entity is a suitable target through a number of checks.
     */
    public static boolean isSuitableTarget(EntityLiving attacker, @Nullable EntityLivingBase target, boolean includeInvincibles, boolean checkSight)
    {
        if (target == null)
        {
            return false;
        }
        else if (target == attacker)
        {
            return false;
        }
        else if (!target.isEntityAlive())
        {
            return false;
        }
        else if (!attacker.canAttackClass(target.getClass()))
        {
            return false;
        }
        else if (attacker.isOnSameTeam(target))
        {
            return false;
        }
        else
        {
            if (attacker instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId() != null)
            {
                if (target instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId().equals(((IEntityOwnable)target).getOwnerId()))
                {
                    return false;
                }

                if (target == ((IEntityOwnable)attacker).getOwner())
                {
                    return false;
                }
            }
            else if (target instanceof EntityPlayer && !includeInvincibles && ((EntityPlayer)target).capabilities.disableDamage)
            {
                return false;
            }

            return !checkSight || attacker.getEntitySenses().canSee(target);
        }
    }

    /**
     * A method used to see if an entity is a suitable target through a number of checks. Args : entity,
     * canTargetInvinciblePlayer
     */
    protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles)
    {
        if (!isSuitableTarget(taskOwner, target, includeInvincibles, shouldCheckSight))
        {
            return false;
        }
        else if (!taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(target)))
        {
            return false;
        }
        else
        {
            if (nearbyOnly)
            {
                if (--targetSearchDelay <= 0)
                {
                    targetSearchStatus = 0;
                }

                if (targetSearchStatus == 0)
                {
                    targetSearchStatus = canEasilyReach(target) ? 1 : 2;
                }

                return targetSearchStatus != 2;
            }

            return true;
        }
    }

    /**
     * Checks to see if this entity can find a short path to the given target.
     */
    private boolean canEasilyReach(EntityLivingBase target)
    {
        targetSearchDelay = 10 + taskOwner.getRNG().nextInt(5);
        Path path = taskOwner.getNavigator().getPathToEntityLiving(target);

        if (path == null)
        {
            return false;
        }
        else
        {
            PathPoint pathpoint = path.getFinalPathPoint();

            if (pathpoint == null)
            {
                return false;
            }
            else
            {
                int i = pathpoint.xCoord - MathHelper.floor(target.posX);
                int j = pathpoint.zCoord - MathHelper.floor(target.posZ);
                return (double)(i * i + j * j) <= 2.25D;
            }
        }
    }

    public EntityAITarget func_190882_b(int p_190882_1_)
    {
        unseenMemoryTicks = p_190882_1_;
        return this;
    }
}
