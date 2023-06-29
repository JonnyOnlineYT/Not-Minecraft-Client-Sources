package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent extends EventCancellable {

	private MatrixStack matrices;
	private EventType type;

	private float tickDelta;

	public RenderEvent(EventType type, MatrixStack matrices, float tickDelta) {
		this.type = type;
		this.matrices = matrices;
		this.tickDelta = tickDelta;
	}

	public MatrixStack getMatrices() {
		return matrices;
	}

	public EventType getType() {
		return type;
	}

	public float getTickDelta() {
		return tickDelta;
	}

}
