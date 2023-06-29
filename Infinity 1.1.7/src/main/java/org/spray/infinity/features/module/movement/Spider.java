package org.spray.infinity.features.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;

@ModuleInfo(category = Category.MOVEMENT, desc = "Allows you to climb walls ", key = -2, name = "Spider", visible = true)
public class Spider extends Module {

	private Setting mode = new Setting(this, "Mode", "Matrix 6.1.1",
			new ArrayList<>(Arrays.asList("Matrix 6.1.1", "Vanilla")));

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

		if (mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.1")) {
			if (!isCollision())
				return;

			if (Helper.getPlayer().age % 8 == 0) {
				Helper.getPlayer().setOnGround(true);
				Helper.getPlayer().velocityDirty = false;
			} else
				Helper.getPlayer().setOnGround(false);

			Helper.getPlayer().prevY -= 2E-232D;

			if (Helper.getPlayer().isOnGround())
				Helper.getPlayer().jump();
		} else if (mode.getCurrentMode().equalsIgnoreCase("Vanilla")) {
			if (!isCollision())
				return;

			MoveUtil.setYVelocity(Helper.getPlayer().getVelocity().getY() + 0.1);
			Helper.getPlayer().velocityDirty = false;
		}
	}

	private boolean isCollision() {
		return Helper.getPlayer().horizontalCollision && !Helper.getPlayer().isTouchingWater();
	}
}
