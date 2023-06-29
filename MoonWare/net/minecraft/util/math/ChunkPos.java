package net.minecraft.util.math;

import net.minecraft.entity.Entity;

public class ChunkPos
{
    /** The X position of this Chunk Coordinate Pair */
    public final int chunkXPos;

    /** The Z position of this Chunk Coordinate Pair */
    public final int chunkZPos;
    private int cachedHashCode;

    public ChunkPos(int x, int z)
    {
        chunkXPos = x;
        chunkZPos = z;
    }

    public ChunkPos(BlockPos pos)
    {
        chunkXPos = pos.getX() >> 4;
        chunkZPos = pos.getZ() >> 4;
    }

    /**
     * Converts the chunk coordinate pair to a long
     */
    public static long asLong(int x, int z)
    {
        return (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
    }

    public int hashCode()
    {
        if (cachedHashCode != 0)
        {
            return cachedHashCode;
        }
        else
        {
            int i = 1664525 * chunkXPos + 1013904223;
            int j = 1664525 * (chunkZPos ^ -559038737) + 1013904223;
            cachedHashCode = i ^ j;
            return cachedHashCode;
        }
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ChunkPos))
        {
            return false;
        }
        else
        {
            ChunkPos chunkpos = (ChunkPos)p_equals_1_;
            return chunkXPos == chunkpos.chunkXPos && chunkZPos == chunkpos.chunkZPos;
        }
    }

    public double getDistanceSq(Entity entityIn)
    {
        double d0 = chunkXPos * 16 + 8;
        double d1 = chunkZPos * 16 + 8;
        double d2 = d0 - entityIn.posX;
        double d3 = d1 - entityIn.posZ;
        return d2 * d2 + d3 * d3;
    }

    /**
     * Get the first world X coordinate that belongs to this Chunk
     */
    public int getXStart()
    {
        return chunkXPos << 4;
    }

    /**
     * Get the first world Z coordinate that belongs to this Chunk
     */
    public int getZStart()
    {
        return chunkZPos << 4;
    }

    /**
     * Get the last world X coordinate that belongs to this Chunk
     */
    public int getXEnd()
    {
        return (chunkXPos << 4) + 15;
    }

    /**
     * Get the last world Z coordinate that belongs to this Chunk
     */
    public int getZEnd()
    {
        return (chunkZPos << 4) + 15;
    }

    /**
     * Get the World coordinates of the Block with the given Chunk coordinates relative to this chunk
     */
    public BlockPos getBlock(int x, int y, int z)
    {
        return new BlockPos((chunkXPos << 4) + x, y, (chunkZPos << 4) + z);
    }

    public String toString()
    {
        return "[" + chunkXPos + ", " + chunkZPos + "]";
    }
}
