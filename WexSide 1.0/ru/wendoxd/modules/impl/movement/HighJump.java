package ru.wendoxd.modules.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class HighJump extends Module {

	private Frame highjump_frame = new Frame("HighJump");
	private CheckBox highjump = new CheckBox("HighJump").markArrayList("HighJump");
	private SelectBox mode = new SelectBox("Mode", new String[] { "Matrix" }, () -> highjump.isEnabled(true));
	public boolean flag;
	public int delay, requestFlags;

	@Override
	protected void initSettings() {
		highjump.markSetting("HighJump");
		mode.modifyPath("Mode_2");
		highjump_frame.register(highjump, mode);
		MenuAPI.movement.register(highjump_frame);
	}

	public static void init() {
		System.out.println("asdasd");
	}

	@Override
	public void onEvent(Event event) {
		if (highjump.isEnabled(false)) {
			if (event instanceof EventMove) {
				this.update(event);
			}
			if (event instanceof EventReceivePacket) {
				this.update2(event);
			}
		} else {
			requestFlags = 0;
		}
	}

	public void update2(Event event) {
		EventReceivePacket erp = (EventReceivePacket) event;
		Packet packet = erp.getPacket();
		if (packet instanceof SPacketPlayerPosLook) {
			if (requestFlags == 0) {
				return;
			}
			requestFlags--;
			flag = true;
		}
	}

	public void update(Event event) {
		EventMove move = (EventMove) event;
		if (((delay == 0 && mc.player.motionY <= 0) || flag)) {
			move.motion().yCoord = 0;
			if (!flag) {
				mc.player.motionY = 9;
				requestFlags = 2;
			} else {
				double prev = (9 - 0.08) * 0.98;
				if (requestFlags == 1) {
					prev = 9;
				}
				mc.player.motionY = (prev - 0.08) * 0.98;
				delay = 0;
			}
			delay = 20;
		}
		if (delay > 0) {
			move.motion().xCoord = 0;
			move.motion().zCoord = 0;
			delay--;
		}
		flag = false;
	}
}