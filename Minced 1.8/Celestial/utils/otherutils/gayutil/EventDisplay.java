package Celestial.utils.otherutils.gayutil;


import Celestial.event.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventDisplay implements Event {
    public float ticks;
    public ScaledResolution sr;

    public EventDisplay(float t, ScaledResolution sr) {
        this.sr = sr;
        ticks = t;
    }
}
