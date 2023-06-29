package org.spray.heaven.features.module.modules.movement;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MovementUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.client.CPacketPlayer;

@ModuleInfo(name = "NoSlowdown", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class NoSlowdown extends Module {

	private final Setting mode = register(new Setting("Mode", "Matrix", Arrays.asList("Matrix", "Matrix New")));

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType() == EventType.SEND) {
			if (mode.getCurrentMode().equalsIgnoreCase("Matrix New")) {
				if (event.getPacket() instanceof CPacketPlayer) {
					CPacketPlayer cp = (CPacketPlayer) event.getPacket();

					if (mc.player.isHandActive() && MovementUtil.isMoving()
							&& !mc.gameSettings.keyBindJump.isPressed()) {
						cp.setY(mc.player.ticksExisted % 2 == 0 ? cp.getY() + 0.0006 : cp.getY() + 0.0002);
						cp.setOnGround(false);
						mc.player.onGround = false;
					}
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

		if (mode.getCurrentMode().equalsIgnoreCase("Matrix")) {
			if (mc.player.isHandActive() && MovementUtil.isMoving()) {
				if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
					if (mc.player.ticksExisted % 2 == 0) {
						mc.player.motionX *= 0.35;
						mc.player.motionZ *= 0.35;
					}
				} else if (mc.player.fallDistance > 0.2) {
					mc.player.motionX *= 0.9100000262260437;
					mc.player.motionZ *= 0.9100000262260437;
				}
			}
		}
	}

}
