package org.spray.heaven.features.module.modules.display;

import java.awt.Color;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.combat.KillAura;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.draggable.Dragging;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.RotationUtil;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.animation.impl.EaseBackIn;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@ModuleInfo(name = "TargetHUD", category = Category.DISPLAY, visible = true, key = Keyboard.KEY_NONE)
public class TargetHUD extends Module {

	private final Setting mode = register(new Setting("Mode", "Heaven", Arrays.asList("Heaven", "Kirka", "Heaven2")));
	private final Setting stickyMode = (new Setting("Sticky Mode", false));

	private final Setting glowing = register(
			new Setting("Glowing", false).setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Heaven")));
	private final Setting color = register(new Setting("Color", new Color(0xFF13F392)));

	public final Dragging drag = Wrapper.createDrag(this, "targethud", 40, 40);

	private final Animation openAnimation = new EaseBackIn(270, .4f, 1.13f);
	private final Animation animation = new DecelerateAnimation(270, 1f);
	private float width, height;

	private EntityLivingBase prevTarget;

	private float healthPos;
	private float stranimation;

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
	}

	@Override
	public void onRender(int scaleWidth, int scaleHeight, float tickDelta) {
		float x = drag.getX(), y = drag.getY();

		KillAura killAura = Wrapper.getModule().get("killaura");

		if (killAura.getTarget() != null)
			prevTarget = (EntityLivingBase) killAura.getTarget();

		openAnimation.setDirection(killAura.getTarget() != null ? Direction.FORWARDS : Direction.BACKWARDS);
		animation.setDirection(killAura.getTarget() != null ? Direction.FORWARDS : Direction.BACKWARDS);

		if (prevTarget == null || openAnimation.finished(Direction.BACKWARDS) || mc.world == null) {
			healthPos = 0;
			return;
		}

		float scaleAnim = (float) (openAnimation.getOutput() + 0.6f);
		float alphaFade = (float) animation.getOutput();

		if (stickyMode.isToggle()) {
			float[] rotation = RotationUtil.lookAtEntity(prevTarget);
			x = (scaleWidth / 2) + ((rotation[0]) - mc.player.rotationYaw) * 1.1f;
			y = (scaleHeight / 2) + (rotation[1] - mc.player.rotationPitch);
		}

		double centerX = x + (width / 2);
		double centerY = y + (height / 2);

		GlStateManager.pushMatrix();

		GlStateManager.translate(centerX, centerY, 0);
		GlStateManager.scale(scaleAnim, scaleAnim, 1);
		GlStateManager.translate(-centerX, -centerY, 0);

		float currentPos = MathUtil.clamp(
				((prevTarget.getHealth() + prevTarget.getAbsorptionAmount()) - 0) / (prevTarget.getMaxHealth() - 0), 0,
				1);

		stranimation = stranimation + (prevTarget.getHealth() * 100 / 100 - stranimation) / 2.0f;
		healthPos = RenderUtil.scrollAnimate(healthPos, currentPos, .5f);

		switch (mode.getCurrentMode()) {
		case "Heaven":
			drawHeaven(x, y, healthPos, alphaFade);
			break;
		case "Kirka":
			drawKirka(x, y, healthPos, alphaFade);
			break;
		case "Heaven2":
			drawDefault(x, y, healthPos, alphaFade);
			break;
		}

		drag.setWidth(this.width);
		drag.setHeight(this.height);

		GlStateManager.popMatrix();
	}

	private void drawKirka(float x, float y, float healthPos, float fade) {
		width = 132;
		height = prevTarget instanceof EntityPlayer ? 46 : 38;
		Drawable.horizontalGradient(x, y, x + width, y + 2,
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 0).getRGB(), fade),
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 190, 0), fade).getRGB());
		Drawable.drawRectWH(x, y + 2, width, height - 2, ColorUtil.applyOpacity(0xFF1E1E1E, fade));

		if (animation.getDirection() == Direction.FORWARDS)
			RenderUtil.drawFace(prevTarget, (int) x + 5, (int) y + 5, (int) height - 18, (int) height - 18);

		Drawable.drawBlurredShadow((int) (x + 10 + (height - 14)), (int) y + 5,
				(int) IFont.WEB_SETTINGS.getStringWidth(prevTarget.getName()) + 1,
				(int) IFont.WEB_SETTINGS.getFontHeight() + 1, 8,
				ColorUtil.applyOpacity(new Color(255, 250, 250, 60), fade));
		IFont.WEB_SETTINGS.drawString(prevTarget.getName(), x + 10 + (height - 14), y + 5,
				ColorUtil.applyOpacity(-1, fade));

		RoundedShader.drawGradientHorizontal(x + 11 + (height - 14), y + 16, (width - 48) * healthPos, 10, 1,
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 0), fade),
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 190, 0), fade));

		IFont.WEB_SETTINGS.drawHVCenteredString(
				(int) prevTarget.getHealth() + "/" + (int) prevTarget.getMaxHealth() + " HP",
				((x + 11 + (height - 14)) + (width - 48) / 2), y + 16, 10, ColorUtil.applyOpacity(-1, fade));

		final String winStatus = String.format("%s",
				prevTarget.getHealth() == Wrapper.getPlayer().getHealth() ? ""
						: prevTarget.getHealth() + prevTarget.getAbsorptionAmount() > Wrapper.getPlayer()
								.getHealth()
										? "Loosing"
										: prevTarget.getHealth() + prevTarget.getAbsorptionAmount() >= 10
												&& Wrapper.getPlayer().getHealth() <= 5
														? "You Suck"
														: prevTarget.getHealth() + prevTarget.getAbsorptionAmount() <= 5
																&& Wrapper.getPlayer().getHealth() >= 10 ? "EZZ!"
																		: "Winning");

		IFont.WEB_SETTINGS_MINI.drawCenteredString(winStatus, x + 5 + (height - 18) / 2, y + height - 11,
				ColorUtil.applyOpacity(-1, fade));

		if (animation.finished(Direction.FORWARDS) && prevTarget instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) prevTarget;

			RoundedShader.drawGradientCornerLR(x + width - 15, y + height - 15, 12, 12, 6,
					ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 0), fade),
					ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 190, 0), fade));
			RoundedShader.drawRound(x + width - 14, y + height - 14, 10, 10, 5,
					new Color(ColorUtil.applyOpacity(0xFF1E1E1E, fade)));

			IFont.WEB_LIST.drawString(String.valueOf((int) mc.player.getDistanceToEntity(player)),
					(int) (x + width - 10), (int) (y + height - 14), ColorUtil.applyOpacity(-1, fade));

			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 40, y + height - 16, 0);
			GlStateManager.scale(0.8f, 0.8f, 1f);
			GlStateManager.translate(-(x + 37), -(y + height - 16), 0);
			int offsetX = 0;
			for (int i = 3; i >= 0; i--) {
				ItemStack itemStack = mc.player.inventory.armorInventory.get(i);

				RenderUtil.drawStack(itemStack, true, (int) (x + 35) + offsetX + 2, (int) (y + height - 16));
				offsetX += 15;
			}

			RenderUtil.drawStack(player.getHeldItemMainhand(), true, (int) (x + 35) + offsetX + 2,
					(int) (y + height - 16));
			offsetX += 15;

			RenderUtil.drawStack(player.getHeldItemOffhand(), true, (int) (x + 35) + offsetX + 2,
					(int) (y + height - 16));
			offsetX += 15;
			GlStateManager.popMatrix();
		}
	}

	private void drawHeaven(float x, float y, float healthPos, float fade) {
		width = 124;
		height = 36;
		float radius = 8;
		if (glowing.isToggle())
			Drawable.drawBlurredShadow((int) x, (int) y, (int) width, (int) height, 20,
					ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 0), fade));
		RoundedShader.drawGradientHorizontal(x, y, width, height, radius, ColorUtil.skyRainbow((int) 5, 0),
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 190), fade));
		RoundedShader.drawGradientCornerRL(x + 1, y + 1, width - 2, height - 2, radius,
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 250), fade),
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 0), fade));

		RenderUtil.drawFace(prevTarget, (int) x + 8, (int) y + 8, (int) height - 21, (int) height - 21);

		IFont.POPPIN_LIST.drawStringWithShadow(prevTarget.getName(), x + 10 + (height - 21), y + 5,
				ColorUtil.applyOpacity(-1, fade));
		IFont.POPPIN_LIST.drawStringWithShadow("Health: " + (int) prevTarget.getHealth(), x + 10 + (height - 21),
				y + 13, ColorUtil.applyOpacity(-1, fade));

		Drawable.drawBlurredShadow((int) x + 8, (int) (y + height - 8), (int) ((width - 16) * healthPos), (int) 4, 6,
				ColorUtil.applyOpacity(new Color(10, 10, 10, 190), fade));
		RoundedShader.drawGradientHorizontal(x + 8, y + height - 10, (width - 16) * healthPos, 4f, 1f,
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 0), fade),
				ColorUtil.applyOpacity(ColorUtil.skyRainbow((int) 5, 190), fade));
	}

	private void drawDefault(float x, float y, float healthPos, float fade) {
		width = 130;
		height = 34;
		RoundedShader.drawRound(x, y, width, height, 5, true, ColorUtil.applyOpacity(new Color(11, 12, 12, 185), fade));

		float hurtPercent = (prevTarget.hurtTime != 0f ? prevTarget.hurtTime - mc.getRenderPartialTicks() : 0f) / 10;
		float scale = hurtPercent == 0f ? 1f
				: hurtPercent < 0.5f ? 1 - (0.2f * hurtPercent * 2) : 0.8f + (0.2f * (hurtPercent - 0.5f) * 2);
		int size = 16;
		float centerX = x + 6 + (height - 16) / 2;
		float centerY = y + 8 + (height - 16) / 2;

		GL11.glPushMatrix();

		GL11.glTranslatef(centerX, centerY, 0);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(-centerX, -centerY, 0);

		GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, fade);

		//RenderUtil.drawCircledFace(prevTarget.getName(), (int) (x + 6), (int) (y + 8), (int) (height - 16),
		//		(int) (height - 16));

		GL11.glPopMatrix();

		IFont.WEB_SMALL.drawString(prevTarget.getName(), x + 12 + (height - 16), y + 8,
				ColorUtil.applyOpacity(-1, fade));

		if (stranimation > 1)
			IFont.WEB_SETTINGS.drawString(String.valueOf((int) stranimation),
					x + 28 + (width - (23 + (height - 16))) * healthPos, y + height - 18,
					ColorUtil.applyOpacity(-1, fade));

		RoundedShader.drawRound(x + 13 + (height - 16), y + height - 9, (width - (23 + (height - 16))) * healthPos,
				1.5f, 1f, true, ColorUtil.applyOpacity(color.getColor(), fade));
	}

	private void stickyPush(EntityLivingBase entity) {
		if (!stickyMode.isToggle())
			return;

		RenderManager renderManager = mc.getRenderManager();
		double partialTicks = Wrapper.MC.getRenderPartialTicks();
		float x = (float) (entity.posX - mc.getRenderManager().getRenderPosX());
		float y = (float) (entity.posY - mc.getRenderManager().getRenderPosY());
		float z = (float) (entity.posZ - mc.getRenderManager().getRenderPosZ());
		GlStateManager.pushMatrix();
		GlStateManager.enablePolygonOffset();
		GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
		GL11.glTranslated(
				entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
						- renderManager.getRenderPosX(),
				entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - renderManager.getRenderPosY()
						+ entity.height + (0.2),
				entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
						- renderManager.getRenderPosZ());

		GL11.glRotatef(-renderManager.playerViewY, 0F, 1F, 0F);
		GL11.glRotatef((mc.getRenderManager().options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1F,
				0F, 0F);

		GlStateManager.scale(0.01f, 0.01f, 0.01f);

		GL11.glDepthMask(false);
	}

	private void stickyPop() {
		if (!stickyMode.isToggle())
			return;

		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glDepthMask(true);
		GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
		GlStateManager.disablePolygonOffset();
		GlStateManager.resetColor();
		GlStateManager.popMatrix();
	}

	private float getYaw(EntityLivingBase entity) {
		double x = entity.posX - mc.player.posX;
		double z = entity.posZ - mc.player.posZ;
		float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(x, z)) - 90.0f);
		return yaw;
	}

	private float getPitch(EntityLivingBase entityIn) {
		double x = entityIn.posX - mc.player.posX;
		double z = entityIn.posZ - mc.player.posZ;
		double diffY;
		double d3 = (double) MathHelper.sqrt(x * x + z * z);
		if (entityIn instanceof EntityLivingBase) {
			diffY = entityIn.posY + entityIn.getEyeHeight()
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		} else {
			diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2
					- (Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight());
		}
		float pitch = MathHelper.wrapDegrees((float) (-Math.toDegrees(Math.atan2(diffY, d3))));
		return pitch;
	}

}
