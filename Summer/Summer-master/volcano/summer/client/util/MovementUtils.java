package volcano.summer.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import volcano.summer.client.events.EventMove;

public final class MovementUtils {

	public static final double Y_ON_GROUND_MAX = 0.0626;
	public static final double BUNNY_SLOPE = 0.66;
	public static final double WATCHDOG_BUNNY_SLOPE = BUNNY_SLOPE * 0.96;
	public static final double SPRINTING_MOD = 1.3;
	public static final double ICE_MOD = 2.5;
	private static final List<Double> frictionValues = new ArrayList<>();
	private static final double MIN_DIF = 1.0E-4;
	public static final double MAX_DIST = 2.15 - MIN_DIF;
	private static final double WALK_SPEED = 0.221;
	private static final double SWIM_MOD = 0.115D / WALK_SPEED;
	private static final double[] DEPTH_STRIDER_VALUES = { 1.0, 0.1645 / SWIM_MOD / WALK_SPEED,
			0.1995 / SWIM_MOD / WALK_SPEED, 1.0 / SWIM_MOD, };
	private static final double SNEAKING_MOD = 0.13 / WALK_SPEED;
	private static final double AIR_FRICTION = 0.98;
	private static final double WATER_FRICTION = 0.89;
	private static final double LAVA_FRICTION = 0.535;
	private static final double BUNNY_DIV_FRICTION = 160.0 - MIN_DIF;

	private MovementUtils() {
	}

	public static void fallDistDamage() {
		if (!isOnGround())
			return;

		EntityPlayerSP player = Wrapper.getPlayer();

		double jumpHeight = getJumpHeight();

		for (int i = 0; i < Math.ceil(getMinFallDist() / jumpHeight); i++) {
			Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + jumpHeight,
					player.posZ, false));
			Wrapper.sendPacketDirect(
					new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + 0.005f, player.posZ, false));
		}

		Wrapper.sendPacketDirect(new C03PacketPlayer(true));
	}

	public static boolean isInLiquid() {
		return Wrapper.getPlayer().isInWater() || Wrapper.getPlayer().isInLava();
	}

	public static double getJumpHeight() {
		double baseJumpHeight = 0.42f;
		if (isInLiquid()) {
			return WALK_SPEED * SWIM_MOD + 0.02;
		} else if (Wrapper.getPlayer().isPotionActive(Potion.jump)) {
			return baseJumpHeight + (Wrapper.getPlayer().getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
		}
		return baseJumpHeight;
	}

	public static double getMinFallDist() {
		double minDist = 3.0;
		if (Wrapper.getPlayer().isPotionActive(Potion.jump))
			minDist += Wrapper.getPlayer().getActivePotionEffect(Potion.jump).getAmplifier() + 1;
		return minDist;
	}

	public static double calculateFriction(double moveSpeed, double lastDist, double baseMoveSpeedRef) {
		frictionValues.clear();
		frictionValues.add(lastDist - (lastDist / BUNNY_DIV_FRICTION));
		frictionValues.add(lastDist - ((moveSpeed - lastDist) / 33.3));
		double materialFriction = Wrapper.getPlayer().isInWater() ? WATER_FRICTION
				: Wrapper.getPlayer().isInLava() ? LAVA_FRICTION : AIR_FRICTION;
		frictionValues.add(lastDist - (baseMoveSpeedRef * (1.0 - materialFriction)));
		Collections.sort(frictionValues);
		return frictionValues.get(0);
	}

	public static boolean isOnIce() {
		Block blockUnder = getBlockUnder();
		return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
	}

	public static Block getBlockUnder() {
		return Wrapper.getWorld().getBlockState(new BlockPos(Wrapper.getPlayer().posX,
				Wrapper.getPlayer().posY - getBlockHeight(), Wrapper.getPlayer().posZ)).getBlock();
	}

	public static double getBlockHeight() {
		return Wrapper.getPlayer().posY - (int) Wrapper.getPlayer().posY;
	}

	public static double getBaseMoveSpeed() {
		final EntityPlayerSP player = Wrapper.getPlayer();
		double base = player.isSprinting() ? WALK_SPEED * SPRINTING_MOD
				: player.isSneaking() ? WALK_SPEED * SNEAKING_MOD : WALK_SPEED;
		if (player.isPotionActive(Potion.moveSpeed)) {
			base *= 1.0 + 0.2 * (player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}
		if (player.isInWater()) {
			base *= SWIM_MOD;
			final int depthStriderLevel = InvUtils.getDepthStriderLevel();
			if (depthStriderLevel > 0) {
				base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
			}
		} else if (player.isInLava()) {
			base *= SWIM_MOD;
		}
		return base;
	}

	public static void setSpeed(EventMove e, double speed) {
		final EntityPlayerSP player = Wrapper.getPlayer();
		float forward = player.moveForward;
		float strafing = player.moveStrafing;
		float yaw = player.rotationYaw;
		boolean reversed = forward < 0.0f;
		float strafingYaw = 90.0f * (forward > 0.0f ? 0.5f : reversed ? -0.5f : 1.0f);

		if (reversed)
			yaw += 180.0f;
		if (strafing > 0.0f)
			yaw -= strafingYaw;
		else if (strafing < 0.0f)
			yaw += strafingYaw;

		double x = Math.cos(Math.toRadians(yaw + 90.0f));
		double z = Math.cos(Math.toRadians(yaw));

		e.setX(x * speed);
		e.setZ(z * speed);
	}

	public static boolean isOnGround() {
//	        List<AxisAlignedBB> collidingList = Wrapper.getWorld().getCollidingBoundingBoxes(Wrapper.getPlayer(), Wrapper.getPlayer().getEntityBoundingBox().offset(0.0, -(0.01 - MIN_DIF), 0.0));
//	        return collidingList.size() > 0;
		return Wrapper.getPlayer().onGround && Wrapper.getPlayer().isCollidedVertically;
	}

	public static boolean isMoving() {
		final GameSettings gameSettings = Wrapper.getGameSettings();
		return !(Wrapper.getCurrentScreen() instanceof GuiChat)
				&& (Keyboard.isKeyDown(gameSettings.keyBindForward.getKeyCode())
						|| Keyboard.isKeyDown(gameSettings.keyBindLeft.getKeyCode())
						|| Keyboard.isKeyDown(gameSettings.keyBindRight.getKeyCode())
						|| Keyboard.isKeyDown(gameSettings.keyBindBack.getKeyCode()));
	}
}
