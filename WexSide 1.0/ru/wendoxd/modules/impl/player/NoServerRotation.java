package ru.wendoxd.modules.impl.player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class NoServerRotation extends Module {

	private Frame noserverrotation_frame = new Frame("NoServerRotation");
	private final CheckBox noserverrotation = new CheckBox("NoServerRotation").markArrayList("NoServerRotation");

	@Override
	protected void initSettings() {
		noserverrotation_frame.register(noserverrotation.markSetting("NoServerRotation"));
		MenuAPI.player.register(noserverrotation_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventReceivePacket && noserverrotation.isEnabled(false)) {
			if (((EventReceivePacket) event).getPacket() instanceof SPacketPlayerPosLook) {
				SPacketPlayerPosLook packet = (SPacketPlayerPosLook) ((EventReceivePacket) event).getPacket();
				packet.yaw = mc.player.rotationYaw;
				packet.pitch = mc.player.rotationPitch;
			}
		}
	}
}
