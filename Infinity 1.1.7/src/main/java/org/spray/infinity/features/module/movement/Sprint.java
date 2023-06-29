package org.spray.infinity.features.module.movement;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.Helper;

@ModuleInfo(category = Category.MOVEMENT, desc = "Auto sprinting", key = -2, name = "Sprint", visible = true)
public class Sprint extends Module {

	@Override
	public void onUpdate() {
		if (Helper.getPlayer().forwardSpeed != 0) {
			Helper.MC.options.keySprint.setPressed(true);
		}
	}

}
