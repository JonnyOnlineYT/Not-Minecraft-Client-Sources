package org.spray.infinity.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.slot.SlotActionType;

public class InvUtil {

	public static int findItem(Item item, int index, int slots) {
		int find = -2;
		for (int i = index; i <= slots; i++)
			if (Helper.getPlayer().getInventory().getStack(i).getItem() == item)
				find = i;
		return find;
	}

	// Find item on full inventory
	public static int findItemFullInv(Item item) {
		return findItem(item, 0, 44);
	}

	// Find item only on hotbat
	public static int findItemOnHotbar(Item item) {
		return findItem(item, 0, 8);
	}

	// Find item only internal inventory , no hotbar
	public static int findItemInternalInv(Item item) {
		return findItem(item, 9, 44);
	}

	// Find item only internal inv potion , no hotbar
	public static int findPotionInternalInv(StatusEffect effect, boolean splash) {
		for (int i = 9; i <= 44; i++) {
			ItemStack stack = Helper.getPlayer().getInventory().getStack(i);
			if (stack.getItem() == Items.SPLASH_POTION && !splash)
				continue;
			if (hasEffect(stack, effect))
				return i;

		}
		return -2;
	}

	// Find item from hotbar potion
	public static int findPotionHotbar(StatusEffect effect, boolean splash) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = Helper.getPlayer().getInventory().getStack(i);
			if (stack.getItem() == Items.SPLASH_POTION && !splash)
				continue;
			if (hasEffect(stack, effect))
				return i;
		}
		return -2;
	}

	public static int findAxe() {
		int find = -2;
		for (int i = 0; i <= 8; i++)
			if (Helper.getPlayer().getInventory().getStack(i).getItem() instanceof AxeItem)
				find = i;
		return find;
	}

	public static boolean checkArmorEmpty(EquipmentSlot slot) {
		return Helper.getPlayer().getInventory().getArmorStack(slot.getEntitySlotId()).isEmpty();
	}

	public static boolean hasEffect(ItemStack stack, StatusEffect effect) {
		for (StatusEffectInstance effectInstance : PotionUtil.getPotionEffects(stack)) {
			if (effectInstance.getEffectType() != effect)
				continue;

			return true;
		}

		return false;
	}

	public static int indexSlot(int invIndex) {
		if (invIndex < 9 && invIndex != -1)
			return 44 - (8 - invIndex);
		return invIndex;
	}

	public static void switchItem(int slot, int button) {
		Helper.MC.interactionManager.clickSlot(button, slot, 1, SlotActionType.PICKUP, Helper.getPlayer());
	}

	public static void quickItem(int slot) {
		Helper.MC.interactionManager.clickSlot(Helper.getPlayer().currentScreenHandler.syncId, slot, 0,
				SlotActionType.QUICK_MOVE, Helper.getPlayer());
	}

	public static void swapItem(int from, int slot) {
		Helper.MC.interactionManager.clickSlot(Helper.getPlayer().currentScreenHandler.syncId, from, slot,
				SlotActionType.SWAP, Helper.getPlayer());
	}

	public static List<ItemStack> getContainerItems(ItemStack item) {
		List<ItemStack> items = new ArrayList<>(Collections.nCopies(27, new ItemStack(Items.AIR)));
		NbtCompound nbt = item.getTag();

		if (nbt != null && nbt.contains("BlockEntityTag")) {
			NbtCompound nbt2 = nbt.getCompound("BlockEntityTag");
			if (nbt2.contains("Items")) {
				NbtList nbt3 = (NbtList) nbt2.get("Items");
				for (int i = 0; i < nbt3.size(); i++) {
					items.set(nbt3.getCompound(i).getByte("Slot"), ItemStack.fromNbt(nbt3.getCompound(i)));
				}
			}
		}

		return items;
	}

}
