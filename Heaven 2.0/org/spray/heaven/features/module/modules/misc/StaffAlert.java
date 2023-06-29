package org.spray.heaven.features.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.spray.heaven.events.EventPlayerList;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification;

@ModuleInfo(name = "StaffAlert", key = 0, category = Category.MISC, visible = true)
public class StaffAlert extends Module {



	@EventTarget
	public void onEvent(EventPlayerList event) {
		SPacketPlayerListItem.Action action = event.getAction();
		SPacketPlayerListItem.AddPlayerData data = event.getData();
		if (data != null && data.getDisplayName() != null) {
			data.getDisplayName().getFormattedText();
			if (data.getProfile().getName() != null) {
				String displayName = data.getDisplayName().getFormattedText();
				boolean havePerm = havePermission(data.getProfile(), displayName);
				if (havePerm) {
					if (action == SPacketPlayerListItem.Action.ADD_PLAYER) {
						log("Администратор " + displayName + " зашел на сервер/вышел из ваниша.");
					} else if (action == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
						log("Администратор " + displayName + " вышел с сервера/зашел в ваниш.");
					}
				}
			}
		}
	}

	private boolean havePermission(GameProfile gameProfile, String displayName) {
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

	void log(String msg) {
		Wrapper.getNotification().call(msg, Notification.Type.WARNING, IFont.DEFAULT_SMALL);
	}
}