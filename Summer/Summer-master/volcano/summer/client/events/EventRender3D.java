package volcano.summer.client.events;

import volcano.summer.base.manager.event.Event;

public class EventRender3D extends Event {

	public static float partialTicks;

	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public static float getPartialTicks() {
		return partialTicks;
	}

}
