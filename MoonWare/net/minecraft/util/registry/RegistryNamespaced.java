package net.minecraft.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V>
{
    protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap<V>(256);
    protected final Map<V, K> inverseObjectRegistry;

    public RegistryNamespaced()
    {
        inverseObjectRegistry = ((BiMap) registryObjects).inverse();
    }

    public void register(int id, K key, V value)
    {
        underlyingIntegerMap.put(value, id);
        putObject(key, value);
    }

    protected Map<K, V> createUnderlyingMap()
    {
        return HashBiMap.create();
    }

    @Nullable
    public V getObject(@Nullable K name)
    {
        return super.getObject(name);
    }

    @Nullable

    /**
     * Gets the name we use to identify the given object.
     */
    public K getNameForObject(V value)
    {
        return inverseObjectRegistry.get(value);
    }

    /**
     * Does this registry contain an entry for the given key?
     */
    public boolean containsKey(K key)
    {
        return super.containsKey(key);
    }

    /**
     * Gets the integer ID we use to identify the given object.
     */
    public int getIDForObject(@Nullable V value)
    {
        return underlyingIntegerMap.getId(value);
    }

    @Nullable

    /**
     * Gets the object identified by the given ID.
     */
    public V getObjectById(int id)
    {
        return underlyingIntegerMap.get(id);
    }

    public Iterator<V> iterator()
    {
        return underlyingIntegerMap.iterator();
    }
}
