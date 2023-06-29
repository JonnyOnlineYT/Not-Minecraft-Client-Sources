package net.minecraft.client.renderer.vec;

import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class Vec3d {
    public static final Vec3d ZERO = new Vec3d(0.0, 0.0, 0.0);
    public final double x;
    public final double y;
    public final double z;

    public Vec3d(double x, double y, double z) {
        if (x == -0.0) {
            x = 0.0;
        }
        if (y == -0.0) {
            y = 0.0;
        }
        if (z == -0.0) {
            z = 0.0;
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(Vec3i vector) {
        this(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vec3d subtractReverse(Vec3d vec) {
        return new Vec3d(vec.x - x, vec.y - y, vec.z - z);
    }

    public Vec3d normalize() {
        double d0 = MathHelper.sqrt(x * x + y * y + z * z);
        return d0 < 1.0E-4 ? ZERO : new Vec3d(x / d0, y / d0, z / d0);
    }

    public double dotProduct(Vec3d vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vec3d crossProduct(Vec3d vec) {
        return new Vec3d(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
    }

    public Vec3d subtract(Vec3d vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public Vec3d subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    public Vec3d add(Vec3d vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vec3d add(double x, double y, double z) {
        return new Vec3d(this.x + x, this.y + y, this.z + z);
    }

    public double distanceTo(Vec3d vec) {
        double d0 = vec.x - x;
        double d1 = vec.y - y;
        double d2 = vec.z - z;
        return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double squareDistanceTo(Vec3d vec) {
        double d0 = vec.x - x;
        double d1 = vec.y - y;
        double d2 = vec.z - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double squareDistanceTo(double xIn, double yIn, double zIn) {
        double d0 = xIn - x;
        double d1 = yIn - y;
        double d2 = zIn - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public Vec3d scale(double p_186678_1_) {
        return new Vec3d(x * p_186678_1_, y * p_186678_1_, z * p_186678_1_);
    }

    public double lengthVector() {
        return MathHelper.sqrt(x * x + y * y + z * z);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    @Nullable
    public Vec3d getIntermediateWithXValue(Vec3d vec, double x) {
        double d0 = vec.x - this.x;
        double d1 = vec.y - y;
        double d2 = vec.z - z;
        if (d0 * d0 < (double)1.0E-7f) {
            return null;
        }
        double d3 = (x - this.x) / d0;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vec3d(this.x + d0 * d3, y + d1 * d3, z + d2 * d3) : null;
    }

    @Nullable
    public Vec3d getIntermediateWithYValue(Vec3d vec, double y) {
        double d0 = vec.x - x;
        double d1 = vec.y - this.y;
        double d2 = vec.z - z;
        if (d1 * d1 < (double)1.0E-7f) {
            return null;
        }
        double d3 = (y - this.y) / d1;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vec3d(x + d0 * d3, this.y + d1 * d3, z + d2 * d3) : null;
    }

    @Nullable
    public Vec3d getIntermediateWithZValue(Vec3d vec, double z) {
        double d0 = vec.x - x;
        double d1 = vec.y - y;
        double d2 = vec.z - this.z;
        if (d2 * d2 < (double)1.0E-7f) {
            return null;
        }
        double d3 = (z - this.z) / d2;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vec3d(x + d0 * d3, y + d1 * d3, this.z + d2 * d3) : null;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Vec3d)) {
            return false;
        }
        Vec3d vec3d = (Vec3d)p_equals_1_;
        if (Double.compare(vec3d.x, x) != 0) {
            return false;
        }
        if (Double.compare(vec3d.y, y) != 0) {
            return false;
        }
        return Double.compare(vec3d.z, z) == 0;
    }

    public int hashCode() {
        long j = Double.doubleToLongBits(x);
        int i = (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(y);
        i = 31 * i + (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(z);
        i = 31 * i + (int)(j ^ j >>> 32);
        return i;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Vec3d rotatePitch(float pitch) {
        float f = MathHelper.cos(pitch);
        float f1 = MathHelper.sin(pitch);
        double d0 = x;
        double d1 = y * (double)f + z * (double)f1;
        double d2 = z * (double)f - y * (double)f1;
        return new Vec3d(d0, d1, d2);
    }

    public Vec3d rotateYaw(float yaw) {
        float f = MathHelper.cos(yaw);
        float f1 = MathHelper.sin(yaw);
        double d0 = x * (double)f + z * (double)f1;
        double d1 = y;
        double d2 = z * (double)f - x * (double)f1;
        return new Vec3d(d0, d1, d2);
    }

    public static Vec3d fromPitchYawVector(Vec2f p_189984_0_) {
        return fromPitchYaw(p_189984_0_.x, p_189984_0_.y);
    }

    public static Vec3d fromPitchYaw(float p_189986_0_, float p_189986_1_) {
        float f = MathHelper.cos(-p_189986_1_ * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = MathHelper.sin(-p_189986_1_ * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = -MathHelper.cos(-p_189986_0_ * ((float)Math.PI / 180));
        float f3 = MathHelper.sin(-p_189986_0_ * ((float)Math.PI / 180));
        return new Vec3d(f1 * f2, f3, f * f2);
    }
}
