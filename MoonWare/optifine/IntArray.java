package optifine;

public class IntArray
{
    private int[] array;
    private int position;
    private int limit;

    public IntArray(int p_i62_1_)
    {
        array = new int[p_i62_1_];
    }

    public void put(int p_put_1_)
    {
        array[position] = p_put_1_;
        ++position;

        if (limit < position)
        {
            limit = position;
        }
    }

    public void put(int p_put_1_, int p_put_2_)
    {
        array[p_put_1_] = p_put_2_;

        if (limit < p_put_1_)
        {
            limit = p_put_1_;
        }
    }

    public void position(int p_position_1_)
    {
        position = p_position_1_;
    }

    public void put(int[] p_put_1_)
    {
        int i = p_put_1_.length;

        for (int j = 0; j < i; ++j)
        {
            array[position] = p_put_1_[j];
            ++position;
        }

        if (limit < position)
        {
            limit = position;
        }
    }

    public int get(int p_get_1_)
    {
        return array[p_get_1_];
    }

    public int[] getArray()
    {
        return array;
    }

    public void clear()
    {
        position = 0;
        limit = 0;
    }

    public int getLimit()
    {
        return limit;
    }

    public int getPosition()
    {
        return position;
    }
}
