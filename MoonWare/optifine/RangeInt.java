package optifine;

public class RangeInt
{
    private int min;
    private int max;

    public RangeInt(int p_i80_1_, int p_i80_2_)
    {
        min = Math.min(p_i80_1_, p_i80_2_);
        max = Math.max(p_i80_1_, p_i80_2_);
    }

    public boolean isInRange(int p_isInRange_1_)
    {
        if (p_isInRange_1_ < min)
        {
            return false;
        }
        else
        {
            return p_isInRange_1_ <= max;
        }
    }

    public int getMin()
    {
        return min;
    }

    public int getMax()
    {
        return max;
    }

    public String toString()
    {
        return "min: " + min + ", max: " + max;
    }
}
