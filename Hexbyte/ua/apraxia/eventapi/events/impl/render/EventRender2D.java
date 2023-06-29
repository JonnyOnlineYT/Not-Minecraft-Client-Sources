//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.eventapi.events.impl.render;

import ua.apraxia.eventapi.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D implements Event {
    private final ScaledResolution resolution;

    public EventRender2D(ScaledResolution resolution) {
        this.resolution = resolution;
    }

    public ScaledResolution getResolution() {
        return this.resolution;
    }
}
