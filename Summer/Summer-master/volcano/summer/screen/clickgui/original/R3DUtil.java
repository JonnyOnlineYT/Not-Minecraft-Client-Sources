package volcano.summer.screen.clickgui.original;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import volcano.summer.base.Summer;

public enum R3DUtil {

	INSTANCE;

	public void drawRect(int x, int y, int width, int height, Color color) {
		Gui.drawRect(x, y, x + width, y + height, color.getRGB());
	}

	public void drawRect(int x, int y, int width, int height, int color) {
		Gui.drawRect(x, y, x + width, y + height, color);
	}

	public void drawFilledCircle(double d, double e, double r, int c) {
		float f = ((c >> 24) & 0xff) / 255F;
		float f1 = ((c >> 16) & 0xff) / 255F;
		float f2 = ((c >> 8) & 0xff) / 255F;
		float f3 = (c & 0xff) / 255F;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		for (int i = 0; i <= 360; i++) {
			double x2 = Math.sin(((i * Math.PI) / 180)) * r;
			double y2 = Math.cos(((i * Math.PI) / 180)) * r;
			GL11.glVertex2d(d + x2, e + y2);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public void drawCircle(double d, double e, double r, int c) {
		float f = ((c >> 24) & 0xff) / 255F;
		float f1 = ((c >> 16) & 0xff) / 255F;
		float f2 = ((c >> 8) & 0xff) / 255F;
		float f3 = (c & 0xff) / 255F;
		GL11.glPushMatrix();
		GL11.glLineWidth(0.5F);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_LINE_LOOP);

		for (int i = 0; i <= 360; i++) {
			double x2 = Math.sin(((i * Math.PI) / 180)) * r - 0.1;
			double y2 = Math.cos(((i * Math.PI) / 180)) * r - 0.1;
			GL11.glVertex2d(d + x2, e + y2);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public void drawCentredStringWithShadow(String s, int x, int y, int colour) {
		x -= Summer.mc.fontRendererObj.getStringWidth(s) / 2;
		Summer.mc.fontRendererObj.func_175063_a(s, x, y, colour);
	}

	public double round(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public void drawArrow(float x, float y, boolean isOpen, int hexColor) {
		GL11.glPushMatrix();
		GL11.glScaled(1.3, 1.3, 1.3);
		if (isOpen) {
			y -= 1.5f;
			x += 2;
		}
		x /= 1.3;
		y /= 1.3;
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		hexColor(hexColor);
		GL11.glLineWidth(2);
		if (isOpen) {
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + 4, y + 3);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x + 4, y + 3);
			GL11.glVertex2d(x, y + 6);
			GL11.glEnd();
		} else {
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + 3, y + 4);
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x + 3, y + 4);
			GL11.glVertex2d(x + 6, y);
			GL11.glEnd();
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}

	public void hexColor(int hexColor) {
		float red = (hexColor >> 16 & 0xFF) / 255.0F;
		float green = (hexColor >> 8 & 0xFF) / 255.0F;
		float blue = (hexColor & 0xFF) / 255.0F;
		float alpha = (hexColor >> 24 & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}
}
