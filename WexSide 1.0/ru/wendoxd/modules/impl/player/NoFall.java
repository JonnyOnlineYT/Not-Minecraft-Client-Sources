package ru.wendoxd.modules.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.TimerUtils;

public class NoFall extends Module {

	private Frame nofall_frame = new Frame("NoFall");
	private final CheckBox nofall = new CheckBox("NoFall").markArrayList("NoFall");
	private final SelectBox mode = new SelectBox("Mode", new String[] { "Vanilla", "Matrix", "ReallyWorld" },
			() -> nofall.isEnabled(true));
	private TimerUtils timerUtils = new TimerUtils();

	@Override
	protected void initSettings() {
		nofall.markSetting("NoFall");
		nofall_frame.register(nofall, mode);
		MenuAPI.player.register(nofall_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntitySync && nofall.isEnabled(false)) {
			if (mode.get() == 2 && mc.player.fallDistance > 3.5) {
				if (timerUtils.hasReached(150L)) {
					mc.player.connection.sendPacket(
							new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
					timerUtils.reset();
				} else {
					mc.player.onGround = false;
				}
			}
			if (mc.player.fallDistance > 3) {
				if (mode.get() == 0) {
					mc.player.connection.sendPacket(new CPacketPlayer(true));
				} else if (mode.get() == 1 && mc.player.ticksExisted % 5 == 0) {
					mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY,
							mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
				} else if (mode.get() == 2) {
					mc.player.connection.sendPacket(
							new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
					mc.player.motionY -= 0.91231425;
				}
			}
		}
	}
}
