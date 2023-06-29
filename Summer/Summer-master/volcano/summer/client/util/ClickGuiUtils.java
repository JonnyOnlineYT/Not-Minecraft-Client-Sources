package volcano.summer.client.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import volcano.summer.base.Summer;

public class ClickGuiUtils {

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

	public static void drawRect(float x, float y, float x1, float y1, int color) {
		enableGL2D();
		glColor(color);
		drawRect(x, y, x1, y1);
		disableGL2D();
	}

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 255.0F;
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
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

	public static void drawRect(float x, float y, float x1, float y1) {
		GL11.glBegin(7);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void scissor2(int x, int y, int xend, int yend) {
		int width = xend - x;
		int height = yend - y;
		ScaledResolution sr = new ScaledResolution(Summer.mc, Summer.mc.displayWidth, Summer.mc.displayHeight);
		int factor = sr.getScaleFactor();
		int bottomY = Summer.mc.currentScreen.height - yend;
		GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
	}
}
