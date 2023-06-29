package net.minecraft.inventory;

import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container {
   private final IInventory horseInventory;
   private final AbstractHorse theHorse;

   public ContainerHorseInventory(IInventory playerInventory, IInventory horseInventoryIn, final AbstractHorse horse, EntityPlayer player) {
      this.horseInventory = horseInventoryIn;
      this.theHorse = horse;
      int i = true;
      horseInventoryIn.openInventory(player);
      int j = true;
      this.addSlotToContainer(new Slot(horseInventoryIn, 0, 8, 18) {
         public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == Items.SADDLE && !this.getHasStack() && horse.func_190685_dA();
         }

         public boolean canBeHovered() {
            return horse.func_190685_dA();
         }
      });
      this.addSlotToContainer(new Slot(horseInventoryIn, 1, 8, 36) {
         public boolean isItemValid(ItemStack stack) {
            return horse.func_190682_f(stack);
         }

         public boolean canBeHovered() {
            return horse.func_190677_dK();
         }

         public int getSlotStackLimit() {
            return 1;
         }
      });
      int j1;
      int k1;
      if (horse instanceof AbstractChestHorse && ((AbstractChestHorse)horse).func_190695_dh()) {
         for(j1 = 0; j1 < 3; ++j1) {
            for(k1 = 0; k1 < ((AbstractChestHorse)horse).func_190696_dl(); ++k1) {
               this.addSlotToContainer(new Slot(horseInventoryIn, 2 + k1 + j1 * ((AbstractChestHorse)horse).func_190696_dl(), 80 + k1 * 18, 18 + j1 * 18));
            }
         }
      }

      for(j1 = 0; j1 < 3; ++j1) {
         for(k1 = 0; k1 < 9; ++k1) {
            this.addSlotToContainer(new Slot(playerInventory, k1 + j1 * 9 + 9, 8 + k1 * 18, 102 + j1 * 18 + -18));
         }
      }

      for(j1 = 0; j1 < 9; ++j1) {
         this.addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
      }

   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return this.horseInventory.isUsableByPlayer(playerIn) && this.theHorse.isEntityAlive() && this.theHorse.getDistanceToEntity(playerIn) < 8.0F;
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = ItemStack.field_190927_a;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (index < this.horseInventory.getSizeInventory()) {
            if (!this.mergeItemStack(itemstack1, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
               return ItemStack.field_190927_a;
            }
         } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
            if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (this.getSlot(0).isItemValid(itemstack1)) {
            if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.horseInventory.getSizeInventory(), false)) {
            return ItemStack.field_190927_a;
         }

         if (itemstack1.func_190926_b()) {
            slot.putStack(ItemStack.field_190927_a);
         } else {
            slot.onSlotChanged();
         }
      }

      return itemstack;
   }

   public void onContainerClosed(EntityPlayer playerIn) {
      super.onContainerClosed(playerIn);
      this.horseInventory.closeInventory(playerIn);
   }
}
