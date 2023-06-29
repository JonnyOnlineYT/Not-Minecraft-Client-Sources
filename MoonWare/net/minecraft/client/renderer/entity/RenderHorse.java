package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.Namespaced;

public class RenderHorse extends RenderLiving<EntityHorse>
{
    private static final Map<String, Namespaced> LAYERED_LOCATION_CACHE = Maps.newHashMap();

    public RenderHorse(RenderManager p_i47205_1_)
    {
        super(p_i47205_1_, new ModelHorse(), 0.75F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityHorse entity)
    {
        String s = entity.getHorseTexture();
        Namespaced resourcelocation = LAYERED_LOCATION_CACHE.get(s);

        if (resourcelocation == null)
        {
            resourcelocation = new Namespaced(s);
            Minecraft.getTextureManager().loadTexture(resourcelocation, new LayeredTexture(entity.getVariantTexturePaths()));
            LAYERED_LOCATION_CACHE.put(s, resourcelocation);
        }

        return resourcelocation;
    }
}
