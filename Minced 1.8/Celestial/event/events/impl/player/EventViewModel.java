package Celestial.event.events.impl.player;

import Celestial.event.events.Event;
import net.minecraft.util.EnumHandSide;

public class EventViewModel implements Event {

    private final EnumHandSide enumHandSide;

    public EventViewModel(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
