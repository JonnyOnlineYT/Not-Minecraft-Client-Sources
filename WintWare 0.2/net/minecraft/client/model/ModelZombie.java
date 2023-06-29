package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;

public class ModelZombie extends ModelBiped {
   public ModelZombie() {
      this(0.0F, false);
   }

   public ModelZombie(float modelSize, boolean p_i1168_2_) {
      super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
   }

   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      boolean flag = entityIn instanceof EntityZombie && ((EntityZombie)entityIn).isArmsRaised();
      float f = MathHelper.sin(this.swingProgress * 3.1415927F);
      float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
      this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
      float f2 = -3.1415927F / (flag ? 1.5F : 2.25F);
      this.bipedRightArm.rotateAngleX = f2;
      this.bipedLeftArm.rotateAngleX = f2;
      ModelRenderer var10000 = this.bipedRightArm;
      var10000.rotateAngleX += f * 1.2F - f1 * 0.4F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX += f * 1.2F - f1 * 0.4F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
   }
}
