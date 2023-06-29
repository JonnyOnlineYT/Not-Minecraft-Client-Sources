package ru.wendoxd.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.events.impl.misc.EventPreMove;
import ru.wendoxd.events.impl.player.EventEntityStrafe;

import java.util.Objects;

public class MoveUtils {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static boolean isMoving() {
		return mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindRight.pressed
				|| mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindBack.pressed;
	}

	public static int getSpeedEffect() {
		if (mc.player.isPotionActive(MobEffects.SPEED)) {
			return Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
		}
		return 0;
	}

	public static double getBaseSpeed() {
		double baseSpeed = 0.2873;
		if (mc.player.isPotionActive(Potion.getPotionById(1))) {
			int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
		}
		return baseSpeed;
	}

	public static double getSpeed() {
		return Math.sqrt(Math.pow(mc.player.motionX, 2) + Math.pow(mc.player.motionZ, 2));
	}

	public static float getDirection() {
		float rotationYaw = mc.player.rotationYaw;

		float factor = 0f;

		if (mc.player.movementInput.moveForward > 0)
			factor = 1;
		if (mc.player.movementInput.moveForward < 0)
			factor = -1;

		if (factor == 0) {
			if (mc.player.movementInput.moveStrafe > 0)
				rotationYaw -= 90;

			if (mc.player.movementInput.moveStrafe < 0)
				rotationYaw += 90;
		} else {
			if (mc.player.movementInput.moveStrafe > 0)
				rotationYaw -= 45 * factor;

			if (mc.player.movementInput.moveStrafe < 0)
				rotationYaw += 45 * factor;
		}

		if (factor < 0)
			rotationYaw -= 180;

		return (float) Math.toRadians(rotationYaw);
	}

	public static boolean isBlockAboveHead() {
		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(),
				mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5),
				mc.player.posZ - 0.3);
		return !mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
	}

	public static void setSpeed(float speed) {
		float yaw = mc.player.rotationYaw;
		float forward = mc.player.movementInput.moveForward;
		float strafe = mc.player.movementInput.moveStrafe;
		if (forward != 0) {
			if (strafe > 0) {
				yaw += (forward > 0 ? -45 : 45);
			} else if (strafe < 0) {
				yaw += (forward > 0 ? 45 : -45);
			}
			strafe = 0;
			if (forward > 0) {
				forward = 1;
			} else if (forward < 0) {
				forward = -1;
			}
		}
		mc.player.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90))
				+ strafe * speed * Math.sin(Math.toRadians(yaw + 90)));
		mc.player.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90))
				- strafe * speed * Math.cos(Math.toRadians(yaw + 90)));
	}

	public static void setEventSpeed(EventPreMove move, float speed) {
		float yaw = mc.player.rotationYaw;
		float forward = mc.player.movementInput.moveForward;
		float strafe = mc.player.movementInput.moveStrafe;
		if (forward != 0) {
			if (strafe > 0) {
				yaw += (forward > 0 ? -45 : 45);
			} else if (strafe < 0) {
				yaw += (forward > 0 ? 45 : -45);
			}
			strafe = 0;
			if (forward > 0) {
				forward = 1;
			} else if (forward < 0) {
				forward = -1;
			}
		}
		move.setX((forward * speed * Math.cos(Math.toRadians(yaw + 90))
				+ strafe * speed * Math.sin(Math.toRadians(yaw + 90))));
		move.setZ((forward * speed * Math.sin(Math.toRadians(yaw + 90))
				- strafe * speed * Math.cos(Math.toRadians(yaw + 90))));
	}

	public static double[] getSpeed(double speed) {
		float yaw = mc.player.rotationYaw;
		float forward = mc.player.movementInput.moveForward;
		float strafe = mc.player.movementInput.moveStrafe;
		if (forward != 0) {
			if (strafe > 0) {
				yaw += (forward > 0 ? -45 : 45);
			} else if (strafe < 0) {
				yaw += (forward > 0 ? 45 : -45);
			}
			strafe = 0;
			if (forward > 0) {
				forward = 1;
			} else if (forward < 0) {
				forward = -1;
			}
		}
		return new double[] {
				(forward * speed * Math.cos(Math.toRadians(yaw + 90))
						+ strafe * speed * Math.sin(Math.toRadians(yaw + 90))),
				(forward * speed * Math.sin(Math.toRadians(yaw + 90))
						- strafe * speed * Math.cos(Math.toRadians(yaw + 90))),
				yaw };
	}

	public static void calculateMove(EventEntityStrafe event, float yaw) {
		float strafe = event.getStrafe();
		float forward = event.getForward();
		float friction = event.getFriction();
		int difference = (int) ((MathHelper.wrapDegrees(mc.player.rotationYaw - yaw - 23.5F - 135) + 180) / 45);
		float calcForward = 0;
		float calcStrafe = 0;
		switch (difference) {
		case 0:
			calcForward = forward;
			calcStrafe = strafe;
			break;
		case 1:
			calcForward += forward;
			calcStrafe -= forward;
			calcForward += strafe;
			calcStrafe += strafe;
			break;
		case 2:
			calcForward = strafe;
			calcStrafe = -forward;
			break;
		case 3:
			calcForward -= forward;
			calcStrafe -= forward;
			calcForward += strafe;
			calcStrafe -= strafe;
			break;
		case 4:
			calcForward = -forward;
			calcStrafe = -strafe;
			break;
		case 5:
			calcForward -= forward;
			calcStrafe += forward;
			calcForward -= strafe;
			calcStrafe -= strafe;
			break;
		case 6:
			calcForward = -strafe;
			calcStrafe = forward;
			break;
		case 7:
			calcForward += forward;
			calcStrafe += forward;
			calcForward -= strafe;
			calcStrafe += strafe;
			break;
		}
		if (calcForward > 1 || calcForward < 0.9F && calcForward > 0.3F || calcForward < -1
				|| calcForward > -0.9F && calcForward < -0.3F) {
			calcForward *= 0.5F;
		}
		if (calcStrafe > 1 || calcStrafe < 0.9F && calcStrafe > 0.3F || calcStrafe < -1
				|| calcStrafe > -0.9F && calcStrafe < -0.3F) {
			calcStrafe *= 0.5F;
		}
		float dist = calcStrafe * calcStrafe + calcForward * calcForward;
		if (dist >= 1E-4F) {
			dist = (float) (friction / Math.max(1, Math.sqrt(dist)));
			calcStrafe *= dist;
			calcForward *= dist;
			float yawSin = MathHelper.sin((float) (yaw * Math.PI / 180));
			float yawCos = MathHelper.cos((float) (yaw * Math.PI / 180));
			mc.player.motionX += calcStrafe * yawCos - calcForward * yawSin;
			mc.player.motionZ += calcForward * yawCos + calcStrafe * yawSin;
		}
	}

	public static void setStrafe(double speed) {
		if (!MoveUtils.isMoving()) {
			return;
		}
		double yaw = getDirection();
		mc.player.motionX = -Math.sin(yaw) * speed;
		mc.player.motionZ = Math.cos(yaw) * speed;
	}

	public static boolean isInLiquid() {
		return mc.player.isInWater() || mc.player.isInLava();
	}
}
