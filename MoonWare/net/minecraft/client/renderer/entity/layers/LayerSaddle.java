package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.Namespaced;

public class LayerSaddle implements LayerRenderer<EntityPig>
{
    private static final Namespaced TEXTURE = new Namespaced("textures/entity/pig/pig_saddle.png");
    private final RenderPig pigRenderer;
    private final ModelPig pigModel = new ModelPig(0.5F);

    public LayerSaddle(RenderPig pigRendererIn)
    {
        pigRenderer = pigRendererIn;
    }

    public void doRenderLayer(EntityPig entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entitylivingbaseIn.getSaddled())
        {
            pigRenderer.bindTexture(TEXTURE);
            pigModel.setModelAttributes(pigRenderer.getMainModel());
            pigModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
