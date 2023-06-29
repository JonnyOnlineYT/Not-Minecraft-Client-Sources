package ru.rockstar.api.event.event;

import net.minecraft.util.EnumHandSide;
import ru.rockstar.api.event.Event;

public class EventTransformSideFirstPerson
        extends Event {
    private final EnumHandSide enumHandSide;

    public EventTransformSideFirstPerson(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
