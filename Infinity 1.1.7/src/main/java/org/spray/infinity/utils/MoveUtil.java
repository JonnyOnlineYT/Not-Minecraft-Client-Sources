package org.spray.infinity.utils;

import net.minecraft.util.math.Vec3d;

public class MoveUtil {

	private static final double diagonal = 1 / Math.sqrt(2);

	/**
	 * Checking player to move
	 * 
	 * @return
	 */
	public static boolean isMoving() {
		return (Helper.getPlayer().forwardSpeed != 0 || Helper.getPlayer().sidewaysSpeed != 0);
	}
	
	public static float getYaw(float yaw) {
		float moveYaw = yaw;
		float forward = 1.0F;
		if ((Helper.getPlayer()).forwardSpeed < 0.0F) {
			moveYaw += 180.0F;
			forward = -0.5F;
		}
		if ((Helper.getPlayer()).forwardSpeed > 0.0F)
			forward = 0.5F;
		if ((Helper.getPlayer()).sidewaysSpeed > 0.0F)
			moveYaw -= 90.0F * forward;
		if ((Helper.getPlayer()).sidewaysSpeed < 0.0F)
			moveYaw += 90.0F * forward;
		moveYaw = (float) Math.toRadians(moveYaw);
		
		float f = (float) (Helper.MC.options.mouseSensitivity * 0.6F + 0.2F);
		float gcd = f * f * f * 1.2F;

		moveYaw -= moveYaw % gcd;
		
		return moveYaw;
	}

	public static float getYaw() {
		return getYaw(Helper.getPlayer().getYaw());
	}

	public static double calcMoveYaw() {
		return calcMoveYaw(Helper.getPlayer().getYaw());
	}
	
	public static double calcMoveYaw(float targetYaw) {
		float moveForward = getRoundedForward();
		float moveString = getRoundedStrafing();
		float yawIn = targetYaw;
		float strafe = 90 * moveString;
		strafe *= (moveForward != 0.0F) ? (moveForward * 0.5F) : 1.0F;
		float yaw = yawIn - strafe;
		yaw -= ((moveForward < 0.0F) ? 180 : 0);
		yaw = (float) Math.toRadians(yaw);
		float sens = (float) (Helper.MC.options.mouseSensitivity);
		float f = (float) (sens * 0.6F + 0.2F);
		float gcd = f * f * f * 1.2F;
		
		yaw -= yaw % gcd;
		return yaw;
	}

	public static double getSpeed() {
		return Math.hypot(Helper.getPlayer().getVelocity().x, Helper.getPlayer().getVelocity().z);
	}

	public static void strafe(double yawL, double speed) {
		if (!isMoving())
			return;
		float yaw = (float) yawL;
		double x = -Math.sin(yaw) * speed;
		double z = Math.cos(yaw) * speed;
		setHVelocity(x, z);
	}

	public static void strafe(double speed) {
		strafe(getYaw(), speed);
	}

	public static float getRoundedForward() {
		return getRoundedMovementInput(Helper.getPlayer().input.movementForward);
	}

	public static float getRoundedStrafing() {
		return getRoundedMovementInput(Helper.getPlayer().input.movementSideways);
	}

	private static final float getRoundedMovementInput(float input) {
		return (input > 0.0F) ? 1.0F : ((input < 0.0F) ? -1.0F : 0.0F);
	}

	public static void getHorizontalVelocity(double bps, float targetYaw) {
		float yaw = targetYaw;

		Vec3d forward = Vec3d.fromPolar(0, yaw);
		Vec3d right = Vec3d.fromPolar(0, yaw + 90);
		double velX = 0;
		double velZ = 0;

		boolean a = false;
		if (Helper.getPlayer().input.pressingForward) {
			velX += forward.x / 20 * bps;
			velZ += forward.z / 20 * bps;
			a = true;
		}
		if (Helper.getPlayer().input.pressingBack) {
			velX -= forward.x / 20 * bps;
			velZ -= forward.z / 20 * bps;
			a = true;
		}

		boolean b = false;
		if (Helper.getPlayer().input.pressingRight) {
			velX += right.x / 20 * bps;
			velZ += right.z / 20 * bps;
			b = true;
		}
		if (Helper.getPlayer().input.pressingLeft) {
			velX -= right.x / 20 * bps;
			velZ -= right.z / 20 * bps;
			b = true;
		}

		if (a && b) {
			velX *= diagonal;
			velZ *= diagonal;
		}
		Helper.getPlayer().setVelocity(velX, Helper.getPlayer().getVelocity().getY(), velZ);

	}

	public static void hClip(double off) {
		double yaw = Math.toRadians(Helper.getPlayer().getYaw());
		Helper.getPlayer().updatePosition(Helper.getPlayer().getPos().x + (-Math.sin(yaw) * off), Helper.getPlayer().getPos().y,
				Helper.getPlayer().getPos().z + (Math.cos(yaw) * off));
	}
	
	public static void vClip(double y) {
		Helper.getPlayer().updatePosition(Helper.getPlayer().getPos().x, Helper.getPlayer().getPos().y + y,
				Helper.getPlayer().getPos().z);
	}

	public static void setHVelocity(double x, double z) {
		Helper.getPlayer().setVelocity(x, Helper.getPlayer().getVelocity().getY(), z);
	}

	public static void setYVelocity(double y) {
		Helper.getPlayer().setVelocity(Helper.getPlayer().getVelocity().getX(), y,
				Helper.getPlayer().getVelocity().getZ());
	}
}