package ru.wendoxd.utils.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.item.*;

public class InventoryUtils {

	private static final Minecraft mc = Minecraft.getMinecraft();

	public static int getPearls() {
		for (int i = 0; i < 9; i++) {
			if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemEnderPearl) {
				return i;
			}
		}
		return -1;
	}

	public static int getBlocks() {
		for (int i = 0; i < 9; i++) {
			if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
				return i;
			}
		}
		return -1;
	}

	public static boolean doesHotbarHaveBlock() {
		for (int i = 0; i < 9; ++i) {
			if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
				return true;
			}
		}
		return false;
	}

	public static int getAxe() {
		for (int i = 0; i < 9; i++) {
			if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
				return i;
			}
		}
		return -1;
	}
}
