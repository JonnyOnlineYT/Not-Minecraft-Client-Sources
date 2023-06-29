package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class NpcMerchant implements IMerchant {
   private final InventoryMerchant theMerchantInventory;
   private final EntityPlayer customer;
   private MerchantRecipeList recipeList;
   private final ITextComponent name;

   public NpcMerchant(EntityPlayer customerIn, ITextComponent nameIn) {
      this.customer = customerIn;
      this.name = nameIn;
      this.theMerchantInventory = new InventoryMerchant(customerIn, this);
   }

   @Nullable
   public EntityPlayer getCustomer() {
      return this.customer;
   }

   public void setCustomer(@Nullable EntityPlayer player) {
   }

   @Nullable
   public MerchantRecipeList getRecipes(EntityPlayer player) {
      return this.recipeList;
   }

   public void setRecipes(@Nullable MerchantRecipeList recipeList) {
      this.recipeList = recipeList;
   }

   public void useRecipe(MerchantRecipe recipe) {
      recipe.incrementToolUses();
   }

   public void verifySellingItem(ItemStack stack) {
   }

   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.name != null ? this.name : new TextComponentTranslation("entity.Villager.name", new Object[0]));
   }

   public World func_190670_t_() {
      return this.customer.world;
   }

   public BlockPos func_190671_u_() {
      return new BlockPos(this.customer);
   }
}
