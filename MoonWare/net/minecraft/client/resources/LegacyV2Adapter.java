package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.Namespaced;

public class LegacyV2Adapter implements IResourcePack
{
    private final IResourcePack field_191383_a;

    public LegacyV2Adapter(IResourcePack p_i47182_1_)
    {
        field_191383_a = p_i47182_1_;
    }

    public InputStream getInputStream(Namespaced location) throws IOException
    {
        return field_191383_a.getInputStream(func_191382_c(location));
    }

    private Namespaced func_191382_c(Namespaced p_191382_1_)
    {
        String s = p_191382_1_.getPath();

        if (!"lang/swg_de.lang".equals(s) && s.startsWith("lang/") && s.endsWith(".lang"))
        {
            int i = s.indexOf(95);

            if (i != -1)
            {
                String s1 = s.substring(0, i + 1) + s.substring(i + 1, s.indexOf(46, i)).toUpperCase() + ".lang";
                return new Namespaced(p_191382_1_.getNamespace(), "")
                {
                    public String getPath()
                    {
                        return s1;
                    }
                };
            }
        }

        return p_191382_1_;
    }

    public boolean resourceExists(Namespaced location)
    {
        return field_191383_a.resourceExists(func_191382_c(location));
    }

    public Set<String> getResourceDomains()
    {
        return field_191383_a.getResourceDomains();
    }

    @Nullable
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException
    {
        return field_191383_a.getPackMetadata(metadataSerializer, metadataSectionName);
    }

    public BufferedImage getPackImage() throws IOException
    {
        return field_191383_a.getPackImage();
    }

    public String getPackName()
    {
        return field_191383_a.getPackName();
    }
}
