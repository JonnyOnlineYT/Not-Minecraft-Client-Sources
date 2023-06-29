package org.spray.heaven.util;

import org.spray.heaven.main.Wrapper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

public class InvUtil {

	public static int findItem(Item item, int index, int slots) {
		int find = -1;
		for (int i = index; i <= slots; i++)
			if (Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() == item)
				find = i;
		return find;
	}

	public static int findItem(Item item) {
		return findItem(item, 0, 44);
	}

	public static int findItemFromHotbar(Item item) {
		return findItem(item, 0, 8);
	}

	public static int findObsidian() {
		for (int i = 0; i < 9; ++i) {
			final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
				Block block = ((ItemBlock) stack.getItem()).getBlock();

				if (block instanceof BlockObsidian) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int findSwordSlot() {
		int slot = -1;
		for (int i = 0; i <= 8; i++)
			if (Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() instanceof ItemSword)
				slot = i;
		return slot;
	}

	public static int findToolSlot() {
		int slot = -1;
		for (int i = 0; i <= 8; i++)
			if (Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() instanceof ItemTool)
				slot = i;
		return slot;
	}

	public static int findPotionFromHotbar(Potion effect, boolean splash) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
			if (stack.getItem() == Items.SPLASH_POTION && !splash)
				continue;
			if (hasEffect(stack, effect))
				return i;
		}
		return -1;
	}

	public static int stackSize(Item item) {
		int size = 0;
		for (int i = 0; i <= 44; i++)
			if (Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() == item)
				size += 1;
		return size;
	}

	public static boolean hasEffect(ItemStack stack, Potion potion) {
		for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
			if (effect.getPotion() == potion)
				return true;
		}
		return false;
	}

	public static int findAxe() {
		int find = -2;
		for (int i = 0; i <= 8; i++) {
			if (Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() instanceof ItemAxe)
				find = i;
		}
		return find;
	}

	public static int findBlock() {
		int find = -2;
		for (int i = 0; i <= 8; i++) {
			if (Wrapper.getPlayer().inventory.getStackInSlot(i).getItem() instanceof ItemBlock)
				find = i;
		}
		return find;
	}

	public static boolean pickUpItem(int slot, int button) {
		try {
			Wrapper.MC.playerController.windowClick(0, slot, button, ClickType.PICKUP, Wrapper.getPlayer());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
