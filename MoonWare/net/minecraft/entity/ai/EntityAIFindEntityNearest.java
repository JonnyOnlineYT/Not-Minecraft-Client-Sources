package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityLiving mob;
    private final Predicate<EntityLivingBase> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase target;
    private final Class <? extends EntityLivingBase > classToCheck;

    public EntityAIFindEntityNearest(EntityLiving mobIn, Class <? extends EntityLivingBase > p_i45884_2_)
    {
        mob = mobIn;
        classToCheck = p_i45884_2_;

        if (mobIn instanceof EntityCreature)
        {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        predicate = new Predicate<EntityLivingBase>()
        {
            public boolean apply(@Nullable EntityLivingBase p_apply_1_)
            {
                double d0 = getFollowRange();

                if (p_apply_1_.isSneaking())
                {
                    d0 *= 0.800000011920929D;
                }

                if (p_apply_1_.isInvisible())
                {
                    return false;
                }
                else
                {
                    return !((double) p_apply_1_.getDistanceToEntity(mob) > d0) && EntityAITarget.isSuitableTarget(mob, p_apply_1_, false, true);
                }
            }
        };
        sorter = new EntityAINearestAttackableTarget.Sorter(mobIn);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        double d0 = getFollowRange();
        List<EntityLivingBase> list = mob.world.getEntitiesWithinAABB(classToCheck, mob.getEntityBoundingBox().expand(d0, 4.0D, d0), predicate);
        Collections.sort(list, sorter);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            target = list.get(0);
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLivingBase entitylivingbase = mob.getAttackTarget();

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
            double d0 = getFollowRange();

            if (mob.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
            {
                return false;
            }
            else
            {
                return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative();
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        mob.setAttackTarget(target);
        super.startExecuting();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        mob.setAttackTarget(null);
        super.startExecuting();
    }

    protected double getFollowRange()
    {
        IAttributeInstance iattributeinstance = mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}
