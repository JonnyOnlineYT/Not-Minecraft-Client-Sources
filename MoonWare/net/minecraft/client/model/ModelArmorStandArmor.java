package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelArmorStandArmor extends ModelBiped
{
    public ModelArmorStandArmor()
    {
        this(0.0F);
    }

    public ModelArmorStandArmor(float modelSize)
    {
        this(modelSize, 64, 32);
    }

    protected ModelArmorStandArmor(float modelSize, int textureWidthIn, int textureHeightIn)
    {
        super(modelSize, 0.0F, textureWidthIn, textureHeightIn);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        if (entityIn instanceof EntityArmorStand)
        {
            EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
            bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
            bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
            bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
            bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
            bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
            bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
            bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
            bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
            bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
            bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
            bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
            bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
            bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
            bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
            bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
            bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            ModelBase.copyModelAngles(bipedHead, bipedHeadwear);
        }
    }
}
