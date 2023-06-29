package optifine;

public class RangeListInt
{
    private RangeInt[] ranges = new RangeInt[0];

    public void addRange(RangeInt p_addRange_1_)
    {
        ranges = (RangeInt[])Config.addObjectToArray(ranges, p_addRange_1_);
    }

    public boolean isInRange(int p_isInRange_1_)
    {
        for (int i = 0; i < ranges.length; ++i)
        {
            RangeInt rangeint = ranges[i];

            if (rangeint.isInRange(p_isInRange_1_))
            {
                return true;
            }
        }

        return false;
    }

    public int getCountRanges()
    {
        return ranges.length;
    }

    public RangeInt getRange(int p_getRange_1_)
    {
        return ranges[p_getRange_1_];
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("[");

        for (int i = 0; i < ranges.length; ++i)
        {
            RangeInt rangeint = ranges[i];

            if (i > 0)
            {
                stringbuffer.append(", ");
            }

            stringbuffer.append(rangeint.toString());
        }

        stringbuffer.append("]");
        return stringbuffer.toString();
    }
}
