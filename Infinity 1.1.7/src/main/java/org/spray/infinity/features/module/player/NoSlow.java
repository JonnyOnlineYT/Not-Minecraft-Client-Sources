package org.spray.infinity.features.module.player;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.event.SlowDownEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@ModuleInfo(category = Category.PLAYER, desc = "Do not slow down when using / eating", key = -2, name = "NoSlow", visible = true)
public class NoSlow extends Module {

	public Setting mode = new Setting(this, "Mode", "Vanilla",
			new ArrayList<>(Arrays.asList("Vanilla", "NCP", "Matrix 6.1.0")));

	private Setting packet = new Setting(this, "Packet", true)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Vanilla"));

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
	}

	@EventTarget
	public void onTickMove(SlowDownEvent event) {
		if (mode.getCurrentMode().equalsIgnoreCase("Vanilla")
				|| isEnabled() && mode.getCurrentMode().equalsIgnoreCase("NCP")) {
			if (Helper.getPlayer().isUsingItem() && !Helper.getPlayer().isRiding()) {
				event.getInput().movementForward *= 5;
				event.getInput().movementSideways *= 5;
			}
		} else if (mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.0")) {
			if (Helper.getPlayer().isUsingItem() && !Helper.getPlayer().isRiding()) {
				if (Helper.getPlayer().isOnGround()) {
					event.getInput().movementForward *= 2.55;
					event.getInput().movementSideways *= 2.55;
				} else if (!Helper.getPlayer().isOnGround()) {
					event.getInput().movementForward *= 4.35;
					event.getInput().movementSideways *= 4.35;
				}
			}
		}
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {

		if (mode.getCurrentMode().equalsIgnoreCase("NCP")) {
			if (Helper.getPlayer().isUsingItem() && !Helper.getPlayer().hasVehicle()) {
				if (!Helper.getPlayer().isOnGround())
					return;
				double d = Math.abs(Helper.getPlayer().getVelocity().y);
				if (d < 0.1D) {
					double e = 0.4D + d * 0.72D;
					Helper.getPlayer().setVelocity(Helper.getPlayer().getVelocity().multiply(e, 1.0D, e));
				}
			}
		} else if (mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.0")) {
			if (Helper.getPlayer().isUsingItem() && !Helper.getPlayer().hasVehicle()) {

				if (Helper.getPlayer().isOnGround()) {
					MoveUtil.strafe(MoveUtil.calcMoveYaw(), MoveUtil.getSpeed());

					if (Helper.getPlayer().age % 3 == 0) {

						MoveUtil.strafe(MoveUtil.calcMoveYaw(), 0.85);

						MoveUtil.strafe(MoveUtil.calcMoveYaw(), 0.139);
					}
				}
			}
		}
	}

	@EventTarget
	public void onSend(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {

			if (mode.getCurrentMode().equalsIgnoreCase("Vanilla") && packet.isToggle()
					|| mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.0")) {
				if (event.getPacket() instanceof PlayerMoveC2SPacket) {
					if (Helper.getPlayer().isUsingItem() && !Helper.getPlayer().hasVehicle()) {
						Helper.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK,
								new BlockPos(-1, -1, -1), Direction.DOWN));
					}
				}
			}
		}
	}
}
