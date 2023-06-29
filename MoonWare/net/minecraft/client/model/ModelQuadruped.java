package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelQuadruped extends ModelBase
{
    public ModelRenderer head = new ModelRenderer(this, 0, 0);
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;
    protected float childYOffset = 8.0F;
    protected float childZOffset = 4.0F;

    public ModelQuadruped(int height, float scale)
    {
        head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, scale);
        head.setRotationPoint(0.0F, (float)(18 - height), -6.0F);
        body = new ModelRenderer(this, 28, 8);
        body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, scale);
        body.setRotationPoint(0.0F, (float)(17 - height), 2.0F);
        leg1 = new ModelRenderer(this, 0, 16);
        leg1.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        leg1.setRotationPoint(-3.0F, (float)(24 - height), 7.0F);
        leg2 = new ModelRenderer(this, 0, 16);
        leg2.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        leg2.setRotationPoint(3.0F, (float)(24 - height), 7.0F);
        leg3 = new ModelRenderer(this, 0, 16);
        leg3.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        leg3.setRotationPoint(-3.0F, (float)(24 - height), -5.0F);
        leg4 = new ModelRenderer(this, 0, 16);
        leg4.addBox(-2.0F, 0.0F, -2.0F, 4, height, 4, scale);
        leg4.setRotationPoint(3.0F, (float)(24 - height), -5.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        if (isChild)
        {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, childYOffset * scale, childZOffset * scale);
            head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            body.render(scale);
            leg1.render(scale);
            leg2.render(scale);
            leg3.render(scale);
            leg4.render(scale);
            GlStateManager.popMatrix();
        }
        else
        {
            head.render(scale);
            body.render(scale);
            leg1.render(scale);
            leg2.render(scale);
            leg3.render(scale);
            leg4.render(scale);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        head.rotateAngleX = headPitch * 0.017453292F;
        head.rotateAngleY = netHeadYaw * 0.017453292F;
        body.rotateAngleX = ((float)Math.PI / 2F);
        leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}
