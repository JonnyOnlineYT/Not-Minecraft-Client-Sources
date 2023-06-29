package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTBase
{
    /** The byte array stored in the tag. */
    private byte[] data;

    NBTTagByteArray()
    {
    }

    public NBTTagByteArray(byte[] data)
    {
        this.data = data;
    }

    public NBTTagByteArray(List<Byte> p_i47529_1_)
    {
        this(func_193589_a(p_i47529_1_));
    }

    private static byte[] func_193589_a(List<Byte> p_193589_0_)
    {
        byte[] abyte = new byte[p_193589_0_.size()];

        for (int i = 0; i < p_193589_0_.size(); ++i)
        {
            Byte obyte = p_193589_0_.get(i);
            abyte[i] = obyte == null ? 0 : obyte.byteValue();
        }

        return abyte;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeInt(data.length);
        output.write(data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(192L);
        int i = input.readInt();
        sizeTracker.read(8 * i);
        data = new byte[i];
        input.readFully(data);
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 7;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[B;");

        for (int i = 0; i < data.length; ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(data[i]).append('B');
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        byte[] abyte = new byte[data.length];
        System.arraycopy(data, 0, abyte, 0, data.length);
        return new NBTTagByteArray(abyte);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Arrays.equals(data, ((NBTTagByteArray)p_equals_1_).data);
    }

    public int hashCode()
    {
        return super.hashCode() ^ Arrays.hashCode(data);
    }

    public byte[] getByteArray()
    {
        return data;
    }
}
