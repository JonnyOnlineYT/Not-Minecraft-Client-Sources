package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventEntityStrafe extends Event implements IEventCancelable {

	private boolean canceled;
	private float yaw, strafe, forward, friction;

	public EventEntityStrafe(float yaw, float strafe, float forward, float friction) {
		this.yaw = yaw;
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

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	@Override
	public void setCanceled() {
		canceled = true;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

}