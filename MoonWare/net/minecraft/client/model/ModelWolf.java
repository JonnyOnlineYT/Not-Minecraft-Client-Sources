package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;

public class ModelWolf extends ModelBase
{
    /** main box for the wolf head */
    public ModelRenderer wolfHeadMain;

    /** The wolf's body */
    public ModelRenderer wolfBody;

    /** Wolf'se first leg */
    public ModelRenderer wolfLeg1;

    /** Wolf's second leg */
    public ModelRenderer wolfLeg2;

    /** Wolf's third leg */
    public ModelRenderer wolfLeg3;

    /** Wolf's fourth leg */
    public ModelRenderer wolfLeg4;

    /** The wolf's tail */
    ModelRenderer wolfTail;

    /** The wolf's mane */
    ModelRenderer wolfMane;

    public ModelWolf()
    {
        float f = 0.0F;
        float f1 = 13.5F;
        wolfHeadMain = new ModelRenderer(this, 0, 0);
        wolfHeadMain.addBox(-2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F);
        wolfHeadMain.setRotationPoint(-1.0F, 13.5F, -7.0F);
        wolfBody = new ModelRenderer(this, 18, 14);
        wolfBody.addBox(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
        wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
        wolfMane = new ModelRenderer(this, 21, 0);
        wolfMane.addBox(-3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F);
        wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        wolfLeg1 = new ModelRenderer(this, 0, 18);
        wolfLeg1.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
        wolfLeg2 = new ModelRenderer(this, 0, 18);
        wolfLeg2.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
        wolfLeg3 = new ModelRenderer(this, 0, 18);
        wolfLeg3.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
        wolfLeg4 = new ModelRenderer(this, 0, 18);
        wolfLeg4.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
        wolfTail = new ModelRenderer(this, 9, 18);
        wolfTail.addBox(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        wolfHeadMain.setTextureOffset(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        wolfHeadMain.setTextureOffset(16, 14).addBox(2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        wolfHeadMain.setTextureOffset(0, 10).addBox(-0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        if (isChild)
        {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
            wolfHeadMain.renderWithRotation(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            wolfBody.render(scale);
            wolfLeg1.render(scale);
            wolfLeg2.render(scale);
            wolfLeg3.render(scale);
            wolfLeg4.render(scale);
            wolfTail.renderWithRotation(scale);
            wolfMane.render(scale);
            GlStateManager.popMatrix();
        }
        else
        {
            wolfHeadMain.renderWithRotation(scale);
            wolfBody.render(scale);
            wolfLeg1.render(scale);
            wolfLeg2.render(scale);
            wolfLeg3.render(scale);
            wolfLeg4.render(scale);
            wolfTail.renderWithRotation(scale);
            wolfMane.render(scale);
        }
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
    {
        EntityWolf entitywolf = (EntityWolf)entitylivingbaseIn;

        if (entitywolf.isAngry())
        {
            wolfTail.rotateAngleY = 0.0F;
        }
        else
        {
            wolfTail.rotateAngleY = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
        }

        if (entitywolf.isSitting())
        {
            wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            wolfMane.rotateAngleX = ((float)Math.PI * 2F / 5F);
            wolfMane.rotateAngleY = 0.0F;
            wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
            wolfBody.rotateAngleX = ((float)Math.PI / 4F);
            wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
            wolfLeg1.rotateAngleX = ((float)Math.PI * 3F / 2F);
            wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
            wolfLeg2.rotateAngleX = ((float)Math.PI * 3F / 2F);
            wolfLeg3.rotateAngleX = 5.811947F;
            wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
            wolfLeg4.rotateAngleX = 5.811947F;
            wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
        }
        else
        {
            wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
            wolfBody.rotateAngleX = ((float)Math.PI / 2F);
            wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            wolfMane.rotateAngleX = wolfBody.rotateAngleX;
            wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
            wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
            wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
            wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
            wolfLeg1.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
            wolfLeg2.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_78086_3_;
            wolfLeg3.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_78086_3_;
            wolfLeg4.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
        }

        wolfHeadMain.rotateAngleZ = entitywolf.getInterestedAngle(partialTickTime) + entitywolf.getShakeAngle(partialTickTime, 0.0F);
        wolfMane.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.08F);
        wolfBody.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.16F);
        wolfTail.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.2F);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        wolfHeadMain.rotateAngleX = headPitch * 0.017453292F;
        wolfHeadMain.rotateAngleY = netHeadYaw * 0.017453292F;
        wolfTail.rotateAngleX = ageInTicks;
    }
}
