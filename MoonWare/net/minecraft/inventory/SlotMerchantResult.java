package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot
{
    /** Merchant's inventory. */
    private final InventoryMerchant theMerchantInventory;

    /** The Player whos trying to buy/sell stuff. */
    private final EntityPlayer thePlayer;
    private int removeCount;

    /** "Instance" of the Merchant. */
    private final IMerchant theMerchant;

    public SlotMerchantResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition)
    {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        thePlayer = player;
        theMerchant = merchant;
        theMerchantInventory = merchantInventory;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int amount)
    {
        if (getHasStack())
        {
            removeCount += Math.min(amount, getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack stack, int amount)
    {
        removeCount += amount;
        onCrafting(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(thePlayer.world, thePlayer, removeCount);
        removeCount = 0;
    }

    public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_)
    {
        onCrafting(p_190901_2_);
        MerchantRecipe merchantrecipe = theMerchantInventory.getCurrentRecipe();

        if (merchantrecipe != null)
        {
            ItemStack itemstack = theMerchantInventory.getStackInSlot(0);
            ItemStack itemstack1 = theMerchantInventory.getStackInSlot(1);

            if (doTrade(merchantrecipe, itemstack, itemstack1) || doTrade(merchantrecipe, itemstack1, itemstack))
            {
                theMerchant.useRecipe(merchantrecipe);
                p_190901_1_.addStat(StatList.TRADED_WITH_VILLAGER);
                theMerchantInventory.setInventorySlotContents(0, itemstack);
                theMerchantInventory.setInventorySlotContents(1, itemstack1);
            }
        }

        return p_190901_2_;
    }

    private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem)
    {
        ItemStack itemstack = trade.getItemToBuy();
        ItemStack itemstack1 = trade.getSecondItemToBuy();

        if (firstItem.getItem() == itemstack.getItem() && firstItem.getCount() >= itemstack.getCount())
        {
            if (!itemstack1.isEmpty() && !secondItem.isEmpty() && itemstack1.getItem() == secondItem.getItem() && secondItem.getCount() >= itemstack1.getCount())
            {
                firstItem.func_190918_g(itemstack.getCount());
                secondItem.func_190918_g(itemstack1.getCount());
                return true;
            }

            if (itemstack1.isEmpty() && secondItem.isEmpty())
            {
                firstItem.func_190918_g(itemstack.getCount());
                return true;
            }
        }

        return false;
    }
}
