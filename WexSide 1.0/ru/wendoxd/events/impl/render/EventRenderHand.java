package ru.wendoxd.events.impl.render;

import net.minecraft.util.EnumHandSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventRenderHand extends Event implements IEventCancelable {

    private EnumHandSide enumHandSide;
    private final Type type;
    private boolean canceled;

    public EventRenderHand(EnumHandSide enumHandSide, Type type) {
        this.enumHandSide = enumHandSide;
        this.type = type;
    }

    public EnumHandSide getEnumHandSide() {
        return enumHandSide;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    public enum Type {
        PRE, POST
    }
}
