package net.minecraft.util;

public class MouseFilter
{
    private float targetValue;
    private float remainingValue;
    private float lastAmount;

    /**
     * Smooths mouse input
     */
    public float smooth(float p_76333_1_, float p_76333_2_)
    {
        targetValue += p_76333_1_;
        p_76333_1_ = (targetValue - remainingValue) * p_76333_2_;
        lastAmount += (p_76333_1_ - lastAmount) * 0.5F;

        if (p_76333_1_ > 0.0F && p_76333_1_ > lastAmount || p_76333_1_ < 0.0F && p_76333_1_ < lastAmount)
        {
            p_76333_1_ = lastAmount;
        }

        remainingValue += p_76333_1_;
        return p_76333_1_;
    }

    public void reset()
    {
        targetValue = 0.0F;
        remainingValue = 0.0F;
        lastAmount = 0.0F;
    }
}
