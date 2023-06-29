package org.spray.heaven.util;

import net.minecraft.util.math.AxisAlignedBB;
import org.spray.heaven.main.Wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MovementUtil {

	public static boolean isMoving() {
		return (Wrapper.getPlayer().movementInput.moveForward != 0
				|| Wrapper.getPlayer().movementInput.moveStrafe != 0);
	}

	public static boolean isMotion() {
		return Wrapper.getPlayer().motionX != 0 || Wrapper.getPlayer().motionZ != 0;
	}

	public static float getDirection() {
		double forward = Wrapper.getPlayer().movementInput.moveForward;
		double strafe = Wrapper.getPlayer().movementInput.moveStrafe;
		float yaw = Wrapper.getPlayer().rotationYaw;
		if (forward < 0f)
			yaw += 180f;

		float forwardS = 1f;
		if (forward < 0f)
			forwardS = -0.5f;
		else if (forward > 0f)
			forwardS = 0.5f;

		if (strafe > 0)
			yaw -= 90 * forwardS;

		if (strafe < 0)
			yaw += 90 * forwardS;

		return (float) Math.toRadians(yaw);
	}

	public static boolean isBlockAboveHead() {
		Minecraft mc = Minecraft.getMinecraft();
		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5), mc.player.posZ - 0.3);
		return mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
	}
	public static float getDirection2() {
		Minecraft mc = Minecraft.getMinecraft();
		float var1 = mc.player.rotationYaw;
		if (mc.player.moveForward < 0.0F) {
			var1 += 180.0F;
		}

		float forward = 1.0F;
		if (mc.player.moveForward < 0.0F) {
			forward = -50.5F;
		} else if (mc.player.moveForward > 0.0F) {
			forward = 50.5F;
		}

		if (mc.player.moveStrafing > 0.0F) {
			var1 -= 22.0F * forward;
		}

		if (mc.player.moveStrafing < 0.0F) {
			var1 += 22.0F * forward;
		}

		return var1 *= 0.017453292F;
	}

	public static float calcMoveYaw(float targetYaw) {
		float moveForward = getRoundedForward();
		float moveString = getRoundedStrafing();
		float yawIn = targetYaw;
		float strafe = 90 * moveString;
		strafe *= (moveForward != 0.0F) ? (moveForward * 0.5F) : 1.0F;
		float yaw = yawIn - strafe;
		yaw -= ((moveForward < 0.0F) ? 180 : 0);
		yaw = (float) Math.toRadians(yaw);

		yaw = RotationUtil.getFixedRotation(yaw);

		return yaw;
	}

	public static void strafe() {
		strafe(getSqrtSpeed());
	}

	public static void strafe(double speed) {
		strafe(getDirection(), speed);
	}

	public static void strafe(float targetYaw, double speed) {
		if (isMoving())
			forward(targetYaw, speed);
	}

	// public static float getMaxFallDist() {
	// PotionEffect potioneffect =
	// mc.player.getActivePotionEffect(Potion.REGISTRY.getObject(new
	// ResourceLocation("jump")));
	// int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
	// return (float)(mc.player.getMaxFallHeight() + f);
	// }
	public static void forward(float targetYaw, double speed) {
		double x = -Math.sin(targetYaw) * speed;
		double z = Math.cos(targetYaw) * speed;
		Wrapper.getPlayer().motionX = x;
		Wrapper.getPlayer().motionZ = z;
	}

	public static double getSpeed() {
		return Math.hypot(Wrapper.getPlayer().motionX, Wrapper.getPlayer().motionZ);
	}

	public static double getSqrtSpeed() {
		return Math.sqrt(Wrapper.getPlayer().motionX * Wrapper.getPlayer().motionX
				+ Wrapper.getPlayer().motionZ * Wrapper.getPlayer().motionZ);
	}

	public static float getRoundedForward() {
		return getRoundedMovementInput(Wrapper.getPlayer().movementInput.moveForward);
	}

	public static float getRoundedStrafing() {
		return getRoundedMovementInput(Wrapper.getPlayer().movementInput.moveStrafe);
	}

	private static final float getRoundedMovementInput(float input) {
		return (input > 0.0F) ? 1.0F : ((input < 0.0F) ? -1.0F : 0.0F);
	}

	public static void hClip(double off) {
		double yaw = Math.toRadians(Wrapper.getPlayer().rotationYaw);
		Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX + (-Math.sin(yaw) * off), Wrapper.getPlayer().posY,
				Wrapper.getPlayer().posZ + (Math.cos(yaw) * off));
	}

	public static void vClip(double y) {
		Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + y,
				Wrapper.getPlayer().posZ);
	}

	public static void teleport(double x, double y, double z) {
		Wrapper.MC.getConnection().sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), Action.STOP_SNEAKING));

//		Wrapper.MC.getConnection().sendPacket(new CPacketPlayer.Position(Wrapper.getPlayer().posX,
//				1.7976931348623157E+1, Wrapper.getPlayer().posZ, true));

		PathUtil.findBlinkPath(x, y, z).forEach(vector3d -> {

			MovementUtil.blinkToPosFromPos(
					new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ),
					new Vec3d(vector3d.xCoord, vector3d.yCoord, vector3d.zCoord), 100);
		});
		Wrapper.MC.getConnection().sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), Action.START_SNEAKING));
	}

	public static void setMotion(double speed) {
		double forward = Wrapper.getPlayer().movementInput.moveForward;
		double strafe = Wrapper.getPlayer().movementInput.moveStrafe;
		float yaw = Wrapper.getPlayer().rotationYaw;
		if (forward == 0.0D && strafe == 0.0D) {
			Wrapper.getPlayer().motionX = 0.0D;
			Wrapper.getPlayer().motionZ = 0.0D;
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (float) (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (float) (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}
			double sin = Math.sin(Math.toRadians(yaw + 90.0F));
			double cos = Math.cos(Math.toRadians(yaw + 90.0F));
			Wrapper.getPlayer().motionX = forward * speed * cos + strafe * speed * sin;
			Wrapper.getPlayer().motionZ = forward * speed * sin - strafe * speed * cos;
		}
	}

	public static float getSpeed(Entity entity) {
		final double deltaX = entity.posX - entity.prevPosX;
		final double deltaZ = entity.posZ - entity.prevPosZ;
		float distance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);

		return (float) Math.floor((distance / 1000.0f) / (0.05f / 3600.0f));
	}

	public static void blinkToPosFromPos(Vec3d src, Vec3d dest, double maxTP) {
		double range = 0;
		double xDist = src.xCoord - dest.xCoord;
		double yDist = src.yCoord - dest.yCoord;
		double zDist = src.zCoord - dest.zCoord;
		double x1 = src.xCoord;
		double y1 = src.yCoord;
		double z1 = src.zCoord;
		double x2 = dest.xCoord;
		double y2 = dest.yCoord;
		double z2 = dest.zCoord;
		range = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
		double step = maxTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			if (maxTP * steps > range) {
				break;
			}
		}
		for (int i = 0; i < steps; i++) {
			double difX = x1 - x2;
			double difY = y1 - y2;
			double difZ = z1 - z2;
			double divider = step * i;
			double x = x1 - difX * divider;
			double y = y1 - difY * divider;
			double z = z1 - difZ * divider;

			Wrapper.sendPacket(new CPacketPlayer.Position(x, y, z, true));
			Wrapper.getPlayer().setPosition(x, y, z);
		}
		Wrapper.sendPacket(new CPacketPlayer.Position(x2, y2, z2, true));
		Wrapper.getPlayer().setPosition(x2, y2, z2);
	}

}
