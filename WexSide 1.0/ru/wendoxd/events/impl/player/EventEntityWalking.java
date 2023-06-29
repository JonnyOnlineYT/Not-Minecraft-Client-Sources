package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;

public class EventEntityWalking extends Event {
	private Phase phase;

	public EventEntityWalking(Phase phase) {
		this.phase = phase;
	}

	public Phase getPhase() {
		return this.phase;
	}

	public static enum Phase {
		PRE, POST
	}
}
