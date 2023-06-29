package ru.wendoxd.modules.impl.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class PortalGodMode extends Module {
	public static Frame frame = new Frame("PortalGodMode");
	public static CheckBox portalGodMode = new CheckBox("PortalGodMode").markSetting("PortalGodMode")
			.markArrayList("PortalGodMode");

	public void initSettings() {
		frame.register(portalGodMode);
		MenuAPI.player.register(frame);
	}

	public void onEvent(Event event) {
		if (event instanceof EventReceivePacket) {
			Packet packet = ((EventReceivePacket) event).getPacket();
			if (packet instanceof CPacketConfirmTeleport && portalGodMode.isEnabled(false)) {
				((EventReceivePacket) event).setCanceled();
			}
		}
	}
}
