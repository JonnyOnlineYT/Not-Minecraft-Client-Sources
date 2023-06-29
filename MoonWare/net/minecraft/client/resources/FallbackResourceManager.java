package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.Namespaced;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FallbackResourceManager implements IResourceManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    protected final List<IResourcePack> resourcePacks = Lists.newArrayList();
    private final MetadataSerializer frmMetadataSerializer;

    public FallbackResourceManager(MetadataSerializer frmMetadataSerializerIn)
    {
        frmMetadataSerializer = frmMetadataSerializerIn;
    }

    public void addResourcePack(IResourcePack resourcePack)
    {
        resourcePacks.add(resourcePack);
    }

    public Set<String> getResourceDomains()
    {
        return Collections.emptySet();
    }

    public IResource getResource(Namespaced location) throws IOException
    {
        checkResourcePath(location);
        IResourcePack iresourcepack = null;
        Namespaced resourcelocation = getLocationMcmeta(location);

        for (int i = resourcePacks.size() - 1; i >= 0; --i)
        {
            IResourcePack iresourcepack1 = resourcePacks.get(i);

            if (iresourcepack == null && iresourcepack1.resourceExists(resourcelocation))
            {
                iresourcepack = iresourcepack1;
            }

            if (iresourcepack1.resourceExists(location))
            {
                InputStream inputstream = null;

                if (iresourcepack != null)
                {
                    inputstream = getInputStream(resourcelocation, iresourcepack);
                }

                return new SimpleResource(iresourcepack1.getPackName(), location, getInputStream(location, iresourcepack1), inputstream, frmMetadataSerializer);
            }
        }

        throw new FileNotFoundException(location.toString());
    }

    protected InputStream getInputStream(Namespaced location, IResourcePack resourcePack) throws IOException
    {
        InputStream inputstream = resourcePack.getInputStream(location);
        return LOGGER.isDebugEnabled() ? new InputStreamLeakedResourceLogger(inputstream, location, resourcePack.getPackName()) : inputstream;
    }

    private void checkResourcePath(Namespaced p_188552_1_) throws IOException
    {
        if (p_188552_1_.getPath().contains(".."))
        {
            throw new IOException("Invalid relative path to resource: " + p_188552_1_);
        }
    }

    public List<IResource> getAllResources(Namespaced location) throws IOException
    {
        checkResourcePath(location);
        List<IResource> list = Lists.newArrayList();
        Namespaced resourcelocation = getLocationMcmeta(location);

        for (IResourcePack iresourcepack : resourcePacks)
        {
            if (iresourcepack.resourceExists(location))
            {
                InputStream inputstream = iresourcepack.resourceExists(resourcelocation) ? getInputStream(resourcelocation, iresourcepack) : null;
                list.add(new SimpleResource(iresourcepack.getPackName(), location, getInputStream(location, iresourcepack), inputstream, frmMetadataSerializer));
            }
        }

        if (list.isEmpty())
        {
            throw new FileNotFoundException(location.toString());
        }
        else
        {
            return list;
        }
    }

    static Namespaced getLocationMcmeta(Namespaced location)
    {
        return new Namespaced(location.getNamespace(), location.getPath() + ".mcmeta");
    }

    static class InputStreamLeakedResourceLogger extends InputStream
    {
        private final InputStream inputStream;
        private final String message;
        private boolean isClosed;

        public InputStreamLeakedResourceLogger(InputStream p_i46093_1_, Namespaced location, String resourcePack)
        {
            inputStream = p_i46093_1_;
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            (new Exception()).printStackTrace(new PrintStream(bytearrayoutputstream));
            message = "Leaked resource: '" + location + "' loaded from pack: '" + resourcePack + "'\n" + bytearrayoutputstream;
        }

        public void close() throws IOException
        {
            inputStream.close();
            isClosed = true;
        }

        protected void finalize() throws Throwable
        {
            if (!isClosed)
            {
                LOGGER.warn(message);
            }

            super.finalize();
        }

        public int read() throws IOException
        {
            return inputStream.read();
        }
    }
}
