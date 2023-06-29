package org.moonware.client.feature.impl.visual.anim;

import org.moonware.client.event.events.callables.EventCancellable;

public class WorldRenderEvent extends EventCancellable {

    private float tickDelta;

    public WorldRenderEvent(float delta) {
        tickDelta = delta;
    }

    public float getTickDelta() {
        return tickDelta;
    }

}
