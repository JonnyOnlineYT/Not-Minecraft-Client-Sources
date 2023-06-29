package ru.wendoxd.modules.impl.visuals;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventClickMouse;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.hud.DraggableElement;

public class HUD extends Module {
	public static MultiSelectBox selected = new MultiSelectBox("HUD",
			new String[] { "Coords", "Potions", "Totems", "Armor", "ShulkerView", "Inventory" });
	public static HashMap<String, SkinData> cache = new HashMap<String, HUD.SkinData>();
	public static DraggableElement de = new DraggableElement();

	@Override
	protected void initSettings() {
		visuals_hud.register(selected);
		de.setX(6);
		de.setY(132);
		de.update(169, 74);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventClickMouse) {
			de.mouseClicked();
		}
		if (event instanceof EventRender2D) {
			final List<String> lines = new ArrayList();
			if (selected.get(1)) {
				ScaledResolution res = ((EventRender2D) event).getScaledResolution();
				ArrayList<PotionEffect> effects = new ArrayList();
				for (PotionEffect potionEffect : mc.player.getActivePotionEffects()) {
					if (potionEffect.getDuration() != 0
							&& !potionEffect.getPotion().getName().contains("effect.nightVision")) {
						effects.add(potionEffect);
					}
				}
				int i = 0;
				int j = res.getScaledHeight() / 2 - (effects.size() * 24) / 2;
				for (PotionEffect potionEffect : effects) {
					Potion potion = potionEffect.getPotion();
					String power = "";
					if (potionEffect.getAmplifier() == 0) {
						power = "I";
					} else if (potionEffect.getAmplifier() == 1) {
						power = "II";
					} else if (potionEffect.getAmplifier() == 2) {
						power = "III";
					} else if (potionEffect.getAmplifier() == 3) {
						power = "IV";
					} else if (potionEffect.getAmplifier() == 4) {
						power = "V";
					}
					String s = I18n.format(potionEffect.getPotion().getName()) + " " + power;
					String s2 = getDuration(potionEffect) + "";
					float maxWidth = Math.max(Fonts.MNTSB_13.getStringWidth(s), Fonts.MNTSB_12.getStringWidth(s2))
							+ 32;
					RectHelper.renderShadow(i + 2, j + 5, maxWidth - 4, 18.5f, RenderUtils.rgba(20, 20, 20, 170), 3);
					this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
					if (potion.hasStatusIcon()) {
						int i1 = potion.getStatusIconIndex();
						GuiScreen.drawTexturedModalRect2(i + 5, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
					}
					Fonts.MNTSB_13.drawString(s, i + 28, j + 11.5f, RenderUtils.rgba(205, 205, 205, 205));
					Fonts.MNTSB_12.drawString(s2, i + 28, j + 18.5f, RenderUtils.rgba(205, 205, 205, 205));
					j += 24;
				}
			}
			ScaledResolution res = ((EventRender2D) event).getScaledResolution();
			int offsetY = 0;
			if (selected.get(0)) {
				double mx = mc.player.motionX;
				double mz = mc.player.motionZ;
				double motion = Math.hypot(mx, mz) * 20;
				lines.add(String.format("Speed : %.1f", motion));
				lines.add("XYZ : " + (int) mc.player.posX + ", " + (int) mc.player.posY + ", " + (int) mc.player.posZ);
			}
			if (selected.get(5)) {
				de.tick(EntityRenderer.res);
				boolean prevBlend = GL11.glIsEnabled(GL11.GL_BLEND);
				GL11.glPushMatrix();
				GL11.glTranslated(de.getX(), de.getY(), 0);
				int offset = 16;
				RectHelper.renderShadow(0, 0, 169, 13, RenderUtils.rgba(20, 20, 20, 150), 4);
				RectHelper.renderShadow(0, offset, 169, 58, RenderUtils.rgba(20, 20, 20, 150), 4);
				for (int i = 9; i < 36; i++) {
					ItemStack stack = mc.player.inventory.mainInventory.get(i);
					int index = i - 9;
					renderHotbarItem((index % 9) * 18 + 5, 4 + offset + (index / 9) * 18, mc.player, stack);
				}
				RenderHelper.disableStandardItemLighting();
				Fonts.ICONS_20.drawString("f", 5, 5.5f, RenderUtils.rgba(200, 200, 200, 255));
				Fonts.MNTSB_12.drawString("Инвентарь", 16, 6, RenderUtils.rgba(200, 200, 200, 255));
				GL11.glPopMatrix();
			}
			if (mc.currentScreen == null)
				for (String line : lines) {
					Fonts.MNTSB_14.drawString(line, 0, res.getScaledHeight() - 5 - (offsetY * 6.5f),
							RenderUtils.rgba(225, 225, 225, 255));
					offsetY++;
				}
			if (selected.get(2)) {
				List<ItemStack> stacks = mc.player.inventory.mainInventory;
				ItemStack context = null;
				int totems = 0;
				for (ItemStack stack : stacks) {
					if (stack.getItem() instanceof ItemAir) {
						continue;
					}
					if (stack.getItem() == Items.field_190929_cY) {
						context = stack;
						totems++;
					}
				}
				if (mc.player.getHeldItemOffhand().getItem() == Items.field_190929_cY) {
					totems++;
					context = mc.player.getHeldItemOffhand();
				}
				if (totems != 0) {
					mc.getRenderItem().renderItemAndEffectIntoGUI(context, res.getScaledWidth() / 2 - 7,
							res.getScaledHeight() / 2 + 25);
					String text = Integer.toString(totems);
					float width = Fonts.MNTSB_12.getStringWidth(text);
					Fonts.MNTSB_13.drawString(text, res.getScaledWidth() / 2 - width / 2 + 0.3f,
							res.getScaledHeight() / 2 + 20, RenderUtils.rgba(220, 220, 220, 255));
				}
			}
			if (selected.get(3)) {
				GL11.glColor4f(1, 1, 1, 1);
				res = ((EventRender2D) event).getScaledResolution();
				final List<ItemStack> armor = new ArrayList<>();
				mc.entityRenderer.setupOverlayRendering();
				res = new ScaledResolution(mc);
				mc.player.getArmorInventoryList().forEach(armor::add);
				GL11.glPushMatrix();
				if (mc.player.getAir() < 300) {
					GL11.glTranslatef(0, -8, 0);
				}
				if (!armor.isEmpty())
					for (int i = 0; i < 4; i++) {
						ItemStack stack = armor.get(i);
						if (!(stack.getItem() instanceof ItemAir)) {
							String str = String.valueOf(stack.getMaxDamage() - stack.getItemDamage());
							mc.fontRendererObj.drawString(str,
									res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(str) / 2 + 18 + i * 22,
									res.getScaledHeight() - 62, RenderUtils.rgba(200, 200, 200, 255));
							RenderHelper.enableGUIStandardItemLighting();
							drawItemStack(stack, res.getScaledWidth() / 2 + 10 + i * 22, res.getScaledHeight() - 55);
							RenderHelper.disableStandardItemLighting();
						}
					}
				GL11.glPopMatrix();
				mc.entityRenderer.setupOverlayRendering(2);
			}
		}
	}

	public static synchronized HashMap<String, SkinData> getCache() {
		return HUD.cache;
	}

	public static SkinData getSkinData(Entity entity) {
		if (entity instanceof AbstractClientPlayer) {
			AbstractClientPlayer player = (AbstractClientPlayer) entity;
			SkinData data = getCache().get(player.getGameProfile().getName());
			if (data == null) {
				data = new SkinData();
				try {
					data.setBufferedImage(
							parseBufferedImage(mc.renderEngine.mapTextureObjects.get(player.getLocationSkin())));
					data.setLoaded();
				} catch (Exception e) {
					e.printStackTrace();
				}
				getCache().put(player.getGameProfile().getName(), data);
			}
			if (!data.isLoaded()) {
				return null;
			}
			return data;
		}
		return null;
	}

	public static BufferedImage parseBufferedImage(ITextureObject ito) throws Exception {
		if (ito instanceof ThreadDownloadImageData) {
			BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
			ThreadDownloadImageData t = (ThreadDownloadImageData) ito;
			return t.imageBuffer.cache();
		}
		if (ito instanceof SimpleTexture) {
			SimpleTexture st = (SimpleTexture) ito;
			return TextureUtil.readBufferedImage(
					mc.renderEngine.theResourceManager.getResource(st.textureLocation).getInputStream());
		}
		return null;
	}

	private void renderHotbarItem(int x, int y, EntityPlayer player, ItemStack stack) {
		if (!stack.func_190926_b()) {
			float f = (float) stack.func_190921_D() - mc.getRenderPartialTicks();

			if (f > 0.0F) {
				GlStateManager.pushMatrix();
				float f1 = 1.0F + f / 5.0F;
				GlStateManager.translate((float) (x + 8), (float) (y + 12), 0.0F);
				GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				GlStateManager.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
			}

			mc.getRenderItem().renderItemAndEffectIntoGUI(player, stack, x, y);

			if (f > 0.0F) {
				GlStateManager.popMatrix();
			}

			mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, stack, x, y);
		}
	}

	private void drawItemStack(ItemStack stack, double x, double y) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
		mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);
		GL11.glPopMatrix();
	}

	public static String getDuration(PotionEffect potionEffect) {
		if (potionEffect.getIsPotionDurationMax()) {
			return "**:**";
		} else {
			return StringUtils.ticksToElapsedTime(potionEffect.getDuration());
		}
	}

	public static class SkinData {
		private int texture;
		private boolean loaded;

		public void setBufferedImage(BufferedImage bi) {
			this.texture = TextureUtil.glGenTextures();
			TextureUtil.uploadTextureImageAllocate(this.texture, bi.getSubimage(8, 8, 8, 8), false, true);
		}

		public boolean isLoaded() {
			return this.loaded;
		}

		public void setLoaded() {
			this.loaded = true;
		}

		public int getTextureID() {
			return this.texture;
		}
	}
}
