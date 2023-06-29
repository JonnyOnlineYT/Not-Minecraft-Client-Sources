package org.spray.heaven.features.module.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MovementUtil;
import org.spray.heaven.util.Timer;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

@ModuleInfo(name = "AutoArmor", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class AutoArmor extends Module {

	private Setting delay = register(new Setting("Delay", 120, 0, 1000));
	private Setting openInventory = register(new Setting("Only Inv Open", true));
	private Setting onlystand = register(new Setting("No Moving Swap", false));

	private final Timer timer = new Timer();

	@EventTarget
	public void onUpdate(MotionEvent event) {
		if (!(mc.currentScreen instanceof GuiInventory) && (openInventory.isToggle()))
			return;

		if (MovementUtil.isMoving() && onlystand.isToggle())
			return;

		if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof InventoryEffectRenderer)) {
			return;
		}

		InventoryPlayer inventory = mc.player.inventory;

		int[] bestArmorSlots = new int[4];
		int[] bestArmorValues = new int[4];

		for (int i = 0; i < 4; i++) {

			bestArmorSlots[i] = -1;

			ItemStack stack = inventory.armorItemInSlot(i);
			if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {

				ItemArmor item = (ItemArmor) stack.getItem();
				bestArmorValues[i] = getArmorValue(item, stack);
			}
		}

		for (int slot = 0; slot < 36; slot++) {

			ItemStack stack = inventory.getStackInSlot(slot);

			if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {

				ItemArmor item = (ItemArmor) stack.getItem();
				int armorType = item.armorType.getIndex();
				int armorValue = getArmorValue(item, stack);

				if (armorValue > bestArmorValues[armorType]) {

					bestArmorSlots[armorType] = slot;
					bestArmorValues[armorType] = armorValue;
				}
			}
		}

		ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
		Collections.shuffle(integers);

		for (int i : integers) {
			int armorSlot = bestArmorSlots[i];
			if (armorSlot == -1) {
				continue;
			}
			ItemStack oldArmor = inventory.armorItemInSlot(i);
			if (!isNullOrEmpty(oldArmor) && inventory.getFirstEmptyStack() == -1) {
				continue;
			}
			if (armorSlot < 9) {
				armorSlot += 36;
			}
			if (timer.hasReached(delay.getValue())) {
				if (!isNullOrEmpty(oldArmor)) {
					mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, mc.player);
				}
				mc.playerController.windowClick(0, armorSlot, 0, ClickType.QUICK_MOVE, mc.player);
				timer.reset();
			}
			break;
		}
	}

	private boolean isNullOrEmpty(ItemStack stack) {
		return !(stack != null && !stack.isEmpty());
	}

	private int getArmorValue(ItemArmor item, ItemStack stack) {
		int armorPoints = item.damageReduceAmount;
		int prtPoints;
		int armorToughness = (int) item.toughness;
		int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
		Enchantment protection = Enchantments.PROTECTION;
		int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
		DamageSource dmgSource = DamageSource.causePlayerDamage(mc.player);
		prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
		return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
	}
}