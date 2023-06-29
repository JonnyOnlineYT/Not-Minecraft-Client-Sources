package net.minecraftforge.registries;

import net.minecraft.util.Namespaced;

public interface IRegistryDelegate<T>
{
    T get();

    Namespaced name();

    Class<T> type();
}
