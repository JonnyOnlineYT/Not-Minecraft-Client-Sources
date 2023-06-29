package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class ClickButtonEvent extends EventCancellable {

	private int button;
	
	public ClickButtonEvent(int button) {
		this.button = button;
	}

	public int getButton() {
		return button;
	}

}
