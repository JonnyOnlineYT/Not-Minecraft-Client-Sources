package net.minecraft.world.gen.structure;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class StructureBoundingBox
{
    /** The first x coordinate of a bounding box. */
    public int minX;

    /** The first y coordinate of a bounding box. */
    public int minY;

    /** The first z coordinate of a bounding box. */
    public int minZ;

    /** The second x coordinate of a bounding box. */
    public int maxX;

    /** The second y coordinate of a bounding box. */
    public int maxY;

    /** The second z coordinate of a bounding box. */
    public int maxZ;

    public StructureBoundingBox()
    {
    }

    public StructureBoundingBox(int[] coords)
    {
        if (coords.length == 6)
        {
            minX = coords[0];
            minY = coords[1];
            minZ = coords[2];
            maxX = coords[3];
            maxY = coords[4];
            maxZ = coords[5];
        }
    }

    /**
     * returns a new StructureBoundingBox with MAX values
     */
    public static StructureBoundingBox getNewBoundingBox()
    {
        return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    /**
     * Create a bounding box with the specified dimensions and rotate it. Used to project a possible new component
     * Bounding Box - to check if it would cut anything already spawned
     */
    public static StructureBoundingBox getComponentToAddBoundingBox(int structureMinX, int structureMinY, int structureMinZ, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return new StructureBoundingBox(structureMinX + xMin, structureMinY + yMin, structureMinZ - zMax + 1 + zMin, structureMinX + xMax - 1 + xMin, structureMinY + yMax - 1 + yMin, structureMinZ + zMin);

            case SOUTH:
                return new StructureBoundingBox(structureMinX + xMin, structureMinY + yMin, structureMinZ + zMin, structureMinX + xMax - 1 + xMin, structureMinY + yMax - 1 + yMin, structureMinZ + zMax - 1 + zMin);

            case WEST:
                return new StructureBoundingBox(structureMinX - zMax + 1 + zMin, structureMinY + yMin, structureMinZ + xMin, structureMinX + zMin, structureMinY + yMax - 1 + yMin, structureMinZ + xMax - 1 + xMin);

            case EAST:
                return new StructureBoundingBox(structureMinX + zMin, structureMinY + yMin, structureMinZ + xMin, structureMinX + zMax - 1 + zMin, structureMinY + yMax - 1 + yMin, structureMinZ + xMax - 1 + xMin);

            default:
                return new StructureBoundingBox(structureMinX + xMin, structureMinY + yMin, structureMinZ + zMin, structureMinX + xMax - 1 + xMin, structureMinY + yMax - 1 + yMin, structureMinZ + zMax - 1 + zMin);
        }
    }

    public static StructureBoundingBox createProper(int p_175899_0_, int p_175899_1_, int p_175899_2_, int p_175899_3_, int p_175899_4_, int p_175899_5_)
    {
        return new StructureBoundingBox(Math.min(p_175899_0_, p_175899_3_), Math.min(p_175899_1_, p_175899_4_), Math.min(p_175899_2_, p_175899_5_), Math.max(p_175899_0_, p_175899_3_), Math.max(p_175899_1_, p_175899_4_), Math.max(p_175899_2_, p_175899_5_));
    }

    public StructureBoundingBox(StructureBoundingBox structurebb)
    {
        minX = structurebb.minX;
        minY = structurebb.minY;
        minZ = structurebb.minZ;
        maxX = structurebb.maxX;
        maxY = structurebb.maxY;
        maxZ = structurebb.maxZ;
    }

    public StructureBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax)
    {
        minX = xMin;
        minY = yMin;
        minZ = zMin;
        maxX = xMax;
        maxY = yMax;
        maxZ = zMax;
    }

    public StructureBoundingBox(Vec3i vec1, Vec3i vec2)
    {
        minX = Math.min(vec1.getX(), vec2.getX());
        minY = Math.min(vec1.getY(), vec2.getY());
        minZ = Math.min(vec1.getZ(), vec2.getZ());
        maxX = Math.max(vec1.getX(), vec2.getX());
        maxY = Math.max(vec1.getY(), vec2.getY());
        maxZ = Math.max(vec1.getZ(), vec2.getZ());
    }

    public StructureBoundingBox(int xMin, int zMin, int xMax, int zMax)
    {
        minX = xMin;
        minZ = zMin;
        maxX = xMax;
        maxZ = zMax;
        minY = 1;
        maxY = 512;
    }

    /**
     * Discover if bounding box can fit within the current bounding box object.
     */
    public boolean intersectsWith(StructureBoundingBox structurebb)
    {
        return maxX >= structurebb.minX && minX <= structurebb.maxX && maxZ >= structurebb.minZ && minZ <= structurebb.maxZ && maxY >= structurebb.minY && minY <= structurebb.maxY;
    }

    /**
     * Discover if a coordinate is inside the bounding box area.
     */
    public boolean intersectsWith(int minXIn, int minZIn, int maxXIn, int maxZIn)
    {
        return maxX >= minXIn && minX <= maxXIn && maxZ >= minZIn && minZ <= maxZIn;
    }

    /**
     * Expands a bounding box's dimensions to include the supplied bounding box.
     */
    public void expandTo(StructureBoundingBox sbb)
    {
        minX = Math.min(minX, sbb.minX);
        minY = Math.min(minY, sbb.minY);
        minZ = Math.min(minZ, sbb.minZ);
        maxX = Math.max(maxX, sbb.maxX);
        maxY = Math.max(maxY, sbb.maxY);
        maxZ = Math.max(maxZ, sbb.maxZ);
    }

    /**
     * Offsets the current bounding box by the specified amount.
     */
    public void offset(int x, int y, int z)
    {
        minX += x;
        minY += y;
        minZ += z;
        maxX += x;
        maxY += y;
        maxZ += z;
    }

    /**
     * Checks if given Vec3i is inside of StructureBoundingBox
     */
    public boolean isVecInside(Vec3i vec)
    {
        return vec.getX() >= minX && vec.getX() <= maxX && vec.getZ() >= minZ && vec.getZ() <= maxZ && vec.getY() >= minY && vec.getY() <= maxY;
    }

    public Vec3i getLength()
    {
        return new Vec3i(maxX - minX, maxY - minY, maxZ - minZ);
    }

    /**
     * Get dimension of the bounding box in the x direction.
     */
    public int getXSize()
    {
        return maxX - minX + 1;
    }

    /**
     * Get dimension of the bounding box in the y direction.
     */
    public int getYSize()
    {
        return maxY - minY + 1;
    }

    /**
     * Get dimension of the bounding box in the z direction.
     */
    public int getZSize()
    {
        return maxZ - minZ + 1;
    }

    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x0", minX).add("y0", minY).add("z0", minZ).add("x1", maxX).add("y1", maxY).add("z1", maxZ).toString();
    }

    public NBTTagIntArray toNBTTagIntArray()
    {
        return new NBTTagIntArray(new int[] {minX, minY, minZ, maxX, maxY, maxZ});
    }
}
