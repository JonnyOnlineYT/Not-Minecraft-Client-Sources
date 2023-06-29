package ru.wendoxd.modules.impl.visuals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import ru.wendoxd.WexSide;
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

public class JumpCircles extends Module {
	public static final List<Circle> circles = new ArrayList<>();

	@Override
	protected void initSettings() {}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D) {
			mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
			for (EntityPlayer player : mc.world.playerEntities) {
				CastHelper castHelper = new CastHelper();
				castHelper.apply(CastHelper.EntityType.PLAYERS);
				castHelper.apply(CastHelper.EntityType.FRIENDS);
				castHelper.apply(CastHelper.EntityType.SELF);
				CastHelper.EntityType type;
				if ((type = CastHelper.isInstanceof(player, castHelper.build())) != null) {
					CheckBox jumpcircles = visuals_entitiestab.get(type.ordinal()).jumpcircles;
					ColorShell color = jumpcircles.getColor();
					if (jumpcircles.isEnabled()) {
						GL11.glPushMatrix();
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glDisable(GL11.GL_ALPHA_TEST);
						GL11.glDisable(GL11.GL_CULL_FACE);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glShadeModel(GL11.GL_SMOOTH);

						for (Circle circle : player.circles) {
							GL11.glBegin(GL11.GL_QUAD_STRIP);
							for (int i = 0; i <= 360; i += 5) {
								float red = color.getRed() / 255f, green = color.getGreen() / 255f, blue = color.getBlue() / 255f;
								if (jumpcircles.getColor().isRainbow()) {
									int rainbow = PaletteHelper.rainbow(i / 360F);
									red = ((rainbow >> 16) & 255) / 255F;
									green = ((rainbow >> 8) & 255) / 255F;
									blue = (rainbow & 255) / 255F;
								}
								Vec3d pos = circle.pos();
								double x = Math.cos(Math.toRadians(i)) * WexSide.createAnimation((1 - circle.getAnimation(mc.getRenderPartialTicks()))) * 0.6;
								double z = Math.sin(Math.toRadians(i)) * WexSide.createAnimation((1 - circle.getAnimation(mc.getRenderPartialTicks()))) * 0.6;
								GL11.glColor4d(red, green, blue, (color.getAlpha() / 255f) * circle.getAnimation(mc.getRenderPartialTicks()));
								GL11.glVertex3d(pos.xCoord + x, pos.yCoord + 0.2f, pos.zCoord + z);
								GL11.glColor4d(red, green, blue, (color.getAlpha() / 255f) * 0.2 * circle.getAnimation(mc.getRenderPartialTicks()));
								GL11.glVertex3d(pos.xCoord + x * 1.4, pos.yCoord + 0.2f, pos.zCoord + z * 1.4);
							}
							GL11.glEnd();
						}
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_ALPHA_TEST);
						GL11.glShadeModel(GL11.GL_FLAT);
						GL11.glEnable(GL11.GL_CULL_FACE);
						GL11.glPopMatrix();
					}
				}
			}
		}
		if (event instanceof EventUpdate) {
			for (EntityPlayer player : mc.world.playerEntities) {
				player.circles.removeIf(Circle::update);
			}
		}
		if (event instanceof EventEntityMove) {
			EventEntityMove eem = (EventEntityMove) event;
			if (eem.ctx() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) eem.ctx();
				double motionY = eem.ctx().posY - eem.from().yCoord;
				if (!player.onGround && player.onGround != player.raycastGround && motionY > 0) {
					player.circles.add(new Circle(eem.from()));
				}
				player.raycastGround = player.onGround;
			}
		}
	}

	public static class Circle {
		private Vec3d vector;
		private int tick, prevTick;

		public Circle(Vec3d vector) {
			this.vector = vector;
			this.tick = 20;
			this.prevTick = tick;
		}

		public double getAnimation(float pt) {
			return (this.prevTick + (this.tick - this.prevTick) * pt) / 20F;
		}

		public boolean update() {
			prevTick = tick;
			return tick-- <= 0;
		}

		public Vec3d pos() {
			return new Vec3d(vector.xCoord - mc.getRenderManager().renderPosX, vector.yCoord - mc.getRenderManager().renderPosY, vector.zCoord - mc.getRenderManager().renderPosZ);
		}
	}
}
