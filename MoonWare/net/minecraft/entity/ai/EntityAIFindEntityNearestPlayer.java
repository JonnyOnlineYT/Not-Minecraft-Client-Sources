package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase
{
    private static final Logger LOGGER = LogManager.getLogger();

    /** The entity that use this AI */
    private final EntityLiving entityLiving;
    private final Predicate<Entity> predicate;

    /** Used to compare two entities */
    private final EntityAINearestAttackableTarget.Sorter sorter;

    /** The current target */
    private EntityLivingBase entityTarget;

    public EntityAIFindEntityNearestPlayer(EntityLiving entityLivingIn)
    {
        entityLiving = entityLivingIn;

        if (entityLivingIn instanceof EntityCreature)
        {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        predicate = new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                if (!(p_apply_1_ instanceof EntityPlayer))
                {
                    return false;
                }
                else if (((EntityPlayer)p_apply_1_).capabilities.disableDamage)
                {
                    return false;
                }
                else
                {
                    double d0 = maxTargetRange();

                    if (p_apply_1_.isSneaking())
                    {
                        d0 *= 0.800000011920929D;
                    }

                    if (p_apply_1_.isInvisible())
                    {
                        float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();

                        if (f < 0.1F)
                        {
                            f = 0.1F;
                        }

                        d0 *= 0.7F * f;
                    }

                    return !((double) p_apply_1_.getDistanceToEntity(entityLiving) > d0) && EntityAITarget.isSuitableTarget(entityLiving, (EntityLivingBase) p_apply_1_, false, true);
                }
            }
        };
        sorter = new EntityAINearestAttackableTarget.Sorter(entityLivingIn);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        double d0 = maxTargetRange();
        List<EntityPlayer> list = entityLiving.world.getEntitiesWithinAABB(EntityPlayer.class, entityLiving.getEntityBoundingBox().expand(d0, 4.0D, d0), predicate);
        Collections.sort(list, sorter);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            entityTarget = list.get(0);
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLivingBase entitylivingbase = entityLiving.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage)
        {
            return false;
        }
        else
        {
            Team team = entityLiving.getTeam();
            Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                double d0 = maxTargetRange();

                if (entityLiving.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
                {
                    return false;
                }
                else
                {
                    return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative();
                }
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        entityLiving.setAttackTarget(entityTarget);
        super.startExecuting();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        entityLiving.setAttackTarget(null);
        super.startExecuting();
    }

    /**
     * Return the max target range of the entiity (16 by default)
     */
    protected double maxTargetRange()
    {
        IAttributeInstance iattributeinstance = entityLiving.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}
