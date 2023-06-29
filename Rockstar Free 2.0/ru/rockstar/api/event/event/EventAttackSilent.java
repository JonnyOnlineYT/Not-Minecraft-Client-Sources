package ru.rockstar.api.event.event;

import net.minecraft.entity.Entity;
import ru.rockstar.api.event.Event;

public class EventAttackSilent extends Event {

    private final Entity targetEntity;

    public EventAttackSilent(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
