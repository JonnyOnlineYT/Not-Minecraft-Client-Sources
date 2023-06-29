package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public interface IRecipe {
   boolean matches(InventoryCrafting var1, World var2);

   ItemStack getCraftingResult(InventoryCrafting var1);

   boolean func_194133_a(int var1, int var2);

   ItemStack getRecipeOutput();

   NonNullList<ItemStack> getRemainingItems(InventoryCrafting var1);

   default NonNullList<Ingredient> func_192400_c() {
      return NonNullList.func_191196_a();
   }

   default boolean func_192399_d() {
      return false;
   }

   default String func_193358_e() {
      return "";
   }
}
