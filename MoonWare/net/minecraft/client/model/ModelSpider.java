package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSpider extends ModelBase
{
    /** The spider's head box */
    public ModelRenderer spiderHead;

    /** The spider's neck box */
    public ModelRenderer spiderNeck;

    /** The spider's body box */
    public ModelRenderer spiderBody;

    /** Spider's first leg */
    public ModelRenderer spiderLeg1;

    /** Spider's second leg */
    public ModelRenderer spiderLeg2;

    /** Spider's third leg */
    public ModelRenderer spiderLeg3;

    /** Spider's fourth leg */
    public ModelRenderer spiderLeg4;

    /** Spider's fifth leg */
    public ModelRenderer spiderLeg5;

    /** Spider's sixth leg */
    public ModelRenderer spiderLeg6;

    /** Spider's seventh leg */
    public ModelRenderer spiderLeg7;

    /** Spider's eight leg */
    public ModelRenderer spiderLeg8;

    public ModelSpider()
    {
        float f = 0.0F;
        int i = 15;
        spiderHead = new ModelRenderer(this, 32, 4);
        spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
        spiderHead.setRotationPoint(0.0F, 15.0F, -3.0F);
        spiderNeck = new ModelRenderer(this, 0, 0);
        spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        spiderNeck.setRotationPoint(0.0F, 15.0F, 0.0F);
        spiderBody = new ModelRenderer(this, 0, 12);
        spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, 0.0F);
        spiderBody.setRotationPoint(0.0F, 15.0F, 9.0F);
        spiderLeg1 = new ModelRenderer(this, 18, 0);
        spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg1.setRotationPoint(-4.0F, 15.0F, 2.0F);
        spiderLeg2 = new ModelRenderer(this, 18, 0);
        spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg2.setRotationPoint(4.0F, 15.0F, 2.0F);
        spiderLeg3 = new ModelRenderer(this, 18, 0);
        spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg3.setRotationPoint(-4.0F, 15.0F, 1.0F);
        spiderLeg4 = new ModelRenderer(this, 18, 0);
        spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg4.setRotationPoint(4.0F, 15.0F, 1.0F);
        spiderLeg5 = new ModelRenderer(this, 18, 0);
        spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg5.setRotationPoint(-4.0F, 15.0F, 0.0F);
        spiderLeg6 = new ModelRenderer(this, 18, 0);
        spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg6.setRotationPoint(4.0F, 15.0F, 0.0F);
        spiderLeg7 = new ModelRenderer(this, 18, 0);
        spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg7.setRotationPoint(-4.0F, 15.0F, -1.0F);
        spiderLeg8 = new ModelRenderer(this, 18, 0);
        spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        spiderLeg8.setRotationPoint(4.0F, 15.0F, -1.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        spiderHead.render(scale);
        spiderNeck.render(scale);
        spiderBody.render(scale);
        spiderLeg1.render(scale);
        spiderLeg2.render(scale);
        spiderLeg3.render(scale);
        spiderLeg4.render(scale);
        spiderLeg5.render(scale);
        spiderLeg6.render(scale);
        spiderLeg7.render(scale);
        spiderLeg8.render(scale);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        spiderHead.rotateAngleY = netHeadYaw * 0.017453292F;
        spiderHead.rotateAngleX = headPitch * 0.017453292F;
        float f = ((float)Math.PI / 4F);
        spiderLeg1.rotateAngleZ = -((float)Math.PI / 4F);
        spiderLeg2.rotateAngleZ = ((float)Math.PI / 4F);
        spiderLeg3.rotateAngleZ = -0.58119464F;
        spiderLeg4.rotateAngleZ = 0.58119464F;
        spiderLeg5.rotateAngleZ = -0.58119464F;
        spiderLeg6.rotateAngleZ = 0.58119464F;
        spiderLeg7.rotateAngleZ = -((float)Math.PI / 4F);
        spiderLeg8.rotateAngleZ = ((float)Math.PI / 4F);
        float f1 = -0.0F;
        float f2 = 0.3926991F;
        spiderLeg1.rotateAngleY = ((float)Math.PI / 4F);
        spiderLeg2.rotateAngleY = -((float)Math.PI / 4F);
        spiderLeg3.rotateAngleY = 0.3926991F;
        spiderLeg4.rotateAngleY = -0.3926991F;
        spiderLeg5.rotateAngleY = -0.3926991F;
        spiderLeg6.rotateAngleY = 0.3926991F;
        spiderLeg7.rotateAngleY = -((float)Math.PI / 4F);
        spiderLeg8.rotateAngleY = ((float)Math.PI / 4F);
        float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
        float f4 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * limbSwingAmount;
        float f5 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f6 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;
        float f7 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
        float f8 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + (float)Math.PI) * 0.4F) * limbSwingAmount;
        float f9 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
        float f10 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + ((float)Math.PI * 3F / 2F)) * 0.4F) * limbSwingAmount;
        spiderLeg1.rotateAngleY += f3;
        spiderLeg2.rotateAngleY += -f3;
        spiderLeg3.rotateAngleY += f4;
        spiderLeg4.rotateAngleY += -f4;
        spiderLeg5.rotateAngleY += f5;
        spiderLeg6.rotateAngleY += -f5;
        spiderLeg7.rotateAngleY += f6;
        spiderLeg8.rotateAngleY += -f6;
        spiderLeg1.rotateAngleZ += f7;
        spiderLeg2.rotateAngleZ += -f7;
        spiderLeg3.rotateAngleZ += f8;
        spiderLeg4.rotateAngleZ += -f8;
        spiderLeg5.rotateAngleZ += f9;
        spiderLeg6.rotateAngleZ += -f9;
        spiderLeg7.rotateAngleZ += f10;
        spiderLeg8.rotateAngleZ += -f10;
    }
}
