package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

public class TickEvent extends EventCancellable {
	
	private EventType type;

	public TickEvent(EventType type) {
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

}
