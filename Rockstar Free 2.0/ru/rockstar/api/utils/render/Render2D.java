package ru.rockstar.api.utils.render;

import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glDisable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class Render2D {
	private static HashMap<Integer, Integer> shadowCache = new HashMap<Integer, Integer>();
	private static ShaderGroup blurShader;
	private static Framebuffer buffer;
	private static int lastScale;
	private static int lastScaleWidth;
	private static int lastScaleHeight;
	private static ResourceLocation shader;

	public static void drawTexture(ResourceLocation texture, double x, double y, double width, double height) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GlStateManager.enableBlend();
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, (int) width, (int) height, (int) width,
				(int) height);
		GL11.glDepthMask(true);
		GlStateManager.disableBlend();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	// GL COLOR
	public static void glColor(final int red, final int green, final int blue, final int alpha) {
		GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
	}

	public static void glColor(final Color color) {
		final float red = color.getRed() / 255F;
		final float green = color.getGreen() / 255F;
		final float blue = color.getBlue() / 255F;
		final float alpha = color.getAlpha() / 255F;

		GlStateManager.color(red, green, blue, alpha);
	}

	public static void glColor(final Color color, final int alpha) {
		glColor(color, alpha/255F);
	}

	public static void glColor(final Color color, final float alpha) {
		final float red = color.getRed() / 255F;
		final float green = color.getGreen() / 255F;
		final float blue = color.getBlue() / 255F;

		GlStateManager.color(red, green, blue, alpha);
	}


	public static void glColor(final int hex, final int alpha) {
		final float red = (hex >> 16 & 0xFF) / 255F;
		final float green = (hex >> 8 & 0xFF) / 255F;
		final float blue = (hex & 0xFF) / 255F;

		GlStateManager.color(red, green, blue, alpha / 255F);
	}

	public static void glColor(final int hex, final float alpha) {
		final float red = (hex >> 16 & 0xFF) / 255F;
		final float green = (hex >> 8 & 0xFF) / 255F;
		final float blue = (hex & 0xFF) / 255F;

		GlStateManager.color(red, green, blue, alpha);
	}

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 255.0F;
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}
	public static final void color(double red, double green, double blue, double alpha) {
		GL11.glColor4d(red, green, blue, alpha);
	}
	// GL COLOR

	public static Color setAlpha(Color color, int alpha) {
		alpha = (int) MathHelper.clamp(alpha, 0, 255);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public static void renderBlurredShadow(double x, double y, double width, double height, final int blurRadius, final Color color) {
		GlStateManager.alphaFunc(516, 0.01f);
		width += blurRadius * 2;
		height += blurRadius * 2;
		x -= blurRadius;
		y -= blurRadius;
		final float _X = (float)(x - 0.25);
		final float _Y = (float)(y + 0.25);
		final int identifier = (int)(width * height + width + color.hashCode() * blurRadius + blurRadius);
		GL11.glEnable(3553);
		GL11.glDisable(2884);
		GL11.glEnable(3008);
		GL11.glEnable(3042);
		int texId = -1;
		if (shadowCache.containsKey(identifier)) {
			texId = shadowCache.get(identifier);
			GlStateManager.bindTexture(texId);
		}
		else {
			width = MathHelper.clamp(width, 0.01, width);
			height = MathHelper.clamp(height, 0.01, height);
			final BufferedImage original = new BufferedImage((int)width, (int)height, 2);
			final Graphics g = original.getGraphics();
			g.setColor(color);
			g.fillRect(blurRadius, blurRadius, (int)width - blurRadius * 2, (int)height - blurRadius * 2);
			g.dispose();
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glBegin(7);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2f(_X, _Y);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex2f(_X, _Y + (int)height);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex2f(_X + (int)width, _Y + (int)height);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex2f(_X + (int)width, _Y);
		GL11.glEnd();
		GL11.glDisable(3553);
	}

	public static void drawRect(Rectangle r, Color color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(r.x, (r.y + r.height), 0).color(color.getRed() / 255.0F, color.getGreen() / 255.0F,
				color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
		bufferbuilder.pos((r.x + r.width), (r.y + r.height), 0).color(color.getRed() / 255.0F,
				color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
		bufferbuilder.pos((r.x + r.width), r.y, 0).color(color.getRed() / 255.0F, color.getGreen() / 255.0F,
				color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
		bufferbuilder.pos(r.x, r.y, 0).color(color.getRed() / 255.0F, color.getGreen() / 255.0F,
				color.getBlue() / 255.0F, color.getAlpha() / 255.0F).endVertex();
		tessellator.draw();
	}

	public static String drawRect(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			double j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
		bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		return null;
	}

	public static void drawRectWH(double x, double y, double width, double height, int color) {
		drawRect(x, y, x + width, y + height, color);
	}

	public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0, (float) 0, (int) width, (int) height, width, height);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, Color color) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		setColor(color.getRGB());
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0, (float) 0, (int) width, (int) height, width, height);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	public static void setColor(int color) {
		GL11.glColor4ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF), (byte) (color >> 24 & 0xFF));
	}

	public static void drawCircle(float x, float y, float start, float end, float radius, int color, int linewidth) {
		float sin;
		float cos;
		float i;
		GlStateManager.color(0, 0, 0, 0);

		float endOffset;
		if (start > end) {
			endOffset = end;
			end = start;
			start = endOffset;
		}

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		enableSmoothLine(linewidth);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (i = end; i >= start; i -= 4) {
			glColor(color, 255);
			cos = (float) (Math.cos(i * Math.PI / 180) * radius * 1);
			sin = (float) (Math.sin(i * Math.PI / 180) * radius * 1);
			GL11.glVertex2f(x + cos, y + sin);
		}
		GL11.glEnd();

		disableSmoothLine();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void enableSmoothLine(float width) {
		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2884);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
		GL11.glLineWidth(width);
	}

	public static void disableSmoothLine() {
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glDepthMask(true);
		GL11.glCullFace(1029);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}
	public static void horizontalGradient(double x1, double y1, double x2, double y2, int startColor, int endColor) {
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) x1, (double) y1, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double) x1, (double) y2, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double) x2, (double) y2, 0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double) x2, (double) y1, 0).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void verticalGradient(double left, double top, double right, double bottom, int startColor,
										int endColor) {
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) right, (double) top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double) left, (double) top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double) left, (double) bottom, 0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, 0).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void centerGradient(double x, double y, double width, double height, int startColor, int endColor) {
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) width, (double) y, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double) x, (double) y, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos((double) x, (double) height, 0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.pos((double) width, (double) height, 0).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawBorderedRect(double x, double y, double width, double height, float lineWidth, int lineColor,
										int bgColor) {
		drawRectWH(x, y, width, height, bgColor);
		float f = (float) (lineColor >> 24 & 255) / 255.0F;
		float f1 = (float) (lineColor >> 16 & 255) / 255.0F;
		float f2 = (float) (lineColor >> 8 & 255) / 255.0F;
		float f3 = (float) (lineColor & 255) / 255.0F;
		GL11.glPushMatrix();
		GL11.glPushAttrib(1048575);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(lineWidth);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x + width, (double) y);
		GL11.glVertex2d((double) x + width, (double) y);
		GL11.glVertex2d((double) x + width, (double) y + height);
		GL11.glVertex2d((double) x + width, (double) y + height);
		GL11.glVertex2d((double) x, (double) y + height);
		GL11.glVertex2d((double) x, (double) y + height);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	public static void drawCircle(double x, double y, float radius, int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(GL_SMOOTH);
		GL11.glColor4d(red, green, blue, alpha);
		GL11.glBegin(GL11.GL_POLYGON);
		for (int i = 0; i <= 360; i++) {
			GL11.glVertex2d(x + (MathHelper.sin((i * 3.141526f / 180)) * radius),
					y + (MathHelper.cos((i * 3.141526f / 180)) * radius));
		}
		GL11.glColor4d(1f, 1f, 1f, 1f);
		GL11.glEnd();
		GlStateManager.shadeModel(GL_FLAT);
		glDisable(GL_LINE_SMOOTH);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.popMatrix();
	}

	public static void drawTriangle(double x, double y, float size, float theta, int color) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glRotatef(180 + theta, 0F, 0F, 1.0F);

		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;

		GL11.glColor4f(red, green, blue, alpha);
		GlStateManager.enableBlend();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(1);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		GL11.glVertex2d(0, (1.0F * size));
		GL11.glVertex2d((1 * size), -(1.0F * size));
		GL11.glVertex2d(-(1 * size), -(1.0F * size));

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GlStateManager.disableBlend();
		GL11.glRotatef(-180 - theta, 0F, 0F, 1.0F);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(-x, -y, 0);
		GL11.glPopMatrix();
	}

	public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
		double x1 = x + width;
		double y1 = y + height;
		float f = (color >> 24 & 0xFF) / 255.0F;
		float f1 = (color >> 16 & 0xFF) / 255.0F;
		float f2 = (color >> 8 & 0xFF) / 255.0F;
		float f3 = (color & 0xFF) / 255.0F;
		GL11.glPushAttrib(0);
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		GlStateManager.enableBlend();
		x *= 2.0D;
		y *= 2.0D;
		x1 *= 2.0D;
		y1 *= 2.0D;
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glBegin(9);
		int i;
		for (i = 0; i <= 90; i += 3)
			GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D,
					y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
		for (i = 90; i <= 180; i += 3)
			GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D,
					y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
		for (i = 0; i <= 90; i += 3)
			GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius,
					y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
		for (i = 90; i <= 180; i += 3)
			GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius,
					y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GlStateManager.disableBlend();
		GL11.glScaled(2.0D, 2.0D, 2.0D);
		GL11.glPopAttrib();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), 0)
				.tex((double) ((float) (textureX + 0) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), 0)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + height) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), 0)
				.tex((double) ((float) (textureX + width) * 0.00390625F),
						(double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), 0)
				.tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F))
				.endVertex();
		tessellator.draw();
	}

	public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
		return mouseX >= x && mouseX - width <= x && mouseY >= y && mouseY - height <= y;
	}

	public static void startScissor(double x, double y, double width, double height) {
		startScissor(x, y, width, height, 1);
	}

	public static void startScissor(double x, double y, double width, double height, double factor) {
		ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
		double scaleWidth = (double) Minecraft.getMinecraft().displayWidth / resolution.getScaledWidth_double();
		double scaleHeight = (double) Minecraft.getMinecraft().displayHeight / resolution.getScaledHeight_double();

		scaleWidth *= factor;
		scaleHeight *= factor;

		GL11.glScissor((int) (x * scaleWidth), (Minecraft.getMinecraft().displayHeight) - (int) ((y + height) * scaleHeight),
				(int) (width * scaleWidth), (int) (height * scaleHeight));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}

	public static void stopScissor() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public static void drawBlurredShadow(int x, int y, int width, int height, int blurRadius, Color color) {
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);

		width = width + blurRadius * 2;
		height = height + blurRadius * 2;
		x = x - blurRadius;
		y = y - blurRadius;

		float _X = x - 0.25f;
		float _Y = y + 0.25f;

		int identifier = width * height + width + color.hashCode() * blurRadius + blurRadius;

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GlStateManager.enableBlend();

		int texId = -1;
		if (shadowCache.containsKey(identifier)) {
			texId = shadowCache.get(identifier);

			GlStateManager.bindTexture(texId);
		} else {
			BufferedImage original = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			Graphics g = original.getGraphics();
			g.setColor(color);
			g.fillRect(blurRadius, blurRadius, width - blurRadius * 2, height - blurRadius * 2);
			g.dispose();

			GaussianFilter op = new GaussianFilter(blurRadius);

			BufferedImage blurred = op.filter(original, null);

			texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
			shadowCache.put(identifier, texId);
		}

		GL11.glColor4f(1f, 1f, 1f, 1f);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0); // top left
		GL11.glVertex2f(_X, _Y);

		GL11.glTexCoord2f(0, 1); // bottom left
		GL11.glVertex2f(_X, _Y + height);

		GL11.glTexCoord2f(1, 1); // bottom right
		GL11.glVertex2f(_X + width, _Y + height);

		GL11.glTexCoord2f(1, 0); // top right
		GL11.glVertex2f(_X + width, _Y);
		GL11.glEnd();

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

}
