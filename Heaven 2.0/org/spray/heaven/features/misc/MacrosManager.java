package org.spray.heaven.features.misc;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.ArrayList;
import java.util.List;

import org.spray.heaven.main.Wrapper;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.util.file.MacrosConfig;

public class MacrosManager extends MacrosConfig {

	private final List<Macro> list;

	public MacrosManager(ClientInitializeEvent event) {
		list = new ArrayList<>();
		event.check();
	}

	public List<Macro> getList() {
		return list;
	}

	public void add(String msg, int key) {
		Macro macros = new Macro(msg, key);
		if (!getList().contains(macros))
		getList().add(macros);
	}

	public void del(int key) {
		for (Macro macro : getList()) {
			if (macro.getKey() == key) {
				list.remove(macro);
			} else {
				Wrapper.message(ChatFormatting.GRAY + "This key not binded");
			}
		}
	}

	public void onKeyPressed(int key) {
		getList().forEach(macros -> {
			if (macros.getKey() == key) {
				Wrapper.getPlayer().sendChatMessage(macros.getMessage());
			}
		});
	}

	public class Macro {

		private String message;
		private int key = -2;

		public Macro(String message, int key) {
			setMessage(message);
			setKey(key);
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

	}

}
