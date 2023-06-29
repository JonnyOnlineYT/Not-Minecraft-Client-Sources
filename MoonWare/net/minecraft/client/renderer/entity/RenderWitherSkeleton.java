package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.Namespaced;

public class RenderWitherSkeleton extends RenderSkeleton
{
    private static final Namespaced WITHER_SKELETON_TEXTURES = new Namespaced("textures/entity/skeleton/wither_skeleton.png");

    public RenderWitherSkeleton(RenderManager p_i47188_1_)
    {
        super(p_i47188_1_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(AbstractSkeleton entity)
    {
        return WITHER_SKELETON_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
    }
}
