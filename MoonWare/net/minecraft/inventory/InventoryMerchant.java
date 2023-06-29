package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory
{
    private final IMerchant theMerchant;
    private final NonNullList<ItemStack> theInventory = NonNullList.func_191197_a(3, ItemStack.EMPTY);
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;

    public InventoryMerchant(EntityPlayer thePlayerIn, IMerchant theMerchantIn)
    {
        thePlayer = thePlayerIn;
        theMerchant = theMerchantIn;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return theInventory.size();
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : theInventory)
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
        return theInventory.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = theInventory.get(index);

        if (index == 2 && !itemstack.isEmpty())
        {
            return ItemStackHelper.getAndSplit(theInventory, index, itemstack.getCount());
        }
        else
        {
            ItemStack itemstack1 = ItemStackHelper.getAndSplit(theInventory, index, count);

            if (!itemstack1.isEmpty() && inventoryResetNeededOnSlotChange(index))
            {
                resetRecipeAndSlots();
            }

            return itemstack1;
        }
    }

    /**
     * if par1 slot has changed, does resetRecipeAndSlots need to be called?
     */
    private boolean inventoryResetNeededOnSlotChange(int slotIn)
    {
        return slotIn == 0 || slotIn == 1;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(theInventory, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        theInventory.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
        {
            stack.func_190920_e(getInventoryStackLimit());
        }

        if (inventoryResetNeededOnSlotChange(index))
        {
            resetRecipeAndSlots();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return "mob.villager";
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
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return theMerchant.getCustomer() == player;
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

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        resetRecipeAndSlots();
    }

    public void resetRecipeAndSlots()
    {
        currentRecipe = null;
        ItemStack itemstack = theInventory.get(0);
        ItemStack itemstack1 = theInventory.get(1);

        if (itemstack.isEmpty())
        {
            itemstack = itemstack1;
            itemstack1 = ItemStack.EMPTY;
        }

        if (itemstack.isEmpty())
        {
            setInventorySlotContents(2, ItemStack.EMPTY);
        }
        else
        {
            MerchantRecipeList merchantrecipelist = theMerchant.getRecipes(thePlayer);

            if (merchantrecipelist != null)
            {
                MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, currentRecipeIndex);

                if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled())
                {
                    currentRecipe = merchantrecipe;
                    setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                }
                else if (!itemstack1.isEmpty())
                {
                    merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, currentRecipeIndex);

                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled())
                    {
                        currentRecipe = merchantrecipe;
                        setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                    }
                    else
                    {
                        setInventorySlotContents(2, ItemStack.EMPTY);
                    }
                }
                else
                {
                    setInventorySlotContents(2, ItemStack.EMPTY);
                }
            }

            theMerchant.verifySellingItem(getStackInSlot(2));
        }
    }

    public MerchantRecipe getCurrentRecipe()
    {
        return currentRecipe;
    }

    public void setCurrentRecipeIndex(int currentRecipeIndexIn)
    {
        currentRecipeIndex = currentRecipeIndexIn;
        resetRecipeAndSlots();
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
        theInventory.clear();
    }
}
