package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventClickMouse;
import ru.wendoxd.events.impl.player.EventTick;
import ru.wendoxd.events.impl.render.EventBlur;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.combat.Aura;
import ru.wendoxd.modules.impl.visuals.HUD.SkinData;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.ColorPicker;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.combat.RaycastHelper;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.AstolfoAnimation;
import ru.wendoxd.utils.visual.animation.BetterAnimation;
import ru.wendoxd.utils.visual.animation.BetterDynamicAnimation;
import ru.wendoxd.utils.visual.hud.DraggableElement;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

import javax.vecmath.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TargetHUD extends Module {
	public static ResourceLocation combat = new ResourceLocation("wexside/combat.png");
	public static Frame frame = new Frame("TargetHUD");
	public static CheckBox enabled = new CheckBox("TargetHUD").markArrayList("TargetHUD");
	public static SelectBox sb = new SelectBox("Color", new String[] { "Default", "Fade", "Astolfo" },
			() -> enabled.isEnabled(true));
	public static ColorPicker firstColor = new ColorPicker("First Color", () -> sb.get() == 0 || sb.get() == 1);
	public static ColorPicker secondColor = new ColorPicker("Second Color", () -> sb.get() == 1);
	public static SelectBox type = new SelectBox("Type", new String[] { "Shadow", "Flat" },
			() -> enabled.isEnabled(true));
	public static CheckBox raycast = new CheckBox("Raycast", () -> enabled.isEnabled(true));
	public static CheckBox blur = new CheckBox("Blur", () -> enabled.isEnabled(true));
	public BetterAnimation animation = new BetterAnimation();
	public static BetterDynamicAnimation health = new BetterDynamicAnimation();
	public static DraggableElement position = new DraggableElement();
	public static AstolfoAnimation astolfo = new AstolfoAnimation();
	private static final double ASTOLFO_STAGE = .6 / 1;

	@Override
	protected void initSettings() {
		EntityRenderer.class.getClass();
		frame.register(enabled, sb, firstColor, secondColor, type, blur, raycast);
		enabled.getColor().setRGB(255, 140, 80);
		position.setX(6);
		position.setY(24);
		MenuAPI.hud.register(frame);
		firstColor.getColor().setRGB(148, 148, 255);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D && enabled.isEnabled(false)) {
			if (blur.isEnabled(false)) {
				EntityRenderer.blurEnabled = true;
				return;
			}
			drawTargetHUD(EntityRenderer.res);
		}
		if (event instanceof EventBlur && enabled.isEnabled(false) && blur.isEnabled(false)) {
			drawTargetHUD(EntityRenderer.res);
		}
		if (event instanceof EventTick) {
			health.update();
			astolfo.update();
			animation.update(getTarget() != null);
		}
		if (event instanceof EventClickMouse) {
			position.mouseClicked();
		}
	}

	public void drawTargetHUD(ScaledResolution res) {
		position.tick(res);
		if (animation.getAnimationd() > 0) {
			boolean nameprotect = StreamerMode.streamermode.isEnabled(false)
					&& (StreamerMode.type.get(1) || StreamerMode.type.get(0));
			int color_1 = firstColor.getColor().build();
			int color_2 = firstColor.getColor().build();
			if (sb.get() == 1) {
				color_2 = secondColor.getColor().build();
			}
			if (sb.get() == 2) {
				color_1 = astolfo.getColor(0);
				color_2 = astolfo.getColor(ASTOLFO_STAGE * animation.getAnimationd());
			}
			if (type.get() == 0) {
				position.update(100, 38);
				GL11.glPushMatrix();
				GL11.glTranslated(position.getX(), position.getY(), 0);
				RenderUtils.sizeAnimation(100, 38, animation.getAnimationd());
				RectHelper.renderShadow(0, 0, 100, 38, RenderUtils.rgba(25, 25, 25, 180 * animation.getAnimationd()),
						5);
				GL11.glPopMatrix();
				EntityLivingBase target = getTarget();
				if (target != null) {
					NetworkPlayerInfo npi = null;
					for (NetworkPlayerInfo i : mc.player.connection.getPlayerInfoMap()) {
						if (mc.world.getPlayerEntityByUUID(i.getGameProfile().getId()) == target) {
							npi = i;
							break;
						}
					}
					if (new ArrayList<>(Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()))
							.stream().filter(w -> !(w.getItem() instanceof ItemAir)).count() == 0) {
						GL11.glPushMatrix();
						GL11.glTranslated(position.getX(), position.getY(), 0);
						RenderUtils.sizeAnimation(100, 38, animation.getAnimationd());
						Fonts.MNTSB_13.drawString(String.format("%.1fm", mc.player.getDistanceToEntity(target)), 27,
								15.5f, RenderUtils.rgba(200, 200, 200, Math.min(255 * animation.getAnimationf(), 255)));
						GL11.glPopMatrix();
					}
					if (animation.getAnimationd() == 1) {
						Fonts.MNTSB_13.drawSubstring(nameprotect ? "Protected" : target.getName(),
								position.getX() + 27, position.getY() + 8,
								RenderUtils.rgba(200, 200, 200, Math.min(255 * animation.getAnimationf(), 255)), 55);
					} else {
						GL11.glPushMatrix();
						GL11.glTranslated(position.getX(), position.getY(), 0);
						RenderUtils.sizeAnimation(100, 38, animation.getAnimationd());
						String s = nameprotect ? "Protected" : target.getName();
						Fonts.MNTSB_13.drawString(s.substring(0, Math.min(s.length(), 15)), 27, 8,
								RenderUtils.rgba(200, 200, 200, Math.min(255 * animation.getAnimationf(), 255)));
						GL11.glPopMatrix();
					}
					int buildColor = enabled.getColor().build();
					GL11.glPushMatrix();
					GL11.glTranslated(position.getX(), position.getY(), 0);
					RenderUtils.sizeAnimation(100, 38, animation.getAnimationd());
					List<ItemStack> stacks = new ArrayList<>(
							Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()));
					double yOffset = 0;
					for (ItemStack stack : stacks) {
						GL11.glPushMatrix();
						GL11.glTranslated(25 + yOffset, 11, 0);
						String s = null;
						if (!(stack.getItem() instanceof ItemAir))
							Fonts.MNTSB_12.drawString(s = ((int) (stack.stackSize * animation.getAnimationd()) + "x"),
									11f, 5.5f, RenderUtils.rgba(200, 200, 200, 255));
						GL11.glScaled(0.5, 0.5, 1);
						ESP.drawItemStack(stack, 4.4f, 3.6f, null, true);
						if (s != null) {
							yOffset += 11 + Fonts.MNTSB_12.getStringWidth(s);
						}
						GL11.glPopMatrix();
					}
					float hp = target.getHealth() / Math.max(target.getHealth(), target.getMaxHealth())
							* Math.min(animation.getAnimationf(), 1);
					health.setValue(hp);
					hp = (float) health.getAnimationD();
					float start = 7;
					float startY = 22;
					RenderUtils.drawRect(start, startY, 94, 33, RenderUtils.rgba(20, 20, 20, 85));
					drawGradientSideways(start + 1, 23, start + (93 - start) * hp, 32, color_1, color_2);
					Fonts.MNTSB_12.drawCenteredString(
							String.format("%.1f", target.getHealth() * animation.getAnimationd()), 50, 26.5f,
							RenderUtils.rgba(255, 255, 255, Math.min(255 * animation.getAnimationf(), 255)));
					SkinData data = HUD.getSkinData(target);
					if (data != null) {
						GlStateManager.bindTexture(data.getTextureID());
					} else {
						mc.getTextureManager().bindTexture(combat);
					}
					ShaderShell.CIRCLE_TEXTURE_SHADER.attach();
					ShaderShell.CIRCLE_TEXTURE_SHADER.set1F("radius", 0.5F);
					ShaderShell.CIRCLE_TEXTURE_SHADER.set1F("glow", 0.08F);
					GL11.glScaled(0.06, 0.06, 1);
					Gui.drawTexturedModalRect2(150, 85, 0, 0, 256, 256);
					ShaderShell.CIRCLE_TEXTURE_SHADER.detach();
					GL11.glPopMatrix();
				}
			} else if (type.get() == 1) {
				position.update(105, 38);
				GL11.glPushMatrix();
				GL11.glTranslated(position.getX(), position.getY(), 0);
				RenderUtils.sizeAnimation(105, 38, animation.getAnimationd());
				RenderUtils.drawRect(0, 0, 105, 38, RenderUtils.rgba(25, 25, 25, 150 * animation.getAnimationd()));
				GL11.glPopMatrix();
				EntityLivingBase target = getTarget();
				if (target != null) {
					NetworkPlayerInfo npi = null;
					for (NetworkPlayerInfo i : mc.player.connection.getPlayerInfoMap()) {
						if (mc.world.getPlayerEntityByUUID(i.getGameProfile().getId()) == target) {
							npi = i;
							break;
						}
					}
					if (new ArrayList<>(Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()))
							.stream().filter(w -> !(w.getItem() instanceof ItemAir)).count() == 0) {
						GL11.glPushMatrix();
						GL11.glTranslated(position.getX(), position.getY(), 0);
						RenderUtils.sizeAnimation(105, 38, animation.getAnimationd());
						Fonts.MNTSB_13.drawString(String.format("%.1fm", mc.player.getDistanceToEntity(target)), 41,
								15.5f, RenderUtils.rgba(200, 200, 200, Math.min(255 * animation.getAnimationf(), 255)));
						GL11.glPopMatrix();
					}
					if (animation.getAnimationd() == 1) {
						String s = nameprotect ? "Protected" : target.getName();
						Fonts.MNTSB_13.drawSubstring(s, position.getX() + 41, position.getY() + 8,
								RenderUtils.rgba(200, 200, 200, Math.min(255 * animation.getAnimationf(), 255)), 49);
					} else {
						GL11.glPushMatrix();
						GL11.glTranslated(position.getX(), position.getY(), 0);
						RenderUtils.sizeAnimation(105, 38, animation.getAnimationd());
						String s = nameprotect ? "Protected" : target.getName();
						Fonts.MNTSB_13.drawString(s, 41, 8,
								RenderUtils.rgba(200, 200, 200, Math.min(255 * animation.getAnimationf(), 255)));
						GL11.glPopMatrix();
					}
					int buildColor = enabled.getColor().build();
					GL11.glPushMatrix();
					GL11.glTranslated(position.getX(), position.getY(), 0);
					RenderUtils.sizeAnimation(105, 38, animation.getAnimationd());
					List<ItemStack> stacks = new ArrayList<>(
							Arrays.asList(target.getHeldItemMainhand(), target.getHeldItemOffhand()));
					double yOffset = 0;
					float hp = target.getHealth() / Math.max(target.getHealth(), target.getMaxHealth())
							* Math.min(animation.getAnimationf(), 1);
					health.setValue(hp);
					hp = (float) health.getAnimationD();
					for (ItemStack stack : stacks) {
						GL11.glPushMatrix();
						GL11.glTranslated(35 + yOffset, 11, 0);
						String s = null;
						if (!(stack.getItem() instanceof ItemAir))
							Fonts.MNTSB_12.drawString(s = ((int) (stack.stackSize * animation.getAnimationd()) + "x"),
									16f, 5.5f, RenderUtils.rgba(200, 200, 200, 255));
						GL11.glScaled(0.5, 0.5, 1);
						ESP.drawItemStack(stack, 14.4f, 3.6f, null, true);
						if (s != null) {
							yOffset += 11 + Fonts.MNTSB_12.getStringWidth(s);
						}
						GL11.glPopMatrix();
					}
					RenderUtils.drawRect(40, 22, 101, 33, RenderUtils.rgba(20, 20, 20, 85));
					drawGradientSideways(41, 23, 41 + (101 - 42) * hp, 32, color_1, color_2);
					Fonts.MNTSB_12.drawCenteredString(
							String.format("%.1f", target.getHealth() * animation.getAnimationd()), 70, 26.5f,
							RenderUtils.rgba(255, 255, 255, Math.min(255 * animation.getAnimationf(), 255)));
					mc.getTextureManager().bindTexture(npi == null ? combat : npi.getLocationSkin());
					GL11.glScaled(1, 1, 1);
					float per = target.hurtTime / 10f;
					GL11.glColor4f(1f, 1f - per * 0.3f, 1 - per * 0.3f, Math.min(animation.getAnimationf(), 1));
					int v1 = npi == null ? 0 : 8;
					int v2 = npi == null ? 64 : 8;
					Gui.drawScaledCustomSizeModalRect(0, 0, v1, v1, v2, v2, 38, 38, 64, 64);
					GL11.glPopMatrix();
				}
			}
		}
	}

	public static void drawGradientSideways(final double left, final double top, final double right,
			final double bottom, final int col1, final int col2) {
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
		final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
		final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
		final float f8 = (col2 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glVertex2d(left, top);
		GL11.glVertex2d(left, bottom);
		GL11.glColor4f(f6, f7, f8, f5);
		GL11.glVertex2d(right, bottom);
		GL11.glVertex2d(right, top);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
	}

	public static void drawGradient(final double x, final double y, final double x2, final double y2, final int col1,
			final int col2) {
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
		final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
		final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
		final float f8 = (col2 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glShadeModel(7425);
		GL11.glPushMatrix();
		GL11.glBegin(7);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glColor4f(f6, f7, f8, f5);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glShadeModel(7424);
		GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
	}

	public static EntityLivingBase getTarget() {
		if (Aura.target != null) {
			return Aura.target;
		}
		if (raycast.isEnabled(false)) {
			EntityLivingBase base = RaycastHelper
					.getPointedEntity(new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch), 100, 1f, false);
			if (base != null) {
				return base;
			}
		}
		if (mc.currentScreen instanceof GuiChat) {
			return mc.player;
		}
		return null;
	}

	protected void drawItemStack(ItemStack p_191962_1_, int p_191962_2_, int p_191962_3_, IBakedModel p_191962_4_) {
		GlStateManager.pushMatrix();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		renderItem(p_191962_1_, p_191962_4_);
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}

	public void renderItem(ItemStack stack, IBakedModel model) {
		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);

			if (model.isBuiltInRenderer()) {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableRescaleNormal();
				TileEntityItemStackRenderer.instance.renderByItem(stack);
			} else {
				mc.getRenderItem().func_191961_a(model, stack);
			}

			GlStateManager.popMatrix();
		}
	}
}
