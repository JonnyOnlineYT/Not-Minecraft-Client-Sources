package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDispenser extends TileEntityLockableLoot
{
    private static final Random RNG = new Random();
    private NonNullList<ItemStack> stacks = NonNullList.func_191197_a(9, ItemStack.EMPTY);

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 9;
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public int getDispenseSlot()
    {
        fillWithLoot(null);
        int i = -1;
        int j = 1;

        for (int k = 0; k < stacks.size(); ++k)
        {
            if (!stacks.get(k).isEmpty() && RNG.nextInt(j++) == 0)
            {
                i = k;
            }
        }

        return i;
    }

    /**
     * Add the given ItemStack to this Dispenser. Return the Slot the Item was placed in or -1 if no free slot is
     * available.
     */
    public int addItemStack(ItemStack stack)
    {
        for (int i = 0; i < stacks.size(); ++i)
        {
            if (stacks.get(i).isEmpty())
            {
                setInventorySlotContents(i, stack);
                return i;
            }
        }

        return -1;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return hasCustomName() ? field_190577_o : "container.dispenser";
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDispenser.class, "Items"));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        stacks = NonNullList.func_191197_a(getSizeInventory(), ItemStack.EMPTY);

        if (!checkLootAndRead(compound))
        {
            ItemStackHelper.func_191283_b(compound, stacks);
        }

        if (compound.hasKey("CustomName", 8))
        {
            field_190577_o = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!checkLootAndWrite(compound))
        {
            ItemStackHelper.func_191282_a(compound, stacks);
        }

        if (hasCustomName())
        {
            compound.setString("CustomName", field_190577_o);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public String getGuiID()
    {
        return "minecraft:dispenser";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        fillWithLoot(playerIn);
        return new ContainerDispenser(playerInventory, this);
    }

    protected NonNullList<ItemStack> func_190576_q()
    {
        return stacks;
    }
}
