package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventAction extends Event {
	private boolean sprintState;

	public EventAction(boolean sprintState) {
		this.sprintState = sprintState;
	}

	public void setSprintState(boolean sprintState) {
		this.sprintState = sprintState;
	}

	public boolean getSprintState() {
		return this.sprintState;
	}
}
