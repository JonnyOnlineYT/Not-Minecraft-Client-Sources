package ru.wendoxd.events.impl.render;

import net.minecraft.entity.Entity;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventEntityRenderName extends Event implements IEventCancelable {

    private final Entity entity;
    private String name;
    private boolean canceled;

    public EventEntityRenderName(Entity entity, String name) {
        this.entity = entity;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

}
