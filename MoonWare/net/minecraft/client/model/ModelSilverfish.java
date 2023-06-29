package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSilverfish extends ModelBase
{
    /** The body parts of the silverfish's model. */
    private final ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];

    /** The wings (dust-looking sprites) on the silverfish's model. */
    private final ModelRenderer[] silverfishWings;
    private final float[] zPlacement = new float[7];

    /** The widths, heights, and lengths for the silverfish model boxes. */
    private static final int[][] SILVERFISH_BOX_LENGTH = {{3, 2, 2}, {4, 3, 2}, {6, 4, 3}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};

    /** The texture positions for the silverfish's model's boxes. */
    private static final int[][] SILVERFISH_TEXTURE_POSITIONS = {{0, 0}, {0, 4}, {0, 9}, {0, 16}, {0, 22}, {11, 0}, {13, 4}};

    public ModelSilverfish()
    {
        float f = -3.5F;

        for (int i = 0; i < silverfishBodyParts.length; ++i)
        {
            silverfishBodyParts[i] = new ModelRenderer(this, SILVERFISH_TEXTURE_POSITIONS[i][0], SILVERFISH_TEXTURE_POSITIONS[i][1]);
            silverfishBodyParts[i].addBox((float) SILVERFISH_BOX_LENGTH[i][0] * -0.5F, 0.0F, (float) SILVERFISH_BOX_LENGTH[i][2] * -0.5F, SILVERFISH_BOX_LENGTH[i][0], SILVERFISH_BOX_LENGTH[i][1], SILVERFISH_BOX_LENGTH[i][2]);
            silverfishBodyParts[i].setRotationPoint(0.0F, (float)(24 - SILVERFISH_BOX_LENGTH[i][1]), f);
            zPlacement[i] = f;

            if (i < silverfishBodyParts.length - 1)
            {
                f += (float)(SILVERFISH_BOX_LENGTH[i][2] + SILVERFISH_BOX_LENGTH[i + 1][2]) * 0.5F;
            }
        }

        silverfishWings = new ModelRenderer[3];
        silverfishWings[0] = new ModelRenderer(this, 20, 0);
        silverfishWings[0].addBox(-5.0F, 0.0F, (float) SILVERFISH_BOX_LENGTH[2][2] * -0.5F, 10, 8, SILVERFISH_BOX_LENGTH[2][2]);
        silverfishWings[0].setRotationPoint(0.0F, 16.0F, zPlacement[2]);
        silverfishWings[1] = new ModelRenderer(this, 20, 11);
        silverfishWings[1].addBox(-3.0F, 0.0F, (float) SILVERFISH_BOX_LENGTH[4][2] * -0.5F, 6, 4, SILVERFISH_BOX_LENGTH[4][2]);
        silverfishWings[1].setRotationPoint(0.0F, 20.0F, zPlacement[4]);
        silverfishWings[2] = new ModelRenderer(this, 20, 18);
        silverfishWings[2].addBox(-3.0F, 0.0F, (float) SILVERFISH_BOX_LENGTH[4][2] * -0.5F, 6, 5, SILVERFISH_BOX_LENGTH[1][2]);
        silverfishWings[2].setRotationPoint(0.0F, 19.0F, zPlacement[1]);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        for (ModelRenderer modelrenderer : silverfishBodyParts)
        {
            modelrenderer.render(scale);
        }

        for (ModelRenderer modelrenderer1 : silverfishWings)
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
        for (int i = 0; i < silverfishBodyParts.length; ++i)
        {
            silverfishBodyParts[i].rotateAngleY = MathHelper.cos(ageInTicks * 0.9F + (float)i * 0.15F * (float)Math.PI) * (float)Math.PI * 0.05F * (float)(1 + Math.abs(i - 2));
            silverfishBodyParts[i].rotationPointX = MathHelper.sin(ageInTicks * 0.9F + (float)i * 0.15F * (float)Math.PI) * (float)Math.PI * 0.2F * (float)Math.abs(i - 2);
        }

        silverfishWings[0].rotateAngleY = silverfishBodyParts[2].rotateAngleY;
        silverfishWings[1].rotateAngleY = silverfishBodyParts[4].rotateAngleY;
        silverfishWings[1].rotationPointX = silverfishBodyParts[4].rotationPointX;
        silverfishWings[2].rotateAngleY = silverfishBodyParts[1].rotateAngleY;
        silverfishWings[2].rotationPointX = silverfishBodyParts[1].rotationPointX;
    }
}
