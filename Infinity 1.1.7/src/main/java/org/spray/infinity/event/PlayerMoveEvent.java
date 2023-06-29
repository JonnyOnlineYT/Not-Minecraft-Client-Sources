package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class PlayerMoveEvent extends EventCancellable {

	public MovementType type;
	public Vec3d vec3d;

	public PlayerMoveEvent(MovementType type, Vec3d vec3d) {
		this.type = type;
		this.vec3d = vec3d;
	}
	
	public MovementType getType() {
		return type;
	}

	public void setType(MovementType type) {
		this.type = type;
	}

	public Vec3d getVec3d() {
		return vec3d;
	}

	public void setVec3d(Vec3d vec3d) {
		this.vec3d = vec3d;
	}


}
