package net.minecraft.util.registry;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V>
{
    /** The key of the default value. */
    private final K defaultValueKey;

    /**
     * The default value for this registry, retrurned in the place of a null value.
     */
    private V defaultValue;

    public RegistryNamespacedDefaultedByKey(K defaultValueKeyIn)
    {
        defaultValueKey = defaultValueKeyIn;
    }

    public void register(int id, K key, V value)
    {
        if (defaultValueKey.equals(key))
        {
            defaultValue = value;
        }

        super.register(id, key, value);
    }

    /**
     * validates that this registry's key is non-null
     */
    public void validateKey()
    {
        Preconditions.checkNotNull(defaultValue, "Missing default of DefaultedMappedRegistry: " + defaultValueKey);
    }

    /**
     * Gets the integer ID we use to identify the given object.
     */
    public int getIDForObject(V value)
    {
        int i = super.getIDForObject(value);
        return i == -1 ? super.getIDForObject(defaultValue) : i;
    }

    @Nonnull

    /**
     * Gets the name we use to identify the given object.
     */
    public K getNameForObject(V value)
    {
        K k = super.getNameForObject(value);
        return k == null ? defaultValueKey : k;
    }

    @Nonnull
    public V getObject(@Nullable K name)
    {
        V v = super.getObject(name);
        return v == null ? defaultValue : v;
    }

    @Nonnull

    /**
     * Gets the object identified by the given ID.
     */
    public V getObjectById(int id)
    {
        V v = super.getObjectById(id);
        return v == null ? defaultValue : v;
    }

    @Nonnull
    public V getRandomObject(Random random)
    {
        V v = super.getRandomObject(random);
        return v == null ? defaultValue : v;
    }
}
