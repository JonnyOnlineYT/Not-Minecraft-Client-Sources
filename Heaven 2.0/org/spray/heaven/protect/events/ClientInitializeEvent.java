package org.spray.heaven.protect.events;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.types.EventType;

public class ClientInitializeEvent implements Event {
	
	private EventType type;
	
	public ClientInitializeEvent(EventType type) {
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

	public void check() {}
	
}
