package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelDragonHead extends ModelBase
{
    private final ModelRenderer head;
    private final ModelRenderer jaw;

    public ModelDragonHead(float p_i46588_1_)
    {
        textureWidth = 256;
        textureHeight = 256;
        setTextureOffset("body.body", 0, 0);
        setTextureOffset("wing.skin", -56, 88);
        setTextureOffset("wingtip.skin", -56, 144);
        setTextureOffset("rearleg.main", 0, 0);
        setTextureOffset("rearfoot.main", 112, 0);
        setTextureOffset("rearlegtip.main", 196, 0);
        setTextureOffset("head.upperhead", 112, 30);
        setTextureOffset("wing.bone", 112, 88);
        setTextureOffset("head.upperlip", 176, 44);
        setTextureOffset("jaw.jaw", 176, 65);
        setTextureOffset("frontleg.main", 112, 104);
        setTextureOffset("wingtip.bone", 112, 136);
        setTextureOffset("frontfoot.main", 144, 104);
        setTextureOffset("neck.box", 192, 104);
        setTextureOffset("frontlegtip.main", 226, 138);
        setTextureOffset("body.scale", 220, 53);
        setTextureOffset("head.scale", 0, 0);
        setTextureOffset("neck.scale", 48, 0);
        setTextureOffset("head.nostril", 112, 0);
        float f = -16.0F;
        head = new ModelRenderer(this, "head");
        head.addBox("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16);
        head.addBox("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16);
        head.mirror = true;
        head.addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6);
        head.addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4);
        head.mirror = false;
        head.addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6);
        head.addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4);
        jaw = new ModelRenderer(this, "jaw");
        jaw.setRotationPoint(0.0F, 4.0F, -8.0F);
        jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
        head.addChild(jaw);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        jaw.rotateAngleX = (float)(Math.sin(limbSwing * (float)Math.PI * 0.2F) + 1.0D) * 0.2F;
        head.rotateAngleY = netHeadYaw * 0.017453292F;
        head.rotateAngleX = headPitch * 0.017453292F;
        GlStateManager.translate(0.0F, -0.374375F, 0.0F);
        GlStateManager.scale(0.75F, 0.75F, 0.75F);
        head.render(scale);
    }
}
