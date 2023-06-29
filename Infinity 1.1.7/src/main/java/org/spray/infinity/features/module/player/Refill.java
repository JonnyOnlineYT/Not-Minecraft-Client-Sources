package org.spray.infinity.features.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.InvUtil;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

@ModuleInfo(category = Category.PLAYER, desc = "Automatically fills the hotbar with healing potions", key = -2, name = "Refill", visible = true)
public class Refill extends Module {

	private Setting mode = new Setting(this, "Mode", "FreeSlots",
			new ArrayList<>(Arrays.asList("FreeSlots", "Select")));

	List<Setting> slots;

	public Refill() {
		this.slots = new ArrayList<Setting>();
		int count = 9;
		for (int i = 1; i < count; i++) {
			Setting slot = new Setting(this, "Slot " + i, true)
					.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Select"));
			this.slots.add(slot);
		}
		this.addSettings(this.slots);
	}

	@Override
	public void onUpdate() {
		int find = InvUtil.findPotionInternalInv(StatusEffects.INSTANT_HEALTH, false);
		int freeSlots = Helper.getPlayer().getInventory().getEmptySlot();

		if (mode.getCurrentMode().equalsIgnoreCase("FreeSlots")) {
			if (find != -2 && freeSlots != -1 && freeSlots < 9)
				switchPotion(find, freeSlots);
		} else if (mode.getCurrentMode().equalsIgnoreCase("Select")) {
			if (find == -2)
				return;
			for (int i = 0; i < this.slots.size(); i++) {
				Setting slot = (Setting) this.slots.get(i);
				if (slot.isToggle())
					switchPotion(find, i);
			}

		}
	}

	private void switchPotion(int from, int slot) {
		if (Helper.getPlayer().getInventory().getStack(slot).getItem() != Items.SPLASH_POTION)
			InvUtil.swapItem(from, slot);
	}

}
