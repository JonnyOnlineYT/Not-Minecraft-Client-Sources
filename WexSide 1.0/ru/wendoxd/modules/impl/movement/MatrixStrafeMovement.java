package ru.wendoxd.modules.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.impl.player.EventAction;
import ru.wendoxd.events.impl.player.EventMove;

public class MatrixStrafeMovement {
	public static double oldSpeed, contextFriction;
	public static boolean needSwap, prevSprint;
	public static int counter, noSlowTicks;

	public static double calculateSpeed(EventMove move, boolean ely, double speed) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean fromGround = mc.player.onGround;
		boolean toGround = move.toGround();
		boolean jump = move.motion().yCoord > 0;
		float speedAttributes = getAIMoveSpeed(mc.player);
		final float frictionFactor = getFrictionFactor(mc.player, move);
		float n6 = 0.91f;
		if (fromGround) {
			n6 = frictionFactor;
		}
		final float n7 = 0.16277136f / (n6 * n6 * n6);
		float n8;
		if (fromGround) {
			n8 = speedAttributes * n7;
			if (jump) {
				n8 += 0.2f;
			}
		} else {
			n8 = 0.0255f;
		}
		boolean noslow = false;
		double max2 = oldSpeed + n8;
		double max = 0.0;
		if (mc.player.isHandActive() && !jump) {
			double n10 = oldSpeed + n8 * 0.5 + 0.004999999888241291;
			double motionY2 = move.motion().yCoord;
			if (motionY2 != 0.0 && Math.abs(motionY2) < 0.08) {
				n10 += 0.055;
			}
			if (max2 > (max = Math.max(0.043, n10))) {
				noslow = true;
				++noSlowTicks;
			} else {
				noSlowTicks = Math.max(noSlowTicks - 1, 0);
			}
		} else {
			noSlowTicks = 0;
		}
		if (noSlowTicks > 3) {
			max2 = max - 0.019;
		} else {
			max2 = Math.max(noslow ? 0 : 0.25, max2) - (counter++ % 2 == 0 ? 0.001 : 0.002);
		}
		contextFriction = n6;
		if (!toGround && !fromGround) {
			needSwap = true;
		} else {
			prevSprint = false;
		}
		if (!fromGround && !toGround) {
			Speed.needSprintState = !mc.player.serverSprintState;
		}
		if (toGround && fromGround) {
			Speed.needSprintState = false;
		}
		return max2 + (ely ? speed : 0);
	}

	public static void postMove(double horizontal) {
		oldSpeed = horizontal * contextFriction;
	}

	public static float getAIMoveSpeed(EntityPlayer contextPlayer) {
		boolean prevSprinting = contextPlayer.isSprinting();
		contextPlayer.setSprinting(false);
		float speed = contextPlayer.getAIMoveSpeed() * 1.3f;
		contextPlayer.setSprinting(prevSprinting);
		return speed;
	}

	public static void actionEvent(EventAction eventAction) {
		if (needSwap) {
			eventAction.setSprintState(!Minecraft.getMinecraft().player.serverSprintState);
			needSwap = false;
		}
	}

	private static float getFrictionFactor(EntityPlayer contextPlayer, EventMove move) {
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos
				.retain(move.from().xCoord, move.getAABBFrom().minY - 1.0D, move.from().zCoord);
		return contextPlayer.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
	}
}
