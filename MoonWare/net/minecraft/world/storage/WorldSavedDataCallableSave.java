package net.minecraft.world.storage;

public class WorldSavedDataCallableSave implements Runnable
{
    private final WorldSavedData data;

    public WorldSavedDataCallableSave(WorldSavedData dataIn)
    {
        data = dataIn;
    }

    public void run()
    {
        data.markDirty();
    }
}
