package net.minecraft.nbt;

import net.minecraft.util.math.MathHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTPrimitive
{
    /** The double value for the tag. */
    private double data;

    NBTTagDouble()
    {
    }

    public NBTTagDouble(double data)
    {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeDouble(data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(128L);
        data = input.readDouble();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 6;
    }

    public String toString()
    {
        return data + "d";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagDouble copy()
    {
        return new NBTTagDouble(data);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && data == ((NBTTagDouble)p_equals_1_).data;
    }

    public int hashCode()
    {
        long i = Double.doubleToLongBits(data);
        return super.hashCode() ^ (int)(i ^ i >>> 32);
    }

    public long getLong()
    {
        return (long)Math.floor(data);
    }

    public int getInt()
    {
        return MathHelper.floor(data);
    }

    public short getShort()
    {
        return (short)(MathHelper.floor(data) & 65535);
    }

    public byte getByte()
    {
        return (byte)(MathHelper.floor(data) & 255);
    }

    public double getDouble()
    {
        return data;
    }

    public float getFloat()
    {
        return (float) data;
    }
}
