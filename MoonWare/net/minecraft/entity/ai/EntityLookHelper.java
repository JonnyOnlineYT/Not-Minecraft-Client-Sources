package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityLookHelper
{
    private final EntityLiving entity;

    /**
     * The amount of change that is made each update for an entity facing a direction.
     */
    private float deltaLookYaw;

    /**
     * The amount of change that is made each update for an entity facing a direction.
     */
    private float deltaLookPitch;

    /** Whether or not the entity is trying to look at something. */
    private boolean isLooking;
    private double posX;
    private double posY;
    private double posZ;

    public EntityLookHelper(EntityLiving entitylivingIn)
    {
        entity = entitylivingIn;
    }

    /**
     * Sets position to look at using entity
     */
    public void setLookPositionWithEntity(Entity entityIn, float deltaYaw, float deltaPitch)
    {
        posX = entityIn.posX;

        if (entityIn instanceof EntityLivingBase)
        {
            posY = entityIn.posY + (double)entityIn.getEyeHeight();
        }
        else
        {
            posY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D;
        }

        posZ = entityIn.posZ;
        deltaLookYaw = deltaYaw;
        deltaLookPitch = deltaPitch;
        isLooking = true;
    }

    /**
     * Sets position to look at
     */
    public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch)
    {
        posX = x;
        posY = y;
        posZ = z;
        deltaLookYaw = deltaYaw;
        deltaLookPitch = deltaPitch;
        isLooking = true;
    }

    /**
     * Updates look
     */
    public void onUpdateLook()
    {
        entity.rotationPitch = 0.0F;

        if (isLooking)
        {
            isLooking = false;
            double d0 = posX - entity.posX;
            double d1 = posY - (entity.posY + (double) entity.getEyeHeight());
            double d2 = posZ - entity.posZ;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float f1 = (float)(-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
            entity.rotationPitch = updateRotation(entity.rotationPitch, f1, deltaLookPitch);
            entity.rotationYawHead = updateRotation(entity.rotationYawHead, f, deltaLookYaw);
        }
        else
        {
            entity.rotationYawHead = updateRotation(entity.rotationYawHead, entity.renderYawOffset, 10.0F);
        }

        float f2 = MathHelper.wrapDegrees(entity.rotationYawHead - entity.renderYawOffset);

        if (!entity.getNavigator().noPath())
        {
            if (f2 < -75.0F)
            {
                entity.rotationYawHead = entity.renderYawOffset - 75.0F;
            }

            if (f2 > 75.0F)
            {
                entity.rotationYawHead = entity.renderYawOffset + 75.0F;
            }
        }
    }

    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_)
    {
        float f = MathHelper.wrapDegrees(p_75652_2_ - p_75652_1_);

        if (f > p_75652_3_)
        {
            f = p_75652_3_;
        }

        if (f < -p_75652_3_)
        {
            f = -p_75652_3_;
        }

        return p_75652_1_ + f;
    }

    public boolean getIsLooking()
    {
        return isLooking;
    }

    public double getLookPosX()
    {
        return posX;
    }

    public double getLookPosY()
    {
        return posY;
    }

    public double getLookPosZ()
    {
        return posZ;
    }
}
