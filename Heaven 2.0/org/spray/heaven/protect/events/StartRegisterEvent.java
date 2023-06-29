package org.spray.heaven.protect.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

public class StartRegisterEvent extends EventCancellable {
	
	private final EventType type;
	
	public StartRegisterEvent(EventType type) {
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

}
