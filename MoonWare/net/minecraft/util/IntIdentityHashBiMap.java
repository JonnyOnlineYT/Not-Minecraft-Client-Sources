package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;

public class IntIdentityHashBiMap<K> implements IObjectIntIterable<K>
{
    private static final Object EMPTY = null;
    private K[] values;
    private int[] intKeys;
    private K[] byId;
    private int nextFreeIndex;
    private int mapSize;

    public IntIdentityHashBiMap(int initialCapacity)
    {
        initialCapacity = (int)((float)initialCapacity / 0.8F);
        values = (K[])(new Object[initialCapacity]);
        intKeys = new int[initialCapacity];
        byId = (K[])(new Object[initialCapacity]);
    }

    public int getId(@Nullable K p_186815_1_)
    {
        return getValue(getIndex(p_186815_1_, hashObject(p_186815_1_)));
    }

    @Nullable
    public K get(int idIn)
    {
        return idIn >= 0 && idIn < byId.length ? byId[idIn] : null;
    }

    private int getValue(int p_186805_1_)
    {
        return p_186805_1_ == -1 ? -1 : intKeys[p_186805_1_];
    }

    /**
     * Adds the given object while expanding this map
     */
    public int add(K objectIn)
    {
        int i = nextId();
        put(objectIn, i);
        return i;
    }

    private int nextId()
    {
        while (nextFreeIndex < byId.length && byId[nextFreeIndex] != null)
        {
            ++nextFreeIndex;
        }

        return nextFreeIndex;
    }

    /**
     * Rehashes the map to the new capacity
     */
    private void grow(int capacity)
    {
        K[] ak = values;
        int[] aint = intKeys;
        values = (K[])(new Object[capacity]);
        intKeys = new int[capacity];
        byId = (K[])(new Object[capacity]);
        nextFreeIndex = 0;
        mapSize = 0;

        for (int i = 0; i < ak.length; ++i)
        {
            if (ak[i] != null)
            {
                put(ak[i], aint[i]);
            }
        }
    }

    /**
     * Puts the provided object value with the integer key.
     */
    public void put(K objectIn, int intKey)
    {
        int i = Math.max(intKey, mapSize + 1);

        if ((float)i >= (float) values.length * 0.8F)
        {
            int j;

            for (j = values.length << 1; j < intKey; j <<= 1)
            {
            }

            grow(j);
        }

        int k = findEmpty(hashObject(objectIn));
        values[k] = objectIn;
        intKeys[k] = intKey;
        byId[intKey] = objectIn;
        ++mapSize;

        if (intKey == nextFreeIndex)
        {
            ++nextFreeIndex;
        }
    }

    private int hashObject(@Nullable K obectIn)
    {
        return (MathHelper.hash(System.identityHashCode(obectIn)) & Integer.MAX_VALUE) % values.length;
    }

    private int getIndex(@Nullable K objectIn, int p_186816_2_)
    {
        for (int i = p_186816_2_; i < values.length; ++i)
        {
            if (values[i] == objectIn)
            {
                return i;
            }

            if (values[i] == EMPTY)
            {
                return -1;
            }
        }

        for (int j = 0; j < p_186816_2_; ++j)
        {
            if (values[j] == objectIn)
            {
                return j;
            }

            if (values[j] == EMPTY)
            {
                return -1;
            }
        }

        return -1;
    }

    private int findEmpty(int p_186806_1_)
    {
        for (int i = p_186806_1_; i < values.length; ++i)
        {
            if (values[i] == EMPTY)
            {
                return i;
            }
        }

        for (int j = 0; j < p_186806_1_; ++j)
        {
            if (values[j] == EMPTY)
            {
                return j;
            }
        }

        throw new RuntimeException("Overflowed :(");
    }

    public Iterator<K> iterator()
    {
        return Iterators.filter(Iterators.forArray(byId), Predicates.notNull());
    }

    public void clear()
    {
        Arrays.fill(values, null);
        Arrays.fill(byId, null);
        nextFreeIndex = 0;
        mapSize = 0;
    }

    public int size()
    {
        return mapSize;
    }
}
