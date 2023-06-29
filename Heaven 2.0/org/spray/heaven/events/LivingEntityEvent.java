package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

public class LivingEntityEvent extends EventCancellable {
	
	private EventType type;
	
	public LivingEntityEvent(EventType type) {
		this.type = type;
	}

}
