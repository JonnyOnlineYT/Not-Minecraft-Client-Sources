package volcano.summer.client.events;

import volcano.summer.base.manager.event.Event;

public class EventMouse extends Event {

	public int key;

	public EventMouse(int key) {
		this.key = key;	
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(final int key) {
		this.key = key;
	}

	public void checkKey() {
		if (this.key == 0) {
			return;
		}
	}
}
