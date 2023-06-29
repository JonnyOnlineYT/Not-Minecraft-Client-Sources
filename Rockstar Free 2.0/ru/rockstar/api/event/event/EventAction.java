package ru.rockstar.api.event.event;

import ru.rockstar.api.event.Event;

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
