package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.Namespaced;
import optifine.Config;
import optifine.CustomColors;

public class LayerWolfCollar implements LayerRenderer<EntityWolf>
{
    private static final Namespaced WOLF_COLLAR = new Namespaced("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;

    public LayerWolfCollar(RenderWolf wolfRendererIn)
    {
        wolfRenderer = wolfRendererIn;
    }

    public void doRenderLayer(EntityWolf entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible())
        {
            wolfRenderer.bindTexture(WOLF_COLLAR);
            float[] afloat = entitylivingbaseIn.getCollarColor().func_193349_f();

            if (Config.isCustomColors())
            {
                afloat = CustomColors.getWolfCollarColors(entitylivingbaseIn.getCollarColor(), afloat);
            }

            GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            wolfRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
