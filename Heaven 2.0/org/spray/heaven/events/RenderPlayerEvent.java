package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.entity.EntityLivingBase;

public class RenderPlayerEvent extends EventCancellable {

	private EntityLivingBase entity;
	private double x, y, z;
	private float renderTicks;
	private EventType type;

	public RenderPlayerEvent(EntityLivingBase entity, double x, double y, double z, float renderTicks, EventType type) {
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.renderTicks = renderTicks;
		this.type = type;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public float getRenderTicks() {
		return renderTicks;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}

	public EventType getType() {
		return type;
	}

}
