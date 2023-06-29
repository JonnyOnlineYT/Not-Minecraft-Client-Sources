package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.visual.CustomModel;

public class LayerHeldItem
        implements LayerRenderer<EntityLivingBase> {
    protected final RenderLivingBase<?> livingEntityRenderer;

    public LayerHeldItem(RenderLivingBase<?> livingEntityRendererIn) {
        livingEntityRenderer = livingEntityRendererIn;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Jeff Killer")) {
            if (!(!CustomModel.onlyMe.getCurrentValue() || entitylivingbaseIn == Minecraft.player || MoonWare.friendManager.isFriend(entitylivingbaseIn.getName()) && CustomModel.friends.getCurrentValue())) {
                ItemStack itemstack1;
                boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
                ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
                ItemStack itemStack = itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
                if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
                    GlStateManager.pushMatrix();
                    if (livingEntityRenderer.getMainModel().isChild) {
                        float f = 0.5f;
                        GlStateManager.translate(0.0f, 0.75f, 0.0f);
                        GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    }
                    renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
                    renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
                    GlStateManager.popMatrix();
                }
            } else {
                ItemStack itemstack;
                boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.LEFT;
                ItemStack itemStack = itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
                if (!itemstack.isEmpty()) {
                    GlStateManager.pushMatrix();
                    if (livingEntityRenderer.getMainModel().isChild) {
                        float f = 0.5f;
                        GlStateManager.translate(0.0f, 0.75f, 0.0f);
                        GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    }
                    renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
                    GlStateManager.popMatrix();
                }
            }
        } else {
            ItemStack itemstack1;
            boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
            ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
            ItemStack itemStack = itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
            if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
                GlStateManager.pushMatrix();
                if (livingEntityRenderer.getMainModel().isChild) {
                    float f = 0.5f;
                    GlStateManager.translate(0.0f, 0.75f, 0.0f);
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                }
                renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
                renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
                GlStateManager.popMatrix();
            }
        }
    }

    private void renderHeldItem(EntityLivingBase p_188358_1_, ItemStack p_188358_2_, ItemCameraTransforms.TransformType p_188358_3_, EnumHandSide handSide) {
        if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Chinchilla"))) {
            if (!p_188358_2_.isEmpty()) {
                boolean flag;
                GlStateManager.pushMatrix();
                func_191361_a(handSide);
                if (p_188358_1_.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.2f, 0.0f);
                }
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                boolean bl = flag = handSide == EnumHandSide.LEFT;
                if (!(!CustomModel.onlyMe.getCurrentValue() || p_188358_1_ == Minecraft.player || MoonWare.friendManager.isFriend(p_188358_1_.getName()) && CustomModel.friends.getCurrentValue())) {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.125f, -0.625f);
                } else {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.525f, -1.05);
                }
                Minecraft.getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
                GlStateManager.popMatrix();
            }
        } else if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Freddy Bear")) {
            if (!p_188358_2_.isEmpty()) {
                boolean flag;
                GlStateManager.pushMatrix();
                func_191361_a(handSide);
                if (p_188358_1_.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.2f, 0.0f);
                }
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                boolean bl = flag = handSide == EnumHandSide.LEFT;
                if (!(!CustomModel.onlyMe.getCurrentValue() || p_188358_1_ == Minecraft.player || MoonWare.friendManager.isFriend(p_188358_1_.getName()) && CustomModel.friends.getCurrentValue())) {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.125f, -0.625f);
                } else if (p_188358_1_.isSneaking()) {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.2f, -0.3);
                } else {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.3f, -0.5);
                }
                Minecraft.getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
                GlStateManager.popMatrix();
            }
        } else if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Sonic")) {
            if (!p_188358_2_.isEmpty()) {
                boolean flag;
                GlStateManager.pushMatrix();
                func_191361_a(handSide);
                if (p_188358_1_.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.2f, 0.0f);
                }
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                boolean bl = flag = handSide == EnumHandSide.LEFT;
                if (!(!CustomModel.onlyMe.getCurrentValue() || p_188358_1_ == Minecraft.player || MoonWare.friendManager.isFriend(p_188358_1_.getName()) && CustomModel.friends.getCurrentValue())) {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.125f, -0.625f);
                } else {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f + 0.05f, 0.05f, -0.925f);
                }
                Minecraft.getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
                GlStateManager.popMatrix();
            }
        } else if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("CupHead")) {
            if (!p_188358_2_.isEmpty()) {
                boolean flag;
                GlStateManager.pushMatrix();
                func_191361_a(handSide);
                if (p_188358_1_.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.2f, 0.0f);
                }
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                boolean bl = flag = handSide == EnumHandSide.LEFT;
                if (!(!CustomModel.onlyMe.getCurrentValue() || p_188358_1_ == Minecraft.player || MoonWare.friendManager.isFriend(p_188358_1_.getName()) && CustomModel.friends.getCurrentValue())) {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.125f, -0.625f);
                } else {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, -0.325f, -0.625f);
                }
                Minecraft.getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
                GlStateManager.popMatrix();
            }
        } else if (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Crazy Rabbit")) {
            if (!p_188358_2_.isEmpty()) {
                boolean flag;
                GlStateManager.pushMatrix();
                func_191361_a(handSide);
                if (p_188358_1_.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.2f, 0.0f);
                }
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                boolean bl = flag = handSide == EnumHandSide.LEFT;
                if (!(!CustomModel.onlyMe.getCurrentValue() || p_188358_1_ == Minecraft.player || MoonWare.friendManager.isFriend(p_188358_1_.getName()) && CustomModel.friends.getCurrentValue())) {
                    GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.125f, -0.625f);
                } else {
                    GlStateManager.translate((float)(flag ? -2 : 2) / 16.0f, 0.15f, -1.0f);
                }
                Minecraft.getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
                GlStateManager.popMatrix();
            }
        } else if (!p_188358_2_.isEmpty()) {
            GlStateManager.pushMatrix();
            func_191361_a(handSide);
            if (p_188358_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate((float)(flag ? -1 : 1) / 16.0f, 0.125f, -0.625f);
            Minecraft.getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
            GlStateManager.popMatrix();
        }
    }

    protected void func_191361_a(EnumHandSide p_191361_1_) {
        ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0625f, p_191361_1_);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
