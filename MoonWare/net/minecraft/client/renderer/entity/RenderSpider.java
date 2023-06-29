package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.Namespaced;

public class RenderSpider<T extends EntitySpider> extends RenderLiving<T>
{
    private static final Namespaced SPIDER_TEXTURES = new Namespaced("textures/entity/spider/spider.png");

    public RenderSpider(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSpider(), 1.0F);
        addLayer(new LayerSpiderEyes(this));
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn)
    {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(T entity)
    {
        return SPIDER_TEXTURES;
    }
}
