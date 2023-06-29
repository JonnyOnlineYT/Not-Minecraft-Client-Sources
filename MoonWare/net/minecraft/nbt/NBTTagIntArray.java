package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagIntArray extends NBTBase
{
    /** The array of saved integers */
    private int[] intArray;

    NBTTagIntArray()
    {
    }

    public NBTTagIntArray(int[] p_i45132_1_)
    {
        intArray = p_i45132_1_;
    }

    public NBTTagIntArray(List<Integer> p_i47528_1_)
    {
        this(func_193584_a(p_i47528_1_));
    }

    private static int[] func_193584_a(List<Integer> p_193584_0_)
    {
        int[] aint = new int[p_193584_0_.size()];

        for (int i = 0; i < p_193584_0_.size(); ++i)
        {
            Integer integer = p_193584_0_.get(i);
            aint[i] = integer == null ? 0 : integer.intValue();
        }

        return aint;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeInt(intArray.length);

        for (int i : intArray)
        {
            output.writeInt(i);
        }
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read(32 * i);
        intArray = new int[i];

        for (int j = 0; j < i; ++j)
        {
            intArray[j] = input.readInt();
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 11;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[I;");

        for (int i = 0; i < intArray.length; ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(intArray[i]);
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagIntArray copy()
    {
        int[] aint = new int[intArray.length];
        System.arraycopy(intArray, 0, aint, 0, intArray.length);
        return new NBTTagIntArray(aint);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Arrays.equals(intArray, ((NBTTagIntArray)p_equals_1_).intArray);
    }

    public int hashCode()
    {
        return super.hashCode() ^ Arrays.hashCode(intArray);
    }

    public int[] getIntArray()
    {
        return intArray;
    }
}
