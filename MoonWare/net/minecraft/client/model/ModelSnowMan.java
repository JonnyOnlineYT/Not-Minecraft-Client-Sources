package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSnowMan extends ModelBase
{
    public ModelRenderer body;
    public ModelRenderer bottomBody;
    public ModelRenderer head;
    public ModelRenderer rightHand;
    public ModelRenderer leftHand;

    public ModelSnowMan()
    {
        float f = 4.0F;
        float f1 = 0.0F;
        head = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
        head.setRotationPoint(0.0F, 4.0F, 0.0F);
        rightHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
        rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
        rightHand.setRotationPoint(0.0F, 6.0F, 0.0F);
        leftHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
        leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
        leftHand.setRotationPoint(0.0F, 6.0F, 0.0F);
        body = (new ModelRenderer(this, 0, 16)).setTextureSize(64, 64);
        body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, -0.5F);
        body.setRotationPoint(0.0F, 13.0F, 0.0F);
        bottomBody = (new ModelRenderer(this, 0, 36)).setTextureSize(64, 64);
        bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, -0.5F);
        bottomBody.setRotationPoint(0.0F, 24.0F, 0.0F);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        head.rotateAngleY = netHeadYaw * 0.017453292F;
        head.rotateAngleX = headPitch * 0.017453292F;
        body.rotateAngleY = netHeadYaw * 0.017453292F * 0.25F;
        float f = MathHelper.sin(body.rotateAngleY);
        float f1 = MathHelper.cos(body.rotateAngleY);
        rightHand.rotateAngleZ = 1.0F;
        leftHand.rotateAngleZ = -1.0F;
        rightHand.rotateAngleY = 0.0F + body.rotateAngleY;
        leftHand.rotateAngleY = (float)Math.PI + body.rotateAngleY;
        rightHand.rotationPointX = f1 * 5.0F;
        rightHand.rotationPointZ = -f * 5.0F;
        leftHand.rotationPointX = -f1 * 5.0F;
        leftHand.rotationPointZ = f * 5.0F;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        body.render(scale);
        bottomBody.render(scale);
        head.render(scale);
        rightHand.render(scale);
        leftHand.render(scale);
    }
}
