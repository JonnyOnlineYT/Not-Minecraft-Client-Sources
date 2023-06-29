package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.Namespaced;

public class RenderSheep extends RenderLiving<EntitySheep>
{
    private static final Namespaced SHEARED_SHEEP_TEXTURES = new Namespaced("textures/entity/sheep/sheep.png");

    public RenderSheep(RenderManager p_i47195_1_)
    {
        super(p_i47195_1_, new ModelSheep2(), 0.7F);
        addLayer(new LayerSheepWool(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntitySheep entity)
    {
        return SHEARED_SHEEP_TEXTURES;
    }
}
