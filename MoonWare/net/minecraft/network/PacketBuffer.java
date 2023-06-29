package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ByteProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Component;

public class PacketBuffer extends ByteBuf
{
    private final ByteBuf buf;

    public PacketBuffer(ByteBuf wrapped)
    {
        buf = wrapped;
    }

    /**
     * Calculates the number of bytes required to fit the supplied int (0-5) if it were to be read/written using
     * readVarIntFromBuffer or writeVarIntToBuffer
     */
    public static int getVarIntSize(int input)
    {
        for (int i = 1; i < 5; ++i)
        {
            if ((input & -1 << i * 7) == 0)
            {
                return i;
            }
        }

        return 5;
    }

    public PacketBuffer writeByteArray(byte[] array)
    {
        writeVarIntToBuffer(array.length);
        writeBytes(array);
        return this;
    }

    public byte[] readByteArray()
    {
        return readByteArray(readableBytes());
    }

    public byte[] readByteArray(int maxLength)
    {
        int i = readVarIntFromBuffer();

        if (i > maxLength)
        {
            throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxLength);
        }
        else
        {
            byte[] abyte = new byte[i];
            readBytes(abyte);
            return abyte;
        }
    }

    /**
     * Writes an array of VarInts to the buffer, prefixed by the length of the array (as a VarInt).
     */
    public PacketBuffer writeVarIntArray(int[] array)
    {
        writeVarIntToBuffer(array.length);

        for (int i : array)
        {
            writeVarIntToBuffer(i);
        }

        return this;
    }

    public int[] readVarIntArray()
    {
        return readVarIntArray(readableBytes());
    }

    public int[] readVarIntArray(int maxLength)
    {
        int i = readVarIntFromBuffer();

        if (i > maxLength)
        {
            throw new DecoderException("VarIntArray with size " + i + " is bigger than allowed " + maxLength);
        }
        else
        {
            int[] aint = new int[i];

            for (int j = 0; j < aint.length; ++j)
            {
                aint[j] = readVarIntFromBuffer();
            }

            return aint;
        }
    }

    /**
     * Writes an array of longs to the buffer, prefixed by the length of the array (as a VarInt).
     */
    public PacketBuffer writeLongArray(long[] array)
    {
        writeVarIntToBuffer(array.length);

        for (long i : array)
        {
            writeLong(i);
        }

        return this;
    }

    /**
     * Reads a length-prefixed array of longs from the buffer.
     */
    public long[] readLongArray(@Nullable long[] array)
    {
        return readLongArray(array, readableBytes() / 8);
    }

    public long[] readLongArray(@Nullable long[] p_189423_1_, int p_189423_2_)
    {
        int i = readVarIntFromBuffer();

        if (p_189423_1_ == null || p_189423_1_.length != i)
        {
            if (i > p_189423_2_)
            {
                throw new DecoderException("LongArray with size " + i + " is bigger than allowed " + p_189423_2_);
            }

            p_189423_1_ = new long[i];
        }

        for (int j = 0; j < p_189423_1_.length; ++j)
        {
            p_189423_1_[j] = readLong();
        }

        return p_189423_1_;
    }

    public BlockPos readBlockPos()
    {
        return BlockPos.fromLong(readLong());
    }

    public PacketBuffer writeBlockPos(BlockPos pos)
    {
        writeLong(pos.toLong());
        return this;
    }

    public Component readTextComponent() throws IOException
    {
        return Component.Serializer.jsonToComponent(readStringFromBuffer(32767));
    }

    public PacketBuffer writeTextComponent(Component component)
    {
        return writeString(Component.Serializer.componentToJson(component));
    }

    public <T extends Enum<T>> T readEnumValue(Class<T> enumClass)
    {
        return (T)((Enum[])enumClass.getEnumConstants())[readVarIntFromBuffer()];
    }

    public PacketBuffer writeEnumValue(Enum<?> value)
    {
        return writeVarIntToBuffer(value.ordinal());
    }

    /**
     * Reads a compressed int from the buffer. To do so it maximally reads 5 byte-sized chunks whose most significant
     * bit dictates whether another byte should be read.
     */
    public int readVarIntFromBuffer()
    {
        int i = 0;
        int j = 0;

        while (true)
        {
            byte b0 = readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > 5)
            {
                throw new RuntimeException("VarInt too big");
            }

            if ((b0 & 128) != 128)
            {
                break;
            }
        }

        return i;
    }

    public long readVarLong()
    {
        long i = 0L;
        int j = 0;

        while (true)
        {
            byte b0 = readByte();
            i |= (long)(b0 & 127) << j++ * 7;

            if (j > 10)
            {
                throw new RuntimeException("VarLong too big");
            }

            if ((b0 & 128) != 128)
            {
                break;
            }
        }

        return i;
    }

    public PacketBuffer writeUuid(UUID uuid)
    {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID readUuid()
    {
        return new UUID(readLong(), readLong());
    }

    /**
     * Writes a compressed int to the buffer. The smallest number of bytes to fit the passed int will be written. Of
     * each such byte only 7 bits will be used to describe the actual value since its most significant bit dictates
     * whether the next byte is part of that same int. Micro-optimization for int values that are expected to have
     * values below 128.
     */
    public PacketBuffer writeVarIntToBuffer(int input)
    {
        while ((input & -128) != 0)
        {
            writeByte(input & 127 | 128);
            input >>>= 7;
        }

        writeByte(input);
        return this;
    }

    public PacketBuffer writeVarLong(long value)
    {
        while ((value & -128L) != 0L)
        {
            writeByte((int)(value & 127L) | 128);
            value >>>= 7;
        }

        writeByte((int)value);
        return this;
    }

    /**
     * Writes a compressed NBTTagCompound to this buffer
     */
    public PacketBuffer writeNBTTagCompoundToBuffer(@Nullable NBTTagCompound nbt)
    {
        if (nbt == null)
        {
            writeByte(0);
        }
        else
        {
            try
            {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(this));
            }
            catch (IOException ioexception)
            {
                throw new EncoderException(ioexception);
            }
        }

        return this;
    }

    @Nullable

    /**
     * Reads a compressed NBTTagCompound from this buffer
     */
    public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException
    {
        int i = readerIndex();
        byte b0 = readByte();

        if (b0 == 0)
        {
            return null;
        }
        else
        {
            readerIndex(i);

            try
            {
                return CompressedStreamTools.read(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
            }
            catch (IOException ioexception)
            {
                throw new EncoderException(ioexception);
            }
        }
    }

    /**
     * Writes the ItemStack's ID (short), then size (byte), then damage. (short)
     */
    public PacketBuffer writeItemStackToBuffer(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            writeShort(-1);
        }
        else
        {
            writeShort(Item.getIdFromItem(stack.getItem()));
            writeByte(stack.getCount());
            writeShort(stack.getMetadata());
            NBTTagCompound nbttagcompound = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag())
            {
                nbttagcompound = stack.getTagCompound();
            }

            writeNBTTagCompoundToBuffer(nbttagcompound);
        }

        return this;
    }

    /**
     * Reads an ItemStack from this buffer
     */
    public ItemStack readItemStackFromBuffer() throws IOException
    {
        int i = readShort();

        if (i < 0)
        {
            return ItemStack.EMPTY;
        }
        else
        {
            int j = readByte();
            int k = readShort();
            ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
            itemstack.setTagCompound(readNBTTagCompoundFromBuffer());
            return itemstack;
        }
    }

    /**
     * Reads a string from this buffer. Expected parameter is maximum allowed string length. Will throw IOException if
     * string length exceeds this value!
     */
    public String readStringFromBuffer(int maxLength)
    {
        int i = readVarIntFromBuffer();

        if (i > maxLength * 4)
        {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        }
        else if (i < 0)
        {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        else
        {
            String s = toString(readerIndex(), i, StandardCharsets.UTF_8);
            readerIndex(readerIndex() + i);

            if (s.length() > maxLength)
            {
                throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
            }
            else
            {
                return s;
            }
        }
    }

    public PacketBuffer writeString(String string)
    {
        byte[] abyte = string.getBytes(StandardCharsets.UTF_8);

        if (abyte.length > 32767)
        {
            throw new EncoderException("String too big (was " + abyte.length + " bytes encoded, max " + 32767 + ")");
        }
        else
        {
            writeVarIntToBuffer(abyte.length);
            writeBytes(abyte);
            return this;
        }
    }

    public Namespaced func_192575_l()
    {
        return new Namespaced(readStringFromBuffer(32767));
    }

    public PacketBuffer func_192572_a(Namespaced p_192572_1_)
    {
        writeString(p_192572_1_.toString());
        return this;
    }

    public Date func_192573_m()
    {
        return new Date(readLong());
    }

    public PacketBuffer func_192574_a(Date p_192574_1_)
    {
        writeLong(p_192574_1_.getTime());
        return this;
    }

    public int capacity()
    {
        return buf.capacity();
    }

    public ByteBuf capacity(int p_capacity_1_)
    {
        return buf.capacity(p_capacity_1_);
    }

    public int maxCapacity()
    {
        return buf.maxCapacity();
    }

    public ByteBufAllocator alloc()
    {
        return buf.alloc();
    }

    public ByteOrder order()
    {
        return buf.order();
    }

    public ByteBuf order(ByteOrder p_order_1_)
    {
        return buf.order(p_order_1_);
    }

    public ByteBuf unwrap()
    {
        return buf.unwrap();
    }

    public boolean isDirect()
    {
        return buf.isDirect();
    }

    public boolean isReadOnly()
    {
        return buf.isReadOnly();
    }

    public ByteBuf asReadOnly()
    {
        return buf.asReadOnly();
    }

    public int readerIndex()
    {
        return buf.readerIndex();
    }

    public ByteBuf readerIndex(int p_readerIndex_1_)
    {
        return buf.readerIndex(p_readerIndex_1_);
    }

    public int writerIndex()
    {
        return buf.writerIndex();
    }

    public ByteBuf writerIndex(int p_writerIndex_1_)
    {
        return buf.writerIndex(p_writerIndex_1_);
    }

    public ByteBuf setIndex(int p_setIndex_1_, int p_setIndex_2_)
    {
        return buf.setIndex(p_setIndex_1_, p_setIndex_2_);
    }

    public int readableBytes()
    {
        return buf.readableBytes();
    }

    public int writableBytes()
    {
        return buf.writableBytes();
    }

    public int maxWritableBytes()
    {
        return buf.maxWritableBytes();
    }

    public boolean isReadable()
    {
        return buf.isReadable();
    }

    public boolean isReadable(int p_isReadable_1_)
    {
        return buf.isReadable(p_isReadable_1_);
    }

    public boolean isWritable()
    {
        return buf.isWritable();
    }

    public boolean isWritable(int p_isWritable_1_)
    {
        return buf.isWritable(p_isWritable_1_);
    }

    public ByteBuf clear()
    {
        return buf.clear();
    }

    public ByteBuf markReaderIndex()
    {
        return buf.markReaderIndex();
    }

    public ByteBuf resetReaderIndex()
    {
        return buf.resetReaderIndex();
    }

    public ByteBuf markWriterIndex()
    {
        return buf.markWriterIndex();
    }

    public ByteBuf resetWriterIndex()
    {
        return buf.resetWriterIndex();
    }

    public ByteBuf discardReadBytes()
    {
        return buf.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes()
    {
        return buf.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int p_ensureWritable_1_)
    {
        return buf.ensureWritable(p_ensureWritable_1_);
    }

    public int ensureWritable(int p_ensureWritable_1_, boolean p_ensureWritable_2_)
    {
        return buf.ensureWritable(p_ensureWritable_1_, p_ensureWritable_2_);
    }

    public boolean getBoolean(int p_getBoolean_1_)
    {
        return buf.getBoolean(p_getBoolean_1_);
    }

    public byte getByte(int p_getByte_1_)
    {
        return buf.getByte(p_getByte_1_);
    }

    public short getUnsignedByte(int p_getUnsignedByte_1_)
    {
        return buf.getUnsignedByte(p_getUnsignedByte_1_);
    }

    public short getShort(int p_getShort_1_)
    {
        return buf.getShort(p_getShort_1_);
    }

    public short getShortLE(int p_getShortLE_1_)
    {
        return buf.getShortLE(p_getShortLE_1_);
    }

    public int getUnsignedShort(int p_getUnsignedShort_1_)
    {
        return buf.getUnsignedShort(p_getUnsignedShort_1_);
    }

    public int getUnsignedShortLE(int p_getUnsignedShortLE_1_)
    {
        return buf.getUnsignedShortLE(p_getUnsignedShortLE_1_);
    }

    public int getMedium(int p_getMedium_1_)
    {
        return buf.getMedium(p_getMedium_1_);
    }

    public int getMediumLE(int p_getMediumLE_1_)
    {
        return buf.getMediumLE(p_getMediumLE_1_);
    }

    public int getUnsignedMedium(int p_getUnsignedMedium_1_)
    {
        return buf.getUnsignedMedium(p_getUnsignedMedium_1_);
    }

    public int getUnsignedMediumLE(int p_getUnsignedMediumLE_1_)
    {
        return buf.getUnsignedMediumLE(p_getUnsignedMediumLE_1_);
    }

    public int getInt(int p_getInt_1_)
    {
        return buf.getInt(p_getInt_1_);
    }

    public int getIntLE(int p_getIntLE_1_)
    {
        return buf.getIntLE(p_getIntLE_1_);
    }

    public long getUnsignedInt(int p_getUnsignedInt_1_)
    {
        return buf.getUnsignedInt(p_getUnsignedInt_1_);
    }

    public long getUnsignedIntLE(int p_getUnsignedIntLE_1_)
    {
        return buf.getUnsignedIntLE(p_getUnsignedIntLE_1_);
    }

    public long getLong(int p_getLong_1_)
    {
        return buf.getLong(p_getLong_1_);
    }

    public long getLongLE(int p_getLongLE_1_)
    {
        return buf.getLongLE(p_getLongLE_1_);
    }

    public char getChar(int p_getChar_1_)
    {
        return buf.getChar(p_getChar_1_);
    }

    public float getFloat(int p_getFloat_1_)
    {
        return buf.getFloat(p_getFloat_1_);
    }

    public double getDouble(int p_getDouble_1_)
    {
        return buf.getDouble(p_getDouble_1_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_)
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_)
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_)
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_)
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_, int p_getBytes_3_, int p_getBytes_4_)
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_4_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, ByteBuffer p_getBytes_2_)
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_);
    }

    public ByteBuf getBytes(int p_getBytes_1_, OutputStream p_getBytes_2_, int p_getBytes_3_) throws IOException
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    public int getBytes(int p_getBytes_1_, GatheringByteChannel p_getBytes_2_, int p_getBytes_3_) throws IOException
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_);
    }

    public int getBytes(int p_getBytes_1_, FileChannel p_getBytes_2_, long p_getBytes_3_, int p_getBytes_5_) throws IOException
    {
        return buf.getBytes(p_getBytes_1_, p_getBytes_2_, p_getBytes_3_, p_getBytes_5_);
    }

    public CharSequence getCharSequence(int p_getCharSequence_1_, int p_getCharSequence_2_, Charset p_getCharSequence_3_)
    {
        return buf.getCharSequence(p_getCharSequence_1_, p_getCharSequence_2_, p_getCharSequence_3_);
    }

    public ByteBuf setBoolean(int p_setBoolean_1_, boolean p_setBoolean_2_)
    {
        return buf.setBoolean(p_setBoolean_1_, p_setBoolean_2_);
    }

    public ByteBuf setByte(int p_setByte_1_, int p_setByte_2_)
    {
        return buf.setByte(p_setByte_1_, p_setByte_2_);
    }

    public ByteBuf setShort(int p_setShort_1_, int p_setShort_2_)
    {
        return buf.setShort(p_setShort_1_, p_setShort_2_);
    }

    public ByteBuf setShortLE(int p_setShortLE_1_, int p_setShortLE_2_)
    {
        return buf.setShortLE(p_setShortLE_1_, p_setShortLE_2_);
    }

    public ByteBuf setMedium(int p_setMedium_1_, int p_setMedium_2_)
    {
        return buf.setMedium(p_setMedium_1_, p_setMedium_2_);
    }

    public ByteBuf setMediumLE(int p_setMediumLE_1_, int p_setMediumLE_2_)
    {
        return buf.setMediumLE(p_setMediumLE_1_, p_setMediumLE_2_);
    }

    public ByteBuf setInt(int p_setInt_1_, int p_setInt_2_)
    {
        return buf.setInt(p_setInt_1_, p_setInt_2_);
    }

    public ByteBuf setIntLE(int p_setIntLE_1_, int p_setIntLE_2_)
    {
        return buf.setIntLE(p_setIntLE_1_, p_setIntLE_2_);
    }

    public ByteBuf setLong(int p_setLong_1_, long p_setLong_2_)
    {
        return buf.setLong(p_setLong_1_, p_setLong_2_);
    }

    public ByteBuf setLongLE(int p_setLongLE_1_, long p_setLongLE_2_)
    {
        return buf.setLongLE(p_setLongLE_1_, p_setLongLE_2_);
    }

    public ByteBuf setChar(int p_setChar_1_, int p_setChar_2_)
    {
        return buf.setChar(p_setChar_1_, p_setChar_2_);
    }

    public ByteBuf setFloat(int p_setFloat_1_, float p_setFloat_2_)
    {
        return buf.setFloat(p_setFloat_1_, p_setFloat_2_);
    }

    public ByteBuf setDouble(int p_setDouble_1_, double p_setDouble_2_)
    {
        return buf.setDouble(p_setDouble_1_, p_setDouble_2_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_)
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_)
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_)
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_)
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_, int p_setBytes_3_, int p_setBytes_4_)
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_4_);
    }

    public ByteBuf setBytes(int p_setBytes_1_, ByteBuffer p_setBytes_2_)
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_);
    }

    public int setBytes(int p_setBytes_1_, InputStream p_setBytes_2_, int p_setBytes_3_) throws IOException
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    public int setBytes(int p_setBytes_1_, ScatteringByteChannel p_setBytes_2_, int p_setBytes_3_) throws IOException
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_);
    }

    public int setBytes(int p_setBytes_1_, FileChannel p_setBytes_2_, long p_setBytes_3_, int p_setBytes_5_) throws IOException
    {
        return buf.setBytes(p_setBytes_1_, p_setBytes_2_, p_setBytes_3_, p_setBytes_5_);
    }

    public ByteBuf setZero(int p_setZero_1_, int p_setZero_2_)
    {
        return buf.setZero(p_setZero_1_, p_setZero_2_);
    }

    public int setCharSequence(int p_setCharSequence_1_, CharSequence p_setCharSequence_2_, Charset p_setCharSequence_3_)
    {
        return buf.setCharSequence(p_setCharSequence_1_, p_setCharSequence_2_, p_setCharSequence_3_);
    }

    public boolean readBoolean()
    {
        return buf.readBoolean();
    }

    public byte readByte()
    {
        return buf.readByte();
    }

    public short readUnsignedByte()
    {
        return buf.readUnsignedByte();
    }

    public short readShort()
    {
        return buf.readShort();
    }

    public short readShortLE()
    {
        return buf.readShortLE();
    }

    public int readUnsignedShort()
    {
        return buf.readUnsignedShort();
    }

    public int readUnsignedShortLE()
    {
        return buf.readUnsignedShortLE();
    }

    public int readMedium()
    {
        return buf.readMedium();
    }

    public int readMediumLE()
    {
        return buf.readMediumLE();
    }

    public int readUnsignedMedium()
    {
        return buf.readUnsignedMedium();
    }

    public int readUnsignedMediumLE()
    {
        return buf.readUnsignedMediumLE();
    }

    public int readInt()
    {
        return buf.readInt();
    }

    public int readIntLE()
    {
        return buf.readIntLE();
    }

    public long readUnsignedInt()
    {
        return buf.readUnsignedInt();
    }

    public long readUnsignedIntLE()
    {
        return buf.readUnsignedIntLE();
    }

    public long readLong()
    {
        return buf.readLong();
    }

    public long readLongLE()
    {
        return buf.readLongLE();
    }

    public char readChar()
    {
        return buf.readChar();
    }

    public float readFloat()
    {
        return buf.readFloat();
    }

    public double readDouble()
    {
        return buf.readDouble();
    }

    public ByteBuf readBytes(int p_readBytes_1_)
    {
        return buf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readSlice(int p_readSlice_1_)
    {
        return buf.readSlice(p_readSlice_1_);
    }

    public ByteBuf readRetainedSlice(int p_readRetainedSlice_1_)
    {
        return buf.readRetainedSlice(p_readRetainedSlice_1_);
    }

    public ByteBuf readBytes(ByteBuf p_readBytes_1_)
    {
        return buf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_)
    {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_)
    {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }

    public ByteBuf readBytes(byte[] p_readBytes_1_)
    {
        return buf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readBytes(byte[] p_readBytes_1_, int p_readBytes_2_, int p_readBytes_3_)
    {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_3_);
    }

    public ByteBuf readBytes(ByteBuffer p_readBytes_1_)
    {
        return buf.readBytes(p_readBytes_1_);
    }

    public ByteBuf readBytes(OutputStream p_readBytes_1_, int p_readBytes_2_) throws IOException
    {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    public int readBytes(GatheringByteChannel p_readBytes_1_, int p_readBytes_2_) throws IOException
    {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_);
    }

    public CharSequence readCharSequence(int p_readCharSequence_1_, Charset p_readCharSequence_2_)
    {
        return buf.readCharSequence(p_readCharSequence_1_, p_readCharSequence_2_);
    }

    public int readBytes(FileChannel p_readBytes_1_, long p_readBytes_2_, int p_readBytes_4_) throws IOException
    {
        return buf.readBytes(p_readBytes_1_, p_readBytes_2_, p_readBytes_4_);
    }

    public ByteBuf skipBytes(int p_skipBytes_1_)
    {
        return buf.skipBytes(p_skipBytes_1_);
    }

    public ByteBuf writeBoolean(boolean p_writeBoolean_1_)
    {
        return buf.writeBoolean(p_writeBoolean_1_);
    }

    public ByteBuf writeByte(int p_writeByte_1_)
    {
        return buf.writeByte(p_writeByte_1_);
    }

    public ByteBuf writeShort(int p_writeShort_1_)
    {
        return buf.writeShort(p_writeShort_1_);
    }

    public ByteBuf writeShortLE(int p_writeShortLE_1_)
    {
        return buf.writeShortLE(p_writeShortLE_1_);
    }

    public ByteBuf writeMedium(int p_writeMedium_1_)
    {
        return buf.writeMedium(p_writeMedium_1_);
    }

    public ByteBuf writeMediumLE(int p_writeMediumLE_1_)
    {
        return buf.writeMediumLE(p_writeMediumLE_1_);
    }

    public ByteBuf writeInt(int p_writeInt_1_)
    {
        return buf.writeInt(p_writeInt_1_);
    }

    public ByteBuf writeIntLE(int p_writeIntLE_1_)
    {
        return buf.writeIntLE(p_writeIntLE_1_);
    }

    public ByteBuf writeLong(long p_writeLong_1_)
    {
        return buf.writeLong(p_writeLong_1_);
    }

    public ByteBuf writeLongLE(long p_writeLongLE_1_)
    {
        return buf.writeLongLE(p_writeLongLE_1_);
    }

    public ByteBuf writeChar(int p_writeChar_1_)
    {
        return buf.writeChar(p_writeChar_1_);
    }

    public ByteBuf writeFloat(float p_writeFloat_1_)
    {
        return buf.writeFloat(p_writeFloat_1_);
    }

    public ByteBuf writeDouble(double p_writeDouble_1_)
    {
        return buf.writeDouble(p_writeDouble_1_);
    }

    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_)
    {
        return buf.writeBytes(p_writeBytes_1_);
    }

    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_)
    {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_)
    {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }

    public ByteBuf writeBytes(byte[] p_writeBytes_1_)
    {
        return buf.writeBytes(p_writeBytes_1_);
    }

    public ByteBuf writeBytes(byte[] p_writeBytes_1_, int p_writeBytes_2_, int p_writeBytes_3_)
    {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_3_);
    }

    public ByteBuf writeBytes(ByteBuffer p_writeBytes_1_)
    {
        return buf.writeBytes(p_writeBytes_1_);
    }

    public int writeBytes(InputStream p_writeBytes_1_, int p_writeBytes_2_) throws IOException
    {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    public int writeBytes(ScatteringByteChannel p_writeBytes_1_, int p_writeBytes_2_) throws IOException
    {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
    }

    public int writeBytes(FileChannel p_writeBytes_1_, long p_writeBytes_2_, int p_writeBytes_4_) throws IOException
    {
        return buf.writeBytes(p_writeBytes_1_, p_writeBytes_2_, p_writeBytes_4_);
    }

    public ByteBuf writeZero(int p_writeZero_1_)
    {
        return buf.writeZero(p_writeZero_1_);
    }

    public int writeCharSequence(CharSequence p_writeCharSequence_1_, Charset p_writeCharSequence_2_)
    {
        return buf.writeCharSequence(p_writeCharSequence_1_, p_writeCharSequence_2_);
    }

    public int indexOf(int p_indexOf_1_, int p_indexOf_2_, byte p_indexOf_3_)
    {
        return buf.indexOf(p_indexOf_1_, p_indexOf_2_, p_indexOf_3_);
    }

    public int bytesBefore(byte p_bytesBefore_1_)
    {
        return buf.bytesBefore(p_bytesBefore_1_);
    }

    public int bytesBefore(int p_bytesBefore_1_, byte p_bytesBefore_2_)
    {
        return buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_);
    }

    public int bytesBefore(int p_bytesBefore_1_, int p_bytesBefore_2_, byte p_bytesBefore_3_)
    {
        return buf.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_, p_bytesBefore_3_);
    }

    public int forEachByte(ByteProcessor p_forEachByte_1_)
    {
        return buf.forEachByte(p_forEachByte_1_);
    }

    public int forEachByte(int p_forEachByte_1_, int p_forEachByte_2_, ByteProcessor p_forEachByte_3_)
    {
        return buf.forEachByte(p_forEachByte_1_, p_forEachByte_2_, p_forEachByte_3_);
    }

    public int forEachByteDesc(ByteProcessor p_forEachByteDesc_1_)
    {
        return buf.forEachByteDesc(p_forEachByteDesc_1_);
    }

    public int forEachByteDesc(int p_forEachByteDesc_1_, int p_forEachByteDesc_2_, ByteProcessor p_forEachByteDesc_3_)
    {
        return buf.forEachByteDesc(p_forEachByteDesc_1_, p_forEachByteDesc_2_, p_forEachByteDesc_3_);
    }

    public ByteBuf copy()
    {
        return buf.copy();
    }

    public ByteBuf copy(int p_copy_1_, int p_copy_2_)
    {
        return buf.copy(p_copy_1_, p_copy_2_);
    }

    public ByteBuf slice()
    {
        return buf.slice();
    }

    public ByteBuf retainedSlice()
    {
        return buf.retainedSlice();
    }

    public ByteBuf slice(int p_slice_1_, int p_slice_2_)
    {
        return buf.slice(p_slice_1_, p_slice_2_);
    }

    public ByteBuf retainedSlice(int p_retainedSlice_1_, int p_retainedSlice_2_)
    {
        return buf.retainedSlice(p_retainedSlice_1_, p_retainedSlice_2_);
    }

    public ByteBuf duplicate()
    {
        return buf.duplicate();
    }

    public ByteBuf retainedDuplicate()
    {
        return buf.retainedDuplicate();
    }

    public int nioBufferCount()
    {
        return buf.nioBufferCount();
    }

    public ByteBuffer nioBuffer()
    {
        return buf.nioBuffer();
    }

    public ByteBuffer nioBuffer(int p_nioBuffer_1_, int p_nioBuffer_2_)
    {
        return buf.nioBuffer(p_nioBuffer_1_, p_nioBuffer_2_);
    }

    public ByteBuffer internalNioBuffer(int p_internalNioBuffer_1_, int p_internalNioBuffer_2_)
    {
        return buf.internalNioBuffer(p_internalNioBuffer_1_, p_internalNioBuffer_2_);
    }

    public ByteBuffer[] nioBuffers()
    {
        return buf.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int p_nioBuffers_1_, int p_nioBuffers_2_)
    {
        return buf.nioBuffers(p_nioBuffers_1_, p_nioBuffers_2_);
    }

    public boolean hasArray()
    {
        return buf.hasArray();
    }

    public byte[] array()
    {
        return buf.array();
    }

    public int arrayOffset()
    {
        return buf.arrayOffset();
    }

    public boolean hasMemoryAddress()
    {
        return buf.hasMemoryAddress();
    }

    public long memoryAddress()
    {
        return buf.memoryAddress();
    }

    public String toString(Charset p_toString_1_)
    {
        return buf.toString(p_toString_1_);
    }

    public String toString(int p_toString_1_, int p_toString_2_, Charset p_toString_3_)
    {
        return buf.toString(p_toString_1_, p_toString_2_, p_toString_3_);
    }

    public int hashCode()
    {
        return buf.hashCode();
    }

    public boolean equals(Object p_equals_1_)
    {
        return buf.equals(p_equals_1_);
    }

    public int compareTo(ByteBuf p_compareTo_1_)
    {
        return buf.compareTo(p_compareTo_1_);
    }

    public String toString()
    {
        return buf.toString();
    }

    public ByteBuf retain(int p_retain_1_)
    {
        return buf.retain(p_retain_1_);
    }

    public ByteBuf retain()
    {
        return buf.retain();
    }

    public ByteBuf touch()
    {
        return buf.touch();
    }

    public ByteBuf touch(Object p_touch_1_)
    {
        return buf.touch(p_touch_1_);
    }

    public int refCnt()
    {
        return buf.refCnt();
    }

    public boolean release()
    {
        return buf.release();
    }

    public boolean release(int p_release_1_)
    {
        return buf.release(p_release_1_);
    }
}
