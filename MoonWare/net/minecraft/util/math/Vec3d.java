package net.minecraft.util.math;

import javax.annotation.Nullable;

public class Vec3d
{
    public static final Vec3d ZERO = new Vec3d(0.0D, 0.0D, 0.0D);

    /** X coordinate of Vec3D */
    public double xCoord;

    /** Y coordinate of Vec3D */
    public double yCoord;

    /** Z coordinate of Vec3D */
    public double zCoord;
    public double xCoordS;
    public double yCoordS;

    /** Z coordinate of Vec3D */
    public double zCoordS;

    public final double x;

    /** Y coordinate of Vec3D */
    public double y;

    /** Z coordinate of Vec3D */
    public final double z;



    public Vec3d(double x, double y, double z)
    {
        if (x == -0.0D)
        {
            x = 0.0D;
        }

        if (y == -0.0D)
        {
            y = 0.0D;
        }

        if (z == -0.0D)
        {
            z = 0.0D;
        }
        xCoordS = x;
        yCoordS = y;
        zCoordS = z;
        xCoord =xCoordS;

        this.x = xCoordS;
        yCoord = yCoordS;

        this.y = yCoordS;
        zCoord = zCoordS;

        this.z = zCoordS;
    }

    public Vec3d(Vec3i vector)
    {
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Returns a new vector with the result of the specified vector minus this.
     */
    public Vec3d subtractReverse(Vec3d vec)
    {
        return new Vec3d(vec.xCoord - xCoord, vec.yCoord - yCoord, vec.zCoord - zCoord);
    }

    /**
     * Normalizes the vector to a length of 1 (except if it is the zero vector)
     */
    public Vec3d normalize()
    {
        double d0 = MathHelper.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
        return d0 < 1.0E-4D ? ZERO : new Vec3d(xCoord / d0, yCoord / d0, zCoord / d0);
    }

    public double dotProduct(Vec3d vec)
    {
        return xCoord * vec.xCoord + yCoord * vec.yCoord + zCoord * vec.zCoord;
    }

    /**
     * Returns a new vector with the result of this vector x the specified vector.
     */
    public Vec3d crossProduct(Vec3d vec)
    {
        return new Vec3d(yCoord * vec.zCoord - zCoord * vec.yCoord, zCoord * vec.xCoord - xCoord * vec.zCoord, xCoord * vec.yCoord - yCoord * vec.xCoord);
    }

    public Vec3d subtract(Vec3d vec)
    {
        return subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3d subtract(double x, double y, double z)
    {
        return addVector(-x, -y, -z);
    }

    public Vec3d add(Vec3d vec)
    {
        return addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    public Vec3d add(double x, double y, double z) {
        return new Vec3d(xCoord + x, yCoord + y, zCoord + z);
    }

    /**
     * Adds the specified x,y,z vector components to this vector and returns the resulting vector. Does not change this
     * vector.
     */
    public Vec3d addVector(double x, double y, double z)
    {
        return new Vec3d(xCoord + x, yCoord + y, zCoord + z);
    }

    /**
     * Euclidean distance between this and the specified vector, returned as double.
     */
    public double distanceTo(Vec3d vec)
    {
        double d0 = vec.xCoord - xCoord;
        double d1 = vec.yCoord - yCoord;
        double d2 = vec.zCoord - zCoord;
        return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * The square of the Euclidean distance between this and the specified vector.
     */
    public double squareDistanceTo(Vec3d vec)
    {
        double d0 = vec.xCoord - xCoord;
        double d1 = vec.yCoord - yCoord;
        double d2 = vec.zCoord - zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double squareDistanceTo(double xIn, double yIn, double zIn)
    {
        double d0 = xIn - xCoord;
        double d1 = yIn - yCoord;
        double d2 = zIn - zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public Vec3d scale(double p_186678_1_)
    {
        return new Vec3d(xCoord * p_186678_1_, yCoord * p_186678_1_, zCoord * p_186678_1_);
    }

    /**
     * Returns the length of the vector.
     */
    public double lengthVector()
    {
        return MathHelper.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
    }

    public double lengthSquared()
    {
        return xCoord * xCoord + yCoord * yCoord + zCoord * zCoord;
    }

    @Nullable

    /**
     * Returns a new vector with x value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vec3d getIntermediateWithXValue(Vec3d vec, double x)
    {
        double d0 = vec.xCoord - xCoord;
        double d1 = vec.yCoord - yCoord;
        double d2 = vec.zCoord - zCoord;

        if (d0 * d0 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (x - xCoord) / d0;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vec3d(xCoord + d0 * d3, yCoord + d1 * d3, zCoord + d2 * d3) : null;
        }
    }

    @Nullable

    /**
     * Returns a new vector with y value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vec3d getIntermediateWithYValue(Vec3d vec, double y)
    {
        double d0 = vec.xCoord - xCoord;
        double d1 = vec.yCoord - yCoord;
        double d2 = vec.zCoord - zCoord;

        if (d1 * d1 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (y - yCoord) / d1;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vec3d(xCoord + d0 * d3, yCoord + d1 * d3, zCoord + d2 * d3) : null;
        }
    }

    @Nullable

    /**
     * Returns a new vector with z value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    public Vec3d getIntermediateWithZValue(Vec3d vec, double z)
    {
        double d0 = vec.xCoord - xCoord;
        double d1 = vec.yCoord - yCoord;
        double d2 = vec.zCoord - zCoord;

        if (d2 * d2 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double d3 = (z - zCoord) / d2;
            return d3 >= 0.0D && d3 <= 1.0D ? new Vec3d(xCoord + d0 * d3, yCoord + d1 * d3, zCoord + d2 * d3) : null;
        }
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof Vec3d))
        {
            return false;
        }
        else
        {
            Vec3d vec3d = (Vec3d)p_equals_1_;

            if (Double.compare(vec3d.xCoord, xCoord) != 0)
            {
                return false;
            }
            else if (Double.compare(vec3d.yCoord, yCoord) != 0)
            {
                return false;
            }
            else
            {
                return Double.compare(vec3d.zCoord, zCoord) == 0;
            }
        }
    }

    public int hashCode()
    {
        long j = Double.doubleToLongBits(xCoord);
        int i = (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(yCoord);
        i = 31 * i + (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(zCoord);
        i = 31 * i + (int)(j ^ j >>> 32);
        return i;
    }

    public String toString()
    {
        return "(" + xCoord + ", " + yCoord + ", " + zCoord + ")";
    }

    public Vec3d rotatePitch(float pitch)
    {
        float f = MathHelper.cos(pitch);
        float f1 = MathHelper.sin(pitch);
        double d0 = xCoord;
        double d1 = yCoord * (double)f + zCoord * (double)f1;
        double d2 = zCoord * (double)f - yCoord * (double)f1;
        return new Vec3d(d0, d1, d2);
    }

    public Vec3d rotateYaw(float yaw)
    {
        float f = MathHelper.cos(yaw);
        float f1 = MathHelper.sin(yaw);
        double d0 = xCoord * (double)f + zCoord * (double)f1;
        double d1 = yCoord;
        double d2 = zCoord * (double)f - xCoord * (double)f1;
        return new Vec3d(d0, d1, d2);
    }

    /**
     * returns a Vec3d from given pitch and yaw degrees as Vec2f
     */
    public static Vec3d fromPitchYawVector(Vec2f p_189984_0_)
    {
        return fromPitchYaw(p_189984_0_.x, p_189984_0_.y);
    }

    /**
     * returns a Vec3d from given pitch and yaw degrees
     */
    public static Vec3d fromPitchYaw(float p_189986_0_, float p_189986_1_)
    {
        float f = MathHelper.cos(-p_189986_1_ * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-p_189986_1_ * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-p_189986_0_ * 0.017453292F);
        float f3 = MathHelper.sin(-p_189986_0_ * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }
}
