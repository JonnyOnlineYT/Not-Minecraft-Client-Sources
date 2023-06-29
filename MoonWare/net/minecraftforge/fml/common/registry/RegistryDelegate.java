package net.minecraftforge.fml.common.registry;

import net.minecraft.util.Namespaced;

public interface RegistryDelegate<T>
{
    T get();

    Namespaced name();

    Class<T> type();
}
