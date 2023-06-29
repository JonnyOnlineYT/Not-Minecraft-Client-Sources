package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.Namespaced;

public class RenderLeashKnot extends Render<EntityLeashKnot>
{
    private static final Namespaced LEASH_KNOT_TEXTURES = new Namespaced("textures/entity/lead_knot.png");
    private final ModelLeashKnot leashKnotModel = new ModelLeashKnot();

    public RenderLeashKnot(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityLeashKnot entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float)x, (float)y, (float)z);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        bindEntityTexture(entity);

        if (renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
        }

        leashKnotModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

        if (renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityLeashKnot entity)
    {
        return LEASH_KNOT_TEXTURES;
    }
}
