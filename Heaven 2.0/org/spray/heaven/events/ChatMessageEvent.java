package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class ChatMessageEvent extends EventCancellable {
	
	private String message;
	
	public ChatMessageEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
