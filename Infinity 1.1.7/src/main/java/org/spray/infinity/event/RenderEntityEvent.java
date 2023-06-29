package org.spray.infinity.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

/**
 * Inject to WorldRenderer.class - renderEntity() method
 *
 */
public class RenderEntityEvent extends EventCancellable {

	protected Entity entity;
	protected MatrixStack matrix;
	protected VertexConsumerProvider vertex;

	public RenderEntityEvent(Entity entity, MatrixStack matrix, VertexConsumerProvider vertex) {
		this.entity = entity;
		this.matrix = matrix;
		this.vertex = vertex;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public MatrixStack getMatrix() {
		return matrix;
	}

	public void setMatrix(MatrixStack matrix) {
		this.matrix = matrix;
	}

	public VertexConsumerProvider getVertex() {
		return vertex;
	}

	public void setVertex(VertexConsumerProvider vertex) {
		this.vertex = vertex;
	}

}
