package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class AxisAlignedBB
{
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        minX = Math.min(x1, x2);
        minY = Math.min(y1, y2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxY = Math.max(y1, y2);
        maxZ = Math.max(z1, z2);
    }

    public AxisAlignedBB(BlockPos pos)
    {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public AxisAlignedBB(BlockPos pos1, BlockPos pos2)
    {
        this(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }

    public AxisAlignedBB(Vec3d min, Vec3d max)
    {
        this(min.xCoord, min.yCoord, min.zCoord, max.xCoord, max.yCoord, max.zCoord);
    }

    public AxisAlignedBB setMaxY(double y2)
    {
        return new AxisAlignedBB(minX, minY, minZ, maxX, y2, maxZ);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof AxisAlignedBB))
        {
            return false;
        }
        else
        {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB)p_equals_1_;

            if (Double.compare(axisalignedbb.minX, minX) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.minY, minY) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.minZ, minZ) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.maxX, maxX) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.maxY, maxY) != 0)
            {
                return false;
            }
            else
            {
                return Double.compare(axisalignedbb.maxZ, maxZ) == 0;
            }
        }
    }

    public int hashCode()
    {
        long i = Double.doubleToLongBits(minX);
        int j = (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(minY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(minZ);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(maxX);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(maxY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(maxZ);
        j = 31 * j + (int)(i ^ i >>> 32);
        return j;
    }

    public AxisAlignedBB func_191195_a(double p_191195_1_, double p_191195_3_, double p_191195_5_)
    {
        double d0 = minX;
        double d1 = minY;
        double d2 = minZ;
        double d3 = maxX;
        double d4 = maxY;
        double d5 = maxZ;

        if (p_191195_1_ < 0.0D)
        {
            d0 -= p_191195_1_;
        }
        else if (p_191195_1_ > 0.0D)
        {
            d3 -= p_191195_1_;
        }

        if (p_191195_3_ < 0.0D)
        {
            d1 -= p_191195_3_;
        }
        else if (p_191195_3_ > 0.0D)
        {
            d4 -= p_191195_3_;
        }

        if (p_191195_5_ < 0.0D)
        {
            d2 -= p_191195_5_;
        }
        else if (p_191195_5_ > 0.0D)
        {
            d5 -= p_191195_5_;
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Adds a coordinate to the bounding box, extending it if the point lies outside the current ranges.
     */
    public AxisAlignedBB addCoord(double x, double y, double z)
    {
        double d0 = minX;
        double d1 = minY;
        double d2 = minZ;
        double d3 = maxX;
        double d4 = maxY;
        double d5 = maxZ;

        if (x < 0.0D)
        {
            d0 += x;
        }
        else if (x > 0.0D)
        {
            d3 += x;
        }

        if (y < 0.0D)
        {
            d1 += y;
        }
        else if (y > 0.0D)
        {
            d4 += y;
        }

        if (z < 0.0D)
        {
            d2 += z;
        }
        else if (z > 0.0D)
        {
            d5 += z;
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Creates a new bounding box that has been expanded. If negative values are used, it will shrink.
     */
    public AxisAlignedBB expand(double x, double y, double z)
    {
        double d0 = minX - x;
        double d1 = minY - y;
        double d2 = minZ - z;
        double d3 = maxX + x;
        double d4 = maxY + y;
        double d5 = maxZ + z;
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB expandXyz(double value)
    {
        return expand(value, value, value);
    }

    public AxisAlignedBB func_191500_a(AxisAlignedBB p_191500_1_)
    {
        double d0 = Math.max(minX, p_191500_1_.minX);
        double d1 = Math.max(minY, p_191500_1_.minY);
        double d2 = Math.max(minZ, p_191500_1_.minZ);
        double d3 = Math.min(maxX, p_191500_1_.maxX);
        double d4 = Math.min(maxY, p_191500_1_.maxY);
        double d5 = Math.min(maxZ, p_191500_1_.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB union(AxisAlignedBB other)
    {
        double d0 = Math.min(minX, other.minX);
        double d1 = Math.min(minY, other.minY);
        double d2 = Math.min(minZ, other.minZ);
        double d3 = Math.max(maxX, other.maxX);
        double d4 = Math.max(maxY, other.maxY);
        double d5 = Math.max(maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Offsets the current bounding box by the specified amount.
     */
    public AxisAlignedBB offset(double x, double y, double z)
    {
        return new AxisAlignedBB(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);
    }

    public AxisAlignedBB offset(BlockPos pos)
    {
        return new AxisAlignedBB(minX + (double)pos.getX(), minY + (double)pos.getY(), minZ + (double)pos.getZ(), maxX + (double)pos.getX(), maxY + (double)pos.getY(), maxZ + (double)pos.getZ());
    }

    public AxisAlignedBB func_191194_a(Vec3d p_191194_1_)
    {
        return offset(p_191194_1_.xCoord, p_191194_1_.yCoord, p_191194_1_.zCoord);
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateXOffset(AxisAlignedBB other, double offsetX)
    {
        if (other.maxY > minY && other.minY < maxY && other.maxZ > minZ && other.minZ < maxZ)
        {
            if (offsetX > 0.0D && other.maxX <= minX)
            {
                double d1 = minX - other.maxX;

                if (d1 < offsetX)
                {
                    offsetX = d1;
                }
            }
            else if (offsetX < 0.0D && other.minX >= maxX)
            {
                double d0 = maxX - other.minX;

                if (d0 > offsetX)
                {
                    offsetX = d0;
                }
            }

            return offsetX;
        }
        else
        {
            return offsetX;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateYOffset(AxisAlignedBB other, double offsetY)
    {
        if (other.maxX > minX && other.minX < maxX && other.maxZ > minZ && other.minZ < maxZ)
        {
            if (offsetY > 0.0D && other.maxY <= minY)
            {
                double d1 = minY - other.maxY;

                if (d1 < offsetY)
                {
                    offsetY = d1;
                }
            }
            else if (offsetY < 0.0D && other.minY >= maxY)
            {
                double d0 = maxY - other.minY;

                if (d0 > offsetY)
                {
                    offsetY = d0;
                }
            }

            return offsetY;
        }
        else
        {
            return offsetY;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateZOffset(AxisAlignedBB other, double offsetZ)
    {
        if (other.maxX > minX && other.minX < maxX && other.maxY > minY && other.minY < maxY)
        {
            if (offsetZ > 0.0D && other.maxZ <= minZ)
            {
                double d1 = minZ - other.maxZ;

                if (d1 < offsetZ)
                {
                    offsetZ = d1;
                }
            }
            else if (offsetZ < 0.0D && other.minZ >= maxZ)
            {
                double d0 = maxZ - other.minZ;

                if (d0 > offsetZ)
                {
                    offsetZ = d0;
                }
            }

            return offsetZ;
        }
        else
        {
            return offsetZ;
        }
    }

    /**
     * Checks if the bounding box intersects with another.
     */
    public boolean intersectsWith(AxisAlignedBB other)
    {
        return intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean intersects(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return minX < x2 && maxX > x1 && minY < y2 && maxY > y1 && minZ < z2 && maxZ > z1;
    }

    public boolean intersects(Vec3d min, Vec3d max)
    {
        return intersects(Math.min(min.xCoord, max.xCoord), Math.min(min.yCoord, max.yCoord), Math.min(min.zCoord, max.zCoord), Math.max(min.xCoord, max.xCoord), Math.max(min.yCoord, max.yCoord), Math.max(min.zCoord, max.zCoord));
    }

    /**
     * Returns if the supplied Vec3D is completely inside the bounding box
     */
    public boolean isVecInside(Vec3d vec)
    {
        if (vec.xCoord > minX && vec.xCoord < maxX)
        {
            if (vec.yCoord > minY && vec.yCoord < maxY)
            {
                return vec.zCoord > minZ && vec.zCoord < maxZ;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the average length of the edges of the bounding box.
     */
    public double getAverageEdgeLength()
    {
        double d0 = maxX - minX;
        double d1 = maxY - minY;
        double d2 = maxZ - minZ;
        return (d0 + d1 + d2) / 3.0D;
    }

    public AxisAlignedBB contract(double value)
    {
        return expandXyz(-value);
    }

    @Nullable
    public RayTraceResult calculateIntercept(Vec3d vecA, Vec3d vecB)
    {
        Vec3d vec3d = collideWithXPlane(minX, vecA, vecB);
        EnumFacing enumfacing = EnumFacing.WEST;
        Vec3d vec3d1 = collideWithXPlane(maxX, vecA, vecB);

        if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.EAST;
        }

        vec3d1 = collideWithYPlane(minY, vecA, vecB);

        if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.DOWN;
        }

        vec3d1 = collideWithYPlane(maxY, vecA, vecB);

        if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.UP;
        }

        vec3d1 = collideWithZPlane(minZ, vecA, vecB);

        if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.NORTH;
        }

        vec3d1 = collideWithZPlane(maxZ, vecA, vecB);

        if (vec3d1 != null && isClosest(vecA, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.SOUTH;
        }

        return vec3d == null ? null : new RayTraceResult(vec3d, enumfacing);
    }

    @VisibleForTesting
    boolean isClosest(Vec3d p_186661_1_, @Nullable Vec3d p_186661_2_, Vec3d p_186661_3_)
    {
        return p_186661_2_ == null || p_186661_1_.squareDistanceTo(p_186661_3_) < p_186661_1_.squareDistanceTo(p_186661_2_);
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithXPlane(double p_186671_1_, Vec3d p_186671_3_, Vec3d p_186671_4_)
    {
        Vec3d vec3d = p_186671_3_.getIntermediateWithXValue(p_186671_4_, p_186671_1_);
        return vec3d != null && intersectsWithYZ(vec3d) ? vec3d : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithYPlane(double p_186663_1_, Vec3d p_186663_3_, Vec3d p_186663_4_)
    {
        Vec3d vec3d = p_186663_3_.getIntermediateWithYValue(p_186663_4_, p_186663_1_);
        return vec3d != null && intersectsWithXZ(vec3d) ? vec3d : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithZPlane(double p_186665_1_, Vec3d p_186665_3_, Vec3d p_186665_4_)
    {
        Vec3d vec3d = p_186665_3_.getIntermediateWithZValue(p_186665_4_, p_186665_1_);
        return vec3d != null && intersectsWithXY(vec3d) ? vec3d : null;
    }

    @VisibleForTesting
    public boolean intersectsWithYZ(Vec3d vec)
    {
        return vec.yCoord >= minY && vec.yCoord <= maxY && vec.zCoord >= minZ && vec.zCoord <= maxZ;
    }

    @VisibleForTesting
    public boolean intersectsWithXZ(Vec3d vec)
    {
        return vec.xCoord >= minX && vec.xCoord <= maxX && vec.zCoord >= minZ && vec.zCoord <= maxZ;
    }

    @VisibleForTesting
    public boolean intersectsWithXY(Vec3d vec)
    {
        return vec.xCoord >= minX && vec.xCoord <= maxX && vec.yCoord >= minY && vec.yCoord <= maxY;
    }

    public String toString()
    {
        return "box[" + minX + ", " + minY + ", " + minZ + " -> " + maxX + ", " + maxY + ", " + maxZ + "]";
    }

    public boolean hasNaN()
    {
        return Double.isNaN(minX) || Double.isNaN(minY) || Double.isNaN(minZ) || Double.isNaN(maxX) || Double.isNaN(maxY) || Double.isNaN(maxZ);
    }

    public Vec3d getCenter()
    {
        return new Vec3d(minX + (maxX - minX) * 0.5D, minY + (maxY - minY) * 0.5D, minZ + (maxZ - minZ) * 0.5D);
    }
}
