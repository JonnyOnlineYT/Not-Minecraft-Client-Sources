package org.spray.infinity.features.module.player;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.mixin.IKeyBinding;
import org.spray.infinity.utils.Helper;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@ModuleInfo(category = Category.PLAYER, desc = "Automatically eats food when there is a certain hunger", key = -2, name = "AutoEat", visible = true)
public class AutoEat extends Module {

	private Setting hunger = new Setting(this, "Hunger", 19, 0, 19);

	private int lastSlot = -1;
	private boolean eating = false;

	@Override
	public void onUpdate() {
		if (Helper.getPlayer().getAbilities().creativeMode)
			return;
		if (eating && !Helper.getPlayer().isUsingItem()) {
			if (lastSlot != -1) {
				Helper.getPlayer().getInventory().selectedSlot = lastSlot;
				lastSlot = -1;
			}
			KeyBinding.setKeyPressed(((IKeyBinding) Helper.MC.options.keyUse).getBoundKey(), false);
			eating = false;
			return;
		}

		if (eating)
			return;

		if (Helper.getPlayer().getOffHandStack().getItem().getGroup() == ItemGroup.FOOD) {
			if (Helper.getPlayer().getHungerManager().getFoodLevel() <= hunger.getCurrentValueInt()) {
				eating = true;
				use();
			}

		} else {
			if (Helper.getPlayer().getHungerManager().getFoodLevel() <= hunger.getCurrentValueInt()) {
				int foodSlot = findFood();
				if (Helper.getPlayer().getMainHandStack().getItem().getGroup() == ItemGroup.FOOD) {
					eating = true;
					use();
				} else {

					if (foodSlot != -2) {
						lastSlot = Helper.getPlayer().getInventory().selectedSlot;
						Helper.getPlayer().getInventory().selectedSlot = foodSlot;
						if (Helper.getPlayer().getMainHandStack().getItem().getGroup() == ItemGroup.FOOD) {
							eating = true;
							use();
						}
					}

				}
			}
		}
	}

	private void use() {
		KeyBinding.setKeyPressed(((IKeyBinding) Helper.MC.options.keyUse).getBoundKey(), true);
		Helper.MC.interactionManager.interactItem(Helper.getPlayer(), Helper.getWorld(), Hand.MAIN_HAND);
	}

	private int findFood() {
		for (int i = 0; i <= 8; i++) {
			ItemStack stack = Helper.getPlayer().getInventory().getStack(i);
			if (hasFood(stack))
				return i;
		}
		return -2;
	}

	private boolean hasFood(ItemStack stack) {
		if (stack.getItem().getGroup() == ItemGroup.FOOD) {
	        if (stack.getItem() == Items.ROTTEN_FLESH) return false;
	        if (stack.getItem() == Items.SPIDER_EYE) return false;
	        if (stack.getItem() == Items.POISONOUS_POTATO) return false;
			return true;
		}
		return false;
	}

}
