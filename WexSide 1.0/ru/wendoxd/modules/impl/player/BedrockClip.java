package ru.wendoxd.modules.impl.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class BedrockClip extends Module {
	private static Frame bedrockClipFrame = new Frame("PearlClip");
	private static CheckBox bedrockClip = new CheckBox("PearlClip").markSetting("PearlClip").markArrayList("PearlClip");
	private static MultiSelectBox condition = new MultiSelectBox("Condition", new String[] { "Teleport" },
			() -> bedrockClip.isEnabled(true));
	private static long lastTeleportTime;

	public void initSettings() {
		MenuAPI.movement.register(bedrockClipFrame);
		bedrockClipFrame.register(bedrockClip, condition);
		condition.selectedValues.set(0, true);
	}

	public void onEvent(Event event) {
		if (event instanceof EventUpdate && bedrockClip.isEnabled(false)) {
			boolean needHurt = mc.player.hurtTime == 0;
			boolean needTeleport = condition.get(0) && System.currentTimeMillis() - lastTeleportTime > 1000;
			if (needHurt) {
				return;
			}
			if (needTeleport) {
				return;
			}
			for (int i = 0; i < 3; i++) {
				mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
			}
			mc.player.setPosition(mc.player.posX, -2, mc.player.posZ);
			mc.player.hurtTime = 0;
			lastTeleportTime = 0;
		}
		if (event instanceof EventReceivePacket) {
			EventReceivePacket erp = (EventReceivePacket) event;
			Packet packet = erp.getPacket();
			if (packet instanceof SPacketPlayerPosLook) {
				SPacketPlayerPosLook spppl = (SPacketPlayerPosLook) packet;
				if (spppl.getY() % 1 != 1) {
					lastTeleportTime = System.currentTimeMillis();
				}
			}
		}
	}
}
