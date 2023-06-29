package ru.wendoxd.events.impl.packet;

import net.minecraft.entity.Entity;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventAttackEntity extends Event implements IEventCancelable {

    private boolean canceled;
    private Entity entity;

    public EventAttackEntity(Entity entity) {
        this.entity = entity;
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
}
