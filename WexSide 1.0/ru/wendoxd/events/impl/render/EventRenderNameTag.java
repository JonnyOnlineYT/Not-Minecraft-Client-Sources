package ru.wendoxd.events.impl.render;

import net.minecraft.entity.Entity;
import ru.wendoxd.events.Event;

public class EventRenderNameTag extends Event {

    private Entity entity;

    public EventRenderNameTag(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
