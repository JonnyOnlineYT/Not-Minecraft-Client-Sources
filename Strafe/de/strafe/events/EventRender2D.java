package de.strafe.events;

import com.eventapi.events.Event;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author XButtonn
 * @created 19.02.2022 - 14:48
 */

public class EventRender2D implements Event {

    public final ScaledResolution sr;

    public EventRender2D(ScaledResolution sr) {
        this.sr = sr;
    }
}
