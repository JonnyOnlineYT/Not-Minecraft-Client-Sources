package ru.wendoxd.modules.impl.movement;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.events.impl.player.EventAction;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class LongJump extends Module {
	public static long lastVelocityTime;
	public static double velocityXZ, velocityY;
	public static Frame frame = new Frame("DamageFly");
	public static CheckBox damageFly = new CheckBox("DamageFly").markArrayList("DamageFly").markSetting("DamageFly");
	private boolean waitForGround;

	public void initSettings() {
		frame.register(damageFly);
		MenuAPI.movement.register(frame);
	}

	public void onEvent(Event event) {
		if (event instanceof EventMove && damageFly.isEnabled(false)) {
			EventMove move = (EventMove) event;
			if (System.currentTimeMillis() - lastVelocityTime < 1350) {
				double speed = Math.hypot(move.motion().xCoord, move.motion().zCoord) + velocityXZ - 0.25;
				double[] brain = MoveUtils.getSpeed(speed);
				move.motion().xCoord = brain[0];
				move.motion().zCoord = brain[1];
				if (velocityY > 0)
					move.motion().yCoord = velocityY;
				mc.player.motionX = move.motion().xCoord;
				mc.player.motionZ = move.motion().zCoord;
				mc.player.motionY = move.motion().yCoord;
			}
		}
		if (event instanceof EventAction) {
			EventAction ea = (EventAction) event;
			if (System.currentTimeMillis() - lastVelocityTime < 1350) {
				ea.setSprintState(!mc.player.serverSprintState);
			}
		}
		if (event instanceof EventReceivePacket) {
			if (((EventReceivePacket) event).getPacket() instanceof SPacketEntityVelocity
					&& damageFly.isEnabled(false)) {
				SPacketEntityVelocity packet = (SPacketEntityVelocity) ((EventReceivePacket) event).getPacket();
				if (packet.getEntityID() == mc.player.getEntityId()
						&& System.currentTimeMillis() - lastVelocityTime > 1350) {
					double vX = Math.abs(packet.motionX / 8000d), vY = packet.motionY / 8000d,
							vZ = Math.abs(packet.motionZ / 8000d);
					if (vX + vZ > 0.3) {
						velocityXZ = vX + vZ;
						lastVelocityTime = System.currentTimeMillis();
						velocityY = vY;
					} else {
						velocityXZ = 0;
						velocityY = 0;
					}
				}
			}
		}
	}

	public static double getProgress() {
		return System.currentTimeMillis() - lastVelocityTime > 1350 ? 0
				: 1 - ((System.currentTimeMillis() - lastVelocityTime) / 1350.);
	}
}
