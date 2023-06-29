package org.spray.infinity.utils.render;

import org.lwjgl.opengl.GL11;
import org.spray.infinity.utils.Helper;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class WorldRender {

	public static void drawFill(BlockPos pos, double alpha, int color) {
		drawFill(new Box(pos), alpha, color);
	}

	public static void drawFill(Box box, double alpha, int color) {
		setup();

		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		// Fill
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		Vertexer.vertexBoxQuads(matrices, buffer, box.offset(new Vec3d(box.minX, box.minY, box.minZ).negate()), alpha,
				color);
		tessellator.draw();

		cleanup();
	}

	public static void drawBox(BlockPos pos, float width, int color) {
		drawBox(new Box(pos), width, color);
	}

	public static void drawBox(Box box, float width, int color) {
		setup();

		MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		// Outline
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
		RenderSystem.lineWidth(width);

		buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
		Vertexer.vertexBoxLines(matrices, buffer, box.offset(new Vec3d(box.minX, box.minY, box.minZ).negate()), color);
		tessellator.draw();

		RenderSystem.enableCull();

		cleanup();
	}

	public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, float width,
			int color) {
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;

		setup();

		RenderSystem.disableDepthTest();
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
		RenderSystem.lineWidth(width);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
		buffer.vertex(x1, y1, z1).color(r, g, b, 0.0F).next();
		buffer.vertex(x1, y1, z1).color(r, g, b, 1.0F).next();
		buffer.vertex(x2, y2, z2).color(r, g, b, 1.0F).next();
		tessellator.draw();

		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();

		cleanup();

	}

	public static void setup() {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableTexture();
	}

	public static void cleanup() {
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

	public static MatrixStack drawText(String str, double x, double y, double z, double scale) {
		MatrixStack matrix = matrixFrom(x, y, z);
		Camera camera = Helper.MC.gameRenderer.getCamera();
		matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
		matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthFunc(GL11.GL_ALWAYS);

		matrix.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);

		int i = Helper.MC.textRenderer.getWidth(str) / 2;
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		FontUtils.drawString(matrix, Formatting.RESET + str, -i, 0, -1);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		return matrix;
	}

	public static MatrixStack draw3DItem(double x, double y, double z, double offX, double offY, double scale,
			ItemStack item) {
		MatrixStack matrix = matrixFrom(x, y, z);

		Camera camera = Helper.MC.gameRenderer.getCamera();
		matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
		matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));

		matrix.scale((float) scale, (float) scale, 0.001f);
		matrix.translate(offX, offY, 0);

		if (item.isEmpty())
			return matrix;

		matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180f));

		Helper.MC.getBufferBuilders().getEntityVertexConsumers().draw();

		DiffuseLighting.disableGuiDepthLighting();
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		Helper.MC.getItemRenderer().renderItem(item, ModelTransformation.Mode.GUI, 0xF000F0, OverlayTexture.DEFAULT_UV,
				matrix, Helper.MC.getBufferBuilders().getEntityVertexConsumers(), 0);

		Helper.MC.getBufferBuilders().getEntityVertexConsumers().draw();
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-180f));

		return matrix;
	}

	public static MatrixStack matrixFrom(double x, double y, double z) {
		MatrixStack matrix = new MatrixStack();

		Camera camera = Helper.MC.gameRenderer.getCamera();
		matrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
		matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));

		matrix.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

		return matrix;
	}
}
