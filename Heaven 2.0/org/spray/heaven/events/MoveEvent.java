package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.entity.MoverType;

/**
 * 
 * Inject into the Entity class
 * 
 * @method = moveEntity()V
 * @type = PRE, POST
 *
 */
public class MoveEvent extends EventCancellable {

	private EventType type;
	private MoverType moverType;
	private double motionX, motionY, motionZ;

	public MoveEvent(EventType type, MoverType moverType, double motionX, double motionY, double motionZ) {
		this.type = type;
		this.moverType = moverType;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public MoverType getMoverType() {
		return moverType;
	}

	public void setMoverType(MoverType moverType) {
		this.moverType = moverType;
	}

	public double getMotionX() {
		return motionX;
	}

	public void setMotionX(double motionX) {
		this.motionX = motionX;
	}

	public double getMotionY() {
		return motionY;
	}

	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}

	public double getMotionZ() {
		return motionZ;
	}

	public void setMotionZ(double motionZ) {
		this.motionZ = motionZ;
	}

}
