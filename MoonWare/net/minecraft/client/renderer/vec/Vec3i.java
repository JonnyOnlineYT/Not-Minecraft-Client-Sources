package net.minecraft.client.renderer.vec;

import com.google.common.base.MoreObjects;
import net.minecraft.util.math.MathHelper;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Vec3i
        implements Comparable<net.minecraft.util.math.Vec3i> {
    public static final net.minecraft.util.math.Vec3i NULL_VECTOR = new net.minecraft.util.math.Vec3i(0, 0, 0);
    private final int x;
    private final int y;
    private final int z;

    public Vec3i(int xIn, int yIn, int zIn) {
        x = xIn;
        y = yIn;
        z = zIn;
    }

    public Vec3i(double xIn, double yIn, double zIn) {
        this(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof net.minecraft.util.math.Vec3i)) {
            return false;
        }
        net.minecraft.util.math.Vec3i vec3i = (net.minecraft.util.math.Vec3i)p_equals_1_;
        if (getX() != vec3i.getX()) {
            return false;
        }
        if (getY() != vec3i.getY()) {
            return false;
        }
        return getZ() == vec3i.getZ();
    }

    public int hashCode() {
        return (getY() + getZ() * 31) * 31 + getX();
    }

    @Override
    public int compareTo(net.minecraft.util.math.Vec3i p_compareTo_1_) {
        if (getY() == p_compareTo_1_.getY()) {
            return getZ() == p_compareTo_1_.getZ() ? getX() - p_compareTo_1_.getX() : getZ() - p_compareTo_1_.getZ();
        }
        return getY() - p_compareTo_1_.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public net.minecraft.util.math.Vec3i crossProduct(net.minecraft.util.math.Vec3i vec) {
        return new net.minecraft.util.math.Vec3i(getY() * vec.getZ() - getZ() * vec.getY(), getZ() * vec.getX() - getX() * vec.getZ(), getX() * vec.getY() - getY() * vec.getX());
    }

    public double getDistance(int xIn, int yIn, int zIn) {
        double d0 = getX() - xIn;
        double d1 = getY() - yIn;
        double d2 = getZ() - zIn;
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double distanceSq(double toX, double toY, double toZ) {
        double d0 = (double) getX() - toX;
        double d1 = (double) getY() - toY;
        double d2 = (double) getZ() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double distanceSqToCenter(double xIn, double yIn, double zIn) {
        double d0 = (double) getX() + 0.5 - xIn;
        double d1 = (double) getY() + 0.5 - yIn;
        double d2 = (double) getZ() + 0.5 - zIn;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double distanceSq(net.minecraft.util.math.Vec3i to) {
        return distanceSq(to.getX(), to.getY(), to.getZ());
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", getX()).add("y", getY()).add("z", getZ()).toString();
    }
}
