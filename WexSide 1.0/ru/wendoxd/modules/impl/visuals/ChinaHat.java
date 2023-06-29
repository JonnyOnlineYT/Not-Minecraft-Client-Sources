package ru.wendoxd.modules.impl.visuals;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRender3D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.utils.combat.CastHelper;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class ChinaHat extends Module {
	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D) {
			for (Entity entity : mc.world.loadedEntityList) {
				if (entity instanceof EntityLivingBase) {
					CastHelper castHelper = new CastHelper();
					castHelper.apply(CastHelper.EntityType.PLAYERS);
					castHelper.apply(CastHelper.EntityType.MOBS);
					castHelper.apply(CastHelper.EntityType.ANIMALS);
					castHelper.apply(CastHelper.EntityType.VILLAGERS);
					castHelper.apply(CastHelper.EntityType.FRIENDS);
					castHelper.apply(CastHelper.EntityType.SELF);
					mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
					CastHelper.EntityType type;
					if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null) {
						CheckBox color = visuals_entitiestab.get(type.ordinal()).chinahat;
						if (color.isEnabled()) {
							if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) {
								continue;
							}
							GL11.glPushMatrix();
							double x = entity.lastTickPosX
									+ (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks()
									- mc.getRenderManager().renderPosX;
							double y = entity.lastTickPosY
									+ (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()
									- mc.getRenderManager().renderPosY + Module.getEyeHeight((EntityLivingBase) entity);
							double z = entity.lastTickPosZ
									+ (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks()
									- mc.getRenderManager().renderPosZ;
							GL11.glTranslated(x, y, z);
							GL11.glDisable(GL11.GL_DEPTH_TEST);
							GL11.glEnable(GL11.GL_BLEND);
							GL11.glDisable(GL11.GL_ALPHA_TEST);
							GL11.glDisable(GL11.GL_CULL_FACE);
							GL11.glDisable(GL11.GL_TEXTURE_2D);
							OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
									GL11.GL_ZERO);
							GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
							GL11.glShadeModel(GL11.GL_SMOOTH);
							int len = 361;
							float[] r = new float[len];
							float[] g = new float[len];
							float[] b = new float[len];
							for (int i = 0; i < len; i++) {
								int c = color.getColor().build();
								if (color.getColor().isRainbow()) {
									c = PaletteHelper.rainbow(i / 360f);
								}
								r[i] = ((c >> 16) & 255) / 255f;
								g[i] = ((c >> 8) & 255) / 255f;
								b[i] = (c & 255) / 255f;
							}
							GL11.glEnable(GL11.GL_LINE_SMOOTH);
							GL11.glLineWidth(1.5f);
							int start = (int) mc.getRenderManager().playerViewY;
							GL11.glBegin(GL11.GL_LINE_STRIP);
							for (int i = start; i <= start + len - 1; i += 1) {
								GL11.glColor4f(r[i - start], g[i - start], b[i - start],
										Math.min(1, color.getColor().getAlpha() / 255f + 0.2f));
								double cos = Math.cos(Math.toRadians(i)) * entity.width * 1.1F;
								double sin = Math.sin(Math.toRadians(i)) * entity.width * 1.1F;
								GL11.glVertex3d(cos, 0, sin);
							}
							GL11.glEnd();
							GL11.glBegin(GL11.GL_QUAD_STRIP);
							for (int i = start; i <= start + len - 1; i += 1) {
								GL11.glColor4f(r[i - start], g[i - start], b[i - start],
										Math.min(1, color.getColor().getAlpha() / 255f));
								double cos = Math.cos(Math.toRadians(i)) * entity.width * 1.1F;
								double sin = Math.sin(Math.toRadians(i)) * entity.width * 1.1F;
								GL11.glVertex3d(cos, 0, sin);
								GL11.glVertex3d(0, entity.height / 6, 0);
							}
							GL11.glEnd();
							GL11.glDisable(GL11.GL_LINE_SMOOTH);
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glEnable(GL11.GL_DEPTH_TEST);
							GL11.glEnable(GL11.GL_ALPHA_TEST);
							GL11.glShadeModel(GL11.GL_FLAT);
							GL11.glEnable(GL11.GL_CULL_FACE);
							GL11.glPopMatrix();
							GlStateManager.resetColor();
						}
					}
				}
			}
		}
	}

	public static float interpolate(final float old, final float now, final float progress) {
		return old + (now - old) * progress;
	}

}
