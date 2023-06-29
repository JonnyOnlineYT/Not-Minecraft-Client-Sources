package net.minecraft.client.resources;

import java.io.File;
import net.minecraft.util.Namespaced;

public class ResourceIndexFolder extends ResourceIndex
{
    private final File baseDir;

    public ResourceIndexFolder(File folder)
    {
        baseDir = folder;
    }

    public File getFile(Namespaced location)
    {
        return new File(baseDir, location.toString().replace(':', '/'));
    }

    public File getPackMcmeta()
    {
        return new File(baseDir, "pack.mcmeta");
    }
}
