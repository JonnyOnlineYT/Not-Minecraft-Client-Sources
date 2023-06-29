package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.misc.EventEntityMove;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.events.impl.render.EventRender3D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.utils.combat.CastHelper;
import ru.wendoxd.utils.visual.ColorShell;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

import java.util.ArrayList;
import java.util.List;

public class Trails extends Module {

	private final List<Trail> trailList = new ArrayList<>();

	@Override
	protected void initSettings() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D) {
			for (EntityPlayer entity : mc.world.playerEntities) {
				if (entity instanceof EntityPlayerSP && mc.gameSettings.thirdPersonView == 0) {
					continue;
				}
				CastHelper castHelper = new CastHelper();
				castHelper.apply(CastHelper.EntityType.PLAYERS);
				castHelper.apply(CastHelper.EntityType.FRIENDS);
				castHelper.apply(CastHelper.EntityType.SELF);
				CastHelper.EntityType type;
				if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null) {
					CheckBox trails = visuals_entitiestab.get(type.ordinal()).trails;
					ColorShell color = trails.getColor();
					float alpha = color.getAlpha() / 255f;
					if (trails.isEnabled()) {
						mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
						if (entity.trails.size() > 0) {
							GL11.glPushMatrix();
							GL11.glEnable(GL11.GL_BLEND);
							GL11.glDisable(GL11.GL_ALPHA_TEST);
							GL11.glDisable(GL11.GL_CULL_FACE);
							GL11.glDisable(GL11.GL_TEXTURE_2D);
							GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
							GL11.glShadeModel(GL11.GL_SMOOTH);
							GL11.glEnable(GL11.GL_LINE_SMOOTH);
							GL11.glBegin(GL11.GL_QUAD_STRIP);
							for (int i = 0; i < entity.trails.size(); i++) {
								Vec3d c = entity.trails.get(i).color(mc.getRenderPartialTicks());
								Trail ctx = entity.trails.get(i);
								Vec3d pos = ctx.interpolate(mc.getRenderPartialTicks());
								GL11.glColor4d(c.xCoord, c.yCoord, c.zCoord,
										alpha * ctx.animation(mc.getRenderPartialTicks()));
								GL11.glVertex3d(pos.xCoord, pos.yCoord, pos.zCoord);
								GL11.glVertex3d(pos.xCoord, pos.yCoord + Module.getEyeHeight(entity), pos.zCoord);
							}
							GL11.glEnd();
							GL11.glLineWidth(1f);
							GL11.glBegin(GL11.GL_LINE_STRIP);
							for (int i = 0; i < entity.trails.size(); i++) {
								Vec3d c = entity.trails.get(i).color(mc.getRenderPartialTicks());
								Trail ctx = entity.trails.get(i);
								Vec3d pos = ctx.interpolate(mc.getRenderPartialTicks());
								GL11.glColor4d(c.xCoord, c.yCoord, c.zCoord,
										(alpha + 0.15f) * ctx.animation(mc.getRenderPartialTicks()));
								GL11.glVertex3d(pos.xCoord, pos.yCoord + Module.getEyeHeight(entity), pos.zCoord);
							}
							GL11.glEnd();
							GL11.glBegin(GL11.GL_LINE_STRIP);
							for (int i = 0; i < entity.trails.size(); i++) {
								Vec3d c = entity.trails.get(i).color(mc.getRenderPartialTicks());
								Trail ctx = entity.trails.get(i);
								Vec3d pos = ctx.interpolate(mc.getRenderPartialTicks());
								GL11.glColor4d(c.xCoord, c.yCoord, c.zCoord,
										(alpha + 0.15f) * ctx.animation(mc.getRenderPartialTicks()));
								GL11.glVertex3d(pos.xCoord, pos.yCoord, pos.zCoord);
							}
							GL11.glEnd();
							GL11.glDisable(GL11.GL_LINE_SMOOTH);
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glEnable(GL11.GL_ALPHA_TEST);
							GL11.glShadeModel(GL11.GL_FLAT);
							GL11.glEnable(GL11.GL_CULL_FACE);
							GL11.glPopMatrix();
						}
						GlStateManager.resetColor();
					}
				}
			}
		}
		if (event instanceof EventUpdate) {
			for (EntityPlayer player : mc.world.playerEntities) {
				player.trails.removeIf(Trail::update);
			}
		}
		if (event instanceof EventEntityMove) {
			try {
				EventEntityMove eem = (EventEntityMove) event;
				CastHelper castHelper = new CastHelper();
				castHelper.apply(CastHelper.EntityType.PLAYERS);
				castHelper.apply(CastHelper.EntityType.FRIENDS);
				castHelper.apply(CastHelper.EntityType.SELF);
				CastHelper.EntityType type;
				if ((type = CastHelper.isInstanceof(eem.ctx(), castHelper.build())) != null) {
					CheckBox trails = visuals_entitiestab.get(type.ordinal()).trails;
					ColorShell color = trails.getColor();
					float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f;
					eem.ctx().trails.add(new Trail(eem.from(), eem.ctx().getPositionVector(),
							color.isRainbow() ? null : new Vec3d(red, green, blue)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class Trail {
		private Vec3d from, to, color;
		private int ticks, prevTicks;

		public Trail(Vec3d from, Vec3d to, Vec3d color) {
			this.from = from;
			this.to = to;
			this.ticks = 10;
			this.color = color;
		}

		public Vec3d interpolate(float pt) {
			double x = from.xCoord + ((to.xCoord - from.xCoord) * pt) - mc.getRenderManager().renderPosX;
			double y = from.yCoord + ((to.yCoord - from.yCoord) * pt) - mc.getRenderManager().renderPosY;
			double z = from.zCoord + ((to.zCoord - from.zCoord) * pt) - mc.getRenderManager().renderPosZ;
			return new Vec3d(x, y, z);
		}

		public double animation(float pt) {
			return (this.prevTicks + (this.ticks - this.prevTicks) * pt) / 10.;
		}

		public boolean update() {
			this.prevTicks = this.ticks;
			return this.ticks-- <= 0;
		}

		public Vec3d color(float pt) {
			if (color == null) {
				int c1 = PaletteHelper.rainbow(((prevTicks + (ticks - prevTicks) * pt) / 100f));
				float red = ((c1 >> 16) & 255) / 255f;
				float green = ((c1 >> 8) & 255) / 255f;
				float blue = (c1 & 255) / 255f;
				return new Vec3d(red, green, blue);
			} else {
				return color;
			}
		}
	}
}
