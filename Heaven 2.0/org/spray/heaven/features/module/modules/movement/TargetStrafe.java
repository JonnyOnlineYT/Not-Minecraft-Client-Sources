package org.spray.heaven.features.module.modules.movement;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.combat.KillAura;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.RotationUtil;
import org.spray.heaven.util.render.ColorUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(name = "TargetStrafe", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class TargetStrafe extends Module {

	private Setting killauraOnly = register(new Setting("Only with KillAura", true));
	private Setting players = register(new Setting("Players", true));
	private Setting friends = register(new Setting("Friends", false).setVisible(() -> players.isToggle()));
	private Setting invisibles = register(new Setting("Invisibles", false));
	private Setting mobs = register(new Setting("Mobs", true));
	private Setting animals = register(new Setting("Animals", true));
//	private Setting ignoreNaked = register(new Setting("Ignore Naked", false));

	private Setting autoJump = register(new Setting("Auto Jump", true));

	private Setting distance = register(new Setting("Distance", 6D, 0D, 15D));

	private Setting radius = register(new Setting("Strafing radius", 1.9D, 0.0D, 6.0D));
	private Setting speed = register(new Setting("Speed", 0.22D, 0.0D, 1.0D));

	private Setting damageBoost = register(new Setting("Damage Boost", false));
	private Setting boost = register(
			new Setting("Boost Value", 0.09D, 0.0D, 0.8D).setVisible(() -> damageBoost.isToggle()));

	private int direction = 1;
	private Entity target;

	@Override
	public void onUpdate() {
		setSuffix(String.valueOf(MathUtil.round(speed.getValue(), 2)));

		KillAura killaura = ((KillAura) Wrapper.getModule().get("KillAura"));

		if (killauraOnly.isToggle()) {
			if (!killaura.isEnabled() || killaura.getTarget() == null)
				return;
		}

		target = killaura.getTarget() != null ? killaura.getTarget()
				: EntityUtil.getTarget(distance.getValue(), 360, players.isToggle(), friends.isToggle(), true,
						invisibles.isToggle(), mobs.isToggle(), animals.isToggle(), true);

		if (target == null)
			return;

		float yaw = (float) (RotationUtil.lookAtEntity(target)[0] + MathUtil.random(-0.5, 0.5));

		if (killaura.isEnabled() && killaura.getTarget() != null)
			yaw = killaura.getYaw();

		if (mc.player.onGround) {
			mc.player.motionX = 0.0;
			mc.player.motionZ = 0.0;
			yaw = RotationUtil.lookAtEntity(target)[0];
			if (autoJump.isToggle())
				mc.player.motionY = 0.4199999D;
		}
		mc.player.jumpTicks = 0;
		if (!mc.player.onGround) {
			mc.player.motionY -= 0.00110;
		}

		direction = mc.gameSettings.keyBindLeft.isPressed() ? 1
				: mc.gameSettings.keyBindRight.isPressed() ? -1 : direction;

		if (mc.player.isCollidedHorizontally)
			direction = direction == -1 ? 1 : -1;

		double speed = damageBoost.isToggle() && mc.player.hurtTime != 0 ? this.speed.getValue() + boost.getValue()
				: this.speed.getValue();

		float dst = mc.player.getDistanceToEntity(target);
		double maxDst = radius.getValue();
		float forward = 0f;
		if (dst <= (maxDst - 0.3)) {
			forward = -1;
		} else if (dst > (maxDst)) {
			forward = 1;
		}

		strafe(yaw, speed, forward, direction);
	}

	private void strafe(float yaw, double speed, double forward, double direction) {
		if (forward != 0.0D) {
			if (direction > 0.0D) {
				yaw += (float) (forward > 0.0D ? -45 : 45);
			} else if (direction < 0.0D) {
				yaw += (float) (forward > 0.0D ? 45 : -45);
			}
			direction = 0.0D;
			if (forward > 0.0D) {
				forward = 1.0D;
			} else if (forward < 0.0D) {
				forward = -1.0D;
			}
		}

		double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
		double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
		double x = forward * speed * cos + direction * speed * sin;
		double z = forward * speed * sin - direction * speed * cos;

		mc.player.motionX = x;
		// mc.player.motionY -= 0.002;
		mc.player.motionZ = z;
	}

	private void drawCircle(double x, double y, double z, double radius, float width) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glLineWidth(width);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int i = 0; i <= 361; i++) {
			ColorUtil.glColor(ColorUtil.skyRainbow(i * 20, 4));
			GL11.glVertex3d(x + radius * Math.cos(i * MathHelper.PI2 / 361), y,
					z + radius * Math.sin(i * MathHelper.PI2 / 361));
		}
		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		GlStateManager.resetColor();
	}

	public Entity getTarget() {
		return target;
	}

}
