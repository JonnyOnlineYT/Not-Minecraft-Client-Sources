package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.Namespaced;

public class RenderPig extends RenderLiving<EntityPig>
{
    private static final Namespaced PIG_TEXTURES = new Namespaced("textures/entity/pig/pig.png");

    public RenderPig(RenderManager p_i47198_1_)
    {
        super(p_i47198_1_, new ModelPig(), 0.7F);
        addLayer(new LayerSaddle(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityPig entity)
    {
        return PIG_TEXTURES;
    }
}
