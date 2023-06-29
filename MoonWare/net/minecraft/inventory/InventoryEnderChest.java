package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic
{
    private TileEntityEnderChest associatedChest;

    public InventoryEnderChest()
    {
        super("container.enderchest", false, 27);
    }

    public void setChestTileEntity(TileEntityEnderChest chestTileEntity)
    {
        associatedChest = chestTileEntity;
    }

    public void loadInventoryFromNBT(NBTTagList p_70486_1_)
    {
        for (int i = 0; i < getSizeInventory(); ++i)
        {
            setInventorySlotContents(i, ItemStack.EMPTY);
        }

        for (int k = 0; k < p_70486_1_.tagCount(); ++k)
        {
            NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < getSizeInventory())
            {
                setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }
    }

    public NBTTagList saveInventoryToNBT()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            ItemStack itemstack = getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return (associatedChest == null || associatedChest.canBeUsed(player)) && super.isUsableByPlayer(player);
    }

    public void openInventory(EntityPlayer player)
    {
        if (associatedChest != null)
        {
            associatedChest.openChest();
        }

        super.openInventory(player);
    }

    public void closeInventory(EntityPlayer player)
    {
        if (associatedChest != null)
        {
            associatedChest.closeChest();
        }

        super.closeInventory(player);
        associatedChest = null;
    }
}
