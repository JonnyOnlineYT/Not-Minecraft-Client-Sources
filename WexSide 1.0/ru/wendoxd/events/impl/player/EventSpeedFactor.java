package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventSpeedFactor extends Event {
	private float speed;

	public EventSpeedFactor(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
