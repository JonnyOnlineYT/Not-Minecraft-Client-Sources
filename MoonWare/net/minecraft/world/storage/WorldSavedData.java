package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData
{
    /** The name of the map data nbt */
    public final String mapName;

    /** Whether this MapDataBase needs saving to disk. */
    private boolean dirty;

    public WorldSavedData(String name)
    {
        mapName = name;
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public abstract void readFromNBT(NBTTagCompound nbt);

    public abstract NBTTagCompound writeToNBT(NBTTagCompound compound);

    /**
     * Marks this MapDataBase dirty, to be saved to disk when the level next saves.
     */
    public void markDirty()
    {
        setDirty(true);
    }

    /**
     * Sets the dirty state of this MapDataBase, whether it needs saving to disk.
     */
    public void setDirty(boolean isDirty)
    {
        dirty = isDirty;
    }

    /**
     * Whether this MapDataBase needs saving to disk.
     */
    public boolean isDirty()
    {
        return dirty;
    }
}
