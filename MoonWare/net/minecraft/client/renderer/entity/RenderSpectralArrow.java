package net.minecraft.client.renderer.entity;

import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.util.Namespaced;

public class RenderSpectralArrow extends RenderArrow<EntitySpectralArrow>
{
    public static final Namespaced RES_SPECTRAL_ARROW = new Namespaced("textures/entity/projectiles/spectral_arrow.png");

    public RenderSpectralArrow(RenderManager manager)
    {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntitySpectralArrow entity)
    {
        return RES_SPECTRAL_ARROW;
    }
}
