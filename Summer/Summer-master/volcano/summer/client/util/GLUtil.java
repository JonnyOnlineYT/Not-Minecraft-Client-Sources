package volcano.summer.client.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;

public class GLUtil {

	private static Map<Integer, Boolean> glCapMap = new HashMap();

	public static void setGLCap(int cap, boolean flag) {
		glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
		if (flag) {
			GL11.glEnable(cap);
		} else {
			GL11.glDisable(cap);
		}
	}

	public static void revertGLCap(int cap) {
		Boolean origCap = glCapMap.get(Integer.valueOf(cap));
		if (origCap != null) {
			if (origCap.booleanValue()) {
				GL11.glEnable(cap);
			} else {
				GL11.glDisable(cap);
			}
		}
	}

	public static void glEnable(int cap) {
		setGLCap(cap, true);
	}

	public static void glDisable(int cap) {
		setGLCap(cap, false);
	}

	public static void revertAllCaps() {
		for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();) {
			int cap = ((Integer) localIterator.next()).intValue();
			revertGLCap(cap);
		}
	}

	public static void drawBorderRect(float left, float top, float right, float bottom, int bcolor, int icolor,
			float f) {
		Gui.drawRect((int) left + (int) f, (int) top + (int) f, (int) right - (int) f, (int) bottom - (int) f, icolor);
		Gui.drawRect((int) left, (int) top, (int) left + (int) f, (int) bottom, bcolor);
		Gui.drawRect((int) left + (int) f, (int) top, (int) right, (int) top + (int) f, bcolor);
		Gui.drawRect((int) left + (int) f, (int) bottom - (int) f, (int) right, (int) bottom, bcolor);
		Gui.drawRect((int) right - (int) f, (int) top + (int) f, (int) right, (int) bottom - (int) f, bcolor);
	}

	public static void drawRect(float x, float y, float x1, float y1, int color) {
		enableGL2D();
		glColor(color);
		drawRect(x, y, x1, y1);
		disableGL2D();
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

	public static void disableGL3D() {
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
}
