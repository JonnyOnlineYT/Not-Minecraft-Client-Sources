package org.spray.infinity.features.module.movement;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.Helper;

@ModuleInfo(category = Category.MOVEMENT, desc = "Lets you not swim underwater", key = -2, name = "NoSwim", visible = true)
public class NoSwim extends Module {

	@Override
	public void onUpdate() {
		if (Helper.getPlayer().isTouchingWater()) {
			Helper.getPlayer().setSwimming(false);
		}
	}

}
