package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class SPacketChunkData implements Packet<INetHandlerPlayClient>
{
    private int chunkX;
    private int chunkZ;
    private int availableSections;
    private byte[] buffer;
    private List<NBTTagCompound> tileEntityTags;
    private boolean loadChunk;

    public SPacketChunkData()
    {
    }

    public SPacketChunkData(Chunk p_i47124_1_, int p_i47124_2_)
    {
        chunkX = p_i47124_1_.xPosition;
        chunkZ = p_i47124_1_.zPosition;
        loadChunk = p_i47124_2_ == 65535;
        boolean flag = p_i47124_1_.getWorld().provider.isNether();
        buffer = new byte[calculateChunkSize(p_i47124_1_, flag, p_i47124_2_)];
        availableSections = extractChunkData(new PacketBuffer(getWriteBuffer()), p_i47124_1_, flag, p_i47124_2_);
        tileEntityTags = Lists.newArrayList();

        for (Map.Entry<BlockPos, TileEntity> entry : p_i47124_1_.getTileEntityMap().entrySet())
        {
            BlockPos blockpos = entry.getKey();
            TileEntity tileentity = entry.getValue();
            int i = blockpos.getY() >> 4;

            if (doChunkLoad() || (p_i47124_2_ & 1 << i) != 0)
            {
                NBTTagCompound nbttagcompound = tileentity.getUpdateTag();
                tileEntityTags.add(nbttagcompound);
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        chunkX = buf.readInt();
        chunkZ = buf.readInt();
        loadChunk = buf.readBoolean();
        availableSections = buf.readVarIntFromBuffer();
        int i = buf.readVarIntFromBuffer();

        if (i > 2097152)
        {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        }
        else
        {
            buffer = new byte[i];
            buf.readBytes(buffer);
            int j = buf.readVarIntFromBuffer();
            tileEntityTags = Lists.newArrayList();

            for (int k = 0; k < j; ++k)
            {
                tileEntityTags.add(buf.readNBTTagCompoundFromBuffer());
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(chunkX);
        buf.writeInt(chunkZ);
        buf.writeBoolean(loadChunk);
        buf.writeVarIntToBuffer(availableSections);
        buf.writeVarIntToBuffer(buffer.length);
        buf.writeBytes(buffer);
        buf.writeVarIntToBuffer(tileEntityTags.size());

        for (NBTTagCompound nbttagcompound : tileEntityTags)
        {
            buf.writeNBTTagCompoundToBuffer(nbttagcompound);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChunkData(this);
    }

    public PacketBuffer getReadBuffer()
    {
        return new PacketBuffer(Unpooled.wrappedBuffer(buffer));
    }

    private ByteBuf getWriteBuffer()
    {
        ByteBuf bytebuf = Unpooled.wrappedBuffer(buffer);
        bytebuf.writerIndex(0);
        return bytebuf;
    }

    public int extractChunkData(PacketBuffer p_189555_1_, Chunk p_189555_2_, boolean p_189555_3_, int p_189555_4_)
    {
        int i = 0;
        ExtendedBlockStorage[] aextendedblockstorage = p_189555_2_.getBlockStorageArray();
        int j = 0;

        for (int k = aextendedblockstorage.length; j < k; ++j)
        {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];

            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!doChunkLoad() || !extendedblockstorage.isEmpty()) && (p_189555_4_ & 1 << j) != 0)
            {
                i |= 1 << j;
                extendedblockstorage.getData().write(p_189555_1_);
                p_189555_1_.writeBytes(extendedblockstorage.getBlocklightArray().getData());

                if (p_189555_3_)
                {
                    p_189555_1_.writeBytes(extendedblockstorage.getSkylightArray().getData());
                }
            }
        }

        if (doChunkLoad())
        {
            p_189555_1_.writeBytes(p_189555_2_.getBiomeArray());
        }

        return i;
    }

    protected int calculateChunkSize(Chunk chunkIn, boolean p_189556_2_, int p_189556_3_)
    {
        int i = 0;
        ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        int j = 0;

        for (int k = aextendedblockstorage.length; j < k; ++j)
        {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];

            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!doChunkLoad() || !extendedblockstorage.isEmpty()) && (p_189556_3_ & 1 << j) != 0)
            {
                i = i + extendedblockstorage.getData().getSerializedSize();
                i = i + extendedblockstorage.getBlocklightArray().getData().length;

                if (p_189556_2_)
                {
                    i += extendedblockstorage.getSkylightArray().getData().length;
                }
            }
        }

        if (doChunkLoad())
        {
            i += chunkIn.getBiomeArray().length;
        }

        return i;
    }

    public int getChunkX()
    {
        return chunkX;
    }

    public int getChunkZ()
    {
        return chunkZ;
    }

    public int getExtractedSize()
    {
        return availableSections;
    }

    public boolean doChunkLoad()
    {
        return loadChunk;
    }

    public List<NBTTagCompound> getTileEntityTags()
    {
        return tileEntityTags;
    }
}
