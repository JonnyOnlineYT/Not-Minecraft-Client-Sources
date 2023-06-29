package net.minecraft.client.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import volcano.summer.base.Summer;
import volcano.summer.client.modules.render.HUD;
import volcano.summer.client.modules.render.Wings;

public class ModelBiped extends ModelBase {
	private static final ResourceLocation enderDragonTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon.png");
	private ModelRenderer wing;
	private ModelRenderer wingTip;
	HashMap<UUID, Long> wingState;

	public ModelRenderer bipedHead;

	/** The Biped's Headwear. Used for the outer layer of player skins. */
	public ModelRenderer bipedHeadwear;
	public ModelRenderer bipedBody;

	/** The Biped's Right Arm */
	public ModelRenderer bipedRightArm;

	/** The Biped's Left Arm */
	public ModelRenderer bipedLeftArm;

	/** The Biped's Right Leg */
	public ModelRenderer bipedRightLeg;

	/** The Biped's Left Leg */
	public ModelRenderer bipedLeftLeg;

	/**
	 * Records whether the model should be rendered holding an item in the left
	 * hand, and if that item is a block.
	 */
	public int heldItemLeft;

	/**
	 * Records whether the model should be rendered holding an item in the right
	 * hand, and if that item is a block.
	 */
	public int heldItemRight;
	public boolean isSneak;

	/** Records whether the model should be rendered aiming a bow. */
	public boolean aimedBow;
	private static final String __OBFID = "CL_00000840";

	public ModelBiped() {
		this(0.0F);
	}

	public ModelBiped(float p_i1148_1_) {
		this(p_i1148_1_, 0.0F, 64, 32);
	}

	public ModelBiped(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_, int p_i1149_4_) {

		// TODO:WINGS
		this.wingState = new HashMap();
		this.setTextureOffset("wingtip.bone", 112, 136);
		this.setTextureOffset("wing.skin", -56, 88);
		this.setTextureOffset("wing.bone", 112, 88);
		this.setTextureOffset("wingtip.skin", -56, 144);
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.wing = new ModelRenderer(this, "wing");
		this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
		this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
		this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 1, 56);
		this.wingTip = new ModelRenderer(this, "wingtip");
		this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
		this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
		this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 1, 56);
		this.wing.addChild(this.wingTip);

		this.textureWidth = p_i1149_3_;
		this.textureHeight = p_i1149_4_;
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1149_1_);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1149_1_ + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i1149_1_);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);

	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			float var8 = 2.0F;
			GlStateManager.scale(1.5F / var8, 1.5F / var8, 1.5F / var8);
			GlStateManager.translate(0.0F, 16.0F * p_78088_7_, 0.0F);
			this.bipedHead.render(p_78088_7_);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
			GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
			this.bipedBody.render(p_78088_7_);
			this.bipedRightArm.render(p_78088_7_);
			this.bipedLeftArm.render(p_78088_7_);
			this.bipedRightLeg.render(p_78088_7_);
			this.bipedLeftLeg.render(p_78088_7_);
			this.bipedHeadwear.render(p_78088_7_);
		} else {
			if (p_78088_1_.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.bipedHead.render(p_78088_7_);
			this.bipedBody.render(p_78088_7_);
			this.bipedRightArm.render(p_78088_7_);
			this.bipedLeftArm.render(p_78088_7_);
			this.bipedRightLeg.render(p_78088_7_);
			this.bipedLeftLeg.render(p_78088_7_);
			this.bipedHeadwear.render(p_78088_7_);
		}

		GlStateManager.popMatrix();
		if ((p_78088_1_ == Minecraft.getMinecraft().thePlayer || Summer.friendManager.isFriend(p_78088_1_.getName()))
				&& Summer.moduleManager.getModule(Wings.class).state) {

			GlStateManager.pushMatrix();
			float f25 = 100.0F;
			boolean flag3 = this.wingState.containsKey(p_78088_1_.getUniqueID());
			boolean flag4 = p_78088_1_.onGround;

			if (!flag4 || flag3) {
				f25 = 10.0F;
			}

			if (!flag3 && !flag4) {
				this.wingState.put(p_78088_1_.getUniqueID(), Long.valueOf(0L));
			}

			float f33 = p_78088_3_ + p_78088_4_ / f25;
			float f34 = p_78088_3_ + p_78088_4_ / 100.0F;
			float f5 = f33 * (float) Math.PI * 2.0F;
			float f35 = 0.125F - (float) Math.cos((double) f5) * 0.2F;
			float f6 = f34 * (float) Math.PI * 2.0F;
			float f36 = 0.125F - (float) Math.cos((double) f6) * 0.2F;

			if (this.wingState.containsKey(p_78088_1_.getUniqueID()) && (int) (f35 * 100.0F) == (int) (f36 * 100.0F)
					&& flag4) {
				this.wingState.remove(p_78088_1_.getUniqueID());
				f25 = 100.0F;
			}

			Minecraft.getMinecraft().getTextureManager().bindTexture(enderDragonTextures);
			GlStateManager.scale(0.0012D * (double) (Wings.size.getValue().doubleValue() + 75),
					0.0012D * (double) (Wings.size.getValue().doubleValue() + 75),
					0.0012D * (double) (Wings.size.getValue().doubleValue() + 75));
			GlStateManager.translate(0.0D, p_78088_1_.isSneaking() ? 0.7D : -0.4D, 0.5D);
			GlStateManager.rotate(50.0F, -50.0F, 0.0F, 0.0F);
			boolean flag5 = false;
			boolean flag = false;
			int rainbowHex = HUD.getRainbow(6000, 10);
			float red = (rainbowHex >> 24/* 24 */ & 255) / 255.0F;
			float green = (rainbowHex >> 16/* 16 */ & 255) / 255.0F;
			float blue = (rainbowHex >> 8/* 8 */ & 255) / 255.0F;
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			if (Wings.rainbow.getValue()) {
				GL11.glColor3f(red, green, blue);
			} else {
				GL11.glColor3d((float) Wings.red.getValue().doubleValue() / 255,
						(float) Wings.green.getValue().doubleValue() / 255,
						(float) Wings.blue.getValue().doubleValue() / 255);
			}

			GlStateManager.disableBooleanStateAt(0);
			GlStateManager.disableBooleanStateAt(1);
			for (int i4 = 0; i4 < 2; ++i4) {
				GlStateManager.enableCull();
				float f8 = f33 * (float) Math.PI * 2.0F;
				this.wing.rotateAngleX = 0.125F - (float) Math.cos((double) f8) * 0.2F;
				this.wing.rotateAngleY = 0.25F;
				this.wing.rotateAngleZ = (float) (Math.sin((double) f8) + 1.225D) * 0.3F;
				this.wingTip.rotateAngleZ = -((float) (Math.sin((double) (f8 + 2.0F)) + 0.5D)) * 0.75F;
				this.wing.isHidden = false;
				this.wingTip.isHidden = false;
				this.wing.render(p_78088_7_);
				this.wing.isHidden = true;
				this.wingTip.isHidden = true;
				GlStateManager.scale(-1.0F, 1.0F, 1.0F);

				if (i4 == 0) {
					GlStateManager.cullFace(1028);

					if (flag5) {
						if (flag) {
							GL11.glColor3d(0.0D, 0.0D, 18.0D);
						} else {
							GL11.glColor3d(18.0D, 0.0D, 0.0D);
						}
					}
				}
			}
			GlStateManager.enableBooleanStateAt(0);
			GlStateManager.enableBooleanStateAt(1);
			GlStateManager.cullFace(1029);
			GlStateManager.disableCull();
			GlStateManager.enableDepth();
			GlStateManager.popMatrix();
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) p_78088_1_;
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			ResourceLocation resourcelocation = abstractclientplayer.getLocationSkin();
			Minecraft.getMinecraft().getTextureManager().bindTexture(resourcelocation);

		}
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used
	 * for animating the movement of arms and legs, where par1 represents the
	 * time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		this.bipedHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		this.bipedRightArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 2.0F * p_78087_2_
				* 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F * p_78087_2_ * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 1.4F * p_78087_2_;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		if (this.isRiding) {
			this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
			this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
			this.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			this.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
			this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
		}

		if (this.heldItemLeft != 0) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F
					- ((float) Math.PI / 10F) * (float) this.heldItemLeft;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleZ = 0.0F;

		switch (this.heldItemRight) {
		case 0:
		case 2:
		default:
			break;

		case 1:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
					- ((float) Math.PI / 10F) * (float) this.heldItemRight;
			break;

		case 3:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
					- ((float) Math.PI / 10F) * (float) this.heldItemRight;
			this.bipedRightArm.rotateAngleY = -0.5235988F;
		}

		this.bipedLeftArm.rotateAngleY = 0.0F;
		float var8;
		float var9;

		if (this.swingProgress > -9990.0F) {
			var8 = this.swingProgress;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float) Math.PI * 2.0F) * 0.2F;
			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			var8 = 1.0F - this.swingProgress;
			var8 *= var8;
			var8 *= var8;
			var8 = 1.0F - var8;
			var9 = MathHelper.sin(var8 * (float) Math.PI);
			float var10 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F)
					* 0.75F;
			this.bipedRightArm.rotateAngleX = (float) ((double) this.bipedRightArm.rotateAngleX
					- ((double) var9 * 1.2D + (double) var10));
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
		}

		if (this.isSneak) {
			this.bipedBody.rotateAngleX = 0.5F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedRightLeg.rotationPointZ = 4.0F;
			this.bipedLeftLeg.rotationPointZ = 4.0F;
			this.bipedRightLeg.rotationPointY = 9.0F;
			this.bipedLeftLeg.rotationPointY = 9.0F;
			this.bipedHead.rotationPointY = 1.0F;
		} else {
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0.1F;
			this.bipedLeftLeg.rotationPointZ = 0.1F;
			this.bipedRightLeg.rotationPointY = 12.0F;
			this.bipedLeftLeg.rotationPointY = 12.0F;
			this.bipedHead.rotationPointY = 0.0F;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;

		if (this.aimedBow) {
			var8 = 0.0F;
			var9 = 0.0F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F) + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
		}

		func_178685_a(this.bipedHead, this.bipedHeadwear);
	}

	public void setModelAttributes(ModelBase p_178686_1_) {
		super.setModelAttributes(p_178686_1_);

		if (p_178686_1_ instanceof ModelBiped) {
			ModelBiped var2 = (ModelBiped) p_178686_1_;
			this.heldItemLeft = var2.heldItemLeft;
			this.heldItemRight = var2.heldItemRight;
			this.isSneak = var2.isSneak;
			this.aimedBow = var2.aimedBow;
		}
	}

	public void func_178719_a(boolean p_178719_1_) {
		this.bipedHead.showModel = p_178719_1_;
		this.bipedHeadwear.showModel = p_178719_1_;
		this.bipedBody.showModel = p_178719_1_;
		this.bipedRightArm.showModel = p_178719_1_;
		this.bipedLeftArm.showModel = p_178719_1_;
		this.bipedRightLeg.showModel = p_178719_1_;
		this.bipedLeftLeg.showModel = p_178719_1_;
	}

	public void postRenderHiddenArm(float p_178718_1_) {
		this.bipedRightArm.postRender(p_178718_1_);
	}

	public static int getRainbow(int speed, int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		hue /= speed;
		return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
	}
}
