package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventJumpSpeedFactor extends Event {
	private float yaw;
	private boolean isSprinting;

	public EventJumpSpeedFactor(boolean isSprinting) {
		this.isSprinting = isSprinting;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public float getYaw() {
		return this.yaw;
	}

	public void setSprinting(boolean isSprinting) {
		this.isSprinting = isSprinting;
	}

	public boolean isSprinting() {
		return this.isSprinting;
	}
}
