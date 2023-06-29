package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;

public class InventoryBasic implements IInventory
{
    private String inventoryTitle;
    private final int slotsCount;
    private final NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> changeListeners;
    private boolean hasCustomName;

    public InventoryBasic(String title, boolean customName, int slotCount)
    {
        inventoryTitle = title;
        hasCustomName = customName;
        slotsCount = slotCount;
        inventoryContents = NonNullList.func_191197_a(slotCount, ItemStack.EMPTY);
    }

    public InventoryBasic(Component title, int slotCount)
    {
        this(title.asString(), true, slotCount);
    }

    /**
     * Add a listener that will be notified when any item in this inventory is modified.
     */
    public void addInventoryChangeListener(IInventoryChangedListener listener)
    {
        if (changeListeners == null)
        {
            changeListeners = Lists.newArrayList();
        }

        changeListeners.add(listener);
    }

    /**
     * removes the specified IInvBasic from receiving further change notices
     */
    public void removeInventoryChangeListener(IInventoryChangedListener listener)
    {
        changeListeners.remove(listener);
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        return index >= 0 && index < inventoryContents.size() ? inventoryContents.get(index) : ItemStack.EMPTY;
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(inventoryContents, index, count);

        if (!itemstack.isEmpty())
        {
            markDirty();
        }

        return itemstack;
    }

    public ItemStack addItem(ItemStack stack)
    {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < slotsCount; ++i)
        {
            ItemStack itemstack1 = getStackInSlot(i);

            if (itemstack1.isEmpty())
            {
                setInventorySlotContents(i, itemstack);
                markDirty();
                return ItemStack.EMPTY;
            }

            if (ItemStack.areItemsEqual(itemstack1, itemstack))
            {
                int j = Math.min(getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

                if (k > 0)
                {
                    itemstack1.func_190917_f(k);
                    itemstack.func_190918_g(k);

                    if (itemstack.isEmpty())
                    {
                        markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack.getCount() != stack.getCount())
        {
            markDirty();
        }

        return itemstack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack itemstack = inventoryContents.get(index);

        if (itemstack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            inventoryContents.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        inventoryContents.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
        {
            stack.func_190920_e(getInventoryStackLimit());
        }

        markDirty();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return slotsCount;
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : inventoryContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return inventoryTitle;
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return hasCustomName;
    }

    /**
     * Sets the name of this inventory. This is displayed to the client on opening.
     */
    public void setCustomName(String inventoryTitleIn)
    {
        hasCustomName = true;
        inventoryTitle = inventoryTitleIn;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return hasCustomName() ? new TextComponent(getName()) : new TranslatableComponent(getName());
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
        if (changeListeners != null)
        {
            for (int i = 0; i < changeListeners.size(); ++i)
            {
                changeListeners.get(i).onInventoryChanged(this);
            }
        }
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
        inventoryContents.clear();
    }
}
