package net.minecraft.util;

import net.minecraft.nbt.NBTTagCompound;

public class WeightedSpawnerEntity extends WeightedRandom.Item
{
    private final NBTTagCompound nbt;

    public WeightedSpawnerEntity()
    {
        super(1);
        nbt = new NBTTagCompound();
        nbt.setString("id", "minecraft:pig");
    }

    public WeightedSpawnerEntity(NBTTagCompound nbtIn)
    {
        this(nbtIn.hasKey("Weight", 99) ? nbtIn.getInteger("Weight") : 1, nbtIn.getCompoundTag("Entity"));
    }

    public WeightedSpawnerEntity(int itemWeightIn, NBTTagCompound nbtIn)
    {
        super(itemWeightIn);
        nbt = nbtIn;
    }

    public NBTTagCompound toCompoundTag()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!nbt.hasKey("id", 8))
        {
            nbt.setString("id", "minecraft:pig");
        }
        else if (!nbt.getString("id").contains(":"))
        {
            nbt.setString("id", (new Namespaced(nbt.getString("id"))).toString());
        }

        nbttagcompound.setTag("Entity", nbt);
        nbttagcompound.setInteger("Weight", itemWeight);
        return nbttagcompound;
    }

    public NBTTagCompound getNbt()
    {
        return nbt;
    }
}
