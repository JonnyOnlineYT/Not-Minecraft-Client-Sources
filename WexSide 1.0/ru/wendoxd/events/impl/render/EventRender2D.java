package ru.wendoxd.events.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import ru.wendoxd.events.Event;

public class EventRender2D extends Event {

    private ScaledResolution sr;

    public EventRender2D(ScaledResolution sr) {
        this.sr = sr;
    }

    public ScaledResolution getScaledResolution() {
        return sr;
    }

    public void setScaledResolution(ScaledResolution sr) {
        this.sr = sr;
    }
}
