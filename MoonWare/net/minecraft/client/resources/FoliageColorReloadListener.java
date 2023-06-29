package net.minecraft.client.resources;

import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.Namespaced;
import net.minecraft.world.ColorizerFoliage;

public class FoliageColorReloadListener implements IResourceManagerReloadListener
{
    private static final Namespaced LOC_FOLIAGE_PNG = new Namespaced("textures/colormap/foliage.png");

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        try
        {
            ColorizerFoliage.setFoliageBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_FOLIAGE_PNG));
        }
        catch (IOException var3)
        {
        }
    }
}
