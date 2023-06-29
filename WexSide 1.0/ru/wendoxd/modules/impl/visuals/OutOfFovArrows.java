package ru.wendoxd.modules.impl.visuals;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.utils.combat.CastHelper;

public class OutOfFovArrows extends Module {

	@Override
	protected void initSettings() {
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D) {
			CastHelper castHelper = new CastHelper();
			if (visuals_entitiestab.get(0).oofarrows.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.PLAYERS);
			}
			if (visuals_entitiestab.get(1).oofarrows.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.MOBS);
			}
			if (visuals_entitiestab.get(2).oofarrows.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.ANIMALS);
			}
			if (visuals_entitiestab.get(3).oofarrows.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.VILLAGERS);
			}
			if (visuals_entitiestab.get(4).oofarrows.isEnabled()) {
				castHelper.apply(CastHelper.EntityType.FRIENDS);
			}
			CastHelper.EntityType type;
			for (Entity entity : mc.world.loadedEntityList) {
				if (entity instanceof EntityPlayer && ((EntityPlayer) entity).bot) {
					continue;
				}
				ScaledResolution res = ((EventRender2D) event).getScaledResolution();
				if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null) {
					float fov = mc.gameSettings.fovSetting;
					double x = entity.posX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
							- mc.getRenderManager().renderPosX;
					double z = entity.posZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
							- mc.getRenderManager().renderPosZ;
					double rot = Math.atan2(z, x) - Math.toRadians(mc.player.rotationYaw) - Math.PI;
					float normalizedDegrees = (float) MathHelper.wrapDegrees(Math.toDegrees(rot)) + 90;
					if (Math.abs(normalizedDegrees) > fov) {
						CheckBox bx = visuals_entitiestab.get(type.ordinal()).oofarrows;
						GL11.glPushMatrix();
						double dst = (res.getScaledWidth() / res.getScaledHeight())
								* (res.getScaledWidth() / (55 - oofdist.getDoubleValue()));
						GL11.glTranslated(res.getScaledWidth() / 2 + Math.cos(rot) * dst,
								res.getScaledHeight() / 2 + Math.sin(rot) * dst, 0);
						GL11.glRotated(normalizedDegrees, 0, 0, 1);
						drawTriangle(bx);
						GL11.glPopMatrix();
					}
				}
			}
		}
	}

	public void drawTriangle(CheckBox checkBox) {
		boolean needBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
		if (needBlend)
			GL11.glEnable(GL11.GL_BLEND);
		int alpha = checkBox.getColor().getAlpha();
		int red_1 = checkBox.getColor().getRed();
		int green_1 = checkBox.getColor().getGreen();
		int blue_1 = checkBox.getColor().getBlue();
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
}
