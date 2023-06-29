package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerShulkerBox extends Container
{
    private final IInventory field_190899_a;

    public ContainerShulkerBox(InventoryPlayer p_i47266_1_, IInventory p_i47266_2_, EntityPlayer p_i47266_3_)
    {
        field_190899_a = p_i47266_2_;
        p_i47266_2_.openInventory(p_i47266_3_);
        int i = 3;
        int j = 9;

        for (int k = 0; k < 3; ++k)
        {
            for (int l = 0; l < 9; ++l)
            {
                addSlotToContainer(new SlotShulkerBox(p_i47266_2_, l + k * 9, 8 + l * 18, 18 + k * 18));
            }
        }

        for (int i1 = 0; i1 < 3; ++i1)
        {
            for (int k1 = 0; k1 < 9; ++k1)
            {
                addSlotToContainer(new Slot(p_i47266_1_, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1)
        {
            addSlotToContainer(new Slot(p_i47266_1_, j1, 8 + j1 * 18, 142));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return field_190899_a.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < field_190899_a.getSizeInventory())
            {
                if (!mergeItemStack(itemstack1, field_190899_a.getSizeInventory(), inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, field_190899_a.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        field_190899_a.closeInventory(playerIn);
    }
}
