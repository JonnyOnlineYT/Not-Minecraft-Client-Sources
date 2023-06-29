package org.spray.infinity.features.module.combat;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.InvUtil;

import net.minecraft.item.Items;

@ModuleInfo(category = Category.COMBAT, desc = "Automatically takes the totem in the off hand", key = -2, name = "AutoTotem", visible = true)
public class AutoTotem extends Module {

	private Setting health = new Setting(this, "Health", 20D, 0D, 20D);

	@Override
	public void onUpdate() {
		if (Helper.getPlayer().getHealth() <= this.health.getCurrentValueDouble()) {

			int totem = InvUtil.findItemFullInv(Items.TOTEM_OF_UNDYING);

			int slot = totem < 9 ? totem + 36 : totem;
			if (Helper.getPlayer().getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
				if (totem != -2) {
					InvUtil.switchItem(slot, 0);
					InvUtil.switchItem(45, 0);
					InvUtil.switchItem(slot, 1);
				}
			}
		}
	}
}
