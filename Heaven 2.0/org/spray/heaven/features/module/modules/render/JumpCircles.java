package org.spray.heaven.features.module.modules.render;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.events.JumpEvent;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "JumpCircles", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class JumpCircles extends Module {

	public final Setting colorMode = register(new Setting("Color Mode", "Analogous",
			Arrays.asList("Heaven", "Light Rainbow", "Rainbow", "Fade", "Double Color", "Analogous")));
	public final Setting colorSpeed = register(new Setting("Color Speed", 12, 2, 30));
	public final Setting color = register(new Setting("Color", Colors.CLIENT_COLOR)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Fade")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Double Color")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));
	
	public final Setting analogColor = register(new Setting("Analogous Color", Colors.CLIENT_COLOR)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));

	private final Setting mode = register(new Setting("Mode", "Default", Arrays.asList("Medusa", "Default")));
	private final Setting jumpMode = register(new Setting("JumpMode", "Jump", Arrays.asList("Jump", "Collide")));
	private final Setting delay = register(new Setting("Delay (ms)", 2400, 300, 3000));

	private final List<Circle> circles = Lists.newArrayList();
	private boolean jumped;

	@Override
	public void onDisable() {
		circles.clear();
		jumped = false;
	}

	@EventTarget
	public void onUpdate() {
		if (jumpMode.getCurrentMode().equalsIgnoreCase("Collide")) {
			if (!mc.player.isCollidedVertically)
				jumped = true;

			if (jumped && mc.player.onGround && mc.player.isCollidedVertically) {
				circles.add(new Circle(mc.player.getPositionVector(), getColor(0),
						new DecelerateAnimation((int) delay.getValue(), 1)));
				jumped = false;
			}
		}
			circles.removeIf(Circle::update);

	}

	@EventTarget
	public void onJump(JumpEvent e) {
		if (jumpMode.getCurrentMode().equalsIgnoreCase("Jump")) {
			circles.add(new Circle(mc.player.getPositionVector(), getColor(0),
					new DecelerateAnimation((int) delay.getValue(), 1)));
		}
	}
	@EventTarget
	public void onRender(WorldRenderEvent event) {
		if (circles.isEmpty())
			return;

		EntityPlayerSP player = mc.player;
		int delay = (int) this.delay.getValue();
		Minecraft mc = Minecraft.getMinecraft();
		double ix = -(player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks());
		double iy = -(player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks());
		double iz = -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks());
		if (mode.getCurrentMode().equalsIgnoreCase("Medusa")) {
			GL11.glPushMatrix();
			GL11.glTranslated(ix, iy, iz);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			Collections.reverse(circles);
			try {
				for (Circle c : circles) {
					float k = (float) c.animation.getTimePassed() / delay;
					double x = c.getPosition().xCoord;
					double y = c.getPosition().yCoord - (k * 0.5) + 0.2;
					double z = c.getPosition().zCoord;
					float end = k + 1f - k;
					GL11.glBegin(GL11.GL_QUAD_STRIP);
					for (int i = 0; i <= 360; i = i + 5) {
						Color color = getColor(i);
						
						GL11.glColor4f((float) color.getRed() / 255F, (float) color.getGreen() / 255F,
								(float) color.getBlue() / 255F,
								0.2f * (1 - ((float) c.animation.getTimePassed() / delay)));
						GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * k, y,
								z + Math.sin(Math.toRadians(i * 4)) * k);
						GL11.glColor4f(1, 1, 1, 0.01f * (1 - ((float) c.animation.getTimePassed() / delay)));
						GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin(k * 8) * 0.5,
								z + Math.sin(Math.toRadians(i) * end));
					}
					GL11.glEnd();
				}
			} catch (Exception ignored) {
			}
			Collections.reverse(circles);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1f, 1f, 1f, 1f);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glPopMatrix();
		} else if (mode.getCurrentMode().equalsIgnoreCase("Default")) {
			GL11.glPushMatrix();
			GL11.glTranslated(ix, iy, iz);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			Collections.reverse(circles);
			for (Circle c : circles) {
				double x = c.getPosition().xCoord;
				double y = c.getPosition().yCoord + 0.1;
				double z = c.getPosition().zCoord;
				float k = (float) c.animation.getTimePassed() / delay;
				float start = k * 2.5f;
				float end = start + 1f - k;
				GL11.glBegin(GL11.GL_QUAD_STRIP);
				for (int i = 0; i <= 360; i = i + 5) {
					Color color = getColor(i);
					
					GL11.glColor4f((float) color.getRed() / 255F, (float) color.getGreen() / 255F,
							(float) color.getBlue() / 255F,
							0.7f * (1 - ((float) c.animation.getTimePassed() / delay)));
					GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y,
							z + Math.sin(Math.toRadians(i)) * start);
					GL11.glColor4f((float) c.getColor().getRed() / 255F, (float) c.getColor().getGreen() / 255F,
							(float) c.getColor().getBlue() / 255F,
							0.01f * (1 - ((float) c.animation.getTimePassed() / delay)));
					GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y, z + Math.sin(Math.toRadians(i)) * end);
				}
				GL11.glEnd();
			}
			Collections.reverse(circles);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glColor4f(1f, 1f, 1f, 1f);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glPopMatrix();
		}
	}

	private Color getColor(int count) {
		int index = (int) (count);
		switch (colorMode.getCurrentMode()) {
		case "Heaven":
			return ColorUtil.skyRainbow((int) colorSpeed.getValue(), index);

		case "Light Rainbow":
			return ColorUtil.rainbow((int) colorSpeed.getValue(), index, .6f, 1, 1);

		case "Rainbow":
			return ColorUtil.rainbow((int) colorSpeed.getValue(), index, 1f, 1, 1);

		case "Fade":
			return ColorUtil.fade((int) colorSpeed.getValue(), index, color.getColor(), 1);

		case "Double Color":
			return ColorUtil.interpolateColorsBackAndForth((int) colorSpeed.getValue(), index, color.getColor(),
					Colors.ALTERNATE_COLOR, true);

		case "Analogous":
			int val = 1;
			Color analogous = ColorUtil.getAnalogousColor(analogColor.getColor())[val];
			return ColorUtil.interpolateColorsBackAndForth((int) colorSpeed.getValue(), index, color.getColor(),
					analogous, true);
		default:
			return color.getColor();
		}
	}

	static class Circle {
		private final Vec3d vec;
		private final Color color;
		public final Animation animation;

		Circle(Vec3d vec, Color color, Animation anim) {
			this.vec = vec;
			this.color = color;
			this.animation = anim;
		}

		public Vec3d getPosition() {
			return this.vec;
		}

		public Color getColor() {
			return this.color;
		}

		public boolean update() {
			return animation.finished(Direction.FORWARDS);
		}
	}
}