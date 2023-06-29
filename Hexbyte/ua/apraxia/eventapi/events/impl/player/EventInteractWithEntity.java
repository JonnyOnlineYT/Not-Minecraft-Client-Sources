package ua.apraxia.eventapi.events.impl.player;

import net.minecraft.entity.Entity;
import ua.apraxia.eventapi.events.Event;

public class EventInteractWithEntity implements Event {
    private Entity entity;
    private boolean canceled;

    public EventInteractWithEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setCanceled() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return this.canceled;
    }
}
