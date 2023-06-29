package volcano.summer.client.events;

import net.minecraft.entity.Entity;
import volcano.summer.base.manager.event.Event;

public class EventName extends Event {
	public Entity entity;
	public String string;
	private boolean cancelled;
	public double x;
	public double y;
	public double z;

	public EventName(final Entity entity, final String string, final double x, final double y, final double z) {
		this.entity = entity;
		this.string = string;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setCancelled(boolean state) {
		this.cancelled = state;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
}
