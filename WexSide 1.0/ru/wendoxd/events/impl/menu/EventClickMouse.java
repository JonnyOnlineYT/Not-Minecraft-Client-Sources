package ru.wendoxd.events.impl.menu;

import ru.wendoxd.events.Event;

public class EventClickMouse extends Event {
	private int x, y, mb;

	public EventClickMouse(int x, int y, int mb) {
		this.x = x;
		this.y = y;
		this.mb = mb;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getMB() {
		return this.mb;
	}
}
