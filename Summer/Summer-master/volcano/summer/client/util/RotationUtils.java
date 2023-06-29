package volcano.summer.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import volcano.summer.base.manager.module.Module;

public class RotationUtils {

	private static Minecraft mc;
	public static float yaw;
	public static float pitch;
	private static boolean lookChanged = false;
	private static int notSet = 0;
	private static Module setBy;

	public static float getYawChangeToEntity(final Entity entity) {
		final double deltaX = entity.posX - mc.thePlayer.posX;
		final double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double yawToEntity;
		if (deltaZ < 0.0 && deltaX < 0.0) {
			yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else if (deltaZ < 0.0 && deltaX > 0.0) {
			yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		}
		return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
	}

	public static void faceVectorPacketInstant(Vec3 vec) {
		float[] rotations = RotationUtils.getNeededRotations(vec);
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
				rotations[0], rotations[1], Minecraft.getMinecraft().thePlayer.onGround));
	}

	public static float[] getNeededRotations(Vec3 vec) {
		Vec3 eyesPos = RotationUtils.getEyesPos();
		double diffX = (vec.xCoord - eyesPos.xCoord) + 0.5D;
		double diffY = (vec.yCoord - eyesPos.yCoord) + 0.5D;
		double diffZ = (vec.zCoord - eyesPos.zCoord) + 0.5D;
		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
		float pitch = (float) (-(Math.atan2(diffY, diffXZ) * 180.0 / Math.PI));
		return new float[] { MathHelper.wrapAngleTo180_float(yaw),
				Minecraft.getMinecraft().gameSettings.keyBindJump.pressed ? 90.0f
						: MathHelper.wrapAngleTo180_float(pitch) };
	}

	public static Vec3 getEyesPos() {
		return new Vec3(mc.getMinecraft().thePlayer.posX,
				mc.getMinecraft().thePlayer.posY + (double) mc.getMinecraft().thePlayer.getEyeHeight(),
				mc.getMinecraft().thePlayer.posZ);
	}

	public static float getPitchChangeToEntity(final Entity entity) {
		final double deltaX = entity.posX - mc.thePlayer.posX;
		final double deltaZ = entity.posZ - mc.thePlayer.posZ;
		final double deltaY = entity.posY - 1.6 + entity.getEyeHeight() - mc.thePlayer.posY;
		final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
	}

	public static float[] getAngles(final Entity e) {
		return new float[] { getYawChangeToEntity(e) + mc.thePlayer.rotationYaw,
				getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch };
	}

	public static float[] getRotation(Entity entity) {
		double x = entity.posX - mc.thePlayer.posX;
		double z = entity.posZ - mc.thePlayer.posZ;
		double y;
		if ((entity instanceof EntityEnderman)) {
			y = entity.posY - mc.thePlayer.posY;
		} else {
			y = entity.posY + (entity.getEyeHeight() - 1.9D) - mc.thePlayer.posY + (mc.thePlayer.getEyeHeight() - 1.9D);
		}
		double helper = MathHelper.sqrt_double(x * x + z * z);

		float newYaw = (float) Math.toDegrees(-Math.atan(x / z));
		float newPitch = (float) -Math.toDegrees(Math.atan(y / helper));
		if ((z < 0.0D) && (x < 0.0D)) {
			newYaw = (float) (90.0D + Math.toDegrees(Math.atan(z / x)));
		} else if ((z < 0.0D) && (x > 0.0D)) {
			newYaw = (float) (-90.0D + Math.toDegrees(Math.atan(z / x)));
		}
		return new float[] { newPitch, newYaw };
	}

	public static float[] getRotations(Entity ent) {
		double x = ent.posX;
		double z = ent.posZ;
		double y = ent.posY + ent.getEyeHeight() / 5.0F;
		return getRotationFromPosition(x, z, y);
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.6D;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { yaw, pitch };
	}

	public static float getNewAngle(float angle) {
		angle %= 360.0F;
		if (angle >= 180.0F) {
			angle -= 360.0F;
		}
		if (angle < -180.0F) {
			angle += 360.0F;
		}
		return angle;
	}

	public static float getDistanceBetweenAngles(float angle1, float angle2) {
		float angle = Math.abs(angle1 - angle2) % 360.0F;
		if (angle > 180.0F) {
			angle = 360.0F - angle;
		}
		return angle;
	}

	public static float[] rotations(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { yaw, pitch };
	}

	public static float getDistanceFromMouseSafe(EntityLivingBase entity) {
		float[] neededRotations = getRotationsNeeded(entity);
		if (neededRotations != null) {
			float neededYaw = Minecraft.getMinecraft().thePlayer.rotationYaw - neededRotations[0];
			float neededPitch = Minecraft.getMinecraft().thePlayer.rotationPitch - neededRotations[1] + 8.0F;

			float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
			if ((neededYaw > -40.0F) && (neededYaw < 40.0F) && (neededPitch > -5.0F) && (neededPitch < 40.0F)) {
				return 999999.0F;
			}
		}
		return -1.0F;
	}

	public static float[] getRotationsNeeded(Entity entity) {
		if (entity == null) {
			return null;
		}
		double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double diffY;
		if ((entity instanceof EntityLivingBase)) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9D
					- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D
					- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		}
		double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	public static void set(Module setter, float _yaw, float _pitch) {
		if (!Double.isNaN(_yaw) && !Double.isNaN(_pitch)) {
			yaw = _yaw;

			for (pitch = _pitch; pitch < -90.0F; pitch += 180.0F) {
				;
			}

			while (pitch > 90.0F) {
				pitch -= 180.0F;
			}

			lookChanged = true;
			notSet = 0;
			setBy = setter;
		}
	}
}