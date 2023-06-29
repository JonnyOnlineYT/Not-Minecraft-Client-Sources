package org.spray.infinity.features.module.combat;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.mixin.IMinecraftClient;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.Timer;

@ModuleInfo(category = Category.COMBAT, desc = "Clicking on pressed LKM", key = -2, name = "AutoClicker", visible = true)
public class AutoClicker extends Module {

	private Setting coolDown = new Setting(this, "Cool Down", true);
	private Setting aps = new Setting(this, "APS", 1.8D, 0.0D, 10.0D).setVisible(() -> !coolDown.isToggle());

	private Timer timer = new Timer();

	@Override
	public void onUpdate() {
		if (Helper.MC.options.keyAttack.isPressed()) {

			if (coolDown.isToggle() ? Helper.getPlayer().getAttackCooldownProgress(0.0f) >= 1
					: timer.hasReached(1000 / aps.getCurrentValueDouble())) {

				((IMinecraftClient) Helper.MC).mouseClick();

				Helper.getPlayer().resetLastAttackedTicks();
				timer.reset();
			}
		}
	}

}
