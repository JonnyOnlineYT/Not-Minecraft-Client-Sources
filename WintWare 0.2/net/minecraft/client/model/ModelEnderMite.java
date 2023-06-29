package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelEnderMite extends ModelBase {
   private static final int[][] BODY_SIZES = new int[][]{{4, 3, 2}, {6, 4, 5}, {3, 3, 1}, {1, 2, 1}};
   private static final int[][] BODY_TEXS = new int[][]{{0, 0}, {0, 5}, {0, 14}, {0, 18}};
   private static final int BODY_COUNT;
   private final ModelRenderer[] bodyParts;

   public ModelEnderMite() {
      this.bodyParts = new ModelRenderer[BODY_COUNT];
      float f = -3.5F;

      for(int i = 0; i < this.bodyParts.length; ++i) {
         this.bodyParts[i] = new ModelRenderer(this, BODY_TEXS[i][0], BODY_TEXS[i][1]);
         this.bodyParts[i].addBox((float)BODY_SIZES[i][0] * -0.5F, 0.0F, (float)BODY_SIZES[i][2] * -0.5F, BODY_SIZES[i][0], BODY_SIZES[i][1], BODY_SIZES[i][2]);
         this.bodyParts[i].setRotationPoint(0.0F, (float)(24 - BODY_SIZES[i][1]), f);
         if (i < this.bodyParts.length - 1) {
            f += (float)(BODY_SIZES[i][2] + BODY_SIZES[i + 1][2]) * 0.5F;
         }
      }

   }

   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
      ModelRenderer[] var8 = this.bodyParts;
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         ModelRenderer modelrenderer = var8[var10];
         modelrenderer.render(scale);
      }

   }

   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      for(int i = 0; i < this.bodyParts.length; ++i) {
         this.bodyParts[i].rotateAngleY = MathHelper.cos(ageInTicks * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.01F * (float)(1 + Math.abs(i - 2));
         this.bodyParts[i].rotationPointX = MathHelper.sin(ageInTicks * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.1F * (float)Math.abs(i - 2);
      }

   }

   static {
      BODY_COUNT = BODY_SIZES.length;
   }
}
