package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.math.MathHelper;

public class ModelBat extends ModelBase
{
    private final ModelRenderer batHead;

    /** The body box of the bat model. */
    private final ModelRenderer batBody;

    /** The inner right wing box of the bat model. */
    private final ModelRenderer batRightWing;

    /** The inner left wing box of the bat model. */
    private final ModelRenderer batLeftWing;

    /** The outer right wing box of the bat model. */
    private final ModelRenderer batOuterRightWing;

    /** The outer left wing box of the bat model. */
    private final ModelRenderer batOuterLeftWing;

    public ModelBat()
    {
        textureWidth = 64;
        textureHeight = 64;
        batHead = new ModelRenderer(this, 0, 0);
        batHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        ModelRenderer modelrenderer = new ModelRenderer(this, 24, 0);
        modelrenderer.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
        batHead.addChild(modelrenderer);
        ModelRenderer modelrenderer1 = new ModelRenderer(this, 24, 0);
        modelrenderer1.mirror = true;
        modelrenderer1.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
        batHead.addChild(modelrenderer1);
        batBody = new ModelRenderer(this, 0, 16);
        batBody.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
        batBody.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
        batRightWing = new ModelRenderer(this, 42, 0);
        batRightWing.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
        batOuterRightWing = new ModelRenderer(this, 24, 16);
        batOuterRightWing.setRotationPoint(-12.0F, 1.0F, 1.5F);
        batOuterRightWing.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
        batLeftWing = new ModelRenderer(this, 42, 0);
        batLeftWing.mirror = true;
        batLeftWing.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
        batOuterLeftWing = new ModelRenderer(this, 24, 16);
        batOuterLeftWing.mirror = true;
        batOuterLeftWing.setRotationPoint(12.0F, 1.0F, 1.5F);
        batOuterLeftWing.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
        batBody.addChild(batRightWing);
        batBody.addChild(batLeftWing);
        batRightWing.addChild(batOuterRightWing);
        batLeftWing.addChild(batOuterLeftWing);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        batHead.render(scale);
        batBody.render(scale);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        if (((EntityBat)entityIn).getIsBatHanging())
        {
            batHead.rotateAngleX = headPitch * 0.017453292F;
            batHead.rotateAngleY = (float)Math.PI - netHeadYaw * 0.017453292F;
            batHead.rotateAngleZ = (float)Math.PI;
            batHead.setRotationPoint(0.0F, -2.0F, 0.0F);
            batRightWing.setRotationPoint(-3.0F, 0.0F, 3.0F);
            batLeftWing.setRotationPoint(3.0F, 0.0F, 3.0F);
            batBody.rotateAngleX = (float)Math.PI;
            batRightWing.rotateAngleX = -0.15707964F;
            batRightWing.rotateAngleY = -((float)Math.PI * 2F / 5F);
            batOuterRightWing.rotateAngleY = -1.7278761F;
            batLeftWing.rotateAngleX = batRightWing.rotateAngleX;
            batLeftWing.rotateAngleY = -batRightWing.rotateAngleY;
            batOuterLeftWing.rotateAngleY = -batOuterRightWing.rotateAngleY;
        }
        else
        {
            batHead.rotateAngleX = headPitch * 0.017453292F;
            batHead.rotateAngleY = netHeadYaw * 0.017453292F;
            batHead.rotateAngleZ = 0.0F;
            batHead.setRotationPoint(0.0F, 0.0F, 0.0F);
            batRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
            batLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
            batBody.rotateAngleX = ((float)Math.PI / 4F) + MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
            batBody.rotateAngleY = 0.0F;
            batRightWing.rotateAngleY = MathHelper.cos(ageInTicks * 1.3F) * (float)Math.PI * 0.25F;
            batLeftWing.rotateAngleY = -batRightWing.rotateAngleY;
            batOuterRightWing.rotateAngleY = batRightWing.rotateAngleY * 0.5F;
            batOuterLeftWing.rotateAngleY = -batRightWing.rotateAngleY * 0.5F;
        }
    }
}
