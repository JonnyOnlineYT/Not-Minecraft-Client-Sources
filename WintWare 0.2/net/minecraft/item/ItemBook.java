package net.minecraft.item;

public class ItemBook extends Item {
   public boolean isItemTool(ItemStack stack) {
      return stack.func_190916_E() == 1;
   }

   public int getItemEnchantability() {
      return 1;
   }
}
