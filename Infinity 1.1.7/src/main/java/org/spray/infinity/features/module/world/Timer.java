package org.spray.infinity.features.module.world;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.main.Infinity;

@ModuleInfo(category = Category.WORLD, desc = "Editing game speed", key = -2, name = "Timer", visible = true)
public class Timer extends Module {

	private Setting value = new Setting(this, "Value", 1.1f, 0.3f, 10.0f);
	
	@Override
	public void onDisable() {
		Infinity.resetTimer();
	}
	
	@Override
	public void onUpdate() {
		Infinity.TIMER = value.getCurrentValueFloat();
	}

}
