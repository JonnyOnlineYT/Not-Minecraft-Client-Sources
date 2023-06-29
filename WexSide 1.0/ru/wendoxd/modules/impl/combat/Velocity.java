package ru.wendoxd.modules.impl.combat;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class Velocity extends Module {

	private final Frame velocity_frame = new Frame("Velocity");
	private final CheckBox velocity = new CheckBox("Velocity").markArrayList("Velocity");
	private final SelectBox mode = new SelectBox("Mode", new String[] { "Packet", "Matrix" },
			() -> velocity.isEnabled(true));
	private final Slider xz = new Slider("XZ", 2, 0, 100, 0, () -> velocity.isEnabled(true) && mode.get() == 0);
	private final Slider y = new Slider("Y", 2, 0, 100, 0, () -> velocity.isEnabled(true) && mode.get() == 0);

	@Override
	protected void initSettings() {
		velocity.markSetting("Velocity");
		xz.modifyPath("XZ");
		y.modifyPath("Y");
		mode.modifyPath("Mode_2");
		velocity_frame.register(velocity, mode, xz, y);
		MenuAPI.combat.register(velocity_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventReceivePacket && velocity.isEnabled(false)) {
			if (mode.get() == 0) {
				if (((EventReceivePacket) event).getPacket() instanceof SPacketEntityVelocity) {
					SPacketEntityVelocity packet = (SPacketEntityVelocity) ((EventReceivePacket) event).getPacket();
					if (packet.getEntityID() == mc.player.getEntityId()) {
						if (xz.getIntValue() > 0 && y.getIntValue() > 0) {
							packet.motionX = packet.motionX / 100 * xz.getIntValue();
							packet.motionY = packet.motionY / 100 * y.getIntValue();
							packet.motionZ = packet.motionZ / 100 * xz.getIntValue();
						} else {
							((EventReceivePacket) event).setCanceled();
						}
					}
				}
				if (((EventReceivePacket) event).getPacket() instanceof SPacketExplosion) {
					SPacketExplosion packet = (SPacketExplosion) ((EventReceivePacket) event).getPacket();
					if (xz.getIntValue() > 0 && y.getIntValue() > 0) {
						packet.motionX = packet.motionX / 100 * xz.getIntValue();
						packet.motionY = packet.motionY / 100 * y.getIntValue();
						packet.motionZ = packet.motionZ / 100 * xz.getIntValue();
					} else {
						((EventReceivePacket) event).setCanceled();
					}
				}
			} else if (mode.get() == 1) {
				if (mc.player.hurtTime > 8) {
					mc.player.onGround = true;
				}
			}
		}
	}
}