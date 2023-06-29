package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.Namespaced;

public interface IResourceManager
{
    Set<String> getResourceDomains();

    IResource getResource(Namespaced location) throws IOException;

    List<IResource> getAllResources(Namespaced location) throws IOException;
}
