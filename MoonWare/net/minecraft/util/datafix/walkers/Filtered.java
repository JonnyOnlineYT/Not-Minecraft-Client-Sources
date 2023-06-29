package net.minecraft.util.datafix.walkers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

public abstract class Filtered implements IDataWalker
{
    private final Namespaced key;

    public Filtered(Class<?> p_i47309_1_)
    {
        if (Entity.class.isAssignableFrom(p_i47309_1_))
        {
            key = EntityList.getKey((Class<Entity>)p_i47309_1_);
        }
        else if (TileEntity.class.isAssignableFrom(p_i47309_1_))
        {
            key = TileEntity.func_190559_a((Class<TileEntity>)p_i47309_1_);
        }
        else
        {
            key = null;
        }
    }

    public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
    {
        if ((new Namespaced(compound.getString("id"))).equals(key))
        {
            compound = filteredProcess(fixer, compound, versionIn);
        }

        return compound;
    }

    abstract NBTTagCompound filteredProcess(IDataFixer fixer, NBTTagCompound compound, int versionIn);
}
