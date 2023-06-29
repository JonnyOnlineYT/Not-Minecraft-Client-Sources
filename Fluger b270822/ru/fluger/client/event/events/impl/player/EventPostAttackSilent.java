/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.player;

import net.minecraft.entity.Entity;
import ru.fluger.client.event.events.callables.EventCancellable;

public class EventPostAttackSilent
extends EventCancellable {
    private final Entity targetEntity;

    public EventPostAttackSilent(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}

