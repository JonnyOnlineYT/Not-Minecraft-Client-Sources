package volcano.summer.client.events;

import volcano.summer.base.manager.event.Event;

public class EventNoSlow extends Event {


	public boolean cancelled;

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}