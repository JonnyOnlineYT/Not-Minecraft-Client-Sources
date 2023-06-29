package volcano.summer.client.util;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import volcano.summer.base.Summer;

public class R2DUtils {

	private static final ScaledResolution scaledResolution;

	static {
		scaledResolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
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

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void drawRect(Rectangle rectangle, int color) {
		drawRect(rectangle.getX(), rectangle.getY(), rectangle.getX() + rectangle.getWidth(),
				rectangle.getY() + rectangle.getHeight(), color);
	}

	public static void drawRect(float x, float y, float x1, float y1, int color) {
		enableGL2D();
		glColor(color);
		drawRect(x, y, x1, y1);
		disableGL2D();
	}

	public static void drawRect323(float g, float h, float i, float j, int col1) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f2 = (col1 >> 16 & 0xFF) / 255.0F;
		float f3 = (col1 >> 8 & 0xFF) / 255.0F;
		float f4 = (col1 & 0xFF) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(7);
		GL11.glVertex2d(i, h);
		GL11.glVertex2d(g, h);
		GL11.glVertex2d(g, j);
		GL11.glVertex2d(i, j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawRectMC(int left, int top, int right, int bottom, int color) {
		if (left < right) {
			int var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			int var5 = top;
			top = bottom;
			bottom = var5;
		}
		float var11 = (color >> 24 & 0xFF) / 255.0F;
		float var6 = (color >> 16 & 0xFF) / 255.0F;
		float var7 = (color >> 8 & 0xFF) / 255.0F;
		float var8 = (color & 0xFF) / 255.0F;
		Tessellator var9 = Tessellator.getInstance();
		WorldRenderer var10 = var9.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);
		var10.startDrawingQuads();
		var10.addVertex(left, bottom, 0.0D);
		var10.addVertex(right, bottom, 0.0D);
		var10.addVertex(right, top, 0.0D);
		var10.addVertex(left, top, 0.0D);
		var9.draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
	}

	public static void drawRect2(final int x, final int y, final int x1, final int y1, final int color) {
		Gui.drawRect(x, y, x1, y1, color);
	}

	public static void drawRectTag(float g, float h, float i, float j, int col1) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);

		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(7);
		GL11.glVertex2d(i, h);
		GL11.glVertex2d(g, h);
		GL11.glVertex2d(g, j);
		GL11.glVertex2d(i, j);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor,
			int borderColor) {
		enableGL2D();
		glColor(internalColor);
		drawRect(x + width, y + width, x1 - width, y1 - width);
		glColor(borderColor);
		drawRect(x + width, y, x1 - width, y + width);
		drawRect(x, y, x + width, y1);
		drawRect(x1 - width, y, x1, y1);
		drawRect(x + width, y1 - width, x1 - width, y1);
		disableGL2D();
	}

	public static void drawBorderedRect232(float x, float y, float x2, float y2, float l1, int col1, int col2) {
		drawRect(x, y, x2, y2, col2);
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f2 = (col1 >> 16 & 0xFF) / 255.0F;
		float f3 = (col1 >> 8 & 0xFF) / 255.0F;
		float f4 = (col1 & 0xFF) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawBorderedRect545(float x, float y, float x2, float y2, float l1, int col1, int col2) {
		drawRect(x, y, x2, y2, col2);
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f2 = (col1 >> 16 & 0xFF) / 255.0F;
		float f3 = (col1 >> 8 & 0xFF) / 255.0F;
		float f4 = (col1 & 0xFF) / 255.0F;
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glPopMatrix();
	}

	public static void drawBorderedRectTag(float x, float y, float x2, float y2, float l1, int col1, int col2) {
		drawRectTag(x, y, x2, y2, col2);

		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);

		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
		enableGL2D();
		x *= 2.0F;
		x1 *= 2.0F;
		y *= 2.0F;
		y1 *= 2.0F;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVLine(x, y, y1, borderC);
		drawVLine(x1 - 1.0F, y, y1, borderC);
		drawHLine(x, x1 - 1.0F, y, borderC);
		drawHLine(x, x1 - 2.0F, y1 - 1.0F, borderC);
		drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
	}

	public static void drawBorderedRect2(double left, double top, double right, double bottom, int borderColor,
			int color) {
		drawBorderedRect(left, top, right, bottom, 1.0F, borderColor, color);
	}

	public static void drawBorderedRect(double left, double top, double right, double bottom, float borderWidth,
			int borderColor, int color) {
		float alpha = (borderColor >> 24 & 0xFF) / 255.0F;
		float red = (borderColor >> 16 & 0xFF) / 255.0F;
		float green = (borderColor >> 8 & 0xFF) / 255.0F;
		float blue = (borderColor & 0xFF) / 255.0F;
		GlStateManager.pushMatrix();
		drawRects(left, top, right, bottom, color);
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		if (borderWidth == 1.0F) {
			GL11.glEnable(2848);
		}
		GL11.glLineWidth(borderWidth);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(left, top, 0.0D);
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, top, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		tessellator.draw();
		GL11.glLineWidth(2.0F);
		if (borderWidth == 1.0F) {
			GL11.glDisable(2848);
		}
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside,
			int border) {
		enableGL2D();
		drawRect(x, y, x1, y1, inside);
		glColor(border);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(lineWidth);
		GL11.glBegin(3);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		disableGL2D();
	}

	public static void drawGradientBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth,
			int border, int bottom, int top) {
		enableGL2D();
		drawGradientRect(x, y, x1, y1, top, bottom);
		glColor(border);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(lineWidth);
		GL11.glBegin(3);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		disableGL2D();
	}

	public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
		enableGL2D();
		x *= 2.0F;
		y *= 2.0F;
		x1 *= 2.0F;
		y1 *= 2.0F;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		drawVLine(x, y + 1.0F, y1 - 2.0F, borderC);
		drawVLine(x1 - 1.0F, y + 1.0F, y1 - 2.0F, borderC);
		drawHLine(x + 2.0F, x1 - 3.0F, y, borderC);
		drawHLine(x + 2.0F, x1 - 3.0F, y1 - 1.0F, borderC);
		drawHLine(x + 1.0F, x + 1.0F, y + 1.0F, borderC);
		drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
		drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
		drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
		drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
	}

	public static void drawBorderedRect(Rectangle rectangle, float width, int internalColor, int borderColor) {
		float x = rectangle.getX();
		float y = rectangle.getY();
		float x2 = rectangle.getX() + rectangle.getWidth();
		float y2 = rectangle.getY() + rectangle.getHeight();
		enableGL2D();
		glColor(internalColor);
		drawRect(x + width, y + width, x2 - width, y2 - width);
		glColor(borderColor);
		drawRect(x + 1.0F, y, x2 - 1.0F, y + width);
		drawRect(x, y, x + width, y2);
		drawRect(x2 - width, y, x2, y2);
		drawRect(x + 1.0F, y2 - width, x2 - 1.0F, y2);
		disableGL2D();
	}

	public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		glColor(topColor);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		glColor(bottomColor);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		disableGL2D();
	}

	public static void drawGradientRect2(int left, int top, int right, int bottom, int startColor, int endColor) {
		float var7 = (startColor >> 24 & 0xFF) / 255.0F;
		float var8 = (startColor >> 16 & 0xFF) / 255.0F;
		float var9 = (startColor >> 8 & 0xFF) / 255.0F;
		float var10 = (startColor & 0xFF) / 255.0F;
		float var11 = (endColor >> 24 & 0xFF) / 255.0F;
		float var12 = (endColor >> 16 & 0xFF) / 255.0F;
		float var13 = (endColor >> 8 & 0xFF) / 255.0F;
		float var14 = (endColor & 0xFF) / 255.0F;
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator var15 = Tessellator.getInstance();
		WorldRenderer var16 = var15.getWorldRenderer();
		var16.startDrawingQuads();
		var16.func_178960_a(var8, var9, var10, var7);
		var16.addVertex(right, top, 0.0D);
		var16.addVertex(left, top, 0.0D);
		var16.func_178960_a(var12, var13, var14, var11);
		var16.addVertex(left, bottom, 0.0D);
		var16.addVertex(right, bottom, 0.0D);
		var15.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
	}

	public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		glColor(topColor);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y1);
		glColor(bottomColor);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		disableGL2D();
	}

	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		glColor(col1);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		glColor(col2);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
	}

	public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2,
			int col3) {
		enableGL2D();
		GL11.glPushMatrix();
		glColor(col1);
		GL11.glLineWidth(1.0F);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		drawGradientRect(x, y, x2, y2, col2, col3);
		disableGL2D();
	}

	public static void glColor(Color color) {
		GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
				color.getAlpha() / 255.0F);
	}

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 255.0F;
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
		float red = 0.003921569F * redRGB;
		float green = 0.003921569F * greenRGB;
		float blue = 0.003921569F * blueRGB;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void drawStrip(int x, int y, float width, double angle, float points, float radius, int color) {
		float f1 = (color >> 24 & 0xFF) / 255.0F;
		float f2 = (color >> 16 & 0xFF) / 255.0F;
		float f3 = (color >> 8 & 0xFF) / 255.0F;
		float f4 = (color & 0xFF) / 255.0F;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0.0D);
		GL11.glColor4f(f2, f3, f4, f1);
		GL11.glLineWidth(width);
		if (angle > 0.0D) {
			GL11.glBegin(3);
			for (int i = 0; i < angle; i++) {
				float a = (float) (i * (angle * 3.141592653589793D / points));
				float xc = (float) (Math.cos(a) * radius);
				float yc = (float) (Math.sin(a) * radius);
				GL11.glVertex2f(xc, yc);
			}
			GL11.glEnd();
		}
		if (angle < 0.0D) {
			GL11.glBegin(3);
			for (int i = 0; i > angle; i--) {
				float a = (float) (i * (angle * 3.141592653589793D / points));
				float xc = (float) (Math.cos(a) * -radius);
				float yc = (float) (Math.sin(a) * -radius);
				GL11.glVertex2f(xc, yc);
			}
			GL11.glEnd();
		}
		disableGL2D();
		GL11.glDisable(3479);
		GL11.glPopMatrix();
	}

	public static void drawHLine(float x, float y, float x1, int y1) {
		if (y < x) {
			float var5 = x;
			x = y;
			y = var5;
		}
		drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
	}

	public static void drawVLine(float x, float y, float x1, int y1) {
		if (x1 < y) {
			float var5 = y;
			y = x1;
			x1 = var5;
		}
		drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
	}

	public static void drawHLine(float x, float y, float x1, int y1, int y2) {
		if (y < x) {
			float var5 = x;
			x = y;
			y = var5;
		}
		drawGradientRect(x, x1, y + 1.0F, x1 + 1.0F, y1, y2);
	}

	public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
		enableGL2D();
		GL11.glColor4f(r, g, b, a);
		drawRect(x, y, x1, y1);
		disableGL2D();
	}

	public static void drawRect(float x, float y, float x1, float y1) {
		GL11.glBegin(7);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
	}

	public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
		r *= 2.0F;
		cx *= 2.0F;
		cy *= 2.0F;
		float f = (c >> 24 & 0xFF) / 255.0F;
		float f2 = (c >> 16 & 0xFF) / 255.0F;
		float f3 = (c >> 8 & 0xFF) / 255.0F;
		float f4 = (c & 0xFF) / 255.0F;
		float theta = (float) (6.2831852D / num_segments);
		float p = (float) Math.cos(theta);
		float s = (float) Math.sin(theta);
		float x = r;
		float y = 0.0F;
		enableGL2D();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(2);
		for (int ii = 0; ii < num_segments; ii++) {
			GL11.glVertex2f(x + cx, y + cy);
			float t = x;
			x = p * x - s * y;
			y = s * t + p * y;
		}
		GL11.glEnd();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
	}

	public static void drawFullCircle(int cx, int cy, double r, int c) {
		r *= 2.0D;
		cx *= 2;
		cy *= 2;
		float f = (c >> 24 & 0xFF) / 255.0F;
		float f2 = (c >> 16 & 0xFF) / 255.0F;
		float f3 = (c >> 8 & 0xFF) / 255.0F;
		float f4 = (c & 0xFF) / 255.0F;
		enableGL2D();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(6);
		for (int i = 0; i <= 360; i++) {
			double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
			double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
			GL11.glVertex2d(cx + x, cy + y);
		}
		GL11.glEnd();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
	}

	public static void drawFullCircle(float cx, float cy, float r, final int c) {
		r *= 2.0f;
		cx *= 2.0f;
		cy *= 2.0f;
		final float theta = 0.19634953f;
		final float p = (float) Math.cos(theta);
		final float s = (float) Math.sin(theta);
		float x = r;
		float y = 0.0f;
		enableGL2D();
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glEnable(3024);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		glColor(c);
		GL11.glBegin(9);
		for (int ii = 0; ii < 32; ++ii) {
			GL11.glVertex2f(x + cx, y + cy);
			final float t = x;
			x = p * x - s * y;
			y = s * t + p * y;
		}
		GL11.glEnd();
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		disableGL2D();
	}

	public static void drawBorderedRectZ(double left, double top, double right, double bottom, float borderWidth,
			int borderColor, int color) {
		float alpha = (borderColor >> 24 & 0xFF) / 255.0F;
		float red = (borderColor >> 16 & 0xFF) / 255.0F;
		float green = (borderColor >> 8 & 0xFF) / 255.0F;
		float blue = (borderColor & 0xFF) / 255.0F;
		GlStateManager.pushMatrix();
		drawRects(left, top, right, bottom, color);
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		if (borderWidth == 1.0F) {
			GL11.glEnable(2848);
		}
		GL11.glLineWidth(borderWidth);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(left, top, 0.0D);
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, top, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		tessellator.draw();
		GL11.glLineWidth(2.0F);
		if (borderWidth == 1.0F) {
			GL11.glDisable(2848);
		}
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawRects(double left, double top, double right, double bottom, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		Tessellator var9 = Tessellator.getInstance();
		WorldRenderer var10 = var9.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		var10.startDrawingQuads();
		var10.addVertex(left, bottom, 0.0D);
		var10.addVertex(right, bottom, 0.0D);
		var10.addVertex(right, top, 0.0D);
		var10.addVertex(left, top, 0.0D);
		var9.draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
	}

	public static void drawBorderedGradientRect(double left, double top, double right, double bottom, float borderWidth,
			int borderColor, int startColor, int endColor) {
		float alpha = (borderColor >> 24 & 0xFF) / 255.0F;
		float red = (borderColor >> 16 & 0xFF) / 255.0F;
		float green = (borderColor >> 8 & 0xFF) / 255.0F;
		float blue = (borderColor & 0xFF) / 255.0F;
		GlStateManager.pushMatrix();
		drawGradientRect(left, top, right, bottom, startColor, endColor);
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		if (borderWidth == 1.0F) {
			GL11.glEnable(2848);
		}
		GL11.glLineWidth(borderWidth);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(1);
		worldRenderer.addVertex(left, top, 0.0D);
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, top, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		tessellator.draw();
		GL11.glLineWidth(2.0F);
		if (borderWidth == 1.0F) {
			GL11.glDisable(2848);
		}
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBorderedGradientRect2(final double left, final double top, final double right,
			final double bottom, final int borderColor, final int startColor, final int endColor) {
		drawBorderedGradientRect(left, top, right, bottom, 1.0f, borderColor, startColor, endColor);
	}

	public static int getMouseX() {
		return Mouse.getX() * getScreenWidth() / Minecraft.getMinecraft().displayWidth;
	}

	public static int getMouseY() {
		return getScreenHeight() - Mouse.getY() * getScreenHeight() / Minecraft.getMinecraft().displayHeight - 1;
	}

	public static int getScreenWidth() {
		return Summer.mc.displayWidth / getScaleFactor();
	}

	public static int getScaleFactor() {
		int scaleFactor = 1;
		boolean isUnicode = Summer.mc.isUnicode();
		int scaleSetting = Summer.mc.fontRendererObj.gameSettings.guiScale;

		if (scaleSetting == 0) {
			scaleSetting = 1000;
		}
		while (scaleFactor < scaleSetting && Summer.mc.displayWidth / (scaleFactor + 1) >= 320
				&& Summer.mc.displayHeight / (scaleFactor + 1) >= 240) {
			scaleFactor++;
		}

		if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
			scaleFactor--;
		}
		return scaleFactor;
	}

	public static int getScreenHeight() {
		return Summer.mc.displayHeight / getScaleFactor();
	}

	public static double[] interpolate(Entity e) {
		double x = e.lastTickPosX + (e.posX - e.lastTickPosX) - RenderManager.renderPosX;
		double y = e.lastTickPosY + (e.posY - e.lastTickPosY) - RenderManager.renderPosY;
		double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) - RenderManager.renderPosZ;
		return new double[] { x, y, z };
	}

	public static double[] interpolate(EntityPlayer ent, float partialTicks) {
		double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) - RenderManager.renderPosX;
		double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) - RenderManager.renderPosY;
		double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) - RenderManager.renderPosZ;
		return new double[] { x, y, z };
	}

	public static void drawRect(int x, int y, int x1, int y1, Color color) {
		Gui.drawRect(x, y, x1, y1, color.getRGB());
	}

	public static void drawRectt(float x, float y, float x1, float y1, int color) {
		enableGL2D();
		glColor(color);
		drawRect(x, y, x1, y1);
		disableGL2D();
	}

	public static void drawRect(float left, float top, float right, float bottom, final Color color) {
		if (left < right) {
			final float var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			final float var5 = top;
			top = bottom;
			bottom = var5;
		}
		final Tessellator var6 = Tessellator.getInstance();
		final WorldRenderer var7 = var6.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		R3DUtils.setColor(color);
		var7.startDrawingQuads();
		var7.addVertex(left, bottom, 0.0);
		var7.addVertex(right, bottom, 0.0);
		var7.addVertex(right, top, 0.0);
		var7.addVertex(left, top, 0.0);
		var6.draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		R3DUtils.setColor(Color.WHITE);
	}

	public static void enableGL3D() {
		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2884);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4353);
		GL11.glDisable(2896);
	}

	public static void disableGL3D() {
		GL11.glEnable(2896);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glDepthMask(true);
		GL11.glCullFace(1029);
	}

	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int border, int c1, int c2,
			int width) {
		rectangleBorderedGradient(x1, y1, x2, y2, new int[] { c1, c2 }, new int[] { border }, width);
	}

	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int border, int c1,
			int c2) {
		rectangleBorderedGradient(x1, y1, x2, y2, new int[] { c1, c2 }, new int[] { border }, 0.5D);
	}

	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int[] fill, int[] outline,
			double width) {
		rectangleOutlinedGradient(x1, y1, x2, y2, outline, width);
		rectangleGradient(x1 + width, y1 + width, x2 - width, y2 - width, fill);
	}

	public static void rectangleOutlinedGradient(double x1, double y1, double x2, double y2, int[] color,
			double width) {
		rectangleGradient(x1, y1, x2, y1 + width, color);
		rectangleGradient(x1, y2 - width, x2, y2, color);
		rectangleGradient(x1, y1 + width, x1 + width, y2 - width, color);
		rectangleGradient(x2 - width, y1 + width, x2, y2 - width, color);
	}

	public static void rectangleGradient(double x1, double y1, double x2, double y2, int[] color) {
		float[] r = new float[color.length];
		float[] g = new float[color.length];
		float[] b = new float[color.length];
		float[] a = new float[color.length];
		for (int i = 0; i < color.length; i++) {
			r[i] = ((color[i] >> 16 & 0xFF) / 255.0F);
			g[i] = ((color[i] >> 8 & 0xFF) / 255.0F);
			b[i] = ((color[i] & 0xFF) / 0.0F);
			a[i] = ((color[i] >> 24 & 0xFF) / 255.0F);
		}
		GlStateManager.func_179090_x();
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GlStateManager.blendFunc(770, 771);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(11);
		if (color.length == 1) {
			worldRenderer.addVertex(x2, y1, 0.0D);
			worldRenderer.addVertex(x1, y1, 0.0D);
			worldRenderer.addVertex(x1, y2, 0.0D);
			worldRenderer.addVertex(x2, y2, 0.0D);
		} else if ((color.length == 2) || (color.length == 3)) {
			worldRenderer.addVertex(x2, y1, 0.0D);
			worldRenderer.addVertex(x1, y1, 0.0D);
			worldRenderer.addVertex(x1, y2, 0.0D);
			worldRenderer.addVertex(x2, y2, 0.0D);
		} else if (color.length >= 4) {
			worldRenderer.addVertex(x2, y1, 0.0D);
			worldRenderer.addVertex(x1, y1, 0.0D);
			worldRenderer.addVertex(x1, y2, 0.0D);
			worldRenderer.addVertex(x2, y2, 0.0D);
		}
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
	}

	// public static final NahrFont getNahrFont() {
	// return R2DUtils.font;
	// }

	public static void drawRectBC(final float g, final float h, final float i, final float j, final int col1) {
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(7);
		GL11.glVertex2d(i, h);
		GL11.glVertex2d(g, h);
		GL11.glVertex2d(g, j);
		GL11.glVertex2d(i, j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static String getPrettyName(final String name, final String splitBy) {
		String prettyName = "";
		final String[] actualNameSplit = name.split(splitBy);
		if (actualNameSplit.length > 0) {
			final String[] var3 = actualNameSplit;
			for (int var4 = actualNameSplit.length, var5 = 0; var5 < var4; ++var5) {
				String arg = var3[var5];
				arg = String.valueOf(arg.substring(0, 1).toUpperCase()) + arg.substring(1, arg.length()).toLowerCase();
				prettyName = String.valueOf(prettyName) + arg + " ";
			}
		} else {
			prettyName = String.valueOf(actualNameSplit[0].substring(0, 1).toUpperCase())
					+ actualNameSplit[0].substring(1, actualNameSplit[0].length()).toLowerCase();
		}
		return prettyName.trim();
	}

	public static void drawBorderedCorneredRect(final float x, final float y, final float x2, final float y2,
			final float lineWidth, final int lineColor, final int bgColor) {
		drawRectBC(x, y, x2, y2, bgColor);
		final float f = (lineColor >> 24 & 0xFF) / 255.0f;
		final float f2 = (lineColor >> 16 & 0xFF) / 255.0f;
		final float f3 = (lineColor >> 8 & 0xFF) / 255.0f;
		final float f4 = (lineColor & 0xFF) / 255.0f;
		drawRectBC(x - 1.0f, y - 1.0f, x2 + 1.0f, y, lineColor);
		drawRectBC(x - 1.0f, y, x, y2, lineColor);
		drawRectBC(x - 1.0f, y2, x2 + 1.0f, y2 + 1.0f, lineColor);
		drawRectBC(x2, y, x2 + 1.0f, y2, lineColor);
	}

	public static boolean isHovering(final int x, final int y, final int x2, final int y2, final int mouseX,
			final int mouseY) {
		return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
	}

	public static final ScaledResolution getScaledRes() {
		final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		return scaledRes;
	}

	public static void scissor2(int x, int y, int xend, int yend) {
		int width = xend - x;
		int height = yend - y;
		int factor = scaledResolution.getScaleFactor();
		int bottomY = Summer.mc.currentScreen.height - yend;
		GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
	}

	public static void rectangle(double left, double top, double right, double bottom, final int color) {
		if (left < right) {
			final double var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			final double var5 = top;
			top = bottom;
			bottom = var5;
		}
		final float var6 = (color >> 24 & 0xFF) / 255.0f;
		final float var7 = (color >> 16 & 0xFF) / 255.0f;
		final float var8 = (color >> 8 & 0xFF) / 255.0f;
		final float var9 = (color & 0xFF) / 255.0f;
		final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var7, var8, var9, var6);
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(left, bottom, 0.0);
		worldRenderer.addVertex(right, bottom, 0.0);
		worldRenderer.addVertex(right, top, 0.0);
		worldRenderer.addVertex(left, top, 0.0);
		Tessellator.getInstance().draw();
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
	}

	public static ScaledResolution newScaledResolution() {
		return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
	}

	public static void drawBorderRect(float left, float top, float right, float bottom, int bcolor, int icolor,
			float f) {
		Gui.drawRect((int) left + (int) f, (int) top + (int) f, (int) right - (int) f, (int) bottom - (int) f, icolor);
		Gui.drawRect((int) left, (int) top, (int) left + (int) f, (int) bottom, bcolor);
		Gui.drawRect((int) left + (int) f, (int) top, (int) right, (int) top + (int) f, bcolor);
		Gui.drawRect((int) left + (int) f, (int) bottom - (int) f, (int) right, (int) bottom, bcolor);
		Gui.drawRect((int) right - (int) f, (int) top + (int) f, (int) right, (int) bottom - (int) f, bcolor);
	}

	public static int getDisplayHeight() {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		int displayHeight = scaledResolution.getScaledHeight();
		return displayHeight;
	}

	public static void drawBorderedCircle(int x, int y, float radius, final int outsideC, final int insideC) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		final float scale = 0.1f;
		GL11.glScalef(0.1f, 0.1f, 0.1f);
		x *= 10;
		y *= 10;
		radius *= 10.0f;
		drawCircle(x, y, radius, insideC);
		drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
		GL11.glScalef(10.0f, 10.0f, 10.0f);
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawUnfilledCircle(final int x, final int y, final float radius, final float lineWidth,
			final int color) {
		final float alpha = (color >> 24 & 0xFF) / 255.0f;
		final float red = (color >> 16 & 0xFF) / 255.0f;
		final float green = (color >> 8 & 0xFF) / 255.0f;
		final float blue = (color & 0xFF) / 255.0f;
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glLineWidth(lineWidth);
		GL11.glEnable(2848);
		GL11.glBegin(2);
		for (int i = 0; i <= 360; ++i) {
			GL11.glVertex2d(x + Math.sin(i * 3.141526 / 180.0) * radius, y + Math.cos(i * 3.141526 / 180.0) * radius);
		}
		GL11.glEnd();
		GL11.glDisable(2848);
	}

	public static void drawCircle(final int x, final int y, final float radius, final int color) {
		final float alpha = (color >> 24 & 0xFF) / 255.0f;
		final float red = (color >> 16 & 0xFF) / 255.0f;
		final float green = (color >> 8 & 0xFF) / 255.0f;
		final float blue = (color & 0xFF) / 255.0f;
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(9);
		for (int i = 0; i <= 360; ++i) {
			GL11.glVertex2d(x + Math.sin(i * 3.141526 / 180.0) * radius, y + Math.cos(i * 3.141526 / 180.0) * radius);
		}
		GL11.glEnd();
	}

	public static int width() {
		return new ScaledResolution(Summer.mc, Summer.mc.displayWidth, Summer.mc.displayHeight).getScaledWidth();
	}

	public static int height() {
		return new ScaledResolution(Summer.mc, Summer.mc.displayWidth, Summer.mc.displayHeight).getScaledHeight();
	}
}