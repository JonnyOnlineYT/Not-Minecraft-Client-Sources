package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.Namespaced;

public class RenderCow extends RenderLiving<EntityCow>
{
    private static final Namespaced COW_TEXTURES = new Namespaced("textures/entity/cow/cow.png");

    public RenderCow(RenderManager p_i47210_1_)
    {
        super(p_i47210_1_, new ModelCow(), 0.7F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityCow entity)
    {
        return COW_TEXTURES;
    }
}
