package Celestial.event.events.impl.render;

import Celestial.event.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D implements Event {

    private final ScaledResolution resolution;

    public EventRender2D(ScaledResolution resolution) {
        this.resolution = resolution;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }

}
