package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Namespaced;

public class RenderBiped<T extends EntityLiving> extends RenderLiving<T>
{
    private static final Namespaced DEFAULT_RES_LOC = new Namespaced("textures/entity/steve.png");

    public RenderBiped(RenderManager renderManagerIn, ModelBiped modelBipedIn, float shadowSize)
    {
        super(renderManagerIn, modelBipedIn, shadowSize);
        addLayer(new LayerCustomHead(modelBipedIn.bipedHead));
        addLayer(new LayerElytra(this));
        addLayer(new LayerHeldItem(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(T entity)
    {
        return DEFAULT_RES_LOC;
    }

    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }
}
