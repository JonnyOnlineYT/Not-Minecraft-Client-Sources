package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagLongArray extends NBTBase
{
    private long[] field_193587_b;

    NBTTagLongArray()
    {
    }

    public NBTTagLongArray(long[] p_i47524_1_)
    {
        field_193587_b = p_i47524_1_;
    }

    public NBTTagLongArray(List<Long> p_i47525_1_)
    {
        this(func_193586_a(p_i47525_1_));
    }

    private static long[] func_193586_a(List<Long> p_193586_0_)
    {
        long[] along = new long[p_193586_0_.size()];

        for (int i = 0; i < p_193586_0_.size(); ++i)
        {
            Long olong = p_193586_0_.get(i);
            along[i] = olong == null ? 0L : olong.longValue();
        }

        return along;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeInt(field_193587_b.length);

        for (long i : field_193587_b)
        {
            output.writeLong(i);
        }
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read(64 * i);
        field_193587_b = new long[i];

        for (int j = 0; j < i; ++j)
        {
            field_193587_b[j] = input.readLong();
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 12;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[L;");

        for (int i = 0; i < field_193587_b.length; ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(field_193587_b[i]).append('L');
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagLongArray copy()
    {
        long[] along = new long[field_193587_b.length];
        System.arraycopy(field_193587_b, 0, along, 0, field_193587_b.length);
        return new NBTTagLongArray(along);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Arrays.equals(field_193587_b, ((NBTTagLongArray)p_equals_1_).field_193587_b);
    }

    public int hashCode()
    {
        return super.hashCode() ^ Arrays.hashCode(field_193587_b);
    }
}
