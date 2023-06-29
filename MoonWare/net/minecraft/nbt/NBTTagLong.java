package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTPrimitive
{
    /** The long value for the tag. */
    private long data;

    NBTTagLong()
    {
    }

    public NBTTagLong(long data)
    {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeLong(data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(128L);
        data = input.readLong();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 4;
    }

    public String toString()
    {
        return data + "L";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagLong copy()
    {
        return new NBTTagLong(data);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && data == ((NBTTagLong)p_equals_1_).data;
    }

    public int hashCode()
    {
        return super.hashCode() ^ (int)(data ^ data >>> 32);
    }

    public long getLong()
    {
        return data;
    }

    public int getInt()
    {
        return (int)(data & -1L);
    }

    public short getShort()
    {
        return (short)((int)(data & 65535L));
    }

    public byte getByte()
    {
        return (byte)((int)(data & 255L));
    }

    public double getDouble()
    {
        return (double) data;
    }

    public float getFloat()
    {
        return (float) data;
    }
}
