package volcano.summer.client.events;

import volcano.summer.base.manager.event.Event;

public class SafeEvent extends Event {

	private boolean cancelled;

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean state) {
		this.cancelled = state;
	}
}
