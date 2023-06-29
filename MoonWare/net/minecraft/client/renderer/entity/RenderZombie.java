package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.Namespaced;

public class RenderZombie extends RenderBiped<EntityZombie>
{
    private static final Namespaced ZOMBIE_TEXTURES = new Namespaced("textures/entity/zombie/zombie.png");

    public RenderZombie(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelZombie(), 0.5F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                modelLeggings = new ModelZombie(0.5F, true);
                modelArmor = new ModelZombie(1.0F, true);
            }
        };
        addLayer(layerbipedarmor);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityZombie entity)
    {
        return ZOMBIE_TEXTURES;
    }
}
