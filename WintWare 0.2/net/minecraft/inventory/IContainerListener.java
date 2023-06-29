package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IContainerListener {
   void updateCraftingInventory(Container var1, NonNullList<ItemStack> var2);

   void sendSlotContents(Container var1, int var2, ItemStack var3);

   void sendProgressBarUpdate(Container var1, int var2, int var3);

   void sendAllWindowProperties(Container var1, IInventory var2);
}
