package net.minecraft.inventory;

import java.util.Iterator;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryCrafting implements IInventory {
   private final NonNullList<ItemStack> stackList;
   private final int inventoryWidth;
   private final int inventoryHeight;
   private final Container eventHandler;

   public InventoryCrafting(Container eventHandlerIn, int width, int height) {
      this.stackList = NonNullList.func_191197_a(width * height, ItemStack.field_190927_a);
      this.eventHandler = eventHandlerIn;
      this.inventoryWidth = width;
      this.inventoryHeight = height;
   }

   public int getSizeInventory() {
      return this.stackList.size();
   }

   public boolean func_191420_l() {
      Iterator var1 = this.stackList.iterator();

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
      return index >= this.getSizeInventory() ? ItemStack.field_190927_a : (ItemStack)this.stackList.get(index);
   }

   public ItemStack getStackInRowAndColumn(int row, int column) {
      return row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight ? this.getStackInSlot(row + column * this.inventoryWidth) : ItemStack.field_190927_a;
   }

   public String getName() {
      return "container.crafting";
   }

   public boolean hasCustomName() {
      return false;
   }

   public ITextComponent getDisplayName() {
      return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
   }

   public ItemStack removeStackFromSlot(int index) {
      return ItemStackHelper.getAndRemove(this.stackList, index);
   }

   public ItemStack decrStackSize(int index, int count) {
      ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);
      if (!itemstack.func_190926_b()) {
         this.eventHandler.onCraftMatrixChanged(this);
      }

      return itemstack;
   }

   public void setInventorySlotContents(int index, ItemStack stack) {
      this.stackList.set(index, stack);
      this.eventHandler.onCraftMatrixChanged(this);
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public void markDirty() {
   }

   public boolean isUsableByPlayer(EntityPlayer player) {
      return true;
   }

   public void openInventory(EntityPlayer player) {
   }

   public void closeInventory(EntityPlayer player) {
   }

   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;
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
      this.stackList.clear();
   }

   public int getHeight() {
      return this.inventoryHeight;
   }

   public int getWidth() {
      return this.inventoryWidth;
   }

   public void func_194018_a(RecipeItemHelper p_194018_1_) {
      Iterator var2 = this.stackList.iterator();

      while(var2.hasNext()) {
         ItemStack itemstack = (ItemStack)var2.next();
         p_194018_1_.func_194112_a(itemstack);
      }

   }
}
