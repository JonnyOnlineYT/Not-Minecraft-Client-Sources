package org.spray.infinity.features.component.macro;

import java.util.ArrayList;
import java.util.List;

import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.file.MacrosFile;

import net.minecraft.util.Formatting;

public class MacroManager extends MacrosFile {

	private List<Macro> list = new ArrayList<>();

	public List<Macro> getList() {
		return list;
	}

	public void save() {
		saveMacro();
	}

	public void load() {
		loadMacro();
	}

	public void del(int key) {
		for (Macro macro : getList()) {
			if (macro.getKey() == key) {
				list.remove(macro);
			} else {
				Helper.message(Formatting.GRAY + "This key not binded");
			}
		}
	}

	public void onKeyPressed(int key) {
		getList().forEach(macro -> {
			if (macro.getKey() == key)
				Helper.getPlayer().sendChatMessage(macro.getMessage());
		});
	}

}
