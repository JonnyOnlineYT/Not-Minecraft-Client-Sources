package org.spray.heaven.features.module.modules.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.awt.Color;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.render.ColorUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(name = "ChinaHat", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class ChinaHat extends Module {

	private final Setting firstPerson = register(new Setting("Show in first person", true));
	private final Setting players = register(new Setting("Show on Players", false));
	private final Setting friends = register(
			new Setting("Show on Friends", true).setVisible(() -> !players.isToggle()));

	private final Setting colorMode = register(new Setting("Color Mode", "Heaven",
			Arrays.asList("Heaven", "Light Rainbow", "Rainbow", "Fade", "Double Color", "Analogous")));
	private final Setting colorSpeed = register(new Setting("Color Speed", 4.5, 2, 30));
	private final Setting color = register(new Setting("Color", Colors.CLIENT_COLOR)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Fade")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Double Color")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));

	private final Setting analogColor = register(new Setting("Analogous Color", Colors.CLIENT_COLOR)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));

	@EventTarget
	public void onWorldRender(WorldRenderEvent event) {
		drawHat(mc.player);

		if (friends.isToggle() || players.isToggle())
			for (EntityPlayer player : mc.world.playerEntities) {
				if (friends.isToggle() && !players.isToggle() && !Wrapper.getFriend().contains(player.getName()))
					continue;

				drawHat(player);
			}
	}

	private void drawHat(EntityPlayer entity) {
		if (entity == null || entity.world == null || entity.isInvisible() || entity.isDead)
			return;

		if (entity == mc.player && !firstPerson.isToggle() && mc.gameSettings.thirdPersonView == 0)
			return;

		double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks()
				- mc.getRenderManager().getRenderPosX(),
				posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()
						- mc.getRenderManager().getRenderPosY(),
				posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks()
						- mc.getRenderManager().getRenderPosZ();

		CustomModels customModel = Wrapper.getModule().get("CustomModels");
		boolean cm = customModel.isEnabled() && customModel.isValid(entity);
		boolean fp = mc.gameSettings.thirdPersonView == 0;
		boolean helmet = !cm && !entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
		float expandY = cm && !fp && customModel.mode.getCurrentMode().equalsIgnoreCase("Amogus") ? -0.28f
				: cm && !fp && customModel.mode.getCurrentMode().equalsIgnoreCase("Demon") ? 0.2f
						: helmet ? 0.1f : 0.02f;
		AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
		double height = axisalignedbb.maxY - axisalignedbb.minY + expandY,
				radius = axisalignedbb.maxX - axisalignedbb.minX;

		glPushMatrix();
		GlStateManager.disableCull();
		glDepthMask(false);
		glDisable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_BLEND);
		GlStateManager.disableLighting();
		GlStateManager.color(1, 1, 1, 1);
		OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

		Double yaw = MathUtil.interpolate(entity.prevRotationYaw, entity.rotationYaw, mc.getRenderPartialTicks());
		Double pitchInterpolate = MathUtil.interpolate(entity.prevRotationPitch, entity.rotationPitch,
				mc.getRenderPartialTicks());

		glTranslated(posX, posY, posZ);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glRotated(yaw, 0, -1, 0);
		glRotated(pitchInterpolate / 3.0, 0, 0, 0);
		glTranslatef((float) 0, (float) 0, (float) (pitchInterpolate / 270.0F));
		glLineWidth(2);
		glBegin(GL_LINE_LOOP);

		for (int i = 0; i <= 180; i++) {
			int color1 = ColorUtil.applyOpacity(getColor(i * 4), 0.5f).getRGB();
			GlStateManager.color(1, 1, 1, 1);
			ColorUtil.glColor(color1);
			glVertex3d(Math.sin(i * MathHelper.PI2 / 90) * radius,
					height - (entity.isSneaking() && !cm ? 0.23 : 0) - 0.002,
					Math.cos(i * MathHelper.PI2 / 90) * radius);
		}
		glEnd();

		glBegin(GL_TRIANGLE_FAN);
		int color12 = ColorUtil.applyOpacity(getColor(4), 0.7f).getRGB();
		ColorUtil.glColor(color12);
		glVertex3d(0, height + 0.3 - (entity.isSneaking() && !cm ? 0.23 : 0), 0);

		// draw hat
		for (int i = 0; i <= 180; i++) {
			int color1 = ColorUtil.applyOpacity(getColor(i * 4), 0.2f).getRGB();
			GlStateManager.color(1, 1, 1, 1);
			ColorUtil.glColor(color1);
			glVertex3d(Math.sin(i * MathHelper.PI2 / 90) * radius, height - (entity.isSneaking() && !cm ? 0.23F : 0),
					Math.cos(i * MathHelper.PI2 / 90) * radius);

		}
		glVertex3d(0, height + 0.3 - (entity.isSneaking() && !cm ? 0.23 : 0), 0);
		glEnd();

		glPopMatrix();

		GL11.glColor4f(1f, 1f, 1f, 1f);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_FLAT);
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
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
}
