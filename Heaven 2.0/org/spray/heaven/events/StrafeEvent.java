package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class StrafeEvent extends EventCancellable {

	private float strafe, forward, friction;

	public StrafeEvent(float strafe, float forward, float friction) {
		this.strafe = strafe;
		this.forward = forward;
		this.friction = friction;
	}

	public float getStrafe() {
		return strafe;
	}

	public float getForward() {
		return forward;
	}

	public float getFriction() {
		return friction;
	}

}
