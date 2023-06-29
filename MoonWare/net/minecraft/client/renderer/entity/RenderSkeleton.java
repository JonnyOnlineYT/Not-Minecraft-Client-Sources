package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.Namespaced;

public class RenderSkeleton extends RenderBiped<AbstractSkeleton>
{
    private static final Namespaced SKELETON_TEXTURES = new Namespaced("textures/entity/skeleton/skeleton.png");

    public RenderSkeleton(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSkeleton(), 0.5F);
        addLayer(new LayerHeldItem(this));
        addLayer(new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                modelLeggings = new ModelSkeleton(0.5F, true);
                modelArmor = new ModelSkeleton(1.0F, true);
            }
        });
    }

    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(AbstractSkeleton entity)
    {
        return SKELETON_TEXTURES;
    }
}
