package net.minecraft.tileentity;

import net.minecraft.block.BlockBed;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityBed extends TileEntity
{
    private EnumDyeColor field_193053_a = EnumDyeColor.RED;

    public void func_193051_a(ItemStack p_193051_1_)
    {
        func_193052_a(EnumDyeColor.byMetadata(p_193051_1_.getMetadata()));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("color"))
        {
            field_193053_a = EnumDyeColor.byMetadata(compound.getInteger("color"));
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("color", field_193053_a.getMetadata());
        return compound;
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 11, getUpdateTag());
    }

    public EnumDyeColor func_193048_a()
    {
        return field_193053_a;
    }

    public void func_193052_a(EnumDyeColor p_193052_1_)
    {
        field_193053_a = p_193052_1_;
        markDirty();
    }

    public boolean func_193050_e()
    {
        return BlockBed.func_193385_b(getBlockMetadata());
    }

    public ItemStack func_193049_f()
    {
        return new ItemStack(Items.BED, 1, field_193053_a.getMetadata());
    }
}
