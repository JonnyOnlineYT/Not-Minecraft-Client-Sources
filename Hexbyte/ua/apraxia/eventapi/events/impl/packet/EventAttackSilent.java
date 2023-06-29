package ua.apraxia.eventapi.events.impl.packet;


import ua.apraxia.eventapi.events.callables.EventCancellable;
import net.minecraft.entity.Entity;

public class EventAttackSilent extends EventCancellable {

    private final Entity targetEntity;

    public EventAttackSilent(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
