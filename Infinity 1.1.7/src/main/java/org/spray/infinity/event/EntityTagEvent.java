package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class EntityTagEvent extends EventCancellable {
	
	private VertexConsumerProvider vertexConsumers;
	private MatrixStack matrices;
	private Entity entity;
	
	public EntityTagEvent(VertexConsumerProvider vertexConsumers, MatrixStack matrices, Entity entity) {
		this.vertexConsumers = vertexConsumers;
		this.matrices = matrices;
		this.entity = entity;
	}

	public VertexConsumerProvider getVertexConsumers() {
		return vertexConsumers;
	}

	public MatrixStack getMatrices() {
		return matrices;
	}

	public Entity getEntity() {
		return entity;
	}

}
