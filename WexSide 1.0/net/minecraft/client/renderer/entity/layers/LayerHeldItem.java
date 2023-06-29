package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import ru.wendoxd.modules.Module;
import ru.wendoxd.utils.combat.CastHelper;

public class LayerHeldItem implements LayerRenderer<EntityLivingBase> {
	protected final RenderLivingBase<?> livingEntityRenderer;

	public LayerHeldItem(RenderLivingBase<?> livingEntityRendererIn) {
		this.livingEntityRenderer = livingEntityRendererIn;
	}

	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
		ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
		ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand()
				: entitylivingbaseIn.getHeldItemOffhand();
		if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
			GlStateManager.pushMatrix();

			if (this.livingEntityRenderer.getMainModel().isChild) {
				float f = 0.5F;
				GlStateManager.translate(0.0F, 0.75F, 0.0F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
			}

			this.renderHeldItem(entitylivingbaseIn, itemstack1,
					ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
			this.renderHeldItem(entitylivingbaseIn, itemstack,
					ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
			GlStateManager.popMatrix();
		}
	}

	private void renderHeldItem(EntityLivingBase p_188358_1_, ItemStack p_188358_2_,
			ItemCameraTransforms.TransformType p_188358_3_, EnumHandSide handSide) {
		if (!p_188358_2_.isEmpty()) {
			GlStateManager.pushMatrix();
			this.func_191361_a(handSide);

			if (p_188358_1_.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			boolean flag = handSide == EnumHandSide.LEFT;
			GlStateManager.translate((float) (flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
			int modelType = 0;
			if (p_188358_1_ instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) p_188358_1_;
				CastHelper castHelper = new CastHelper();
				castHelper.apply(CastHelper.EntityType.PLAYERS);
				castHelper.apply(CastHelper.EntityType.FRIENDS);
				castHelper.apply(CastHelper.EntityType.SELF);
				CastHelper.EntityType type;
				if ((type = CastHelper.isInstanceof(p_188358_1_, castHelper.build())) != null) {
					modelType = Module.visuals_entitiestab.get(type.ordinal()).model.get();
				}
				if (modelType == 1) {
					GlStateManager.translate(0.0F, 0.3F, 0.0F);
				}
				if (modelType == 3) {
					GlStateManager.translate(flag ? -0.1F : 0.1F, 0.0F, -0.3F);
				}
				if (modelType == 4) {
					GlStateManager.translate(0, 0.0F, -0.3F);
				}
			}
			Minecraft.getMinecraft().getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
			GlStateManager.popMatrix();
		}
	}

	protected void func_191361_a(EnumHandSide p_191361_1_) {
		((ModelBiped) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}
