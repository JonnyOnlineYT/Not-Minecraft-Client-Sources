package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

public class AttackEvent extends EventCancellable {
	
	private EventType type;
	
	public AttackEvent(EventType type) {
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

}
