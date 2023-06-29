package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.Namespaced;

public interface IResourcePack
{
    InputStream getInputStream(Namespaced location) throws IOException;

    boolean resourceExists(Namespaced location);

    Set<String> getResourceDomains();

    @Nullable
    <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException;

    BufferedImage getPackImage() throws IOException;

    String getPackName();
}
