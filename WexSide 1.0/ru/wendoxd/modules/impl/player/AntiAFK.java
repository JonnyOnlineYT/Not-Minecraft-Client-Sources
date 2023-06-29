package ru.wendoxd.modules.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

import java.util.concurrent.ThreadLocalRandom;

public class AntiAFK extends Module {

	private Frame antiafk_frame = new Frame("AntiAFK");
	private final CheckBox antiafk = new CheckBox("AntiAFK").markArrayList("AntiAFK");
	private final MultiSelectBox actions = new MultiSelectBox("Actions",
			new String[] { "Rotation", "Jump", "Message", "Click", "Swap Slot", "AC Flag" },
			() -> antiafk.isEnabled(true));
	private int oldSlot = -1;

	@Override
	protected void initSettings() {
		antiafk.markSetting("AntiAfk");
		antiafk_frame.register(antiafk, actions);
		MenuAPI.player.register(antiafk_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntitySync && antiafk.isEnabled(false)) {
			if (actions.get(0)) {
				if (mc.player.ticksExisted % 60 == 0) {
					mc.player.rotationYaw += 300;
				}
			}
			if (actions.get(1)) {
				if (mc.player.ticksExisted % 40 == 0) {
					mc.player.jump();
				}
			}
			if (actions.get(2)) {
				if (mc.player.ticksExisted % 400 == 0) {
					mc.player.sendChatMessage("/dfjlngdjjghdf");
				}
			}
			if (actions.get(3)) {
				if (mc.player.ticksExisted % 60 == 0) {
					mc.clickMouse();
				}
			}
			if (actions.get(4) && oldSlot != -1) {
				if (mc.player.ticksExisted % 5 == 0) {
					mc.player.inventory.currentItem = ThreadLocalRandom.current().nextInt(0, 9);
				}
			}
			if (actions.get(5)) {
				if (mc.player.ticksExisted % 600 == 0) {
					mc.player.connection
							.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + 1, mc.player.posY + 1,
									mc.player.posZ + 1, mc.player.rotationYaw, mc.player.rotationPitch, true));
				}
			}
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == antiafk) {
				if (((EventSwapState) event).getState()) {
					oldSlot = mc.player.inventory.currentItem;
				} else {
					mc.player.inventory.currentItem = oldSlot;
				}
			}
		}
	}
}
