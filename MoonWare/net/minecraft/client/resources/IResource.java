package net.minecraft.client.resources;

import java.io.Closeable;
import java.io.InputStream;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.Namespaced;

public interface IResource extends Closeable
{
    Namespaced getResourceLocation();

    InputStream getInputStream();

    boolean hasMetadata();

    @Nullable
    <T extends IMetadataSection> T getMetadata(String sectionName);

    String getResourcePackName();
}
