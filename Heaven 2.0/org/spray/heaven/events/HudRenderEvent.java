package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class HudRenderEvent extends EventCancellable {

	private int width, height;
	private float tickDelta;

	public HudRenderEvent(int width, int height, float tickDelta) {
		this.width = width;
		this.height = height;
		this.tickDelta = tickDelta;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getTickDelta() {
		return tickDelta;
	}

	public void setTickDelta(float tickDelta) {
		this.tickDelta = tickDelta;
	}

}
