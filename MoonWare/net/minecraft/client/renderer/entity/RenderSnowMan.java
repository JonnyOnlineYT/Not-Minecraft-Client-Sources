package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.util.Namespaced;

public class RenderSnowMan extends RenderLiving<EntitySnowman>
{
    private static final Namespaced SNOW_MAN_TEXTURES = new Namespaced("textures/entity/snowman.png");

    public RenderSnowMan(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSnowMan(), 0.5F);
        addLayer(new LayerSnowmanHead(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntitySnowman entity)
    {
        return SNOW_MAN_TEXTURES;
    }

    public ModelSnowMan getMainModel()
    {
        return (ModelSnowMan)super.getMainModel();
    }
}
