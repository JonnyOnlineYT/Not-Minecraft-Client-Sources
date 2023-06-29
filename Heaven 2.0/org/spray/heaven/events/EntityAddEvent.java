package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.entity.Entity;

public class EntityAddEvent extends EventCancellable {
	
	private Entity entity;

	public EntityAddEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
	
}
