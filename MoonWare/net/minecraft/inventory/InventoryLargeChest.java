package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest implements ILockableContainer
{
    /** Name of the chest. */
    private final String name;

    /** Inventory object corresponding to double chest upper part */
    private final ILockableContainer upperChest;

    /** Inventory object corresponding to double chest lower part */
    private final ILockableContainer lowerChest;

    public InventoryLargeChest(String nameIn, ILockableContainer upperChestIn, ILockableContainer lowerChestIn)
    {
        name = nameIn;

        if (upperChestIn == null)
        {
            upperChestIn = lowerChestIn;
        }

        if (lowerChestIn == null)
        {
            lowerChestIn = upperChestIn;
        }

        upperChest = upperChestIn;
        lowerChest = lowerChestIn;

        if (upperChestIn.isLocked())
        {
            lowerChestIn.setLockCode(upperChestIn.getLockCode());
        }
        else if (lowerChestIn.isLocked())
        {
            upperChestIn.setLockCode(lowerChestIn.getLockCode());
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return upperChest.getSizeInventory() + lowerChest.getSizeInventory();
    }

    public boolean func_191420_l()
    {
        return upperChest.func_191420_l() && lowerChest.func_191420_l();
    }

    /**
     * Return whether the given inventory is part of this large chest.
     */
    public boolean isPartOfLargeChest(IInventory inventoryIn)
    {
        return upperChest == inventoryIn || lowerChest == inventoryIn;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        if (upperChest.hasCustomName())
        {
            return upperChest.getName();
        }
        else
        {
            return lowerChest.hasCustomName() ? lowerChest.getName() : name;
        }
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return upperChest.hasCustomName() || lowerChest.hasCustomName();
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return hasCustomName() ? new TextComponent(getName()) : new TranslatableComponent(getName());
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        return index >= upperChest.getSizeInventory() ? lowerChest.getStackInSlot(index - upperChest.getSizeInventory()) : upperChest.getStackInSlot(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        return index >= upperChest.getSizeInventory() ? lowerChest.decrStackSize(index - upperChest.getSizeInventory(), count) : upperChest.decrStackSize(index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return index >= upperChest.getSizeInventory() ? lowerChest.removeStackFromSlot(index - upperChest.getSizeInventory()) : upperChest.removeStackFromSlot(index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= upperChest.getSizeInventory())
        {
            lowerChest.setInventorySlotContents(index - upperChest.getSizeInventory(), stack);
        }
        else
        {
            upperChest.setInventorySlotContents(index, stack);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return upperChest.getInventoryStackLimit();
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        upperChest.markDirty();
        lowerChest.markDirty();
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return upperChest.isUsableByPlayer(player) && lowerChest.isUsableByPlayer(player);
    }

    public void openInventory(EntityPlayer player)
    {
        upperChest.openInventory(player);
        lowerChest.openInventory(player);
    }

    public void closeInventory(EntityPlayer player)
    {
        upperChest.closeInventory(player);
        lowerChest.closeInventory(player);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value)
    {
    }

    public int getFieldCount()
    {
        return 0;
    }

    public boolean isLocked()
    {
        return upperChest.isLocked() || lowerChest.isLocked();
    }

    public void setLockCode(LockCode code)
    {
        upperChest.setLockCode(code);
        lowerChest.setLockCode(code);
    }

    public LockCode getLockCode()
    {
        return upperChest.getLockCode();
    }

    public String getGuiID()
    {
        return upperChest.getGuiID();
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerChest(playerInventory, this, playerIn);
    }

    public void clear()
    {
        upperChest.clear();
        lowerChest.clear();
    }
}
