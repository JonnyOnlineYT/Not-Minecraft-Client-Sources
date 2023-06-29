package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

@ModuleInfo(name = "ItemScroller", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class ItemScroller extends Module {
	
	public final Setting delay = register(new Setting("Delay", 90, 0, 1000));

}
