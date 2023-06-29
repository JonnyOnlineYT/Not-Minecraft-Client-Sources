package net.minecraft.inventory;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;

public class InventoryCrafting implements IInventory
{
    private final NonNullList<ItemStack> stackList;

    /** the width of the crafting inventory */
    private final int inventoryWidth;
    private final int inventoryHeight;

    /**
     * Class containing the callbacks for the events on_GUIClosed and on_CraftMaxtrixChanged.
     */
    private final Container eventHandler;

    public InventoryCrafting(Container eventHandlerIn, int width, int height)
    {
        stackList = NonNullList.func_191197_a(width * height, ItemStack.EMPTY);
        eventHandler = eventHandlerIn;
        inventoryWidth = width;
        inventoryHeight = height;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return stackList.size();
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : stackList)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        return index >= getSizeInventory() ? ItemStack.EMPTY : stackList.get(index);
    }

    /**
     * Gets the ItemStack in the slot specified.
     */
    public ItemStack getStackInRowAndColumn(int row, int column)
    {
        return row >= 0 && row < inventoryWidth && column >= 0 && column <= inventoryHeight ? getStackInSlot(row + column * inventoryWidth) : ItemStack.EMPTY;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return "container.crafting";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return false;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return hasCustomName() ? new TextComponent(getName()) : new TranslatableComponent(getName());
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(stackList, index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(stackList, index, count);

        if (!itemstack.isEmpty())
        {
            eventHandler.onCraftMatrixChanged(this);
        }

        return itemstack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        stackList.set(index, stack);
        eventHandler.onCraftMatrixChanged(this);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
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

    public void clear()
    {
        stackList.clear();
    }

    public int getHeight()
    {
        return inventoryHeight;
    }

    public int getWidth()
    {
        return inventoryWidth;
    }

    public void func_194018_a(RecipeItemHelper p_194018_1_)
    {
        for (ItemStack itemstack : stackList)
        {
            p_194018_1_.func_194112_a(itemstack);
        }
    }
}
