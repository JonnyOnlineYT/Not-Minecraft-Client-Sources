package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import volcano.summer.screen.mainmenu.ColorContainer;

public class Gui {

	public static Gui INSTANCE;
	public static final ResourceLocation optionsBackground = new ResourceLocation(
			"textures/gui/options_background.png");
	public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
	public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
	protected float zLevel;
	private static final String __OBFID = "CL_00000662";

	public Gui() {
		INSTANCE = this;
	}

	/**
	 * Draw a 1 pixel wide horizontal line. Args: x1, x2, y, color
	 */

	public static void drawHorizontalLine(int startX, int endX, int y, int color) {
		if (endX < startX) {
			int var5 = startX;
			startX = endX;
			endX = var5;
		}

		drawRect(startX, y, endX + 1, y + 1, color);
	}

	/**
	 * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
	 */
	public static void drawVerticalLine(int x, int startY, int endY, int color) {
		if (endY < startY) {
			int var5 = startY;
			startY = endY;
			endY = var5;
		}

		drawRect(x, startY + 1, x + 1, endY, color);
	}

	/**
	 * Draws a solid color rectangle with the specified coordinates and color
	 * (ARGB format). Args: x1, y1, x2, y2, color
	 */

	public static void fillHorizontalGrad(final double x, final double y, final double x2, final double y2,
			final ColorContainer colorContainer, final ColorContainer colorContainer2) {
		final float a1 = colorContainer2.getAlpha() / 255.0f;
		final float r1 = colorContainer2.getRed() / 255.0f;
		final float g1 = colorContainer2.getGreen() / 255.0f;
		final float b1 = colorContainer2.getBlue() / 255.0f;
		final float a2 = colorContainer.getAlpha() / 255.0f;
		final float r2 = colorContainer.getRed() / 255.0f;
		final float g2 = colorContainer.getGreen() / 255.0f;
		final float b2 = colorContainer.getBlue() / 255.0f;
		final Tessellator tess = Tessellator.getInstance();
		final WorldRenderer wr = tess.getWorldRenderer();
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		wr.startDrawingQuads();
		wr.func_178960_a(r1, g1, b1, a1);
		wr.addVertex(x + x2, y + y2, 0.0);
		wr.addVertex(x + x2, y, 0.0);
		wr.func_178960_a(r2, g2, b2, a2);
		wr.addVertex(x, y, 0.0);
		wr.addVertex(x, y + y2, 0.0);
		tess.draw();
		GlStateManager.func_179098_w();
	}

	public static void drawRect(double d, double y, double e, double g, int color) {
		if (d < e) {
			int i = (int) d;
			d = e;
			e = i;
		}

		if (y < g) {
			int j = (int) y;
			y = g;
			g = j;
		}

		float var11 = (color >> 24 & 255) / 255.0F;
		float var6 = (color >> 16 & 255) / 255.0F;
		float var7 = (color >> 8 & 255) / 255.0F;
		float var8 = (color & 255) / 255.0F;
		Tessellator var9 = Tessellator.getInstance();
		WorldRenderer var10 = var9.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);
		var10.startDrawingQuads();
		var10.addVertex(d, g, 0.0D);
		var10.addVertex(e, g, 0.0D);
		var10.addVertex(e, y, 0.0D);
		var10.addVertex(d, y, 0.0D);
		var9.draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors
	 * (ARGB format). Args : x1, y1, x2, y2, topColor, bottomColor
	 */
	public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
		float var7 = (startColor >> 24 & 255) / 255.0F;
		float var8 = (startColor >> 16 & 255) / 255.0F;
		float var9 = (startColor >> 8 & 255) / 255.0F;
		float var10 = (startColor & 255) / 255.0F;
		float var11 = (endColor >> 24 & 255) / 255.0F;
		float var12 = (endColor >> 16 & 255) / 255.0F;
		float var13 = (endColor >> 8 & 255) / 255.0F;
		float var14 = (endColor & 255) / 255.0F;
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator var15 = Tessellator.getInstance();
		WorldRenderer var16 = var15.getWorldRenderer();
		var16.startDrawingQuads();
		var16.func_178960_a(var8, var9, var10, var7);
		var16.addVertex(right, top, this.zLevel);
		var16.addVertex(left, top, this.zLevel);
		var16.func_178960_a(var12, var13, var14, var11);
		var16.addVertex(left, bottom, this.zLevel);
		var16.addVertex(right, bottom, this.zLevel);
		var15.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
	}

	/**
	 * Renders the specified text to the screen, center-aligned. Args :
	 * renderer, string, x, y, color
	 */
	public static void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		fontRendererIn.func_175063_a(text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
	}

	/**
	 * Renders the specified text to the screen. Args : renderer, string, x, y,
	 * color
	 */
	public static void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		fontRendererIn.func_175063_a(text, x, y, color);
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v,
	 * width, height
	 */
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.getInstance();
		WorldRenderer var10 = var9.getWorldRenderer();
		var10.startDrawingQuads();
		var10.addVertexWithUV(x + 0, y + height, this.zLevel, (textureX + 0) * var7, (textureY + height) * var8);
		var10.addVertexWithUV(x + width, y + height, this.zLevel, (textureX + width) * var7,
				(textureY + height) * var8);
		var10.addVertexWithUV(x + width, y + 0, this.zLevel, (textureX + width) * var7, (textureY + 0) * var8);
		var10.addVertexWithUV(x + 0, y + 0, this.zLevel, (textureX + 0) * var7, (textureY + 0) * var8);
		var9.draw();
	}

	public void func_175174_a(float p_175174_1_, float p_175174_2_, int p_175174_3_, int p_175174_4_, int p_175174_5_,
			int p_175174_6_) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.getInstance();
		WorldRenderer var10 = var9.getWorldRenderer();
		var10.startDrawingQuads();
		var10.addVertexWithUV(p_175174_1_ + 0.0F, p_175174_2_ + p_175174_6_, this.zLevel, (p_175174_3_ + 0) * var7,
				(p_175174_4_ + p_175174_6_) * var8);
		var10.addVertexWithUV(p_175174_1_ + p_175174_5_, p_175174_2_ + p_175174_6_, this.zLevel,
				(p_175174_3_ + p_175174_5_) * var7, (p_175174_4_ + p_175174_6_) * var8);
		var10.addVertexWithUV(p_175174_1_ + p_175174_5_, p_175174_2_ + 0.0F, this.zLevel,
				(p_175174_3_ + p_175174_5_) * var7, (p_175174_4_ + 0) * var8);
		var10.addVertexWithUV(p_175174_1_ + 0.0F, p_175174_2_ + 0.0F, this.zLevel, (p_175174_3_ + 0) * var7,
				(p_175174_4_ + 0) * var8);
		var9.draw();
	}

	public void func_175175_a(int p_175175_1_, int p_175175_2_, TextureAtlasSprite p_175175_3_, int p_175175_4_,
			int p_175175_5_) {
		Tessellator var6 = Tessellator.getInstance();
		WorldRenderer var7 = var6.getWorldRenderer();
		var7.startDrawingQuads();
		var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMinU(),
				p_175175_3_.getMaxV());
		var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMaxU(),
				p_175175_3_.getMaxV());
		var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMaxU(),
				p_175175_3_.getMinV());
		var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMinU(),
				p_175175_3_.getMinV());
		var6.draw();
	}

	/**
	 * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height,
	 * textureWidth, textureHeight
	 */
	public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height,
			float textureWidth, float textureHeight) {
		float var8 = 1.0F / textureWidth;
		float var9 = 1.0F / textureHeight;
		Tessellator var10 = Tessellator.getInstance();
		WorldRenderer var11 = var10.getWorldRenderer();
		var11.startDrawingQuads();
		var11.addVertexWithUV(x, y + height, 0.0D, u * var8, (v + height) * var9);
		var11.addVertexWithUV(x + width, y + height, 0.0D, (u + width) * var8, (v + height) * var9);
		var11.addVertexWithUV(x + width, y, 0.0D, (u + width) * var8, v * var9);
		var11.addVertexWithUV(x, y, 0.0D, u * var8, v * var9);
		var10.draw();
	}

	/**
	 * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't
	 * used anywhere in vanilla code.
	 */
	public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width,
			int height, float tileWidth, float tileHeight) {
		float var10 = 1.0F / tileWidth;
		float var11 = 1.0F / tileHeight;
		Tessellator var12 = Tessellator.getInstance();
		WorldRenderer var13 = var12.getWorldRenderer();
		var13.startDrawingQuads();
		var13.addVertexWithUV(x, y + height, 0.0D, u * var10, (v + vHeight) * var11);
		var13.addVertexWithUV(x + width, y + height, 0.0D, (u + uWidth) * var10, (v + vHeight) * var11);
		var13.addVertexWithUV(x + width, y, 0.0D, (u + uWidth) * var10, v * var11);
		var13.addVertexWithUV(x, y, 0.0D, u * var10, v * var11);
		var12.draw();
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void outlineRect(int left, int top, int right, int bottom, int color) {
		drawHorizontalLine(left, right, top, color);
		drawHorizontalLine(left, right, bottom, color);
		drawVerticalLine(left, top, bottom, color);
		drawVerticalLine(right, top, bottom, color);
	}

}
