package org.moonware.client.event.events.impl.packet;

import net.minecraft.entity.Entity;
import org.moonware.client.event.events.callables.EventCancellable;

public class EventAttackClient extends EventCancellable {

    private final Entity entity;

    public EventAttackClient(Entity targetEntity) {
        entity = targetEntity;
    }

    public Entity getEntity() {
        return entity;
    }
}
