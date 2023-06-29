package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.entity.layers.LayerLlamaDecor;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.Namespaced;

public class RenderLlama extends RenderLiving<EntityLlama>
{
    private static final Namespaced[] field_191350_a = {new Namespaced("textures/entity/llama/llama_creamy.png"), new Namespaced("textures/entity/llama/llama_white.png"), new Namespaced("textures/entity/llama/llama_brown.png"), new Namespaced("textures/entity/llama/llama_gray.png")};

    public RenderLlama(RenderManager p_i47203_1_)
    {
        super(p_i47203_1_, new ModelLlama(0.0F), 0.7F);
        addLayer(new LayerLlamaDecor(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityLlama entity)
    {
        return field_191350_a[entity.func_190719_dM()];
    }
}
