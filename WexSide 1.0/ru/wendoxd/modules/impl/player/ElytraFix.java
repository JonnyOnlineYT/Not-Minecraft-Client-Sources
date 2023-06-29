package ru.wendoxd.modules.impl.player;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class ElytraFix extends Module {
	public static Frame frame = new Frame("ElytraFix");
	public static CheckBox elytraFix = (CheckBox) new CheckBox("ElytraFix").markSetting("ElytraFix")
			.markDescription("Автоматически меняет элитры\nна нагрудник.");
	public static long delay;

	public void initSettings() {
		frame.register(elytraFix);
		MenuAPI.player.register(frame);
	}

	public void onEvent(Event event) {
		if (event instanceof EventUpdate && elytraFix.isEnabled(false)) {
			ItemStack stack = mc.player.inventory.getItemStack();
			if (stack != null && stack.getItem() instanceof ItemArmor && System.currentTimeMillis() > delay) {
				ItemArmor ia = (ItemArmor) stack.getItem();
				if (ia.armorType == EntityEquipmentSlot.CHEST
						&& mc.player.inventory.armorItemInSlot(2).getItem() == Items.ELYTRA) {
					mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
					int nullSlot = findNullSlot();
					boolean needDrop = nullSlot == 999;
					if (needDrop) {
						nullSlot = 9;
					}
					mc.playerController.windowClick(0, nullSlot, 1, ClickType.PICKUP, mc.player);
					if (needDrop) {
						mc.playerController.windowClick(0, -999, 1, ClickType.PICKUP, mc.player);
					}
					delay = System.currentTimeMillis() + 300;
				}
			}
		}
	}

	public static int findNullSlot() {
		for (int i = 0; i < 36; i++) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack.getItem() instanceof ItemAir) {
				if (i < 9) {
					i += 36;
				}
				return i;
			}
		}
		return 999;
	}
}
