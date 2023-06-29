package ru.wendoxd.events.impl.player;

import net.minecraft.entity.Entity;
import ru.wendoxd.events.Event;

public class EventInteractWithEntity extends Event {
	private Entity entity;
	private boolean canceled;

	public EventInteractWithEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setCanceled() {
		this.canceled = true;
	}

	public boolean isCanceled() {
		return this.canceled;
	}
}
