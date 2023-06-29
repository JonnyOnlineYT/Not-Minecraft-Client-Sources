package ru.wendoxd.modules.impl.player;

import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventSendPacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class PingSpoof extends Module {

	private Frame pingspoof_frame = new Frame("PingSpoof");
	private CheckBox pingspoof = new CheckBox("PingSpoof").markArrayList("PingSpoof");
	private Slider delay = new Slider("Delay", 0, 50, 30000, 0.5, () -> pingspoof.isEnabled(false));

	private long key;
	private int id;
	private int windowId;

	@Override
	protected void initSettings() {
		pingspoof_frame.register(pingspoof.markSetting("PingSpoof"), delay);
		MenuAPI.player.register(pingspoof_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventSendPacket && pingspoof.isEnabled(false)) {
			if (((EventSendPacket) event).getPacket() instanceof CPacketConfirmTransaction) {
				if (id == ((CPacketConfirmTransaction) ((EventSendPacket) event).getPacket()).getUid()
						&& windowId == ((CPacketConfirmTransaction) ((EventSendPacket) event).getPacket())
								.getWindowId())
					return;
				((EventSendPacket) event).setCanceled();

				new Thread(() -> {
					try {
						Thread.sleep((long) delay.getFloatValue());
					} catch (InterruptedException ignore) {

					}

					id = ((CPacketConfirmTransaction) ((EventSendPacket) event).getPacket()).getUid();
					windowId = ((CPacketConfirmTransaction) ((EventSendPacket) event).getPacket()).getWindowId();

					mc.player.connection.sendPacket(((EventSendPacket) event).getPacket());

				}).start();
			}

			if (((EventSendPacket) event).getPacket() instanceof CPacketKeepAlive) {
				if (key == ((CPacketKeepAlive) ((EventSendPacket) event).getPacket()).getKey())
					return;
				((EventSendPacket) event).setCanceled();
				new Thread(() -> {
					try {
						Thread.sleep((long) delay.getFloatValue());
					} catch (InterruptedException ignore) {

					}

					key = ((CPacketKeepAlive) ((EventSendPacket) event).getPacket()).getKey();
					mc.player.connection.sendPacket(((EventSendPacket) event).getPacket());
				}).start();
			}
		}
	}
}
