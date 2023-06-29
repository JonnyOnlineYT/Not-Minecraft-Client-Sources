package ru.wendoxd.modules.impl.visuals;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import optifine.Config;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EntityRenderContextEvent;
import ru.wendoxd.modules.Module;
import ru.wendoxd.utils.combat.CastHelper;
import shadersmod.client.Shaders;

public class HitChams extends Module {
	public static HitPosition hitPosition;

	public void onEvent(Event event) {
		if (event instanceof EntityRenderContextEvent) {
		}
	}

	public static class HitPosition {
		private HitRender model;
		private float yaw, yawHead, pitch, limbSwing, swingProgress;
		private Vec3d position;
		private EntityPlayer player;

		public HitPosition(Vec3d position, float yaw, float yawHead, float pitch, float limbSwing, float swingProgress,
				HitRender model, EntityPlayer player) {
			this.model = model;
			this.position = position;
			this.yaw = yaw;
			this.yawHead = yawHead;
			this.pitch = pitch;
			this.limbSwing = limbSwing;
			this.swingProgress = swingProgress;
			this.player = player;
		}

		public void render() {
			float pt = mc.getRenderPartialTicks();
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4f(1, 1, 1, 0.5f);
			double x = position.xCoord - mc.getRenderManager().renderPosX;
			double y = position.yCoord - mc.getRenderManager().renderPosY;
			double z = position.zCoord - mc.getRenderManager().renderPosZ;
			this.model.doRender((AbstractClientPlayer) player, x, y, z, player.rotationYaw, pt, yaw, yawHead, pitch,
					limbSwing, swingProgress);
			GL11.glColor4f(1, 1, 1, 1f);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}

	public class HitRender extends RenderHitBase<AbstractClientPlayer> {
		/** this field is used to indicate the 3-pixel wide arms */
		private final boolean smallArms;

		public HitRender(RenderManager renderManager) {
			this(renderManager, false);
		}

		public HitRender(RenderManager renderManager, boolean useSmallArms) {
			super(renderManager, new ModelPlayer(0.0F, useSmallArms), 0.5F);
			this.smallArms = useSmallArms;
		}

		public ModelPlayer getMainModel() {
			return (ModelPlayer) super.getMainModel();
		}

		/**
		 * Renders the desired {@code T} type Entity.
		 */
		public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw,
				float partialTicks, float yaw, float yawHead, float pitch, float limbSwing, float swingProgress) {
			if (!entity.isUser() || this.renderManager.renderViewEntity == entity) {
				this.yaw = yaw;
				this.yawHead = yawHead;
				this.pitch = pitch;
				this.limbSwing = limbSwing;
				this.swingProgress = swingProgress;
				double d0 = y;
				if (entity.isSneaking()) {
					d0 = y - 0.125D;
				}
				this.setModelVisibilities(entity);
				GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
				super.doRender(entity, x, d0, z, entityYaw, partialTicks);
				GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
			}
		}

		private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
			ModelPlayer modelplayer = this.getMainModel();

			if (clientPlayer.isSpectator()) {
				modelplayer.setInvisible(false);
				modelplayer.bipedHead.showModel = true;
				modelplayer.bipedHeadwear.showModel = true;
			} else {
				modelplayer.setInvisible(true);
				modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
				modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
				modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
				modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
				modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
				modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
				modelplayer.isSneak = clientPlayer.isSneaking();
				ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
				ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;
			}
		}

		/**
		 * Returns the location of an entity's texture. Doesn't seem to be called unless
		 * you call Render.bindEntityTexture.
		 */
		public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
			int modelType = 0;
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				CastHelper castHelper = new CastHelper();
				castHelper.apply(CastHelper.EntityType.PLAYERS);
				castHelper.apply(CastHelper.EntityType.FRIENDS);
				castHelper.apply(CastHelper.EntityType.SELF);
				CastHelper.EntityType type;
				if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null) {
					modelType = Module.visuals_entitiestab.get(type.ordinal()).model.get();
				}
			}
			if (modelType == 1) {
				return RenderPlayer.amogusTexture;
			}
			if (modelType == 2) {
				return RenderPlayer.demonTexture;
			}
			if (modelType == 3) {
				return RenderPlayer.rabbitTexture;
			}
			if (modelType == 4) {
				return RenderPlayer.sonicTexture;
			}
			return entity.getLocationSkin();
		}
	}

	public abstract class RenderHitBase<T extends EntityLivingBase> extends Render<T> {
		public ModelBase mainModel;
		protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
		protected List<LayerRenderer<T>> layerRenderers = Lists.<LayerRenderer<T>>newArrayList();
		protected boolean renderMarker;
		public float yaw, yawHead, pitch, limbSwing, swingProgress;

		public RenderHitBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
			super(renderManagerIn);
			this.mainModel = modelBaseIn;
			this.shadowSize = shadowSizeIn;
		}

		public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
			return this.layerRenderers.add((LayerRenderer<T>) layer);
		}

		public ModelBase getMainModel() {
			return this.mainModel;
		}

		/**
		 * Returns a rotation angle that is inbetween two other rotation angles. par1
		 * and par2 are the angles between which to interpolate, par3 is probably a
		 * float between 0.0 and 1.0 that tells us where "between" the two angles we
		 * are. Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
		 */
		protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
			float f;

			for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
				;
			}

			while (f >= 180.0F) {
				f -= 360.0F;
			}

			return prevYawOffset + partialTicks * f;
		}

		public void transformHeldFull3DItemLayer() {
		}

		/**
		 * Renders the desired {@code T} type Entity.
		 */
		public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
			GlStateManager.pushMatrix();
			GlStateManager.disableCull();
			this.mainModel.swingProgress = swingProgress;
			this.mainModel.isRiding = entity.isRiding();

			this.mainModel.isChild = entity.isChild();

			try {
				float f = entity.rotationYaw;
				float f1 = entity.rotationYawHead;
				float f2 = f1 - f;

				float f7 = pitch;
				this.renderLivingAt(entity, x, y, z);
				float f8 = 0;
				float f4 = this.prepareScale(entity, partialTicks);
				float f5 = 0.0F;
				float f6 = 0.0F;

				if (!entity.isRiding()) {
					f5 = limbSwing;
					f6 = limbSwing;

					if (f5 > 1.0F) {
						f5 = 1.0F;
					}
				}

				GlStateManager.enableAlpha();
				this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
				this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

				boolean flag = this.setDoRenderBrightness(entity, partialTicks);
				this.renderModel(entity, f6, f5, f8, f2, f7, f4, partialTicks);

				if (flag) {
					this.unsetBrightness();
				}

				GlStateManager.depthMask(true);

				if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
					this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
				}

				GlStateManager.disableRescaleNormal();
			} catch (Exception exception1) {
			}

			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.enableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
			super.doRender(entity, x, y, z, entityYaw, partialTicks);

		}

		public float prepareScale(T entitylivingbaseIn, float partialTicks) {
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(entitylivingbaseIn, partialTicks);
			float f = 0.0625F;
			GlStateManager.translate(0.0F, -1.501F, 0.0F);
			return 0.0625F;
		}

		protected boolean setScoreTeamColor(T entityLivingBaseIn) {
			GlStateManager.disableLighting();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
			return true;
		}

		protected void unsetScoreTeamColor() {
			GlStateManager.enableLighting();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.enableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		}

		/**
		 * Renders the model in RenderLiving
		 */
		protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks,
				float netHeadYaw, float headPitch, float scaleFactor, float partialTicks) {
			boolean flag = this.func_193115_c(entitylivingbaseIn);
			boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().player);

			if (flag || flag1) {
				if (!this.bindEntityTexture(entitylivingbaseIn)) {
					return;
				}

				if (flag1) {
					GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
				}

				this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
						scaleFactor);

				if (flag1) {
					GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
				}
			}
		}

		protected boolean func_193115_c(T p_193115_1_) {
			return !p_193115_1_.isInvisible() || this.renderOutlines;
		}

		protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
			return this.setBrightness(entityLivingBaseIn, partialTicks, true);
		}

		protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
			float f = entitylivingbaseIn.getBrightness();
			int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
			boolean flag = (i >> 24 & 255) > 0;
			boolean flag1 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;

			if (!flag && !flag1) {
				return false;
			} else if (!flag && !combineTextures) {
				return false;
			} else {
				return true;
			}
		}

		protected void unsetBrightness() {
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
			GlStateManager.enableTexture2D();
			GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
			GlStateManager.disableTexture2D();
			GlStateManager.bindTexture(0);
			GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
			GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

			if (Config.isShaders()) {
				Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
			}
		}

		/**
		 * Sets a simple glTranslate on a LivingEntity.
		 */
		protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
			GlStateManager.translate((float) x, (float) y, (float) z);
		}

		/**
		 * Returns where in the swing animation the living entity is (from 0 to 1). Args
		 * : entity, partialTickTime
		 */
		protected float getSwingProgress(T livingBase, float partialTickTime) {
			return livingBase.getSwingProgress(partialTickTime);
		}

		/**
		 * Defines what float the third param in setRotationAngles of ModelBase is
		 */
		protected float handleRotationFloat(T livingBase, float partialTicks) {
			return (float) livingBase.ticksExisted + partialTicks;
		}

		protected void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
				float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
			for (LayerRenderer<T> layerrenderer : this.layerRenderers) {
				boolean flag = this.setBrightness(entitylivingbaseIn, partialTicks,
						layerrenderer.shouldCombineTextures());
				layerrenderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks,
						netHeadYaw, headPitch, scaleIn);

				if (flag) {
					this.unsetBrightness();
				}
			}
		}

		protected float getDeathMaxRotation(T entityLivingBaseIn) {
			return 90.0F;
		}

		/**
		 * Gets an RGBA int color multiplier to apply.
		 */
		protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
			return 0;
		}

		/**
		 * Allows the render to do state modifications necessary before the model is
		 * rendered.
		 */
		protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
		}

		public void renderName(T entity, double x, double y, double z) {
		}

		protected boolean canRenderName(T entity) {

			return false;
		}

		public List<LayerRenderer<T>> getLayerRenderers() {
			return this.layerRenderers;
		}
	}

}
