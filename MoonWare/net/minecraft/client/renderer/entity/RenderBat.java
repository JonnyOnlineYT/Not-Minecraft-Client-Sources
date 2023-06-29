package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;

public class RenderBat extends RenderLiving<EntityBat>
{
    private static final Namespaced BAT_TEXTURES = new Namespaced("textures/entity/bat.png");

    public RenderBat(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBat(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected Namespaced getEntityTexture(EntityBat entity)
    {
        return BAT_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityBat entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.35F, 0.35F, 0.35F);
    }

    protected void rotateCorpse(EntityBat entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        if (entityLiving.getIsBatHanging())
        {
            GlStateManager.translate(0.0F, -0.1F, 0.0F);
        }
        else
        {
            GlStateManager.translate(0.0F, MathHelper.cos(p_77043_2_ * 0.3F) * 0.1F, 0.0F);
        }

        super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
    }
}
