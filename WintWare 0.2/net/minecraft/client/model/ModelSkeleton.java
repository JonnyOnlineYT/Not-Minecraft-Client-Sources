package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelSkeleton extends ModelBiped {
   public ModelSkeleton() {
      this(0.0F, false);
   }

   public ModelSkeleton(float modelSize, boolean p_i46303_2_) {
      super(modelSize, 0.0F, 64, 32);
      if (!p_i46303_2_) {
         this.bipedRightArm = new ModelRenderer(this, 40, 16);
         this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
         this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
         this.bipedLeftArm = new ModelRenderer(this, 40, 16);
         this.bipedLeftArm.mirror = true;
         this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
         this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
         this.bipedRightLeg = new ModelRenderer(this, 0, 16);
         this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
         this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
         this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
         this.bipedLeftLeg.mirror = true;
         this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
         this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
      }

   }

   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
      this.rightArmPose = ModelBiped.ArmPose.EMPTY;
      this.leftArmPose = ModelBiped.ArmPose.EMPTY;
      ItemStack itemstack = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);
      if (itemstack.getItem() == Items.BOW && ((AbstractSkeleton)entitylivingbaseIn).isSwingingArms()) {
         if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT) {
            this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
         } else {
            this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
         }
      }

      super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
   }

   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      ItemStack itemstack = ((EntityLivingBase)entityIn).getHeldItemMainhand();
      AbstractSkeleton abstractskeleton = (AbstractSkeleton)entityIn;
      if (abstractskeleton.isSwingingArms() && (itemstack.func_190926_b() || itemstack.getItem() != Items.BOW)) {
         float f = MathHelper.sin(this.swingProgress * 3.1415927F);
         float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
         this.bipedRightArm.rotateAngleZ = 0.0F;
         this.bipedLeftArm.rotateAngleZ = 0.0F;
         this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
         this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
         this.bipedRightArm.rotateAngleX = -1.5707964F;
         this.bipedLeftArm.rotateAngleX = -1.5707964F;
         ModelRenderer var10000 = this.bipedRightArm;
         var10000.rotateAngleX -= f * 1.2F - f1 * 0.4F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX -= f * 1.2F - f1 * 0.4F;
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

   public void postRenderArm(float scale, EnumHandSide side) {
      float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
      ModelRenderer modelrenderer = this.getArmForSide(side);
      modelrenderer.rotationPointX += f;
      modelrenderer.postRender(scale);
      modelrenderer.rotationPointX -= f;
   }
}
