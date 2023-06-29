package net.minecraft.inventory;

import java.util.Iterator;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
   private final IMerchant theMerchant;
   private final NonNullList<ItemStack> theInventory;
   private final EntityPlayer thePlayer;
   private MerchantRecipe currentRecipe;
   private int currentRecipeIndex;

   public InventoryMerchant(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
      this.theInventory = NonNullList.func_191197_a(3, ItemStack.field_190927_a);
      this.thePlayer = thePlayerIn;
      this.theMerchant = theMerchantIn;
   }

   public int getSizeInventory() {
      return this.theInventory.size();
   }

   public boolean func_191420_l() {
      Iterator var1 = this.theInventory.iterator();

      ItemStack itemstack;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         itemstack = (ItemStack)var1.next();
      } while(itemstack.func_190926_b());

      return false;
   }

   public ItemStack getStackInSlot(int index) {
      return (ItemStack)this.theInventory.get(index);
   }

   public ItemStack decrStackSize(int index, int count) {
      ItemStack itemstack = (ItemStack)this.theInventory.get(index);
      if (index == 2 && !itemstack.func_190926_b()) {
         return ItemStackHelper.getAndSplit(this.theInventory, index, itemstack.func_190916_E());
      } else {
         ItemStack itemstack1 = ItemStackHelper.getAndSplit(this.theInventory, index, count);
         if (!itemstack1.func_190926_b() && this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
         }

         return itemstack1;
      }
   }

   private boolean inventoryResetNeededOnSlotChange(int slotIn) {
      return slotIn == 0 || slotIn == 1;
   }

   public ItemStack removeStackFromSlot(int index) {
      return ItemStackHelper.getAndRemove(this.theInventory, index);
   }

   public void setInventorySlotContents(int index, ItemStack stack) {
      this.theInventory.set(index, stack);
      if (!stack.func_190926_b() && stack.func_190916_E() > this.getInventoryStackLimit()) {
         stack.func_190920_e(this.getInventoryStackLimit());
      }

      if (this.inventoryResetNeededOnSlotChange(index)) {
         this.resetRecipeAndSlots();
      }

   }

   public String getName() {
      return "mob.villager";
   }

   public boolean hasCustomName() {
      return false;
   }

   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUsableByPlayer(EntityPlayer player) {
      return this.theMerchant.getCustomer() == player;
   }

   public void openInventory(EntityPlayer player) {
   }

   public void closeInventory(EntityPlayer player) {
   }

   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;
   }

   public void markDirty() {
      this.resetRecipeAndSlots();
   }

   public void resetRecipeAndSlots() {
      this.currentRecipe = null;
      ItemStack itemstack = (ItemStack)this.theInventory.get(0);
      ItemStack itemstack1 = (ItemStack)this.theInventory.get(1);
      if (itemstack.func_190926_b()) {
         itemstack = itemstack1;
         itemstack1 = ItemStack.field_190927_a;
      }

      if (itemstack.func_190926_b()) {
         this.setInventorySlotContents(2, ItemStack.field_190927_a);
      } else {
         MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
         if (merchantrecipelist != null) {
            MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
            if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
               this.currentRecipe = merchantrecipe;
               this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
            } else if (!itemstack1.func_190926_b()) {
               merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
               if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                  this.currentRecipe = merchantrecipe;
                  this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
               } else {
                  this.setInventorySlotContents(2, ItemStack.field_190927_a);
               }
            } else {
               this.setInventorySlotContents(2, ItemStack.field_190927_a);
            }
         }

         this.theMerchant.verifySellingItem(this.getStackInSlot(2));
      }

   }

   public MerchantRecipe getCurrentRecipe() {
      return this.currentRecipe;
   }

   public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
      this.currentRecipeIndex = currentRecipeIndexIn;
      this.resetRecipeAndSlots();
   }

   public int getField(int id) {
      return 0;
   }

   public void setField(int id, int value) {
   }

   public int getFieldCount() {
      return 0;
   }

   public void clear() {
      this.theInventory.clear();
   }
}
