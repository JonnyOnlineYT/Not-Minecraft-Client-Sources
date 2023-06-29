package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.Namespaced;

public class RenderStray extends RenderSkeleton
{
    private static final Namespaced STRAY_SKELETON_TEXTURES = new Namespaced("textures/entity/skeleton/stray.png");

    public RenderStray(RenderManager p_i47191_1_)
    {
        super(p_i47191_1_);
        addLayer(new LayerStrayClothing(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(AbstractSkeleton entity)
    {
        return STRAY_SKELETON_TEXTURES;
    }
}
