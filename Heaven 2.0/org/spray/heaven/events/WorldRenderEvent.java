package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

/**
 * 
 * Inject into the EntityRenderer class
 * 
 * @method = renderWorldPass()V;
 * @target = Invoke
 *
 */
public class WorldRenderEvent extends EventCancellable {

	private float tickDelta;

	public WorldRenderEvent(float delta) {
		this.tickDelta = delta;
	}

	public float getTickDelta() {
		return tickDelta;
	}

}
