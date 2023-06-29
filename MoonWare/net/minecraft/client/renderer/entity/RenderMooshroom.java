package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.util.Namespaced;

public class RenderMooshroom extends RenderLiving<EntityMooshroom>
{
    private static final Namespaced MOOSHROOM_TEXTURES = new Namespaced("textures/entity/cow/mooshroom.png");

    public RenderMooshroom(RenderManager p_i47200_1_)
    {
        super(p_i47200_1_, new ModelCow(), 0.7F);
        addLayer(new LayerMooshroomMushroom(this));
    }

    public ModelCow getMainModel()
    {
        return (ModelCow)super.getMainModel();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityMooshroom entity)
    {
        return MOOSHROOM_TEXTURES;
    }
}
