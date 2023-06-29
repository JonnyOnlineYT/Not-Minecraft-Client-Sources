package volcano.summer.client.events;

import net.minecraft.entity.Entity;
import volcano.summer.base.manager.event.Event;

public class EventEntityStep extends Event {

	private float stepHeight;
	private Entity entity;
	private boolean cancelled;

	public EventEntityStep(Entity entity, float stepHeight) {
		this.entity = entity;
		this.stepHeight = stepHeight;
	}

	public float getStepHeight() {
		return this.stepHeight;
	}

	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
