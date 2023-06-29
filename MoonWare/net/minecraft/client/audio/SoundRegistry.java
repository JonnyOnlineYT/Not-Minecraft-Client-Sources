package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.Namespaced;
import net.minecraft.util.registry.RegistrySimple;

public class SoundRegistry extends RegistrySimple<Namespaced, SoundEventAccessor>
{
    private Map<Namespaced, SoundEventAccessor> soundRegistry;

    protected Map<Namespaced, SoundEventAccessor> createUnderlyingMap()
    {
        soundRegistry = Maps.newHashMap();
        return soundRegistry;
    }

    public void add(SoundEventAccessor accessor)
    {
        putObject(accessor.getLocation(), accessor);
    }

    /**
     * Reset the underlying sound map (Called on resource manager reload)
     */
    public void clearMap()
    {
        soundRegistry.clear();
    }
}
