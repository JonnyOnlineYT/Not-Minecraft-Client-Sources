package net.minecraft.util;

import baritone.utils.accessor.IBitArray;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.Validate;

public class BitArray implements IBitArray
{
    /** The long array that is used to store the data for this BitArray. */
    private final long[] longArray;

    /** Number of bits a single entry takes up */
    private final int bitsPerEntry;

    /**
     * The maximum value for a single entry. This also asks as a bitmask for a single entry.
     * For instance, if bitsPerEntry were 5, this value would be 31 (ie, {@code 0b00011111}).
     */
    private final long maxEntryValue;

    /**
     * Number of entries in this array (<b>not</b> the length of the long array that internally backs this array)
     */
    private final int arraySize;

    public BitArray(int bitsPerEntryIn, int arraySizeIn)
    {
        Validate.inclusiveBetween(1L, 32L, bitsPerEntryIn);
        arraySize = arraySizeIn;
        bitsPerEntry = bitsPerEntryIn;
        maxEntryValue = (1L << bitsPerEntryIn) - 1L;
        longArray = new long[MathHelper.roundUp(arraySizeIn * bitsPerEntryIn, 64) / 64];
    }

    /**
     * Sets the entry at the given location to the given value
     */
    public void setAt(int index, int value)
    {
        Validate.inclusiveBetween(0L, arraySize - 1, index);
        Validate.inclusiveBetween(0L, maxEntryValue, value);
        int i = index * bitsPerEntry;
        int j = i / 64;
        int k = ((index + 1) * bitsPerEntry - 1) / 64;
        int l = i % 64;
        longArray[j] = longArray[j] & ~(maxEntryValue << l) | ((long)value & maxEntryValue) << l;

        if (j != k)
        {
            int i1 = 64 - l;
            int j1 = bitsPerEntry - i1;
            longArray[k] = longArray[k] >>> j1 << j1 | ((long)value & maxEntryValue) >> i1;
        }
    }

    /**
     * Gets the entry at the given index
     */
    public int getAt(int index)
    {
        Validate.inclusiveBetween(0L, arraySize - 1, index);
        int i = index * bitsPerEntry;
        int j = i / 64;
        int k = ((index + 1) * bitsPerEntry - 1) / 64;
        int l = i % 64;

        if (j == k)
        {
            return (int)(longArray[j] >>> l & maxEntryValue);
        }
        else
        {
            int i1 = 64 - l;
            return (int)((longArray[j] >>> l | longArray[k] << i1) & maxEntryValue);
        }
    }

    /**
     * Gets the long array that is used to store the data in this BitArray. This is useful for sending packet data.
     */
    public long[] getBackingLongArray()
    {
        return longArray;
    }

    public int size()
    {
        return arraySize;
    }

    @Override
    public int[] toArray() {
        int[] out = new int[arraySize];

        for (int idx = 0, kl = bitsPerEntry - 1; idx < arraySize; idx++, kl += bitsPerEntry) {
            int i = idx * bitsPerEntry;
            int j = i >> 6;
            int l = i & 63;
            int k = kl >> 6;
            long jl = longArray[j] >>> l;

            if (j == k) {
                out[idx] = (int) (jl & maxEntryValue);
            } else {
                out[idx] = (int) ((jl | longArray[k] << (64 - l)) & maxEntryValue);
            }
        }

        return out;
    }
}
