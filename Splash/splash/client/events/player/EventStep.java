package splash.client.events.player;

import me.hippo.systems.lwjeb.event.MultiStage;
import net.minecraft.entity.Entity;

public class EventStep extends MultiStage {

    private Entity entity;
    private float stepHeight;

    public EventStep(Entity entity, float stepHeight) {
        this.entity = entity;
        this.stepHeight = stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getStepHeight() {
        return stepHeight;
    }

}

