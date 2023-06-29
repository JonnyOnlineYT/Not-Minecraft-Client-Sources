package org.spray.heaven.features.module.modules.movement;

import java.util.Arrays;
import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.BlockUtil;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "WaterLeave", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class WaterLeave extends Module {

	private Setting mode = register(new Setting("Mode", "Matrix", Arrays.asList("Matrix", "Old")));
	private Setting height = register(new Setting("Height", 6, 3, 20));
	private Setting damage = register(new Setting("Damage", true));

	private boolean jumped;

	@Override
	public void onDisable() {
		mc.getTimer().reset();
		jumped = false;
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (mode.getCurrentMode().equalsIgnoreCase("Old")) {
			if (event.getType().equals(EventType.POST)) {
				if (EntityUtil.isFluid(0.1)) {
					mc.player.fallDistance = 0.0f;
					mc.player.motionX = 0.0;
					mc.player.motionZ = 0.0;
					mc.player.motionY = 0.1f;
					// mc.player.jumpMovementFactor = 0.01f;
					jumped = false;
				} else if (EntityUtil.isFluid(-0.95) && !jumped) {
					if (damage.isToggle())
						EntityUtil.damage();

					mc.getConnection().sendPacket(
							new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
					mc.player.motionY = height.getValue();
					// mc.getConnection().sendPacket(new CPacketInput(0, 0, true, false));
					// mc.getConnection().sendPacket(new CPacketVehicleMove(mc.player));
					mc.getConnection().sendPacket(new CPacketPlayer(true));
					// mc.player.onGround = true;

					jumped = true;
					// mc.getTimer().setSpeed(0.84234f);
				} else
					mc.getTimer().reset();

			} else if (event.getType().equals(EventType.PRE)) {

				if (jumped) {
					// if (mc.player.ticksExisted % 6 == 0)
					// mc.getConnection().sendPacket(new CPacketVehicleMove(mc.player));
					// mc.player.onGround = true;
					mc.getConnection().sendPacket(new CPacketInput(0, 0, true, false));
					if (mc.player.motionY <= 1 + height.getValue() * 0.2) {
						mc.player.motionX = 0;
						mc.player.motionZ = 0;
						mc.player.motionY = 0;
						event.cancel();
					}
				}
			}
		}
		if (mode.getCurrentMode().equalsIgnoreCase("Matrix")) {
			BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ);
			Block block = mc.world.getBlockState(blockPos).getBlock();
			if (block instanceof BlockLiquid) {
				if (BlockUtil.collideBlock(mc.player.getEntityBoundingBox(), 0, Block::isLiquid)
						&& mc.player.isInsideOfMaterial(Material.AIR)) {
					mc.player.motionY = 0.08;
				}
				if (mc.player.fallDistance > 0 && mc.player.ticksExisted % 2 == 0 && mc.player.motionY < 0) {
					mc.player.motionY = height.getValue();
				}
				if (MovementUtil.isMoving()) {
					mc.player.motionX = 0;
					mc.player.motionZ = 0;
				}
			}
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (mode.getCurrentMode().equalsIgnoreCase("Old")) {
			if (event.getType().equals(EventType.SEND)) {
				if (jumped)
					if (event.getPacket() instanceof CPacketPlayer) {
						mc.getConnection().sendPacket(new CPacketInput());
						CPacketPlayer packetPlayer = (CPacketPlayer) event.getPacket();

						packetPlayer.setY(packetPlayer.getY() + 7.0E-9D);
					}
			}
		}
	}

}
