package ru.wendoxd.events.impl.player;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventRightClick extends Event implements IEventCancelable {
	private boolean canceled;

	@Override
	public void setCanceled() {
		this.canceled = true;
	}

	@Override
	public boolean isCanceled() {
		return this.canceled;
	}

}
