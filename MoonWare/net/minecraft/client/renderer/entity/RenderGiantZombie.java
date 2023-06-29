package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.Namespaced;

public class RenderGiantZombie extends RenderLiving<EntityGiantZombie>
{
    private static final Namespaced ZOMBIE_TEXTURES = new Namespaced("textures/entity/zombie/zombie.png");

    /** Scale of the model to use */
    private final float scale;

    public RenderGiantZombie(RenderManager p_i47206_1_, float p_i47206_2_)
    {
        super(p_i47206_1_, new ModelZombie(), 0.5F * p_i47206_2_);
        scale = p_i47206_2_;
        addLayer(new LayerHeldItem(this));
        addLayer(new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                modelLeggings = new ModelZombie(0.5F, true);
                modelArmor = new ModelZombie(1.0F, true);
            }
        });
    }

    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(scale, scale, scale);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityGiantZombie entity)
    {
        return ZOMBIE_TEXTURES;
    }
}
