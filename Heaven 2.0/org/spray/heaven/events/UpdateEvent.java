package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

public class UpdateEvent extends EventCancellable {
	
	private EventType type;

	public UpdateEvent(EventType type) {
		this.type = type;
	}

	public EventType getType() {
		return type;
	}
}
