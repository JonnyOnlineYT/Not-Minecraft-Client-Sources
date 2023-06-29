package ru.wendoxd.modules.impl.visuals;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;

public class Crosshair extends Module {
	public static Frame crosshairFrame = new Frame("Crosshair");
	public static CheckBox crosshair = new CheckBox("Crosshair").markColorPicker().markArrayList("Crosshair");
	public static CheckBox movementCheck = new CheckBox("Movement", () -> crosshair.isEnabled(true));
	private static double x, z;
	private static boolean enabled;

	protected void initSettings() {
		crosshairFrame.register(crosshair, movementCheck);
		MenuAPI.hud.register(crosshairFrame);
	}

	public void onEvent(Event event) {
		if (event instanceof EventRender2D) {
			ScaledResolution res = ((EventRender2D) event).getScaledResolution();
			if (crosshair.isEnabled(false)) {
				ScaledResolution scaledRes = res;
				int color = crosshair.getColor().build();
				int alpha = RenderUtils.rgba(0, 0, 0, crosshair.getColor().getAlpha());
				double gap = 1.5;
				double width = 0.5;
				double size = 3.5;
				rectangleBordered(scaledRes.getScaledWidth() / 2 - width,
						scaledRes.getScaledHeight() / 2 - gap - size - (this.isMoving() ? 2 : 0),
						scaledRes.getScaledWidth() / 2 + 1.0f + width,
						scaledRes.getScaledHeight() / 2 - gap - (this.isMoving() ? 2 : 0), 0.5, color, alpha);
				rectangleBordered(scaledRes.getScaledWidth() / 2 - width,
						scaledRes.getScaledHeight() / 2 + gap + 1.0 + (this.isMoving() ? 2 : 0) - 0.15,
						scaledRes.getScaledWidth() / 2 + 1.0f + width,
						scaledRes.getScaledHeight() / 2 + 1 + gap + size + (this.isMoving() ? 2 : 0) - 0.15, 0.5, color,
						alpha);
				rectangleBordered(scaledRes.getScaledWidth() / 2 - gap - size - (this.isMoving() ? 2 : 0) + 0.15,
						scaledRes.getScaledHeight() / 2 - width,
						scaledRes.getScaledWidth() / 2 - gap - (this.isMoving() ? 2 : 0) + 0.15,
						scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, color, alpha);
				rectangleBordered(scaledRes.getScaledWidth() / 2 + 1 + gap + (this.isMoving() ? 2 : 0),
						scaledRes.getScaledHeight() / 2 - width,
						scaledRes.getScaledWidth() / 2 + size + gap + 1.0 + (this.isMoving() ? 2 : 0),
						scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, color, alpha);
			}
			if (enabled) {
				double yaw = Math.toDegrees(Math.atan2(z - mc.player.posZ, x - mc.player.posX)) - mc.player.rotationYaw
						- 90;
				double dst = Math.sqrt(Math.pow(x - mc.player.posX, 2) + Math.pow(z - mc.player.posZ, 2));
				GL11.glPushMatrix();
				GL11.glTranslated(res.getScaledWidth_double() / 2d + 0.5, res.getScaledHeight_double() / 2d - 90, 0);
				GL11.glTranslated(
						(Math.cos(Math.toRadians(yaw - 90)) * 1.3)
								* (Fonts.MNTSB_12.getStringWidth((int) dst + "m") / 2),
						Math.sin(Math.toRadians(yaw - 90)) * 5, 0);
				GL11.glRotated(yaw, 0, 0, 1);
				drawTriangle();
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glTranslated(res.getScaledWidth_double() / 2d, res.getScaledHeight_double() / 2d - 90, 0);
				Fonts.MNTSB_12.drawCenteredString((int) dst + "m", 0, 0, RenderUtils.rgba(235, 235, 235, 255));
				GL11.glPopMatrix();
			}
		}
	}

	public boolean isMoving() {
		return movementCheck.isEnabled(false) && !mc.player.isCollidedHorizontally
				&& (mc.player.movementInput.moveForward != 0 || mc.player.movementInput.moveStrafe != 0)
				&& !mc.player.isSneaking();
	}

	public static void setGPS(double x, double z) {
		setEnabled(true);
		Crosshair.x = x;
		Crosshair.z = z;
	}

	public static void setEnabled(boolean enabled) {
		Crosshair.enabled = enabled;
	}

	public void drawTriangle() {
		boolean needBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
		if (needBlend)
			GL11.glEnable(GL11.GL_BLEND);
		int alpha = 255;
		int red_1 = 255;
		int green_1 = 255;
		int blue_1 = 255;
		int red_2 = Math.max(red_1 - 40, 0);
		int green_2 = Math.max(green_1 - 40, 0);
		int blue_2 = Math.max(blue_1 - 40, 0);
		float width = 6, height = 12;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor4f(red_1 / 255f, green_1 / 255f, blue_1 / 255f, alpha / 255f);
		GL11.glVertex2d(0, 0 - height);
		GL11.glVertex2d(0 - width, 0);
		GL11.glVertex2d(0, 0 - 3);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glColor4f(red_2 / 255f, green_2 / 255f, blue_2 / 255f, alpha / 255f);
		GL11.glVertex2d(0, 0 - height);
		GL11.glVertex2d(0, 0 - 3);
		GL11.glVertex2d(0 + width, 0);
		GL11.glEnd();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if (needBlend)
			GL11.glDisable(GL11.GL_BLEND);
	}

	public static void rectangleBordered(final double x, final double y, final double x1, final double y1,
			final double width, final int internalColor, final int borderColor) {
		RenderUtils.drawRect(x + width, y + width, x1 - width, y1 - width, internalColor);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderUtils.drawRect(x + width, y, x1 - width, y + width, borderColor);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderUtils.drawRect(x, y, x + width, y1, borderColor);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderUtils.drawRect(x1 - width, y, x1, y1, borderColor);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		RenderUtils.drawRect(x + width, y1 - width, x1 - width, y1, borderColor);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
