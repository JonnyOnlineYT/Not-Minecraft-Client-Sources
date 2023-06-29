package ru.wendoxd.events.impl.render;

import net.minecraft.util.EnumHandSide;
import ru.wendoxd.events.Event;

public class EventTransformSideFirstPerson extends Event {

    private EnumHandSide enumHandSide;

    public EventTransformSideFirstPerson(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return enumHandSide;
    }

    public void setEnumHandSide(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }
}
