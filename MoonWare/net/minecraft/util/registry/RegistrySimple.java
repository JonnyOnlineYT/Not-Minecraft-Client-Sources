package net.minecraft.util.registry;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class RegistrySimple<K, V> implements IRegistry<K, V>
{
    private static final Logger LOGGER = LogManager.getLogger();
    protected final Map<K, V> registryObjects = createUnderlyingMap();
    private Object[] values;

    protected Map<K, V> createUnderlyingMap()
    {
        return Maps.newHashMap();
    }

    @Nullable
    public V getObject(@Nullable K name)
    {
        return registryObjects.get(name);
    }

    /**
     * Register an object on this registry.
     */
    public void putObject(K key, V value)
    {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        values = null;

        if (registryObjects.containsKey(key))
        {
            LOGGER.debug("Adding duplicate key '{}' to registry", key);
        }

        registryObjects.put(key, value);
    }

    public Set<K> getKeys()
    {
        return Collections.unmodifiableSet(registryObjects.keySet());
    }

    @Nullable
    public V getRandomObject(Random random)
    {
        if (values == null)
        {
            Collection<?> collection = registryObjects.values();

            if (collection.isEmpty())
            {
                return null;
            }

            values = collection.toArray(new Object[collection.size()]);
        }

        return (V) values[random.nextInt(values.length)];
    }

    /**
     * Does this registry contain an entry for the given key?
     */
    public boolean containsKey(K key)
    {
        return registryObjects.containsKey(key);
    }

    public Iterator<V> iterator()
    {
        return registryObjects.values().iterator();
    }
}
