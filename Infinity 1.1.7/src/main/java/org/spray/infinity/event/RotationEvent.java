package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class RotationEvent extends EventCancellable {
	
	private float yaw;
	private float pitch;
	
	public RotationEvent(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public float getPitch() {
		return pitch;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

}
