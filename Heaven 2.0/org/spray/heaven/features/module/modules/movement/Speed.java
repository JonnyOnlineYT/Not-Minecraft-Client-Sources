package org.spray.heaven.features.module.modules.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.util.BlockUtil;
import org.spray.heaven.util.MovementUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name = "Speed", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Speed extends Module {

	private final Setting mode = register(new Setting("Mode", "MatrixStrafe",
			new ArrayList<>(Arrays.asList("Sunrise Strafe", "Sunrise Damage",
					"MatrixStrafe", "Matrix Elytra", "Matrix New"))));



	// -2.9999999E7D 0.699999988079071D
	private double recX = 0, recZ = 0;
	private boolean jumped;
	private int ticks = 0;
	private float speedCounter;

	@Override
	public void onDisable() {
		mc.getTimer().reset();
		mc.getTimer().field_194148_c = 1.0f;
		mc.player.speedInAir = 0.02f;
		mc.player.jumpMovementFactor = 0.02f;
		ticks = 0;
		jumped = false;
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
		if (mc.player.isOnLadder())
			return;
		float yaw = MovementUtil.getDirection();

		switch (mode.getCurrentMode()) {

			case "MatrixStrafe":
				mc.player.setSprinting(false);
				mc.player.jumpMovementFactor = 0.0266f;
				if (!mc.player.onGround) {
					mc.gameSettings.keyBindJump.setPressed(mc.gameSettings.keyBindJump.isKeyDown());
					if (MovementUtil.getSqrtSpeed() < 0.218) {
						MovementUtil.strafe(0.218f);
						mc.player.jumpMovementFactor = 0.0269f;
					}
				}
				if (mc.player.motionY < 0) {
					mc.getTimer().setSpeed(1.09f);
					if (mc.player.fallDistance > 1.4)
						mc.getTimer().reset();
				} else {
					mc.getTimer().setSpeed(0.95f);
				}
				if (mc.player.onGround && MovementUtil.isMoving()) {
					mc.getTimer().setSpeed(1.08f);
					mc.gameSettings.keyBindJump.setPressed(true);
					// if(mc.player.movementInput.moveStrafe <= 0.01) {
					MovementUtil.strafe((MovementUtil.getSqrtSpeed() * 1.3371f));
					// }
				} else if (!MovementUtil.isMoving()) {
					mc.getTimer().reset();
				}

				if (MovementUtil.getSqrtSpeed() < 0.22)
					MovementUtil.strafe();
				break;


		case "Sunrise Damage":
			if (MovementUtil.isMoving()) {
				if (mc.player.onGround) {
					mc.player.addVelocity(-Math.sin((double) MovementUtil.getDirection()) * 10.2D / 25.5D, 0.0D,
							Math.cos((double) MovementUtil.getDirection()) * 10.2 / 25.5D);
					MovementUtil.strafe();
				} else if (mc.player.isInWater()) {
					mc.player.addVelocity(-Math.sin((double) MovementUtil.getDirection()) * 10.2D / 25.5D, 0.0D,
							Math.cos((double) MovementUtil.getDirection()) * 10.2D / 25.5D);
					MovementUtil.strafe();
				} else if (!mc.player.onGround) {
					mc.player.addVelocity(-Math.sin((double) MovementUtil.getDirection2()) * 0.11D / 25.5D, 0.0D,
							Math.cos((double) MovementUtil.getDirection2()) * 0.11D / 25.5D);
					MovementUtil.strafe();
				} else {
					mc.player.addVelocity(
							-Math.sin((double) MovementUtil.getDirection()) * 0.005D * (double) MovementUtil.getSpeed(),
							0.0D,
							Math.cos((double) MovementUtil.getDirection()) * 0.005D * (double) MovementUtil.getSpeed());
					MovementUtil.strafe();
				}
			}
			break;
		case "Sunrise Bhop":
			if (!MovementUtil.isMoving() || mc.player.getRidingEntity() != null || mc.player.hurtTime > 0)
				return;

			if (mc.player.onGround) {
				mc.player.jump();
				mc.player.motionY = 0.405;
				mc.player.motionX *= 1.004;
				mc.player.motionZ *= 1.004;
				return;
			}

			double speed = MovementUtil.getSpeed() * 1.0072;
			MovementUtil.strafe(speed);

			break;
		case "Sunrise Strafe":
			if (MovementUtil.isMoving()) {
				double motion = Math
						.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);

				if (mc.player.onGround) {
					mc.player.jump();
					mc.player.speedInAir = 0.0223f;
				} else
					MovementUtil.strafe(MovementUtil.calcMoveYaw(mc.player.rotationYaw), motion);

			} else {
				mc.player.motionX = 0.0;
				mc.player.motionZ = 0.0;
			}
			break;
		case "Matrix Elytra":
			if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInWater()) {
				return;
			}

			int eIndex = -1;
			int elytraCount = 0;

			for (int i = 0; i < 45; i++) {
				if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
					eIndex = i;
				}
				if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
					elytraCount++;
				}
			}

			if (elytraCount == 0 && eIndex == -1) {
				if (mc.player.getHeldItemOffhand().getItem() != Items.ELYTRA) {
					Wrapper.notify(ChatFormatting.RED + "Возьмите элитры в инвентарь!", Type.WARNING);
					disable();
					return;
				}
			}

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				return;
			}
			if (mc.player.ticksExisted % 18 == 0) {
				mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
				mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
				mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
				mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, mc.player);
			}
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				return;
			}
			if (mc.player.onGround) {
				mc.player.addVelocity(-Math.sin((double) MovementUtil.getDirection()) * 4.8D / 25.5D, 0.0D,
						Math.cos((double) MovementUtil.getDirection()) * 4.8 / 25.5D);
			}

			break;
		case "Matrix New":
			if (mc.player.isInWeb || mc.player.isOnLadder() || mc.player.isInWater()) {
				return;
			}

			if (mc.player.onGround && !mc.gameSettings.keyBindJump.isPressed()) {
				mc.player.jump();
			}
			if (mc.player.ticksExisted % 3 == 0) {
				mc.getTimer().setSpeed(1.3f);
			} else {
				mc.getTimer().setSpeed(1.f);
			}
			if (mc.player.motionY == -0.4448259643949201D
					&& mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.9D, mc.player.posZ))
							.getBlock() != Blocks.AIR) {
				mc.player.jumpMovementFactor = 0.05F;
				if (mc.player.ticksExisted % 2 == 0) {
					mc.player.motionX *= 2.D;
					mc.player.motionZ *= 2.D;
				} else {
					MovementUtil.setMotion(MovementUtil.getSqrtSpeed() * 1 + (0.22f));
				}
			}
			break;
		}
	}
}