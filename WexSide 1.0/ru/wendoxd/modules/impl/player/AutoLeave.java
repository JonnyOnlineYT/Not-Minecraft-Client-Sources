package ru.wendoxd.modules.impl.player;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AutoLeave extends Module {
	public static Frame frame = new Frame("AutoLeave");
	public static CheckBox autoLeave = new CheckBox("AutoLeave").markArrayList("AutoLeave").markSetting("AutoLeave");
	public static SelectBox leaveType = new SelectBox("Type", new String[] { "Disconnect", "/home", "/spawn", "/hub" },
			() -> autoLeave.isEnabled(true));
	public static CheckBox disableBaritone = new CheckBox("Disable Baritone",
			() -> autoLeave.isEnabled(true) && leaveType.get() > 0);
	public static Slider distance = new Slider("Distance", 0, 0, 100, 0.5, () -> autoLeave.isEnabled(true));
	public static long delay;

	public void initSettings() {
		frame.register(autoLeave, leaveType, disableBaritone, distance);
		MenuAPI.player.register(frame);
	}

	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			if (autoLeave.isEnabled(false) && System.currentTimeMillis() > delay) {
				for (EntityPlayer player : mc.world.playerEntities) {
					if (player != mc.player && !WexSide.friendManager.isFriend(player.getName())) {
						if (player.getDistanceToEntity(mc.player) > distance.getDoubleValue()) {
							break;
						}
						if (leaveType.get() == 0) {
							mc.player.connection.getNetworkManager().closeChannel(new TextComponentString("AutoLeave"));
						} else if (leaveType.get() == 1) {
							mc.player.sendChatMessage("/home");
						} else if (leaveType.get() == 2) {
							mc.player.sendChatMessage("/spawn");
						} else if (leaveType.get() == 3) {
							mc.player.sendChatMessage("/hub");
						}
						delay = System.currentTimeMillis() + 1000 * 60 * 5;
						if (disableBaritone.isEnabled(false)) {
							IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(mc.player);
							baritone.getMineProcess().cancel();
						}
					}
				}
			}
		}
	}
}
