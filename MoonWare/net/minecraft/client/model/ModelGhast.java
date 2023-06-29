package net.minecraft.client.model;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelGhast extends ModelBase
{
    ModelRenderer body;
    ModelRenderer[] tentacles = new ModelRenderer[9];

    public ModelGhast()
    {
        int i = -16;
        body = new ModelRenderer(this, 0, 0);
        body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
        body.rotationPointY += 8.0F;
        Random random = new Random(1660L);

        for (int j = 0; j < tentacles.length; ++j)
        {
            tentacles[j] = new ModelRenderer(this, 0, 0);
            float f = (((float)(j % 3) - (float)(j / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float f1 = ((float)(j / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            int k = random.nextInt(7) + 8;
            tentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, k, 2);
            tentacles[j].rotationPointX = f;
            tentacles[j].rotationPointZ = f1;
            tentacles[j].rotationPointY = 15.0F;
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        for (int i = 0; i < tentacles.length; ++i)
        {
            tentacles[i].rotateAngleX = 0.2F * MathHelper.sin(ageInTicks * 0.3F + (float)i) + 0.4F;
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.6F, 0.0F);
        body.render(scale);

        for (ModelRenderer modelrenderer : tentacles)
        {
            modelrenderer.render(scale);
        }

        GlStateManager.popMatrix();
    }
}
