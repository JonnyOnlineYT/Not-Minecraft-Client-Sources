package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.math.Vec3d;

public class EntityAILlamaFollowCaravan extends EntityAIBase
{
    public EntityLlama field_190859_a;
    private double field_190860_b;
    private int field_190861_c;

    public EntityAILlamaFollowCaravan(EntityLlama p_i47305_1_, double p_i47305_2_)
    {
        field_190859_a = p_i47305_1_;
        field_190860_b = p_i47305_2_;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_190859_a.getLeashed() && !field_190859_a.func_190718_dR())
        {
            List<EntityLlama> list = field_190859_a.world.getEntitiesWithinAABB(field_190859_a.getClass(), field_190859_a.getEntityBoundingBox().expand(9.0D, 4.0D, 9.0D));
            EntityLlama entityllama = null;
            double d0 = Double.MAX_VALUE;

            for (EntityLlama entityllama1 : list)
            {
                if (entityllama1.func_190718_dR() && !entityllama1.func_190712_dQ())
                {
                    double d1 = field_190859_a.getDistanceSqToEntity(entityllama1);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        entityllama = entityllama1;
                    }
                }
            }

            if (entityllama == null)
            {
                for (EntityLlama entityllama2 : list)
                {
                    if (entityllama2.getLeashed() && !entityllama2.func_190712_dQ())
                    {
                        double d2 = field_190859_a.getDistanceSqToEntity(entityllama2);

                        if (d2 <= d0)
                        {
                            d0 = d2;
                            entityllama = entityllama2;
                        }
                    }
                }
            }

            if (entityllama == null)
            {
                return false;
            }
            else if (d0 < 4.0D)
            {
                return false;
            }
            else if (!entityllama.getLeashed() && !func_190858_a(entityllama, 1))
            {
                return false;
            }
            else
            {
                field_190859_a.func_190715_a(entityllama);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (field_190859_a.func_190718_dR() && field_190859_a.func_190716_dS().isEntityAlive() && func_190858_a(field_190859_a, 0))
        {
            double d0 = field_190859_a.getDistanceSqToEntity(field_190859_a.func_190716_dS());

            if (d0 > 676.0D)
            {
                if (field_190860_b <= 3.0D)
                {
                    field_190860_b *= 1.2D;
                    field_190861_c = 40;
                    return true;
                }

                if (field_190861_c == 0)
                {
                    return false;
                }
            }

            if (field_190861_c > 0)
            {
                --field_190861_c;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_190859_a.func_190709_dP();
        field_190860_b = 2.1D;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (field_190859_a.func_190718_dR())
        {
            EntityLlama entityllama = field_190859_a.func_190716_dS();
            double d0 = field_190859_a.getDistanceToEntity(entityllama);
            float f = 2.0F;
            Vec3d vec3d = (new Vec3d(entityllama.posX - field_190859_a.posX, entityllama.posY - field_190859_a.posY, entityllama.posZ - field_190859_a.posZ)).normalize().scale(Math.max(d0 - 2.0D, 0.0D));
            field_190859_a.getNavigator().tryMoveToXYZ(field_190859_a.posX + vec3d.xCoord, field_190859_a.posY + vec3d.yCoord, field_190859_a.posZ + vec3d.zCoord, field_190860_b);
        }
    }

    private boolean func_190858_a(EntityLlama p_190858_1_, int p_190858_2_)
    {
        if (p_190858_2_ > 8)
        {
            return false;
        }
        else if (p_190858_1_.func_190718_dR())
        {
            if (p_190858_1_.func_190716_dS().getLeashed())
            {
                return true;
            }
            else
            {
                EntityLlama entityllama = p_190858_1_.func_190716_dS();
                ++p_190858_2_;
                return func_190858_a(entityllama, p_190858_2_);
            }
        }
        else
        {
            return false;
        }
    }
}
