package org.spray.infinity.features.component.cape;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class CapeModel extends EntityModel {

	public ModelPart capeRoot;

	public CapeModel(ModelPart root) {
		capeRoot = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		return TexturedModelData.of(modelData, 64, 32);
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
			float headPitch) {
		capeRoot.setAngles(headPitch, headYaw, headPitch);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
			float blue, float alpha) {
		capeRoot.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

}