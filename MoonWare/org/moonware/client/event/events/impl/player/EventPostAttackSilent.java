package org.moonware.client.event.events.impl.player;

import net.minecraft.entity.Entity;
import org.moonware.client.event.events.callables.EventCancellable;

public class EventPostAttackSilent
        extends EventCancellable {
    private final Entity targetEntity;

    public EventPostAttackSilent(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }
}
