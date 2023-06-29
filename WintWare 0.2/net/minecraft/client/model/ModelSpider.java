package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSpider extends ModelBase {
   public ModelRenderer spiderHead;
   public ModelRenderer spiderNeck;
   public ModelRenderer spiderBody;
   public ModelRenderer spiderLeg1;
   public ModelRenderer spiderLeg2;
   public ModelRenderer spiderLeg3;
   public ModelRenderer spiderLeg4;
   public ModelRenderer spiderLeg5;
   public ModelRenderer spiderLeg6;
   public ModelRenderer spiderLeg7;
   public ModelRenderer spiderLeg8;

   public ModelSpider() {
      float f = 0.0F;
      int i = true;
      this.spiderHead = new ModelRenderer(this, 32, 4);
      this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
      this.spiderHead.setRotationPoint(0.0F, 15.0F, -3.0F);
      this.spiderNeck = new ModelRenderer(this, 0, 0);
      this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
      this.spiderNeck.setRotationPoint(0.0F, 15.0F, 0.0F);
      this.spiderBody = new ModelRenderer(this, 0, 12);
      this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, 0.0F);
      this.spiderBody.setRotationPoint(0.0F, 15.0F, 9.0F);
      this.spiderLeg1 = new ModelRenderer(this, 18, 0);
      this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg1.setRotationPoint(-4.0F, 15.0F, 2.0F);
      this.spiderLeg2 = new ModelRenderer(this, 18, 0);
      this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg2.setRotationPoint(4.0F, 15.0F, 2.0F);
      this.spiderLeg3 = new ModelRenderer(this, 18, 0);
      this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg3.setRotationPoint(-4.0F, 15.0F, 1.0F);
      this.spiderLeg4 = new ModelRenderer(this, 18, 0);
      this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg4.setRotationPoint(4.0F, 15.0F, 1.0F);
      this.spiderLeg5 = new ModelRenderer(this, 18, 0);
      this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg5.setRotationPoint(-4.0F, 15.0F, 0.0F);
      this.spiderLeg6 = new ModelRenderer(this, 18, 0);
      this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg6.setRotationPoint(4.0F, 15.0F, 0.0F);
      this.spiderLeg7 = new ModelRenderer(this, 18, 0);
      this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg7.setRotationPoint(-4.0F, 15.0F, -1.0F);
      this.spiderLeg8 = new ModelRenderer(this, 18, 0);
      this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
      this.spiderLeg8.setRotationPoint(4.0F, 15.0F, -1.0F);
   }

   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
      this.spiderHead.render(scale);
      this.spiderNeck.render(scale);
      this.spiderBody.render(scale);
      this.spiderLeg1.render(scale);
      this.spiderLeg2.render(scale);
      this.spiderLeg3.render(scale);
      this.spiderLeg4.render(scale);
      this.spiderLeg5.render(scale);
      this.spiderLeg6.render(scale);
      this.spiderLeg7.render(scale);
      this.spiderLeg8.render(scale);
   }

   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      this.spiderHead.rotateAngleY = netHeadYaw * 0.017453292F;
      this.spiderHead.rotateAngleX = headPitch * 0.017453292F;
      float f = 0.7853982F;
      this.spiderLeg1.rotateAngleZ = -0.7853982F;
      this.spiderLeg2.rotateAngleZ = 0.7853982F;
      this.spiderLeg3.rotateAngleZ = -0.58119464F;
      this.spiderLeg4.rotateAngleZ = 0.58119464F;
      this.spiderLeg5.rotateAngleZ = -0.58119464F;
      this.spiderLeg6.rotateAngleZ = 0.58119464F;
      this.spiderLeg7.rotateAngleZ = -0.7853982F;
      this.spiderLeg8.rotateAngleZ = 0.7853982F;
      float f1 = -0.0F;
      float f2 = 0.3926991F;
      this.spiderLeg1.rotateAngleY = 0.7853982F;
      this.spiderLeg2.rotateAngleY = -0.7853982F;
      this.spiderLeg3.rotateAngleY = 0.3926991F;
      this.spiderLeg4.rotateAngleY = -0.3926991F;
      this.spiderLeg5.rotateAngleY = -0.3926991F;
      this.spiderLeg6.rotateAngleY = 0.3926991F;
      this.spiderLeg7.rotateAngleY = -0.7853982F;
      this.spiderLeg8.rotateAngleY = 0.7853982F;
      float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
      float f4 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * limbSwingAmount;
      float f5 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * limbSwingAmount;
      float f6 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 4.712389F) * 0.4F) * limbSwingAmount;
      float f7 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
      float f8 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 3.1415927F) * 0.4F) * limbSwingAmount;
      float f9 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 1.5707964F) * 0.4F) * limbSwingAmount;
      float f10 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 4.712389F) * 0.4F) * limbSwingAmount;
      ModelRenderer var10000 = this.spiderLeg1;
      var10000.rotateAngleY += f3;
      var10000 = this.spiderLeg2;
      var10000.rotateAngleY += -f3;
      var10000 = this.spiderLeg3;
      var10000.rotateAngleY += f4;
      var10000 = this.spiderLeg4;
      var10000.rotateAngleY += -f4;
      var10000 = this.spiderLeg5;
      var10000.rotateAngleY += f5;
      var10000 = this.spiderLeg6;
      var10000.rotateAngleY += -f5;
      var10000 = this.spiderLeg7;
      var10000.rotateAngleY += f6;
      var10000 = this.spiderLeg8;
      var10000.rotateAngleY += -f6;
      var10000 = this.spiderLeg1;
      var10000.rotateAngleZ += f7;
      var10000 = this.spiderLeg2;
      var10000.rotateAngleZ += -f7;
      var10000 = this.spiderLeg3;
      var10000.rotateAngleZ += f8;
      var10000 = this.spiderLeg4;
      var10000.rotateAngleZ += -f8;
      var10000 = this.spiderLeg5;
      var10000.rotateAngleZ += f9;
      var10000 = this.spiderLeg6;
      var10000.rotateAngleZ += -f9;
      var10000 = this.spiderLeg7;
      var10000.rotateAngleZ += f10;
      var10000 = this.spiderLeg8;
      var10000.rotateAngleZ += -f10;
   }
}
