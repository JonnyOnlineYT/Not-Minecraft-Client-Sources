package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRender3D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.utils.combat.CastHelper;
import ru.wendoxd.utils.visual.ColorShell;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class Tracers extends Module {

	@Override
	protected void initSettings() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D) {
			for (Entity entity : mc.world.loadedEntityList) {
				if (entity instanceof EntityPlayer && ((EntityPlayer) entity).bot) {
					continue;
				}
				CastHelper castHelper = new CastHelper();
				if (visuals_entitiestab.get(0).tracers.isEnabled()) {
					castHelper.apply(CastHelper.EntityType.PLAYERS);
				}
				if (visuals_entitiestab.get(1).tracers.isEnabled()) {
					castHelper.apply(CastHelper.EntityType.MOBS);
				}
				if (visuals_entitiestab.get(2).tracers.isEnabled()) {
					castHelper.apply(CastHelper.EntityType.ANIMALS);
				}
				if (visuals_entitiestab.get(3).tracers.isEnabled()) {
					castHelper.apply(CastHelper.EntityType.VILLAGERS);
				}
				if (visuals_entitiestab.get(4).tracers.isEnabled()) {
					castHelper.apply(CastHelper.EntityType.FRIENDS);
				}
				CastHelper.EntityType type;
				boolean prevViewBobbing = mc.gameSettings.viewBobbing;
				mc.gameSettings.viewBobbing = false;
				if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null && entity != mc.player) {
					CheckBox tracers = visuals_entitiestab.get(type.ordinal()).tracers;
					if (tracers.isEnabled()) {
						ColorShell color = tracers.getColor();
						boolean rainbow = tracers.getColor().isRainbow();

						double x = entity.lastTickPosX
								+ (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks()
								- mc.getRenderManager().renderPosX;
						double y = entity.lastTickPosY
								+ (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()
								- mc.getRenderManager().renderPosY;
						double z = entity.lastTickPosZ
								+ (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks()
								- mc.getRenderManager().renderPosZ;

						float red = color.getRed() / 255F;
						float green = color.getGreen() / 255F;
						float blue = color.getBlue() / 255F;
						float alpha = tracers.getColor().getAlpha() / 255F;

						if (rainbow) {
							int c1 = PaletteHelper.rainbow(1);
							red = ((c1 >> 16) & 255) / 255F;
							green = ((c1 >> 8) & 255) / 255F;
							blue = (c1 & 255) / 255F;
						}
						GL11.glPushMatrix();
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_LINE_SMOOTH);
						GL11.glLineWidth(2);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glDepthMask(false);
						GlStateManager.color(red, green, blue, alpha);
						GL11.glBegin(GL11.GL_LINE_STRIP);
						Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch)))
								.rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
						GL11.glVertex3d(vec.xCoord, vec.yCoord + mc.player.getEyeHeight(), vec.zCoord);
						GL11.glVertex3d(x, y, z);
						GL11.glEnd();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_LINE_SMOOTH);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glDepthMask(true);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glPopMatrix();
						GlStateManager.resetColor();
					}
				}
				this.mc.entityRenderer.setupCameraTransform(((EventRender3D) event).getPartialTicks(), 0);
				mc.gameSettings.viewBobbing = prevViewBobbing;
			}
		}
	}
}
