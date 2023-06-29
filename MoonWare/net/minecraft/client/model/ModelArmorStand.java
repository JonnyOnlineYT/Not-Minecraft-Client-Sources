package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumHandSide;

public class ModelArmorStand extends ModelArmorStandArmor
{
    public ModelRenderer standRightSide;
    public ModelRenderer standLeftSide;
    public ModelRenderer standWaist;
    public ModelRenderer standBase;

    public ModelArmorStand()
    {
        this(0.0F);
    }

    public ModelArmorStand(float modelSize)
    {
        super(modelSize, 64, 64);
        bipedHead = new ModelRenderer(this, 0, 0);
        bipedHead.addBox(-1.0F, -7.0F, -1.0F, 2, 7, 2, modelSize);
        bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedBody = new ModelRenderer(this, 0, 26);
        bipedBody.addBox(-6.0F, 0.0F, -1.5F, 12, 3, 3, modelSize);
        bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedRightArm = new ModelRenderer(this, 24, 0);
        bipedRightArm.addBox(-2.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        bipedLeftArm = new ModelRenderer(this, 32, 16);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(0.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        bipedRightLeg = new ModelRenderer(this, 8, 0);
        bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, modelSize);
        bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        bipedLeftLeg = new ModelRenderer(this, 40, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, modelSize);
        bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        standRightSide = new ModelRenderer(this, 16, 0);
        standRightSide.addBox(-3.0F, 3.0F, -1.0F, 2, 7, 2, modelSize);
        standRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        standRightSide.showModel = true;
        standLeftSide = new ModelRenderer(this, 48, 16);
        standLeftSide.addBox(1.0F, 3.0F, -1.0F, 2, 7, 2, modelSize);
        standLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        standWaist = new ModelRenderer(this, 0, 48);
        standWaist.addBox(-4.0F, 10.0F, -1.0F, 8, 2, 2, modelSize);
        standWaist.setRotationPoint(0.0F, 0.0F, 0.0F);
        standBase = new ModelRenderer(this, 0, 32);
        standBase.addBox(-6.0F, 11.0F, -6.0F, 12, 1, 12, modelSize);
        standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
        bipedHeadwear.showModel = false;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if (entityIn instanceof EntityArmorStand)
        {
            EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
            bipedLeftArm.showModel = entityarmorstand.getShowArms();
            bipedRightArm.showModel = entityarmorstand.getShowArms();
            standBase.showModel = !entityarmorstand.hasNoBasePlate();
            bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
            bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
            standRightSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            standRightSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            standRightSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            standLeftSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            standLeftSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            standLeftSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            standWaist.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            standWaist.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            standWaist.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            standBase.rotateAngleX = 0.0F;
            standBase.rotateAngleY = 0.017453292F * -entityIn.rotationYaw;
            standBase.rotateAngleZ = 0.0F;
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        if (isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            standRightSide.render(scale);
            standLeftSide.render(scale);
            standWaist.render(scale);
            standBase.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            standRightSide.render(scale);
            standLeftSide.render(scale);
            standWaist.render(scale);
            standBase.render(scale);
        }

        GlStateManager.popMatrix();
    }

    public void postRenderArm(float scale, EnumHandSide side)
    {
        ModelRenderer modelrenderer = getArmForSide(side);
        boolean flag = modelrenderer.showModel;
        modelrenderer.showModel = true;
        super.postRenderArm(scale, side);
        modelrenderer.showModel = flag;
    }
}
