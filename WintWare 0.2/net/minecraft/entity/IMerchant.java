package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public interface IMerchant {
   void setCustomer(EntityPlayer var1);

   @Nullable
   EntityPlayer getCustomer();

   @Nullable
   MerchantRecipeList getRecipes(EntityPlayer var1);

   void setRecipes(MerchantRecipeList var1);

   void useRecipe(MerchantRecipe var1);

   void verifySellingItem(ItemStack var1);

   ITextComponent getDisplayName();

   World func_190670_t_();

   BlockPos func_190671_u_();
}
