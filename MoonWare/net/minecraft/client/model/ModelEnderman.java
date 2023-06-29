package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelEnderman extends ModelBiped
{
    /** Is the enderman carrying a block? */
    public boolean isCarrying;

    /** Is the enderman attacking an entity? */
    public boolean isAttacking;

    public ModelEnderman(float scale)
    {
        super(0.0F, -14.0F, 64, 32);
        float f = -14.0F;
        bipedHeadwear = new ModelRenderer(this, 0, 16);
        bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale - 0.5F);
        bipedHeadwear.setRotationPoint(0.0F, -14.0F, 0.0F);
        bipedBody = new ModelRenderer(this, 32, 16);
        bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        bipedBody.setRotationPoint(0.0F, -14.0F, 0.0F);
        bipedRightArm = new ModelRenderer(this, 56, 0);
        bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, scale);
        bipedRightArm.setRotationPoint(-3.0F, -12.0F, 0.0F);
        bipedLeftArm = new ModelRenderer(this, 56, 0);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, scale);
        bipedLeftArm.setRotationPoint(5.0F, -12.0F, 0.0F);
        bipedRightLeg = new ModelRenderer(this, 56, 0);
        bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, scale);
        bipedRightLeg.setRotationPoint(-2.0F, -2.0F, 0.0F);
        bipedLeftLeg = new ModelRenderer(this, 56, 0);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, scale);
        bipedLeftLeg.setRotationPoint(2.0F, -2.0F, 0.0F);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        bipedHead.showModel = true;
        float f = -14.0F;
        bipedBody.rotateAngleX = 0.0F;
        bipedBody.rotationPointY = -14.0F;
        bipedBody.rotationPointZ = -0.0F;
        bipedRightLeg.rotateAngleX -= 0.0F;
        bipedLeftLeg.rotateAngleX -= 0.0F;
        bipedRightArm.rotateAngleX = (float)((double) bipedRightArm.rotateAngleX * 0.5D);
        bipedLeftArm.rotateAngleX = (float)((double) bipedLeftArm.rotateAngleX * 0.5D);
        bipedRightLeg.rotateAngleX = (float)((double) bipedRightLeg.rotateAngleX * 0.5D);
        bipedLeftLeg.rotateAngleX = (float)((double) bipedLeftLeg.rotateAngleX * 0.5D);
        float f1 = 0.4F;

        if (bipedRightArm.rotateAngleX > 0.4F)
        {
            bipedRightArm.rotateAngleX = 0.4F;
        }

        if (bipedLeftArm.rotateAngleX > 0.4F)
        {
            bipedLeftArm.rotateAngleX = 0.4F;
        }

        if (bipedRightArm.rotateAngleX < -0.4F)
        {
            bipedRightArm.rotateAngleX = -0.4F;
        }

        if (bipedLeftArm.rotateAngleX < -0.4F)
        {
            bipedLeftArm.rotateAngleX = -0.4F;
        }

        if (bipedRightLeg.rotateAngleX > 0.4F)
        {
            bipedRightLeg.rotateAngleX = 0.4F;
        }

        if (bipedLeftLeg.rotateAngleX > 0.4F)
        {
            bipedLeftLeg.rotateAngleX = 0.4F;
        }

        if (bipedRightLeg.rotateAngleX < -0.4F)
        {
            bipedRightLeg.rotateAngleX = -0.4F;
        }

        if (bipedLeftLeg.rotateAngleX < -0.4F)
        {
            bipedLeftLeg.rotateAngleX = -0.4F;
        }

        if (isCarrying)
        {
            bipedRightArm.rotateAngleX = -0.5F;
            bipedLeftArm.rotateAngleX = -0.5F;
            bipedRightArm.rotateAngleZ = 0.05F;
            bipedLeftArm.rotateAngleZ = -0.05F;
        }

        bipedRightArm.rotationPointZ = 0.0F;
        bipedLeftArm.rotationPointZ = 0.0F;
        bipedRightLeg.rotationPointZ = 0.0F;
        bipedLeftLeg.rotationPointZ = 0.0F;
        bipedRightLeg.rotationPointY = -5.0F;
        bipedLeftLeg.rotationPointY = -5.0F;
        bipedHead.rotationPointZ = -0.0F;
        bipedHead.rotationPointY = -13.0F;
        bipedHeadwear.rotationPointX = bipedHead.rotationPointX;
        bipedHeadwear.rotationPointY = bipedHead.rotationPointY;
        bipedHeadwear.rotationPointZ = bipedHead.rotationPointZ;
        bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
        bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
        bipedHeadwear.rotateAngleZ = bipedHead.rotateAngleZ;

        if (isAttacking)
        {
            float f2 = 1.0F;
            bipedHead.rotationPointY -= 5.0F;
        }
    }
}
