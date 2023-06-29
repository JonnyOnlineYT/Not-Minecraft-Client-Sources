package net.minecraft.client.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.Namespaced;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractResourcePack implements IResourcePack
{
    private static final Logger LOGGER = LogManager.getLogger();
    public final File resourcePackFile;

    public AbstractResourcePack(File resourcePackFileIn)
    {
        resourcePackFile = resourcePackFileIn;
    }

    private static String locationToName(Namespaced location)
    {
        return String.format("%s/%s/%s", "assets", location.getNamespace(), location.getPath());
    }

    protected static String getRelativeName(File p_110595_0_, File p_110595_1_)
    {
        return p_110595_0_.toURI().relativize(p_110595_1_.toURI()).getPath();
    }

    public InputStream getInputStream(Namespaced location) throws IOException
    {
        return getInputStreamByName(locationToName(location));
    }

    public boolean resourceExists(Namespaced location)
    {
        return hasResourceName(locationToName(location));
    }

    protected abstract InputStream getInputStreamByName(String name) throws IOException;

    protected abstract boolean hasResourceName(String name);

    protected void logNameNotLowercase(String name)
    {
        LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", name, resourcePackFile);
    }

    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException
    {
        return readMetadata(metadataSerializer, getInputStreamByName("pack.mcmeta"), metadataSectionName);
    }

    static <T extends IMetadataSection> T readMetadata(MetadataSerializer metadataSerializer, InputStream p_110596_1_, String sectionName)
    {
        JsonObject jsonobject = null;
        BufferedReader bufferedreader = null;

        try
        {
            bufferedreader = new BufferedReader(new InputStreamReader(p_110596_1_, StandardCharsets.UTF_8));
            jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
        }
        catch (RuntimeException runtimeexception)
        {
            throw new JsonParseException(runtimeexception);
        }
        finally
        {
            IOUtils.closeQuietly(bufferedreader);
        }

        return metadataSerializer.parseMetadataSection(sectionName, jsonobject);
    }

    public BufferedImage getPackImage() throws IOException
    {
        return TextureUtil.readBufferedImage(getInputStreamByName("pack.png"));
    }

    public String getPackName()
    {
        return resourcePackFile.getName();
    }
}
