package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityFlowerPot extends TileEntity
{
    private Item flowerPotItem;
    private int flowerPotData;

    public TileEntityFlowerPot()
    {
    }

    public TileEntityFlowerPot(Item potItem, int potData)
    {
        flowerPotItem = potItem;
        flowerPotData = potData;
    }

    public static void registerFixesFlowerPot(DataFixer fixer)
    {
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        Namespaced resourcelocation = Item.REGISTRY.getNameForObject(flowerPotItem);
        compound.setString("Item", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setInteger("Data", flowerPotData);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("Item", 8))
        {
            flowerPotItem = Item.getByNameOrId(compound.getString("Item"));
        }
        else
        {
            flowerPotItem = Item.getItemById(compound.getInteger("Item"));
        }

        flowerPotData = compound.getInteger("Data");
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 5, getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public void func_190614_a(ItemStack p_190614_1_)
    {
        flowerPotItem = p_190614_1_.getItem();
        flowerPotData = p_190614_1_.getMetadata();
    }

    public ItemStack getFlowerItemStack()
    {
        return flowerPotItem == null ? ItemStack.EMPTY : new ItemStack(flowerPotItem, 1, flowerPotData);
    }

    @Nullable
    public Item getFlowerPotItem()
    {
        return flowerPotItem;
    }

    public int getFlowerPotData()
    {
        return flowerPotData;
    }
}
