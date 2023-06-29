package volcano.summer.client.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class GuiUtils {
	private static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

	public static void drawCentredStringWithShadow(String s, int x, int y, int colour) {
		x -= fr.getStringWidth(s) / 2;
		fr.func_175063_a(s, x, y, colour);
	}

	public static void drawRect(int x, int y, int x2, int y2, int color) {
		Gui.drawRect(x, y, x2, y2, color);
	}

	public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd,
			int paramColor) {
		float alpha = (paramColor >> 24 & 0xFF) / 255.0F;
		float red = (paramColor >> 16 & 0xFF) / 255.0F;
		float green = (paramColor >> 8 & 0xFF) / 255.0F;
		float blue = (paramColor & 0xFF) / 255.0F;

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);

		GL11.glPushMatrix();
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(7);
		GL11.glVertex2d(paramXEnd, paramYStart);
		GL11.glVertex2d(paramXStart, paramYStart);
		GL11.glVertex2d(paramXStart, paramYEnd);
		GL11.glVertex2d(paramXEnd, paramYEnd);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawRectM(float x, float y, float x1, float y1, int color) {
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

	public static void drawRect(float x, float y, float x1, float y1) {
		GL11.glBegin(7);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
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

	public static void drawPoint(int x, int y, int color) {
		drawRect(x, y, x + 1, y + 1, color);
	}

	public static void drawVerticalLine(int x, int y, int height, int color) {
		drawRect(x, y, x + 1, height, color);
	}

	public static void drawHorizontalLine(int x, int y, int width, int color) {
		drawRect(x, y, width, y + 1, color);
	}

	public static void drawBorderedRect(int x, int y, int x1, int y1, int bord, int color) {
		drawRect(x + 1, y + 1, x1, y1, color);
		drawVerticalLine(x, y, y1, bord);
		drawVerticalLine(x1, y, y1, bord);
		drawHorizontalLine(x + 1, y, x1, bord);
		drawHorizontalLine(x, y1, x1 + 1, bord);
	}

	public static void drawFineBorderedRect(int x, int y, int x1, int y1, int bord, int color) {
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		x *= 2;
		y *= 2;
		x1 *= 2;
		y1 *= 2;
		drawRect(x + 1, y + 1, x1, y1, color);
		drawVerticalLine(x, y, y1, bord);
		drawVerticalLine(x1, y, y1, bord);
		drawHorizontalLine(x + 1, y, x1, bord);
		drawHorizontalLine(x, y1, x1 + 1, bord);
		GL11.glScaled(2.0D, 2.0D, 2.0D);
	}

	public static void drawBorderRectNoCorners(int x, int y, int x2, int y2, int bord, int color) {
		x *= 2;
		y *= 2;
		x2 *= 2;
		y2 *= 2;
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		drawRect(x + 1, y + 1, x2, y2, color);
		drawVerticalLine(x, y + 1, y2, bord);
		drawVerticalLine(x2, y + 1, y2, bord);
		drawHorizontalLine(x + 1, y, x2, bord);
		drawHorizontalLine(x + 1, y2, x2, bord);
		GL11.glScaled(2.0D, 2.0D, 2.0D);
	}

	public static void drawBorderedGradient(int x, int y, int x1, int y1, int bord, int gradTop, int gradBot) {
		GL11.glScaled(0.5D, 0.5D, 0.5D);
		x *= 2;
		y *= 2;
		x1 *= 2;
		y1 *= 2;

		float f = (gradTop >> 24 & 0xFF) / 255.0F;
		float f1 = (gradTop >> 16 & 0xFF) / 255.0F;
		float f2 = (gradTop >> 8 & 0xFF) / 255.0F;
		float f3 = (gradTop & 0xFF) / 255.0F;

		float f4 = (gradBot >> 24 & 0xFF) / 255.0F;
		float f5 = (gradBot >> 16 & 0xFF) / 255.0F;
		float f6 = (gradBot >> 8 & 0xFF) / 255.0F;
		float f7 = (gradBot & 0xFF) / 255.0F;

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);

		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x1, y + 1);
		GL11.glVertex2d(x + 1, y + 1);

		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x + 1, y1);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);

		drawVLine(x, y, y1 - 1, bord);
		drawVLine(x1 - 1, y, y1 - 1, bord);
		drawHLine(x, x1 - 1, y, bord);
		drawHLine(x, x1 - 1, y1 - 1, bord);

		GL11.glScaled(2.0D, 2.0D, 2.0D);
	}

	public static void drawHLine(float par1, float par2, float par3, int par4) {
		if (par2 < par1) {
			float var5 = par1;
			par1 = par2;
			par2 = var5;
		}
		drawRect(par1, par3, par2 + 1.0F, par3 + 1.0F, par4);
	}

	public static void drawVLine(float par1, float par2, float par3, int par4) {
		if (par3 < par2) {
			float var5 = par2;
			par2 = par3;
			par3 = var5;
		}
		drawRect(par1, par2 + 1.0F, par1 + 1.0F, par3, par4);
	}

	public static void drawGradientBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2,
			int col3) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;

		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(3042);

		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
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

		GL11.glEnable(3042);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
	}

	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;

		float f4 = (col2 >> 24 & 0xFF) / 255.0F;
		float f5 = (col2 >> 16 & 0xFF) / 255.0F;
		float f6 = (col2 >> 8 & 0xFF) / 255.0F;
		float f7 = (col2 & 0xFF) / 255.0F;

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);

		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);

		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
	}

	public static void drawSidewaysGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;

		float f4 = (col2 >> 24 & 0xFF) / 255.0F;
		float f5 = (col2 >> 16 & 0xFF) / 255.0F;
		float f6 = (col2 >> 8 & 0xFF) / 255.0F;
		float f7 = (col2 & 0xFF) / 255.0F;

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);

		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);

		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x2, y);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
	}

	public static void drawBorderedCircle(int x, int y, float radius, int outsideC, int insideC) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		float scale = 0.1F;
		GL11.glScalef(scale, scale, scale);
		x = (int) (x * (1.0F / scale));
		y = (int) (y * (1.0F / scale));
		radius *= 1.0F / scale;
		drawCircle(x, y, radius, insideC);
		drawUnfilledCircle(x, y, radius, 1.0F, outsideC);
		GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawUnfilledCircle(int x, int y, float radius, float lineWidth, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glLineWidth(lineWidth);
		GL11.glEnable(2848);
		GL11.glBegin(2);
		for (int i = 0; i <= 360; i++) {
			GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius,
					y + Math.cos(i * 3.141526D / 180.0D) * radius);
		}
		GL11.glEnd();
		GL11.glDisable(2848);
	}

	public static void drawCircle(int x, int y, float radius, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(9);
		for (int i = 0; i <= 360; i++) {
			GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius,
					y + Math.cos(i * 3.141526D / 180.0D) * radius);
		}
		GL11.glEnd();
	}

	public static double getAlphaFromHex(int color) {
		return (color >> 24 & 0xFF) / 255.0F;
	}

	public static double getRedFromHex(int color) {
		return (color >> 16 & 0xFF) / 255.0F;
	}

	public static double getGreenFromHex(int color) {
		return (color >> 8 & 0xFF) / 255.0F;
	}

	public static double getBlueFromHex(int color) {
		return (color & 0xFF) / 255.0F;
	}
}
