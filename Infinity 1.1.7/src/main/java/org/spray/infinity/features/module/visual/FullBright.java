package org.spray.infinity.features.module.visual;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.Helper;

@ModuleInfo(category = Category.VISUAL, desc = "Removes shadows, adds brightness", key = -2, name = "FullBright", visible = true)
public class FullBright extends Module {

	private double oldGamma = -2;

	@Override
	public void onEnable() {
		oldGamma = Helper.MC.options.gamma;
	}

	@Override
	public void onDisable() {
		if (oldGamma != -2) {
			Helper.MC.options.gamma = oldGamma;
			oldGamma = -2;
		}
	}

	@Override
	public void onUpdate() {
		Helper.MC.options.gamma = 100;
	}

}
