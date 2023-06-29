package net.minecraft.client.resources;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.Namespaced;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleReloadableResourceManager implements IReloadableResourceManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Joiner JOINER_RESOURCE_PACKS = Joiner.on(", ");
    private final Map<String, FallbackResourceManager> domainResourceManagers = Maps.newHashMap();
    private final List<IResourceManagerReloadListener> reloadListeners = Lists.newArrayList();
    private final Set<String> setResourceDomains = Sets.newLinkedHashSet();
    private final MetadataSerializer rmMetadataSerializer;

    public SimpleReloadableResourceManager(MetadataSerializer rmMetadataSerializerIn)
    {
        rmMetadataSerializer = rmMetadataSerializerIn;
    }

    public void reloadResourcePack(IResourcePack resourcePack)
    {
        for (String s : resourcePack.getResourceDomains())
        {
            setResourceDomains.add(s);
            FallbackResourceManager fallbackresourcemanager = domainResourceManagers.get(s);

            if (fallbackresourcemanager == null)
            {
                fallbackresourcemanager = new FallbackResourceManager(rmMetadataSerializer);
                domainResourceManagers.put(s, fallbackresourcemanager);
            }

            fallbackresourcemanager.addResourcePack(resourcePack);
        }
    }

    public Set<String> getResourceDomains()
    {
        return setResourceDomains;
    }

    public IResource getResource(Namespaced location) throws IOException
    {
        IResourceManager iresourcemanager = domainResourceManagers.get(location.getNamespace());

        if (iresourcemanager != null)
        {
            return iresourcemanager.getResource(location);
        }
        else
        {
            throw new FileNotFoundException(location.toString());
        }
    }

    public List<IResource> getAllResources(Namespaced location) throws IOException
    {
        IResourceManager iresourcemanager = domainResourceManagers.get(location.getNamespace());

        if (iresourcemanager != null)
        {
            return iresourcemanager.getAllResources(location);
        }
        else
        {
            throw new FileNotFoundException(location.toString());
        }
    }

    private void clearResources()
    {
        domainResourceManagers.clear();
        setResourceDomains.clear();
    }

    public void reloadResources(List<IResourcePack> resourcesPacksList)
    {
        clearResources();
        LOGGER.info("Reloading ResourceManager: {}", JOINER_RESOURCE_PACKS.join(Iterables.transform(resourcesPacksList, new Function<IResourcePack, String>()
        {
            public String apply(@Nullable IResourcePack p_apply_1_)
            {
                return p_apply_1_ == null ? "<NULL>" : p_apply_1_.getPackName();
            }
        })));

        for (IResourcePack iresourcepack : resourcesPacksList)
        {
            reloadResourcePack(iresourcepack);
        }

        notifyReloadListeners();
    }

    public void registerReloadListener(IResourceManagerReloadListener reloadListener)
    {
        reloadListeners.add(reloadListener);
        reloadListener.onResourceManagerReload(this);
    }

    private void notifyReloadListeners()
    {
        for (IResourceManagerReloadListener iresourcemanagerreloadlistener : reloadListeners)
        {
            iresourcemanagerreloadlistener.onResourceManagerReload(this);
        }
    }
}
