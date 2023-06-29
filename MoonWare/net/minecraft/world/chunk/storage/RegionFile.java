package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;

public class RegionFile
{
    private static final byte[] EMPTY_SECTOR = new byte[4096];
    private final File fileName;
    private RandomAccessFile dataFile;
    private final int[] offsets = new int[1024];
    private final int[] chunkTimestamps = new int[1024];
    private List<Boolean> sectorFree;

    /** McRegion sizeDelta */
    private int sizeDelta;
    private long lastModified;

    public RegionFile(File fileNameIn)
    {
        fileName = fileNameIn;
        sizeDelta = 0;

        try
        {
            if (fileNameIn.exists())
            {
                lastModified = fileNameIn.lastModified();
            }

            dataFile = new RandomAccessFile(fileNameIn, "rw");

            if (dataFile.length() < 4096L)
            {
                dataFile.write(EMPTY_SECTOR);
                dataFile.write(EMPTY_SECTOR);
                sizeDelta += 8192;
            }

            if ((dataFile.length() & 4095L) != 0L)
            {
                for (int i = 0; (long)i < (dataFile.length() & 4095L); ++i)
                {
                    dataFile.write(0);
                }
            }

            int i1 = (int) dataFile.length() / 4096;
            sectorFree = Lists.newArrayListWithCapacity(i1);

            for (int j = 0; j < i1; ++j)
            {
                sectorFree.add(Boolean.valueOf(true));
            }

            sectorFree.set(0, Boolean.valueOf(false));
            sectorFree.set(1, Boolean.valueOf(false));
            dataFile.seek(0L);

            for (int j1 = 0; j1 < 1024; ++j1)
            {
                int k = dataFile.readInt();
                offsets[j1] = k;

                if (k != 0 && (k >> 8) + (k & 255) <= sectorFree.size())
                {
                    for (int l = 0; l < (k & 255); ++l)
                    {
                        sectorFree.set((k >> 8) + l, Boolean.valueOf(false));
                    }
                }
            }

            for (int k1 = 0; k1 < 1024; ++k1)
            {
                int l1 = dataFile.readInt();
                chunkTimestamps[k1] = l1;
            }
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    @Nullable

    /**
     * Returns an uncompressed chunk stream from the region file.
     */
    public synchronized DataInputStream getChunkDataInputStream(int x, int z)
    {
        if (outOfBounds(x, z))
        {
            return null;
        }
        else
        {
            try
            {
                int i = getOffset(x, z);

                if (i == 0)
                {
                    return null;
                }
                else
                {
                    int j = i >> 8;
                    int k = i & 255;

                    if (j + k > sectorFree.size())
                    {
                        return null;
                    }
                    else
                    {
                        dataFile.seek(j * 4096);
                        int l = dataFile.readInt();

                        if (l > 4096 * k)
                        {
                            return null;
                        }
                        else if (l <= 0)
                        {
                            return null;
                        }
                        else
                        {
                            byte b0 = dataFile.readByte();

                            if (b0 == 1)
                            {
                                byte[] abyte1 = new byte[l - 1];
                                dataFile.read(abyte1);
                                return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte1))));
                            }
                            else if (b0 == 2)
                            {
                                byte[] abyte = new byte[l - 1];
                                dataFile.read(abyte);
                                return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(abyte))));
                            }
                            else
                            {
                                return null;
                            }
                        }
                    }
                }
            }
            catch (IOException var9)
            {
                return null;
            }
        }
    }

    @Nullable

    /**
     * Returns an output stream used to write chunk data. Data is on disk when the returned stream is closed.
     */
    public DataOutputStream getChunkDataOutputStream(int x, int z)
    {
        return outOfBounds(x, z) ? null : new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(new RegionFile.ChunkBuffer(x, z))));
    }

    /**
     * Writes the specified chunk to disk.
     */
    protected synchronized void write(int x, int z, byte[] data, int length)
    {
        try
        {
            int i = getOffset(x, z);
            int j = i >> 8;
            int k = i & 255;
            int l = (length + 5) / 4096 + 1;

            if (l >= 256)
            {
                return;
            }

            if (j != 0 && k == l)
            {
                write(j, data, length);
            }
            else
            {
                for (int i1 = 0; i1 < k; ++i1)
                {
                    sectorFree.set(j + i1, Boolean.valueOf(true));
                }

                int l1 = sectorFree.indexOf(Boolean.valueOf(true));
                int j1 = 0;

                if (l1 != -1)
                {
                    for (int k1 = l1; k1 < sectorFree.size(); ++k1)
                    {
                        if (j1 != 0)
                        {
                            if (sectorFree.get(k1).booleanValue())
                            {
                                ++j1;
                            }
                            else
                            {
                                j1 = 0;
                            }
                        }
                        else if (sectorFree.get(k1).booleanValue())
                        {
                            l1 = k1;
                            j1 = 1;
                        }

                        if (j1 >= l)
                        {
                            break;
                        }
                    }
                }

                if (j1 >= l)
                {
                    j = l1;
                    setOffset(x, z, l1 << 8 | l);

                    for (int j2 = 0; j2 < l; ++j2)
                    {
                        sectorFree.set(j + j2, Boolean.valueOf(false));
                    }

                    write(j, data, length);
                }
                else
                {
                    dataFile.seek(dataFile.length());
                    j = sectorFree.size();

                    for (int i2 = 0; i2 < l; ++i2)
                    {
                        dataFile.write(EMPTY_SECTOR);
                        sectorFree.add(Boolean.valueOf(false));
                    }

                    sizeDelta += 4096 * l;
                    write(j, data, length);
                    setOffset(x, z, j << 8 | l);
                }
            }

            setChunkTimestamp(x, z, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    /**
     * Writes the chunk data to this RegionFile.
     */
    private void write(int sectorNumber, byte[] data, int length) throws IOException
    {
        dataFile.seek(sectorNumber * 4096);
        dataFile.writeInt(length + 1);
        dataFile.writeByte(2);
        dataFile.write(data, 0, length);
    }

    /**
     * Checks if region is out of bounds.
     */
    private boolean outOfBounds(int x, int z)
    {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }

    /**
     * Gets a chunk's offset in region file.
     */
    private int getOffset(int x, int z)
    {
        return offsets[x + z * 32];
    }

    /**
     * Checks if a chunk has been saved.
     */
    public boolean isChunkSaved(int x, int z)
    {
        return getOffset(x, z) != 0;
    }

    /**
     * Sets the chunk's offset in the region file.
     */
    private void setOffset(int x, int z, int offset) throws IOException
    {
        offsets[x + z * 32] = offset;
        dataFile.seek((x + z * 32) * 4);
        dataFile.writeInt(offset);
    }

    /**
     * Updates the specified chunk's write timestamp.
     */
    private void setChunkTimestamp(int x, int z, int timestamp) throws IOException
    {
        chunkTimestamps[x + z * 32] = timestamp;
        dataFile.seek(4096 + (x + z * 32) * 4);
        dataFile.writeInt(timestamp);
    }

    /**
     * close this RegionFile and prevent further writes
     */
    public void close() throws IOException
    {
        if (dataFile != null)
        {
            dataFile.close();
        }
    }

    class ChunkBuffer extends ByteArrayOutputStream
    {
        private final int chunkX;
        private final int chunkZ;

        public ChunkBuffer(int x, int z)
        {
            super(8096);
            chunkX = x;
            chunkZ = z;
        }

        public void close() throws IOException
        {
            RegionFile.this.write(chunkX, chunkZ, buf, count);
        }
    }
}
