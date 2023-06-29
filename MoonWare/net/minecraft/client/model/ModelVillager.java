package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelVillager extends ModelBase
{
    /** The head box of the VillagerModel */
    public ModelRenderer villagerHead;

    /** The body of the VillagerModel */
    public ModelRenderer villagerBody;

    /** The arms of the VillagerModel */
    public ModelRenderer villagerArms;

    /** The right leg of the VillagerModel */
    public ModelRenderer rightVillagerLeg;

    /** The left leg of the VillagerModel */
    public ModelRenderer leftVillagerLeg;
    public ModelRenderer villagerNose;

    public ModelVillager(float scale)
    {
        this(scale, 0.0F, 64, 64);
    }

    public ModelVillager(float scale, float p_i1164_2_, int width, int height)
    {
        villagerHead = (new ModelRenderer(this)).setTextureSize(width, height);
        villagerHead.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
        villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, scale);
        villagerNose = (new ModelRenderer(this)).setTextureSize(width, height);
        villagerNose.setRotationPoint(0.0F, p_i1164_2_ - 2.0F, 0.0F);
        villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, scale);
        villagerHead.addChild(villagerNose);
        villagerBody = (new ModelRenderer(this)).setTextureSize(width, height);
        villagerBody.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
        villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, scale);
        villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, scale + 0.5F);
        villagerArms = (new ModelRenderer(this)).setTextureSize(width, height);
        villagerArms.setRotationPoint(0.0F, 0.0F + p_i1164_2_ + 2.0F, 0.0F);
        villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, scale);
        villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, scale);
        villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, scale);
        rightVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(width, height);
        rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + p_i1164_2_, 0.0F);
        rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
        leftVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(width, height);
        leftVillagerLeg.mirror = true;
        leftVillagerLeg.setRotationPoint(2.0F, 12.0F + p_i1164_2_, 0.0F);
        leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        villagerHead.render(scale);
        villagerBody.render(scale);
        rightVillagerLeg.render(scale);
        leftVillagerLeg.render(scale);
        villagerArms.render(scale);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        villagerHead.rotateAngleY = netHeadYaw * 0.017453292F;
        villagerHead.rotateAngleX = headPitch * 0.017453292F;
        villagerArms.rotationPointY = 3.0F;
        villagerArms.rotationPointZ = -1.0F;
        villagerArms.rotateAngleX = -0.75F;
        rightVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        leftVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        rightVillagerLeg.rotateAngleY = 0.0F;
        leftVillagerLeg.rotateAngleY = 0.0F;
    }
}
