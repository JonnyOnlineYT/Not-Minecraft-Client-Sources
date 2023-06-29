package org.spray.heaven.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.render.ColorUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "Trails", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class Trails extends Module {

	private final Setting players = register(new Setting("Show on Players", false));
	private final Setting friends = register(
			new Setting("Show on Friends", true).setVisible(() -> !players.isToggle()));
	private final Setting alpha = register(new Setting("Alpha", 0.84, 0, 1));
	private final Setting fadeTime = register(new Setting("Fade Time", 450, 0, 1000));

	private final Setting mode = register(
			new Setting("Color Mode", "Heaven", Arrays.asList("Heaven", "Static", "Rainbow", "Gradient")));

	private final Setting color = register(new Setting("Color", new Color(0xFFFC9513)).disableAlpha()
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Static")
					|| mode.getCurrentMode().equalsIgnoreCase("Gradient")));

	private final List<Lines> lines = new ArrayList<>();

	@Override
	public void onDisable() {
		synchronized (lines) {
			lines.clear();
		}
	}

	@EventTarget
	public void onWorldRender(WorldRenderEvent event) {
		int fTime = (int) ((400 + fadeTime.getValue()) / mc.getTimer().getSpeed());
		long fadeSec = System.currentTimeMillis() - fTime;
        CustomModels customModel = Wrapper.getModule().get("CustomModels");
        
		synchronized (lines) {
			if (players.isToggle() || friends.isToggle()) {
				for (EntityPlayer player : mc.world.playerEntities) {
					if (friends.isToggle() && !players.isToggle() && !Wrapper.getFriend().contains(player.getName()))
						continue;

					double[] ipos = EntityUtil.interpolate(player);
					double sin = -Math.sin(Math.toRadians(player.rotationYaw)) * -0.1;
					double cos = Math.cos(Math.toRadians(player.rotationYaw)) * -0.1;
					lines.add(new Lines(player, ipos[0] + sin, ipos[1] + 0.2, ipos[2] + cos,
							ColorUtil.applyOpacity(getColor(20, 50), (float) (alpha.getValue())),
							System.currentTimeMillis()));
				}
			}
			double partialTicks = Wrapper.MC.getRenderPartialTicks();
			double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTicks;
			double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTicks;
			double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTicks;

			double sin = -Math.sin(Math.toRadians(mc.player.rotationYaw)) * -0.1;
			double cos = Math.cos(Math.toRadians(mc.player.rotationYaw)) * -0.1;
			lines.add(new Lines(mc.player, x + sin, y + 0.2, z + cos,
					getColor(20, 50), System.currentTimeMillis()));

			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			mc.entityRenderer.disableLightmap();
			GL11.glLineWidth(1);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

			Collection<Lines> linesCollection = new ArrayList<>(lines);

			for (Lines line : linesCollection) {
		        boolean cm = customModel.isEnabled() && customModel.isValid(line.entity);
		        float cy = cm && customModel.mode.getCurrentMode().equalsIgnoreCase("Amogus") ? 0.56f : 0.2f;
		        
				double xCoord = line.getX() - mc.getRenderManager().getRenderPosX();
				double yCoord = line.getY() - mc.getRenderManager().getRenderPosY();
				double zCoord = line.getZ() - mc.getRenderManager().getRenderPosZ();

				Color color = new Color(line.getColor());

				float tc = (float) (line.getTime() - fadeSec) / (float) fTime;

				if (tc < 0 || tc > 1) {
					lines.remove(line);
					continue;
				}

				GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, tc * (float) (alpha.getValue()));
				GL11.glVertex3d(xCoord, yCoord, zCoord);
				GL11.glVertex3d(xCoord, yCoord + mc.player.height - cy, zCoord);
			}

			GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
			GL11.glEnd();

			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
	}

	private int getColor(int offset, int speed) {
		int color = -1;
		switch (mode.getCurrentMode()) {
		case "Heaven":
			color = ColorUtil.astolfoRainbow(offset * speed).getRGB();
			break;
		case "Static":
			color = this.color.getColor().getRGB();
			break;
		case "Rainbow":
			color = ColorUtil.rainbow(offset * speed, 20);
			break;
		case "Gradient":
			color = ColorUtil.fade(this.color.getColor(), offset * speed);
			break;
		}
		return color;
	}

	private class Lines {
		private Entity entity;
		private double x, y, z;
		private int color;
		private long time;

		public Lines(Entity entity, double x, double y, double z, int color, long time) {
			this.entity = entity;
			this.x = x;
			this.y = y;
			this.z = z;
			this.color = color;
			this.time = time;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getZ() {
			return z;
		}

		public int getColor() {
			return color;
		}

		public long getTime() {
			return time;
		}
	}
}