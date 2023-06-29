package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;

public class EntityAIFollow extends EntityAIBase
{
    private final EntityLiving field_192372_a;
    private final Predicate<EntityLiving> field_192373_b;
    private EntityLiving field_192374_c;
    private final double field_192375_d;
    private final PathNavigate field_192376_e;
    private int field_192377_f;
    private final float field_192378_g;
    private float field_192379_h;
    private final float field_192380_i;

    public EntityAIFollow(EntityLiving p_i47417_1_, double p_i47417_2_, float p_i47417_4_, float p_i47417_5_)
    {
        field_192372_a = p_i47417_1_;
        field_192373_b = new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
                return p_apply_1_ != null && p_i47417_1_.getClass() != p_apply_1_.getClass();
            }
        };
        field_192375_d = p_i47417_2_;
        field_192376_e = p_i47417_1_.getNavigator();
        field_192378_g = p_i47417_4_;
        field_192380_i = p_i47417_5_;
        setMutexBits(3);

        if (!(p_i47417_1_.getNavigator() instanceof PathNavigateGround) && !(p_i47417_1_.getNavigator() instanceof PathNavigateFlying))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List<EntityLiving> list = field_192372_a.world.getEntitiesWithinAABB(EntityLiving.class, field_192372_a.getEntityBoundingBox().expandXyz(field_192380_i), field_192373_b);

        if (!list.isEmpty())
        {
            for (EntityLiving entityliving : list)
            {
                if (!entityliving.isInvisible())
                {
                    field_192374_c = entityliving;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_192374_c != null && !field_192376_e.noPath() && field_192372_a.getDistanceSqToEntity(field_192374_c) > (double)(field_192378_g * field_192378_g);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_192377_f = 0;
        field_192379_h = field_192372_a.getPathPriority(PathNodeType.WATER);
        field_192372_a.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_192374_c = null;
        field_192376_e.clearPathEntity();
        field_192372_a.setPathPriority(PathNodeType.WATER, field_192379_h);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (field_192374_c != null && !field_192372_a.getLeashed())
        {
            field_192372_a.getLookHelper().setLookPositionWithEntity(field_192374_c, 10.0F, (float) field_192372_a.getVerticalFaceSpeed());

            if (--field_192377_f <= 0)
            {
                field_192377_f = 10;
                double d0 = field_192372_a.posX - field_192374_c.posX;
                double d1 = field_192372_a.posY - field_192374_c.posY;
                double d2 = field_192372_a.posZ - field_192374_c.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > (double)(field_192378_g * field_192378_g))
                {
                    field_192376_e.tryMoveToEntityLiving(field_192374_c, field_192375_d);
                }
                else
                {
                    field_192376_e.clearPathEntity();
                    EntityLookHelper entitylookhelper = field_192374_c.getLookHelper();

                    if (d3 <= (double) field_192378_g || entitylookhelper.getLookPosX() == field_192372_a.posX && entitylookhelper.getLookPosY() == field_192372_a.posY && entitylookhelper.getLookPosZ() == field_192372_a.posZ)
                    {
                        double d4 = field_192374_c.posX - field_192372_a.posX;
                        double d5 = field_192374_c.posZ - field_192372_a.posZ;
                        field_192376_e.tryMoveToXYZ(field_192372_a.posX - d4, field_192372_a.posY, field_192372_a.posZ - d5, field_192375_d);
                    }
                }
            }
        }
    }
}
