package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTPrimitive
{
    /** The byte value for the tag. */
    private byte data;

    NBTTagByte()
    {
    }

    public NBTTagByte(byte data)
    {
        this.data = data;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeByte(data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(72L);
        data = input.readByte();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 1;
    }

    public String toString()
    {
        return data + "b";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagByte copy()
    {
        return new NBTTagByte(data);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && data == ((NBTTagByte)p_equals_1_).data;
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
        return data;
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
