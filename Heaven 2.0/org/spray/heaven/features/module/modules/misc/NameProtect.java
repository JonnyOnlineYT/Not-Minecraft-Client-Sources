package org.spray.heaven.features.module.modules.misc;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "NameProtect", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class NameProtect extends Module {

	public final Setting self = register(new Setting("Self", true));
	public final Setting friends = register(new Setting("Friends", true));
	public final Setting tab = register(new Setting("Tab Protect", false));
	public final Setting scoreboard = register(new Setting("ScoreBoard Protect", true));

	public String PROTECT = "justprotect";

	public boolean isValid(String name) {
		if (self.isToggle() && mc.player.getName().equals(name))
			return true;
		if (friends.isToggle() && Wrapper.getFriend().contains(name))
			return true;
		
		return false;
	}

}
