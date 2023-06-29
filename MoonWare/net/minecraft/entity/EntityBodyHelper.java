package net.minecraft.entity;

import net.minecraft.util.math.MathHelper;

public class EntityBodyHelper
{
    /** Instance of EntityLiving. */
    private final EntityLivingBase theLiving;

    /**
     * Used to progressively ajust the rotation of the body to the rotation of the head
     */
    private int rotationTickCounter;
    private float prevRenderYawHead;

    public EntityBodyHelper(EntityLivingBase livingIn)
    {
        theLiving = livingIn;
    }

    /**
     * Update the Head and Body rendenring angles
     */
    public void updateRenderAngles()
    {
        double d0 = theLiving.posX - theLiving.prevPosX;
        double d1 = theLiving.posZ - theLiving.prevPosZ;

        if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D)
        {
            theLiving.renderYawOffset = theLiving.rotationYaw;
            theLiving.rotationYawHead = computeAngleWithBound(theLiving.renderYawOffset, theLiving.rotationYawHead, 75.0F);
            prevRenderYawHead = theLiving.rotationYawHead;
            rotationTickCounter = 0;
        }
        else
        {
            if (theLiving.getPassengers().isEmpty() || !(theLiving.getPassengers().get(0) instanceof EntityLiving))
            {
                float f = 75.0F;

                if (Math.abs(theLiving.rotationYawHead - prevRenderYawHead) > 15.0F)
                {
                    rotationTickCounter = 0;
                    prevRenderYawHead = theLiving.rotationYawHead;
                }
                else
                {
                    ++rotationTickCounter;
                    int i = 10;

                    if (rotationTickCounter > 10)
                    {
                        f = Math.max(1.0F - (float)(rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
                    }
                }

                theLiving.renderYawOffset = computeAngleWithBound(theLiving.rotationYawHead, theLiving.renderYawOffset, f);
            }
        }
    }

    /**
     * Return the new angle2 such that the difference between angle1 and angle2 is lower than angleMax. Args : angle1,
     * angle2, angleMax
     */
    private float computeAngleWithBound(float p_75665_1_, float p_75665_2_, float p_75665_3_)
    {
        float f = MathHelper.wrapDegrees(p_75665_1_ - p_75665_2_);

        if (f < -p_75665_3_)
        {
            f = -p_75665_3_;
        }

        if (f >= p_75665_3_)
        {
            f = p_75665_3_;
        }

        return p_75665_1_ - f;
    }
}
