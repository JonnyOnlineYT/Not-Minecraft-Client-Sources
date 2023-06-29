package ru.wendoxd.events.impl.render;

import net.minecraft.entity.Entity;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventRenderModelEntity extends Event implements IEventCancelable {

    private boolean canceled;
    private Type type;
    private Entity entity;

    public EventRenderModelEntity(Entity entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
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
