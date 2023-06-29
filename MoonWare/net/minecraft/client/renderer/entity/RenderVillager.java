package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.Namespaced;

public class RenderVillager extends RenderLiving<EntityVillager>
{
    private static final Namespaced VILLAGER_TEXTURES = new Namespaced("textures/entity/villager/villager.png");
    private static final Namespaced FARMER_VILLAGER_TEXTURES = new Namespaced("textures/entity/villager/farmer.png");
    private static final Namespaced LIBRARIAN_VILLAGER_TEXTURES = new Namespaced("textures/entity/villager/librarian.png");
    private static final Namespaced PRIEST_VILLAGER_TEXTURES = new Namespaced("textures/entity/villager/priest.png");
    private static final Namespaced SMITH_VILLAGER_TEXTURES = new Namespaced("textures/entity/villager/smith.png");
    private static final Namespaced BUTCHER_VILLAGER_TEXTURES = new Namespaced("textures/entity/villager/butcher.png");

    public RenderVillager(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelVillager(0.0F), 0.5F);
        addLayer(new LayerCustomHead(getMainModel().villagerHead));
    }

    public ModelVillager getMainModel()
    {
        return (ModelVillager)super.getMainModel();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityVillager entity)
    {
        switch (entity.getProfession())
        {
            case 0:
                return FARMER_VILLAGER_TEXTURES;

            case 1:
                return LIBRARIAN_VILLAGER_TEXTURES;

            case 2:
                return PRIEST_VILLAGER_TEXTURES;

            case 3:
                return SMITH_VILLAGER_TEXTURES;

            case 4:
                return BUTCHER_VILLAGER_TEXTURES;

            case 5:
            default:
                return VILLAGER_TEXTURES;
        }
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0)
        {
            f = (float)((double)f * 0.5D);
            shadowSize = 0.25F;
        }
        else
        {
            shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
}
