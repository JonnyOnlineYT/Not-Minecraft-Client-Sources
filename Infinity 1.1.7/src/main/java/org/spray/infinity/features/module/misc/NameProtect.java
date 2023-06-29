package org.spray.infinity.features.module.misc;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;

@ModuleInfo(category = Category.MISC, desc = "Changes your name visually", key = -2, name = "NameProtect", visible = false)
public class NameProtect extends Module {

//	public Setting prefix = new Setting(this, "Prefix", "Operator");
	public Setting name = new Setting(this, "Name", "sempai");

	public String protect(String string) {
		if (!isEnabled() || Helper.getPlayer() == null)
			return string;

		String me = Helper.MC.getSession().getUsername();
		if (string.contains(me) && !this.name.getText().isEmpty())
			return string.replace(me, this.name.getText());

		return string;
	}
}
