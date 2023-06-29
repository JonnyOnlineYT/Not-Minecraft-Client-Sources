package net.minecraft.village;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MerchantRecipe
{
    /** Item the Villager buys. */
    private ItemStack itemToBuy;

    /** Second Item the Villager buys. */
    private ItemStack secondItemToBuy;

    /** Item the Villager sells. */
    private ItemStack itemToSell;

    /**
     * Saves how much has been tool used when put into to slot to be enchanted.
     */
    private int toolUses;

    /** Maximum times this trade can be used. */
    private int maxTradeUses;
    private boolean rewardsExp;

    public MerchantRecipe(NBTTagCompound tagCompound)
    {
        itemToBuy = ItemStack.EMPTY;
        secondItemToBuy = ItemStack.EMPTY;
        itemToSell = ItemStack.EMPTY;
        readFromTags(tagCompound);
    }

    public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell)
    {
        this(buy1, buy2, sell, 0, 7);
    }

    public MerchantRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell, int toolUsesIn, int maxTradeUsesIn)
    {
        itemToBuy = ItemStack.EMPTY;
        secondItemToBuy = ItemStack.EMPTY;
        itemToSell = ItemStack.EMPTY;
        itemToBuy = buy1;
        secondItemToBuy = buy2;
        itemToSell = sell;
        toolUses = toolUsesIn;
        maxTradeUses = maxTradeUsesIn;
        rewardsExp = true;
    }

    public MerchantRecipe(ItemStack buy1, ItemStack sell)
    {
        this(buy1, ItemStack.EMPTY, sell);
    }

    public MerchantRecipe(ItemStack buy1, Item sellItem)
    {
        this(buy1, new ItemStack(sellItem));
    }

    /**
     * Gets the itemToBuy.
     */
    public ItemStack getItemToBuy()
    {
        return itemToBuy;
    }

    /**
     * Gets secondItemToBuy.
     */
    public ItemStack getSecondItemToBuy()
    {
        return secondItemToBuy;
    }

    /**
     * Gets if Villager has secondItemToBuy.
     */
    public boolean hasSecondItemToBuy()
    {
        return !secondItemToBuy.isEmpty();
    }

    /**
     * Gets itemToSell.
     */
    public ItemStack getItemToSell()
    {
        return itemToSell;
    }

    public int getToolUses()
    {
        return toolUses;
    }

    public int getMaxTradeUses()
    {
        return maxTradeUses;
    }

    public void incrementToolUses()
    {
        ++toolUses;
    }

    public void increaseMaxTradeUses(int increment)
    {
        maxTradeUses += increment;
    }

    public boolean isRecipeDisabled()
    {
        return toolUses >= maxTradeUses;
    }

    /**
     * Compensates {@link net.minecraft.village.MerchantRecipe#toolUses toolUses} with {@link
     * net.minecraft.village.MerchantRecipe#maxTradeUses maxTradeUses}
     */
    public void compensateToolUses()
    {
        toolUses = maxTradeUses;
    }

    public boolean getRewardsExp()
    {
        return rewardsExp;
    }

    public void readFromTags(NBTTagCompound tagCompound)
    {
        NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("buy");
        itemToBuy = new ItemStack(nbttagcompound);
        NBTTagCompound nbttagcompound1 = tagCompound.getCompoundTag("sell");
        itemToSell = new ItemStack(nbttagcompound1);

        if (tagCompound.hasKey("buyB", 10))
        {
            secondItemToBuy = new ItemStack(tagCompound.getCompoundTag("buyB"));
        }

        if (tagCompound.hasKey("uses", 99))
        {
            toolUses = tagCompound.getInteger("uses");
        }

        if (tagCompound.hasKey("maxUses", 99))
        {
            maxTradeUses = tagCompound.getInteger("maxUses");
        }
        else
        {
            maxTradeUses = 7;
        }

        if (tagCompound.hasKey("rewardExp", 1))
        {
            rewardsExp = tagCompound.getBoolean("rewardExp");
        }
        else
        {
            rewardsExp = true;
        }
    }

    public NBTTagCompound writeToTags()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setTag("buy", itemToBuy.writeToNBT(new NBTTagCompound()));
        nbttagcompound.setTag("sell", itemToSell.writeToNBT(new NBTTagCompound()));

        if (!secondItemToBuy.isEmpty())
        {
            nbttagcompound.setTag("buyB", secondItemToBuy.writeToNBT(new NBTTagCompound()));
        }

        nbttagcompound.setInteger("uses", toolUses);
        nbttagcompound.setInteger("maxUses", maxTradeUses);
        nbttagcompound.setBoolean("rewardExp", rewardsExp);
        return nbttagcompound;
    }
}
