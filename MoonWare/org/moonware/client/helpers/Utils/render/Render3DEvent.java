package org.moonware.client.helpers.Utils.render;

import org.moonware.client.event.events.Event;

public class Render3DEvent implements Event {

    private float ticks;
    private static float tickss;

    public Render3DEvent(float ticks) {
        this.ticks = ticks;
        tickss = ticks;
    }

    public float getTicks() {
        return ticks;
    }
    public static float getTick() {
        return tickss;
    }

    public void setTicks(float ticks) {
        this.ticks = ticks;
    }

}
