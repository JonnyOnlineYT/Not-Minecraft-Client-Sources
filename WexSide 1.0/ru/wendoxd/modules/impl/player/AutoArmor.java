package ru.wendoxd.modules.impl.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.GuiConnecting;
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
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.TimerUtils;

public class AutoArmor extends Module {
	public static Frame frame = new Frame("AutoArmor");
	public static CheckBox autoArmor = new CheckBox("AutoArmor").markSetting("AutoArmor").markArrayList("AutoArmor");
	public static CheckBox openInventory = new CheckBox("OnlyInventory", () -> autoArmor.isEnabled(true));
	public static Slider delay = new Slider("Delay", 0, 0, 10, 0.5, () -> autoArmor.isEnabled(true));
	private final TimerUtils timerUtils = new TimerUtils();

	public void initSettings() {
		frame.register(autoArmor, openInventory, delay);
		MenuAPI.player.register(frame);
		GuiConnecting.class.getClass();
	}

	public void onEvent(Event event) {
		if (event instanceof EventUpdate && autoArmor.isEnabled(false)) {
			if (!(AutoArmor.mc.currentScreen instanceof GuiInventory) && AutoArmor.openInventory.isEnabled(false)) {
				return;
			}
			if (AutoArmor.mc.currentScreen instanceof GuiContainer
					&& !(AutoArmor.mc.currentScreen instanceof InventoryEffectRenderer)) {
				return;
			}
			final InventoryPlayer inventory = AutoArmor.mc.player.inventory;
			final int[] bestArmorSlots = new int[4];
			final int[] bestArmorValues = new int[4];
			for (int type = 0; type < 4; ++type) {
				bestArmorSlots[type] = -1;
				final ItemStack stack = inventory.armorItemInSlot(type);
				if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
					final ItemArmor item = (ItemArmor) stack.getItem();
					bestArmorValues[type] = this.getArmorValue(item, stack);
				}
			}
			for (int slot = 0; slot < 36; ++slot) {
				final ItemStack stack = inventory.getStackInSlot(slot);
				if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
					final ItemArmor item = (ItemArmor) stack.getItem();
					final int armorType = item.armorType.getIndex();
					final int armorValue = this.getArmorValue(item, stack);
					if (armorValue > bestArmorValues[armorType]) {
						bestArmorSlots[armorType] = slot;
						bestArmorValues[armorType] = armorValue;
					}
				}
			}
			final ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
			Collections.shuffle(types);
			for (final int i : types) {
				int j = bestArmorSlots[i];
				if (j == -1) {
					continue;
				}
				final ItemStack oldArmor = inventory.armorItemInSlot(i);
				if (!isNullOrEmpty(oldArmor) && inventory.getFirstEmptyStack() == -1) {
					continue;
				}
				if (j < 9) {
					j += 36;
				}
				if (this.timerUtils.hasReached(this.delay.getIntValue() * 100.0f)) {
					if (!isNullOrEmpty(oldArmor)) {
						AutoArmor.mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE,
								AutoArmor.mc.player);
					}
					AutoArmor.mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, AutoArmor.mc.player);
					this.timerUtils.reset();
					break;
				}
				break;
			}
		}
	}

	private int getArmorValue(final ItemArmor item, final ItemStack stack) {
		final int armorPoints = item.damageReduceAmount;
		int prtPoints = 0;
		final int armorToughness = (int) item.toughness;
		final int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
		final Enchantment protection = Enchantments.PROTECTION;
		final int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
		final DamageSource dmgSource = DamageSource.causePlayerDamage(AutoArmor.mc.player);
		prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
		return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
	}

	public static boolean isNullOrEmpty(final ItemStack stack) {
		return stack == null || stack.isEmpty();
	}
}
