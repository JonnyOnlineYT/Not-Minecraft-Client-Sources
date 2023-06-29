package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

/**
 * event for Entity , method move
 * 
 * @author spray
 *
 */
public class MoveEvent extends EventCancellable {
	public MovementType type;
	private EventType eType;
	public Vec3d movement;

	public MoveEvent(EventType eType, MovementType type, Vec3d movement) {
		this.eType = eType;
		this.type = type;
		this.movement = movement;
	}
	
	public MovementType getMoveType() {
		return type;
	}

	public void setType(MovementType type) {
		this.type = type;
	}

	public Vec3d getMovement() {
		return movement;
	}

	public void setMovement(Vec3d movement) {
		this.movement = movement;
	}

	public EventType getType() {
		return eType;
	}

}
