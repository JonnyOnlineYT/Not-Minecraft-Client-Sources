package org.spray.heaven.features.module.modules.render;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

@ModuleInfo(name = "HitAnimation", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class HitAnimation extends Module {

	public Setting speed = register(new Setting("HitAnimation Speed", 11.2, 8, 20));

}
