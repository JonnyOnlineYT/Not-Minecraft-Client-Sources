package org.spray.infinity.features.module.visual;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;

@ModuleInfo(category = Category.VISUAL, desc = "Allows you to edit the scoreboard", key = -2, name = "Scoreboard", visible = true)
public class ScoreboardMod extends Module {

	public Setting remove = new Setting(this, "Remove the scoreboard", false);
	public Setting height = new Setting(this, "Custom Height", 85D, 0D, 150D).setVisible(() -> !remove.isToggle());

}
