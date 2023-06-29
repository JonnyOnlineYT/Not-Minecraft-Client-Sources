package org.spray.infinity.features.module.player;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.mixin.IMinecraftClient;
import org.spray.infinity.utils.Helper;

import net.minecraft.item.Items;

@ModuleInfo(category = Category.PLAYER, desc = "Throw experience bubbles faster", key = -2, name = "FastEXP", visible = true)
public class FastEXP extends Module {

	@Override
	public void onUpdate() {
		// Super module, Very hard
		if (Helper.getPlayer().getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE) {
			((IMinecraftClient) Helper.MC).setItemCooldown(0);
		}
	}

}
