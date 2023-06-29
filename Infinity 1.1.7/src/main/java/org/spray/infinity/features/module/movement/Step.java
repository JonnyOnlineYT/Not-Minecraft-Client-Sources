package org.spray.infinity.features.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.event.TickEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;
import org.spray.infinity.utils.block.BlockUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@ModuleInfo(category = Category.MOVEMENT, desc = "Let you walk up Blocks very fast", key = -2, name = "Step", visible = true)
public class Step extends Module {

	private Setting mode = new Setting(this, "Mode", "Matrix 6.1.0",
			new ArrayList<>(Arrays.asList("Vanilla", "Intave", "Matrix 6.1.0", "NCP")));

	private Setting height = new Setting(this, "Height", 1.5, 0.5, 10.0)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Vanilla"));

	private double previousX, previousY, previousZ;
	private double offsetX, offsetY, offsetZ;
	private byte cancelStage;
	private boolean hasStep;

	// ncp
	public static boolean cancelSomePackets;
	private int stepTicks;

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		Infinity.resetTimer();
		Helper.getPlayer().stepHeight = 0.6f;
	}

	@EventTarget
	public void onTick(TickEvent event) {
		setSuffix(mode.getCurrentMode());

		if (mode.getCurrentMode().equalsIgnoreCase("Intave")) {

			EntityUtil.setStepHeight(1.02f);

			if (hasStep) {
				MoveUtil.getHorizontalVelocity(MoveUtil.getSpeed(), (float) MoveUtil.calcMoveYaw());
				Infinity.TIMER = 0.9f + Helper.getPlayer().age % 4 / 21f;
			}

			if (Helper.getPlayer().isOnGround()) {
				Infinity.resetTimer();
				hasStep = false;
			}

			if (Helper.getPlayer().horizontalCollision && Helper.getPlayer().isOnGround()) {
				float y = (float) Math.max(0.5, 0.4);
				hasStep = true;
				Infinity.TIMER = y;
				MoveUtil.setYVelocity(0.45);
				Helper.getPlayer().tick();
				Helper.getPlayer().tick();
			}
		} else if (mode.getCurrentMode().equalsIgnoreCase("AAC 5.0.14")) {
			if (hasStep) {
				Infinity.TIMER = 0.9f + Helper.getPlayer().age % 4 / 20;
				MoveUtil.setYVelocity(Helper.getPlayer().getVelocity().getY() - 0.007);
				Helper.getPlayer().setBoundingBox(Helper.getPlayer().getBoundingBox().offset(0, 0, 0));
			}

			if (Helper.getPlayer().isOnGround()) {
				hasStep = false;
				Infinity.resetTimer();
			}
			if (Helper.getPlayer().horizontalCollision && MoveUtil.isMoving() && Helper.getPlayer().isOnGround()) {
				if (!BlockUtil.isTouchWall(Helper.getPlayer().getBoundingBox().offset(0, 1, 0))) {
					hasStep = true;
					MoveUtil.setYVelocity(Helper.getPlayer().getVelocity().getY() + 0.47);
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		if (mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.0")) {
			if (Helper.MC.options.keyJump.isPressed()
					|| Infinity.getModuleManager().get(TargetStrafe.class).isEnabled()
							&& TargetStrafe.target != null)
				EntityUtil.setStepHeight(0.6f);
			else
				EntityUtil.setStepHeight(1.4f);

		} else if (mode.getCurrentMode().equalsIgnoreCase("Vanilla")) {
			EntityUtil.setStepHeight((float) height.getCurrentValueDouble());

		} else if (mode.getCurrentMode().equalsIgnoreCase("NCP")) {
			if (Helper.getPlayer().horizontalCollision) {
				cancelSomePackets = true;
				Helper.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Helper.getPlayer().getX(),
						Helper.getPlayer().getY() + (.41999998688698 * (1 + Helper.getPlayer().stepHeight)),
						Helper.getPlayer().getZ(), Helper.getPlayer().isOnGround()));
				Helper.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Helper.getPlayer().getX(),
						Helper.getPlayer().getY() + (.7531999805212 * (1 + Helper.getPlayer().stepHeight)),
						Helper.getPlayer().getZ(), Helper.getPlayer().isOnGround()));
			} else {
				Infinity.resetTimer();
			}
		}
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {
			if (mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.0")) {
				if (Helper.MC.options.keyJump.isPressed())
					return;

				offsetX = 0;
				offsetY = 0;
				offsetZ = 0;

				if (cancelStage == -1) {
					cancelStage = 0;
					return;
				}

				double yDist = Helper.getPlayer().getY() - previousY;
				double hDistSq = (Helper.getPlayer().getX() - previousX) * (Helper.getPlayer().getX() - previousX)
						+ (Helper.getPlayer().getZ() - previousZ) * (Helper.getPlayer().getZ() - previousZ);

				if (yDist > 0.5 && yDist < 1.05 && hDistSq < 1 && cancelStage == 0) {
					Helper.sendPacket(
							new PlayerMoveC2SPacket.PositionAndOnGround(previousX, previousY + 0.42, previousZ, false));
					offsetX = previousX - Helper.getPlayer().getX();
					offsetY = 0.755 - yDist;
					offsetZ = previousZ - Helper.getPlayer().getZ();

					Helper.getPlayer().stepHeight = 1.05F;
					cancelStage = 1;
				}

				switch (cancelStage) {
				case 1:
					cancelStage = 2;
					break;
				case 2:
					event.cancel();
					cancelStage = -1;
					break;
				}

				previousX = Helper.getPlayer().getX();
				previousY = Helper.getPlayer().getY();
				previousZ = Helper.getPlayer().getZ();

				if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
					MoveUtil.setHVelocity(Helper.getPlayer().getVelocity().getX() + offsetX,
							Helper.getPlayer().getVelocity().getZ() + offsetZ);
					Helper.getPlayer().setBoundingBox(Helper.getPlayer().getBoundingBox().offset(0, 0, 0));
				}

			} else {
				if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
					MoveUtil.setHVelocity(Helper.getPlayer().getVelocity().getX() - offsetX,
							Helper.getPlayer().getVelocity().getZ() - offsetZ);
					Helper.getPlayer().setBoundingBox(Helper.getPlayer().getBoundingBox().offset(0, -offsetY, 0));
				}
			}
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {

			if (mode.getCurrentMode().equalsIgnoreCase("NCP")) {
				if (Helper.getPlayer() != null && Helper.getPlayer().isOnGround()
						|| (Helper.getPlayer().verticalCollision))
					Helper.getPlayer().stepHeight = 1F;

				if (cancelSomePackets) {
					Infinity.resetTimer();
					this.stepTicks++;
				}
				if (this.stepTicks >= 3) {
					Infinity.TIMER = 0.35F;
					cancelSomePackets = false;
					this.stepTicks = 0;
				}
			}
		}
	}

}
