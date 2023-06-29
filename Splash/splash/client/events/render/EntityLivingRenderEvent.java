package splash.client.events.render;

import me.hippo.systems.lwjeb.event.MultiStage;
import net.minecraft.entity.EntityLivingBase;

public class EntityLivingRenderEvent extends MultiStage {
	
	private EntityLivingBase entityLivingBase;
	
	public EntityLivingRenderEvent(EntityLivingBase entityLivingBase) {
        this.entityLivingBase = entityLivingBase;
	}
	
	public EntityLivingBase getEntity() {
		return entityLivingBase;
	}

}