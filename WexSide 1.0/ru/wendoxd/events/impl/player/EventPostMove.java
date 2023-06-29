package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventPostMove extends Event {
	private double horizontalMove;

	public EventPostMove(double horizontalMove) {
		this.horizontalMove = horizontalMove;
	}

	public double getHorizontalMove() {
		return this.horizontalMove;
	}
}
