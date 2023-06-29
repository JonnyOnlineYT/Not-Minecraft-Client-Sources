package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.math.MathHelper;

public class ModelWither extends ModelBase
{
    private final ModelRenderer[] upperBodyParts;
    private final ModelRenderer[] heads;

    public ModelWither(float p_i46302_1_)
    {
        textureWidth = 64;
        textureHeight = 64;
        upperBodyParts = new ModelRenderer[3];
        upperBodyParts[0] = new ModelRenderer(this, 0, 16);
        upperBodyParts[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, p_i46302_1_);
        upperBodyParts[1] = (new ModelRenderer(this)).setTextureSize(textureWidth, textureHeight);
        upperBodyParts[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
        upperBodyParts[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, p_i46302_1_);
        upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, p_i46302_1_);
        upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, p_i46302_1_);
        upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, p_i46302_1_);
        upperBodyParts[2] = new ModelRenderer(this, 12, 22);
        upperBodyParts[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, p_i46302_1_);
        heads = new ModelRenderer[3];
        heads[0] = new ModelRenderer(this, 0, 0);
        heads[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, p_i46302_1_);
        heads[1] = new ModelRenderer(this, 32, 0);
        heads[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
        heads[1].rotationPointX = -8.0F;
        heads[1].rotationPointY = 4.0F;
        heads[2] = new ModelRenderer(this, 32, 0);
        heads[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
        heads[2].rotationPointX = 10.0F;
        heads[2].rotationPointY = 4.0F;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        for (ModelRenderer modelrenderer : heads)
        {
            modelrenderer.render(scale);
        }

        for (ModelRenderer modelrenderer1 : upperBodyParts)
        {
            modelrenderer1.render(scale);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float f = MathHelper.cos(ageInTicks * 0.1F);
        upperBodyParts[1].rotateAngleX = (0.065F + 0.05F * f) * (float)Math.PI;
        upperBodyParts[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(upperBodyParts[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(upperBodyParts[1].rotateAngleX) * 10.0F);
        upperBodyParts[2].rotateAngleX = (0.265F + 0.1F * f) * (float)Math.PI;
        heads[0].rotateAngleY = netHeadYaw * 0.017453292F;
        heads[0].rotateAngleX = headPitch * 0.017453292F;
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
    {
        EntityWither entitywither = (EntityWither)entitylivingbaseIn;

        for (int i = 1; i < 3; ++i)
        {
            heads[i].rotateAngleY = (entitywither.getHeadYRotation(i - 1) - entitylivingbaseIn.renderYawOffset) * 0.017453292F;
            heads[i].rotateAngleX = entitywither.getHeadXRotation(i - 1) * 0.017453292F;
        }
    }
}
