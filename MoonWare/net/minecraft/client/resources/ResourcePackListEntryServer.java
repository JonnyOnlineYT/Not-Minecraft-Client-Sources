package net.minecraft.client.resources;

import com.google.gson.JsonParseException;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourcePackListEntryServer extends ResourcePackListEntry
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final IResourcePack resourcePack;
    private final Namespaced resourcePackIcon;

    public ResourcePackListEntryServer(GuiScreenResourcePacks p_i46594_1_, IResourcePack p_i46594_2_)
    {
        super(p_i46594_1_);
        resourcePack = p_i46594_2_;
        DynamicTexture dynamictexture;

        try
        {
            dynamictexture = new DynamicTexture(p_i46594_2_.getPackImage());
        }
        catch (IOException var5)
        {
            dynamictexture = TextureUtil.MISSING_TEXTURE;
        }

        resourcePackIcon = Minecraft.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
    }

    protected int getResourcePackFormat()
    {
        return 3;
    }

    protected String getResourcePackDescription()
    {
        try
        {
            PackMetadataSection packmetadatasection = resourcePack.getPackMetadata(Minecraft.getResourcePackRepository().rprMetadataSerializer, "pack");

            if (packmetadatasection != null)
            {
                return packmetadatasection.getPackDescription().asFormattedString();
            }
        }
        catch (JsonParseException jsonparseexception)
        {
            LOGGER.error("Couldn't load metadata info", jsonparseexception);
        }
        catch (IOException ioexception)
        {
            LOGGER.error("Couldn't load metadata info", ioexception);
        }

        return Formatting.RED + "Missing " + "pack.mcmeta" + " :(";
    }

    protected boolean canMoveRight()
    {
        return false;
    }

    protected boolean canMoveLeft()
    {
        return false;
    }

    protected boolean canMoveUp()
    {
        return false;
    }

    protected boolean canMoveDown()
    {
        return false;
    }

    protected String getResourcePackName()
    {
        return "Server";
    }

    protected void bindResourcePackIcon()
    {
        Minecraft.getTextureManager().bindTexture(resourcePackIcon);
    }

    protected boolean showHoverOverlay()
    {
        return false;
    }

    public boolean isServerPack()
    {
        return true;
    }
}
