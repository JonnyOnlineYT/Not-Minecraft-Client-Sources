package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerShulkerBox extends Container {
   private final IInventory field_190899_a;

   public ContainerShulkerBox(InventoryPlayer p_i47266_1_, IInventory p_i47266_2_, EntityPlayer p_i47266_3_) {
      this.field_190899_a = p_i47266_2_;
      p_i47266_2_.openInventory(p_i47266_3_);
      int i = true;
      int j = true;

      int j1;
      int k1;
      for(j1 = 0; j1 < 3; ++j1) {
         for(k1 = 0; k1 < 9; ++k1) {
            this.addSlotToContainer(new SlotShulkerBox(p_i47266_2_, k1 + j1 * 9, 8 + k1 * 18, 18 + j1 * 18));
         }
      }

      for(j1 = 0; j1 < 3; ++j1) {
         for(k1 = 0; k1 < 9; ++k1) {
            this.addSlotToContainer(new Slot(p_i47266_1_, k1 + j1 * 9 + 9, 8 + k1 * 18, 84 + j1 * 18));
         }
      }

      for(j1 = 0; j1 < 9; ++j1) {
         this.addSlotToContainer(new Slot(p_i47266_1_, j1, 8 + j1 * 18, 142));
      }

   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return this.field_190899_a.isUsableByPlayer(playerIn);
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = ItemStack.field_190927_a;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (index < this.field_190899_a.getSizeInventory()) {
            if (!this.mergeItemStack(itemstack1, this.field_190899_a.getSizeInventory(), this.inventorySlots.size(), true)) {
               return ItemStack.field_190927_a;
            }
         } else if (!this.mergeItemStack(itemstack1, 0, this.field_190899_a.getSizeInventory(), false)) {
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
      this.field_190899_a.closeInventory(playerIn);
   }
}
