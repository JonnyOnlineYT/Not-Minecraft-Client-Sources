package ua.apraxia.eventapi.events.impl.player;

import net.minecraft.entity.Entity;
import ua.apraxia.eventapi.events.Event;
import ua.apraxia.eventapi.events.callables.EventCancellable;

public class EventRenderModelEntity extends EventCancellable implements Event {

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

    public void setCanceled() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public enum Type {
        PRE, POST
    }

}
