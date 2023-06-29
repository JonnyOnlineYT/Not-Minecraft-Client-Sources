package org.spray.infinity.utils.render;

import org.spray.infinity.utils.Helper;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class Drawable {

	public static int getScaledWidth() {
		return MinecraftClient.getInstance().getWindow().getScaledWidth();
	}

	public static int getScaledHeight() {
		return MinecraftClient.getInstance().getWindow().getScaledHeight();
	}

	public static void drawRect(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
		fill(matrices.peek().getModel(), x1, y1, x2, y2, color);
	}

	public static void drawRect(MatrixStack matrices, double x1, double y1, double x2, double y2, int color) {
		fill(matrices.peek().getModel(), x1, y1, x2, y2, color);
	}

	public static void drawRectWH(MatrixStack matrices, int x, int y, int width, int height, int color) {
		fill(matrices.peek().getModel(), x, y, x + width, y + height, color);
	}

	public static void drawRectWH(MatrixStack matrices, double x, double y, double width, double height, int color) {
		fill(matrices.peek().getModel(), x, y, x + width, y + height, color);
	}

	public static void verticalGradient(MatrixStack matrix, double x1, double y1, double x2, double y2, int color1,
			int color2) {
		float alpha1 = (color1 >> 24 & 255) / 255.0F;
		float red1 = (color1 >> 16 & 255) / 255.0F;
		float green1 = (color1 >> 8 & 255) / 255.0F;
		float blue1 = (color1 & 255) / 255.0F;
		float alpha2 = (color2 >> 24 & 255) / 255.0F;
		float red2 = (color2 >> 16 & 255) / 255.0F;
		float green2 = (color2 >> 8 & 255) / 255.0F;
		float blue2 = (color2 & 255) / 255.0F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(x2, y1, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x1, y1, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x1, y2, 0).color(red2, green2, blue2, alpha2).next();
		bufferBuilder.vertex(x2, y2, 0).color(red2, green2, blue2, alpha2).next();
		tessellator.draw();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

	public static void horizontalGradient(MatrixStack matrix, double x1, double y1, double x2, double y2, int color1,
			int color2) {
		float alpha1 = (color1 >> 24 & 255) / 255.0F;
		float red1 = (color1 >> 16 & 255) / 255.0F;
		float green1 = (color1 >> 8 & 255) / 255.0F;
		float blue1 = (color1 & 255) / 255.0F;
		float alpha2 = (color2 >> 24 & 255) / 255.0F;
		float red2 = (color2 >> 16 & 255) / 255.0F;
		float green2 = (color2 >> 8 & 255) / 255.0F;
		float blue2 = (color2 & 255) / 255.0F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(x1, y1, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x1, y2, 0).color(red1, green1, blue1, alpha1).next();
		bufferBuilder.vertex(x2, y2, 0).color(red2, green2, blue2, alpha2).next();
		bufferBuilder.vertex(x2, y1, 0).color(red2, green2, blue2, alpha2).next();
		tessellator.draw();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();

	}

	public static void drawBorderedRect(MatrixStack matrices, double xPos, double yPos, double width, double height,
			float lineWidth, int lineColor, int bgColor) {
		drawRectWH(matrices, xPos - lineWidth, yPos - lineWidth, width + lineWidth, lineWidth, lineColor);
		drawRectWH(matrices, xPos - lineWidth, yPos, lineWidth, height + lineWidth, lineColor);
		drawRectWH(matrices, xPos, yPos + height, width + lineWidth, lineWidth, lineColor);
		drawRectWH(matrices, xPos + width, yPos - lineWidth, lineWidth, height + lineWidth, lineColor);
		drawRectWH(matrices, xPos, yPos, width, height, bgColor);
	}

	/**
	 * Vertical rounded rect
	 */
	public static void drawVRoundedRect(MatrixStack matrices, double x, double y, double width, double height,
			int color) {
		drawCircle(matrices, x + (width / 2), y, width, color);
		drawCircle(matrices, x + (width / 2), y + height, width, color);
		drawRectWH(matrices, x, y, width, height, color);
	}

	/**
	 * Horizontal rounded rect
	 */
	public static void drawHRoundedRect(MatrixStack matrices, double x, double y, double width, double height,
			int color) {
		drawCircle(matrices, x, y + (height / 2), height, color);
		drawCircle(matrices, x + width, y + (height / 2), height, color);
		drawRectWH(matrices, x, y, width, height, color);
	}

	public static void drawBorderedCircle(MatrixStack matrices, float x, float y, float radius, int lineWidth,
			int outsideC, int insideC) {
		drawCircle(matrices, x, y, radius + lineWidth, outsideC);
		drawCircle(matrices, x, y, radius, insideC);
	}

	public static void drawCircle(MatrixStack matrices, double x, double y, double radius, int color) {
		RenderUtil.drawImage(matrices, true, x - (radius / 2), y - (radius / 2), radius, radius,
				"/assets/infinity/textures/icons/element/circle.png", color);
	}

	public static void drawTriangle(MatrixStack matrices, double x, double y, float radius, float rotation, int color) {
		matrices.push();
		matrices.translate(x, y, 0);
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
		matrices.translate(-x, -y, 0);

		RenderUtil.drawImage(matrices, true, x - (radius / 2), y - (radius / 2), radius, radius,
				"/assets/infinity/textures/icons/element/triangle.png", color);

		matrices.pop();
	}
	
	public static void drawArrow(MatrixStack matrices, double x, double y, float radius, float rotation, int color) {
		matrices.push();
		matrices.translate(x, y, 0);
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotation));
		matrices.translate(-x, -y, 0);

		RenderUtil.drawImage(matrices, true, x - (radius / 2), y - (radius / 2), radius, radius,
				"/assets/infinity/textures/icons/element/arrow.png", color);

		matrices.pop();
	}

	public static void fill(Matrix4f matrix4f, double x1, double y1, double x2, double y2, int color) {
		double j;
		if (x1 < x2) {
			j = x1;
			x1 = x2;
			x2 = j;
		}

		if (y1 < y2) {
			j = y1;
			y1 = y2;
			y2 = j;
		}

		float f = (float) (color >> 24 & 255) / 255.0F;
		float g = (float) (color >> 16 & 255) / 255.0F;
		float h = (float) (color >> 8 & 255) / 255.0F;
		float k = (float) (color & 255) / 255.0F;
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(matrix4f, (float) x1, (float) y2, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix4f, (float) x2, (float) y2, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix4f, (float) x2, (float) y1, 0.0F).color(g, h, k, f).next();
		bufferBuilder.vertex(matrix4f, (float) x1, (float) y1, 0.0F).color(g, h, k, f).next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

	public static boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX - width <= x && mouseY >= y && mouseY - height <= y;
	}

	public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
		return mouseX >= x && mouseX - width <= x && mouseY >= y && mouseY - height <= y;
	}

	public static boolean isFillHovered(double x, double y, double minX, double minY, double maxX, double maxY) {
		if (x > minX && x < maxX && y > minY && y < maxY)
			return true;
		return false;
	}

	public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
		return mouseX >= x && mouseX - width <= x && mouseY >= y && mouseY - height <= y;
	}

	public static void startScissor(double x, double y, double width, double height) {
		double scaleWidth = (double) Helper.MC.getWindow().getWidth()
				/ Helper.MC.getWindow().getScaledWidth();
		double scaleHeight = (double) Helper.MC.getWindow().getHeight()
				/ Helper.MC.getWindow().getScaledHeight();

		RenderSystem.enableScissor((int) (x * scaleWidth),
				(int) ((Helper.MC.getWindow().getHeight()) - (int) ((y + height) * scaleHeight)),
				(int) (width * scaleWidth), (int) (height * scaleHeight));
	}

	public static void stopScissor() {
		RenderSystem.disableScissor();
	}
}
