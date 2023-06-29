package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class NpcMerchant implements IMerchant
{
    /** Instance of Merchants Inventory. */
    private final InventoryMerchant theMerchantInventory;

    /** This merchant's current player customer. */
    private final EntityPlayer customer;

    /** The MerchantRecipeList instance. */
    private MerchantRecipeList recipeList;
    private final Component name;

    public NpcMerchant(EntityPlayer customerIn, Component nameIn)
    {
        customer = customerIn;
        name = nameIn;
        theMerchantInventory = new InventoryMerchant(customerIn, this);
    }

    @Nullable
    public EntityPlayer getCustomer()
    {
        return customer;
    }

    public void setCustomer(@Nullable EntityPlayer player)
    {
    }

    @Nullable
    public MerchantRecipeList getRecipes(EntityPlayer player)
    {
        return recipeList;
    }

    public void setRecipes(@Nullable MerchantRecipeList recipeList)
    {
        this.recipeList = recipeList;
    }

    public void useRecipe(MerchantRecipe recipe)
    {
        recipe.incrementToolUses();
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack)
    {
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        return name != null ? name : new TranslatableComponent("entity.Villager.name");
    }

    public World func_190670_t_()
    {
        return customer.world;
    }

    public BlockPos func_190671_u_()
    {
        return new BlockPos(customer);
    }
}
