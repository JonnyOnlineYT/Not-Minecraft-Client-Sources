package volcano.summer.screen.clickgui.sektor;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import volcano.summer.base.Summer;

public class Render {

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

	public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
		enableGL2D();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
		drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
		drawHLine(x, x1 - 1.0f, y, borderC);
		drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
		drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		disableGL2D();
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

	private static void drawHLine(float x, float y, float x1, int y1) {
		if (y < x) {
			float var5 = x;
			x = y;
			y = var5;
		}
		drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
	}

	private static void drawVLine(float x, float y, float x1, int y1) {
		if (x1 < y) {
			float var5 = y;
			y = x1;
			x1 = var5;
		}
		drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
	}

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 255) / 255.0f;
		float red = (hex >> 16 & 255) / 255.0f;
		float green = (hex >> 8 & 255) / 255.0f;
		float blue = (hex & 255) / 255.0f;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void drawRect(float x, float y, float x1, float y1, int color) {
		enableGL2D();
		glColor(color);
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

	public static void drawRoundedBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		drawVLine(x, y, y1 - 1, borderC);
		drawVLine(x1 - 1, y, y1 - 1, borderC);
		drawHLine(x + 1, x1 - 2, y, borderC);
		drawHLine(x + 1, x1 - 2, y1 - 1, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
	}

	public static int getMouseX() {
		return Mouse.getX() * getScreenWidth() / Summer.mc.displayWidth;
	}

	public static int getMouseY() {
		return getScreenHeight() - Mouse.getY() * getScreenHeight() / Summer.mc.displayHeight - 1;
	}

	public static int getScreenWidth() {
		return Summer.mc.displayWidth / getScaleFactor();
	}

	public static int getScreenHeight() {
		return Summer.mc.displayHeight / getScaleFactor();
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
			++scaleFactor;
		}
		if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
			--scaleFactor;
		}
		return scaleFactor;
	}

	public static void rectangleGradient(double x1, double y1, double x2, double y2, int[] color) {
		float[] r = new float[color.length];
		float[] g = new float[color.length];
		float[] b = new float[color.length];
		float[] a = new float[color.length];
		int i = 0;
		while (i < color.length) {
			r[i] = (color[i] >> 16 & 255) / 255.0f;
			g[i] = (color[i] >> 8 & 255) / 255.0f;
			b[i] = (color[i] & 255) / 255.0f;
			a[i] = (color[i] >> 24 & 255) / 255.0f;
			++i;
		}
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.func_179090_x();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(r.length, g.length, b.length, a.length);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(i);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.startDrawing(3);
		worldRenderer.addVertex(x1, y1, 0.0F);
		worldRenderer.addVertex(x1, y1, 0.0F);
		worldRenderer.addVertex(x1, y2, 0.0F);
		worldRenderer.addVertex(x2, y2, 0.0F);
		worldRenderer.addVertex(x2, y1, 0.0F);
		worldRenderer.addVertex(x1, y1, 0.0F);
		worldRenderer.addVertex(x1, y2, 0.0F);
		worldRenderer.addVertex(x2, y2, 0.0F);
		worldRenderer.addVertex(x2, y1, 0.0F);
		worldRenderer.addVertex(x1, y1, 0.0F);
		worldRenderer.addVertex(x1, y2, 0.0F);
		worldRenderer.addVertex(x2, y2, 0.0F);
		tessellator.draw();
		GL11.glLineWidth(6.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.func_179098_w();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int border, int c1, int c2,
			int width) {
		rectangleBorderedGradient(x1, y1, x2, y2, new int[] { c1, c2 }, new int[] { border }, width);
	}

	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int border, int c1,
			int c2) {
		rectangleBorderedGradient(x1, y1, x2, y2, new int[] { c1, c2 }, new int[] { border }, 0.5);
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
}