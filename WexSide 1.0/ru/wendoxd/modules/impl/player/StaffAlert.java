package ru.wendoxd.modules.impl.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventPacketPlayerList;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.visuals.Notifications;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.ChatUtils;
import ru.wendoxd.utils.sound.SoundUtils;

public class StaffAlert extends Module {

	private Frame staffAlert_frame = new Frame("StaffAlert");
	private final CheckBox staffAlert = new CheckBox("StaffAlert").markArrayList("StaffAlert");
	private final MultiSelectBox msb = new MultiSelectBox("Notify", new String[] { "HUD", "Chat", "Sound" },
			() -> staffAlert.isEnabled(true));

	@Override
	protected void initSettings() {
		staffAlert_frame.register(staffAlert, msb);
		MenuAPI.player.register(staffAlert_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPacketPlayerList && staffAlert.isEnabled(false)) {
			EventPacketPlayerList e = (EventPacketPlayerList) event;
			Action action = e.getAction();
			AddPlayerData data = e.getPlayerData();
			if (data != null && data.getDisplayName() != null && data.getDisplayName().getFormattedText() != null
					&& data.getProfile() != null && data.getProfile().getName() != null) {
				String displayName = data.getDisplayName().getFormattedText();
				boolean havePerm = havePermission(data.getProfile(), displayName);
				if (havePerm) {
					if (action == Action.ADD_PLAYER) {
						log("Администратор " + displayName + " зашел на сервер/вышел из ваниша.");
					} else if (action == Action.REMOVE_PLAYER) {
						log("Администратор " + displayName + " вышел с сервера/зашел в ваниш.");
					}
				}
			}
		}
	}

	public boolean havePermission(GameProfile gameProfile, String displayName) {
		StringBuilder builder = new StringBuilder();
		char[] buffer = displayName.toCharArray();
		for (int i = 0; i < buffer.length; i++) {
			char c = buffer[i];
			if (c == '§') {
				i++;
			} else {
				builder.append(c);
			}
		}
		return havePermissionFixed(builder.toString().toLowerCase().replace(gameProfile.getName().toLowerCase(), ""));
	}

	private boolean havePermissionFixed(String displayName) {
		return displayName.contains("helper") || displayName.contains("хелпер") || displayName.contains("модер")
				|| displayName.contains("moder") || displayName.contains("куратор") || displayName.contains("админ")
				|| displayName.contains("admin");
	}

	public void log(String msg) {
		if (msb.get(0))
			Notifications.notify("Staff Alert", msg);
		if (msb.get(1))
			ChatUtils.addChatMessage(msg);
		if (msb.get(2))
			SoundUtils.playSound();
	}
}