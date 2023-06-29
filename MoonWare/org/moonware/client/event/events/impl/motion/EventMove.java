package org.moonware.client.event.events.impl.motion;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.moonware.client.event.events.Event;
import org.moonware.client.event.events.callables.EventCancellable;

public class EventMove extends EventCancellable implements Event {

    private Vec3d from,to,motion,collisionOffset;
    private boolean toGround;
    private AxisAlignedBB aabbFrom;
    public boolean ignoreHorizontal, ignoreVertical, collidedHorizontal, collidedVertical;
    private double x, y, z;

    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public EventMove(Vec3d from, Vec3d to, Vec3d motion, Vec3d collisionOffset, boolean toGround,
                     boolean isCollidedHorizontal, boolean isCollidedVertical, AxisAlignedBB aabbFrom) {
        this.from = from;
        this.to = to;
        this.motion = motion;
        this.collisionOffset = collisionOffset;
        this.toGround = toGround;
        collidedHorizontal = isCollidedHorizontal;
        collidedVertical = isCollidedVertical;
        this.aabbFrom = aabbFrom;
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    public void setIgnoreHorizontalCollision() {
        ignoreHorizontal = true;
    }

    public void setIgnoreVerticalCollision() {
        ignoreVertical = true;
    }

    public boolean isIgnoreHorizontal() {
        return ignoreHorizontal;
    }

    public AxisAlignedBB getAABBFrom() {
        return aabbFrom;
    }

    public boolean isIgnoreVertical() {
        return ignoreVertical;
    }

    public boolean isCollidedHorizontal() {
        return collidedHorizontal;
    }

    public boolean isCollidedVertical() {
        return collidedVertical;
    }

    public boolean toGround() {
        return toGround;
    }

    public Vec3d from() {
        return from;
    }

    public Vec3d to() {
        return to;
    }

    public Vec3d motion() {
        return motion;
    }

    public Vec3d collisionOffset() {
        return collisionOffset;
    }
}