package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTPrimitive
{
    /** The short value for the tag. */
    private short data;

    public NBTTagShort()
    {
    }

    public NBTTagShort(short data)
    {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeShort(data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(80L);
        data = input.readShort();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 2;
    }

    public String toString()
    {
        return data + "s";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagShort copy()
    {
        return new NBTTagShort(data);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && data == ((NBTTagShort)p_equals_1_).data;
    }

    public int hashCode()
    {
        return super.hashCode() ^ data;
    }

    public long getLong()
    {
        return data;
    }

    public int getInt()
    {
        return data;
    }

    public short getShort()
    {
        return data;
    }

    public byte getByte()
    {
        return (byte)(data & 255);
    }

    public double getDouble()
    {
        return data;
    }

    public float getFloat()
    {
        return data;
    }
}
