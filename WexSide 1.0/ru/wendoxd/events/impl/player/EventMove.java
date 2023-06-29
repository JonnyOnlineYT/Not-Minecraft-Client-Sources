package ru.wendoxd.events.impl.player;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import ru.wendoxd.events.Event;

public class EventMove extends Event {
	private Vec3d from, to, motion, collisionOffset;
	private boolean toGround;
	private AxisAlignedBB aabbFrom;
	public boolean ignoreHorizontal, ignoreVertical, collidedHorizontal, collidedVertical;

	public EventMove(Vec3d from, Vec3d to, Vec3d motion, Vec3d collisionOffset, boolean toGround,
			boolean isCollidedHorizontal, boolean isCollidedVertical, AxisAlignedBB aabbFrom) {
		this.from = from;
		this.to = to;
		this.motion = motion;
		this.collisionOffset = collisionOffset;
		this.toGround = toGround;
		this.collidedHorizontal = isCollidedHorizontal;
		this.collidedVertical = isCollidedVertical;
		this.aabbFrom = aabbFrom;
	}

	public void setIgnoreHorizontalCollision() {
		this.ignoreHorizontal = true;
	}

	public void setIgnoreVerticalCollision() {
		this.ignoreVertical = true;
	}

	public boolean isIgnoreHorizontal() {
		return this.ignoreHorizontal;
	}

	public AxisAlignedBB getAABBFrom() {
		return this.aabbFrom;
	}

	public boolean isIgnoreVertical() {
		return this.ignoreVertical;
	}

	public boolean isCollidedHorizontal() {
		return this.collidedHorizontal;
	}

	public boolean isCollidedVertical() {
		return this.collidedVertical;
	}

	public boolean toGround() {
		return this.toGround;
	}

	public Vec3d from() {
		return this.from;
	}

	public Vec3d to() {
		return this.to;
	}

	public Vec3d motion() {
		return this.motion;
	}

	public Vec3d collisionOffset() {
		return this.collisionOffset;
	}
}
