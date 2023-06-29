package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class ServerChatEvent extends EventCancellable {

	private final String message;

	public ServerChatEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
