package ru.wendoxd.events;

import java.util.ArrayList;
import java.util.List;

public class Event {
	private final List<IEventCallback> callbacks = new ArrayList<IEventCallback>();

	public void addCallback(IEventCallback callback) {
		this.callbacks.add(callback);
	}

	public void runCallbacks() {
		this.callbacks.removeIf(w -> {
			w.run();
			return true;
		});
	}
}
