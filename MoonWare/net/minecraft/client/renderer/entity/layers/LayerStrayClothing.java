package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.util.Namespaced;

public class LayerStrayClothing implements LayerRenderer<EntityStray>
{
    private static final Namespaced STRAY_CLOTHES_TEXTURES = new Namespaced("textures/entity/skeleton/stray_overlay.png");
    private final RenderLivingBase<?> renderer;
    private final ModelSkeleton layerModel = new ModelSkeleton(0.25F, true);

    public LayerStrayClothing(RenderLivingBase<?> p_i47183_1_)
    {
        renderer = p_i47183_1_;
    }

    public void doRenderLayer(EntityStray entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        layerModel.setModelAttributes(renderer.getMainModel());
        layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        renderer.bindTexture(STRAY_CLOTHES_TEXTURES);
        layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
