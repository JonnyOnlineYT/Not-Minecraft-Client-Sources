package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "FastPlace", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class FastPlace extends Module {

	@Override
	public void onDisable() {
		mc.setRightClickDelayTimer(1);
	}

	@Override
	public void onUpdate() {
		mc.setRightClickDelayTimer(0);
	}

}
