package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase {
   ModelRenderer squidBody;
   ModelRenderer[] squidTentacles = new ModelRenderer[8];

   public ModelSquid() {
      int i = true;
      this.squidBody = new ModelRenderer(this, 0, 0);
      this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
      ModelRenderer var10000 = this.squidBody;
      var10000.rotationPointY += 8.0F;

      for(int j = 0; j < this.squidTentacles.length; ++j) {
         this.squidTentacles[j] = new ModelRenderer(this, 48, 0);
         double d0 = (double)j * 3.141592653589793D * 2.0D / (double)this.squidTentacles.length;
         float f = (float)Math.cos(d0) * 5.0F;
         float f1 = (float)Math.sin(d0) * 5.0F;
         this.squidTentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
         this.squidTentacles[j].rotationPointX = f;
         this.squidTentacles[j].rotationPointZ = f1;
         this.squidTentacles[j].rotationPointY = 15.0F;
         d0 = (double)j * 3.141592653589793D * -2.0D / (double)this.squidTentacles.length + 1.5707963267948966D;
         this.squidTentacles[j].rotateAngleY = (float)d0;
      }

   }

   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      ModelRenderer[] var8 = this.squidTentacles;
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         ModelRenderer modelrenderer = var8[var10];
         modelrenderer.rotateAngleX = ageInTicks;
      }

   }

   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
      this.squidBody.render(scale);
      ModelRenderer[] var8 = this.squidTentacles;
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         ModelRenderer modelrenderer = var8[var10];
         modelrenderer.render(scale);
      }

   }
}
