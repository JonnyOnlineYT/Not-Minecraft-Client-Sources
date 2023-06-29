package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class KeyEvent extends EventCancellable {

	private int key;

	public KeyEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

}
