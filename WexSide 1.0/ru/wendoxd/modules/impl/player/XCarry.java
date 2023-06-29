package ru.wendoxd.modules.impl.player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventSendPacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class XCarry extends Module {

	private Frame xcarry_frame = new Frame("XCarry");
	private final CheckBox xcarry = new CheckBox("XCarry").markArrayList("XCarry");

	@Override
	protected void initSettings() {
		xcarry.markSetting("XCarry");
		xcarry_frame.register(xcarry);
		MenuAPI.player.register(xcarry_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventSendPacket && xcarry.isEnabled(false)) {
			if (((EventSendPacket) event).getPacket() instanceof CPacketCloseWindow) {
				((EventSendPacket) event).setCanceled();
			}
		}
	}
}
