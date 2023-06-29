package ru.wendoxd.utils.lib;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;

public class RectHelper {
	public static Map<Integer, Integer> cache = new HashMap();

	private static void renderShadow0(double x, double y, double width, double height, int color, int blurRadius) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		width += 10;
		height += 10;
		x -= blurRadius;
		y -= blurRadius;
		int identifier = (int) (width * height * blurRadius);
		int texId = cache.getOrDefault(identifier, -1);
		if (texId == -1) {
			BufferedImage original = new BufferedImage((int) width, (int) height, 2);
			Graphics g = original.getGraphics();
			g.fillRect(blurRadius, blurRadius, (int) width - blurRadius * 2, (int) height - blurRadius * 2);
			g.dispose();
			BufferedImage blurred = new GaussianFilter((float) blurRadius).filter(original, null);
			cache.put(identifier,
					texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false));
		}
		GlStateManager.bindTexture(texId);
		GL11.glColor4f((color >> 16 & 0xFF) / 255F, (color >> 8 & 0xFF) / 255F, (color & 0xFF) / 255F,
				(color >> 24 & 0xFF) / 255F);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex2d(x + width, y + height);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex2d(x + width, y);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	public static void renderShadow(double x, double y, double width, double height, int color, int blurRadius) {
		renderShadow0(x, y, width, height, color, blurRadius);
		GL11.glColor4f(1, 1, 1, 1);
	}

}
