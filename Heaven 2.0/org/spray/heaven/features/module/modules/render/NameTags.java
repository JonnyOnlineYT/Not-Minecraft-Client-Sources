package org.spray.heaven.features.module.modules.render;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.events.WorldRenderEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.misc.NameProtect;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

@ModuleInfo(name = "NameTags", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class NameTags extends Module {

	private final Setting throughWalls = register(new Setting("Through Walls", true));
	private final Setting players = register(new Setting("Players", true));
	private final Setting friends = register(new Setting("Friends", false).setVisible(players::isToggle));
	private final Setting invisibles = register(new Setting("Invisible", false));
	private final Setting mobs = register(new Setting("Mobs", false));
	private final Setting animals = register(new Setting("Animals", true));

	private final Setting self = register(new Setting("Self", true));

	private final Setting potions = register(new Setting("Potion Effects", true));
	private final Setting potionsName = register(new Setting("Potion Effects Name", true));

	private final Setting items = register(new Setting("Items", true));
	private final Setting itemEnchats = register(new Setting("Items Enchantments", true).setVisible(items::isToggle));

	private final Setting scale = register(new Setting("Scale", 1, 1, 4));

	@EventTarget
	public void onWorldRender(WorldRenderEvent event) {
		for (Entity entity : mc.world.loadedEntityList) {
			
			if (!throughWalls.isToggle() && !mc.player.canEntityBeSeen(entity))
				continue;

			if (EntityUtil.isValid(entity, players.isToggle(), friends.isToggle(), self.isToggle(),
					invisibles.isToggle(), mobs.isToggle(), animals.isToggle())) {

				EntityLivingBase livingEntity = (EntityLivingBase) entity;

				if (livingEntity == null)
					return;

				String tag = livingEntity.getDisplayName().getUnformattedText();
	            NameProtect nameProtect = Wrapper.getModule().get("NameProtect");
	            if (nameProtect.isEnabled() && nameProtect.scoreboard.isToggle() && nameProtect.isValid(tag)) {
	            	tag = nameProtect.PROTECT;
	            }

				ChatFormatting chatFormatting = ChatFormatting.GREEN;
				int health = (int) livingEntity.getHealth();
				if (health <= livingEntity.getMaxHealth() * 0.25D) {
					chatFormatting = ChatFormatting.RED;
				} else if (health <= livingEntity.getMaxHealth() * 0.5D) {
					chatFormatting = ChatFormatting.GOLD;
				} else if (health <= livingEntity.getMaxHealth() * 0.75D) {
					chatFormatting = ChatFormatting.YELLOW;
				}
				tag = tag + " " + chatFormatting + (int) health;

				renderNameTag(livingEntity, tag, false);
			}

			if (items.isToggle() && entity instanceof EntityItem) {
				EntityItem item = (EntityItem) entity;
				renderNameTag(item, !item.getCustomNameTag().isEmpty() ? item.getCustomNameTag()
						: item.getEntityItem().getDisplayName(), false);
			}
		}
	}

	private void renderNameTag(Entity entity, String tag, boolean screen) {
		GL11.glPushMatrix();

		RenderManager renderManager = Wrapper.MC.getRenderManager();
		double partialTicks = Wrapper.MC.getRenderPartialTicks();
		float distance = mc.player.getDistanceToEntity(entity) / 4F;

		if (distance < 1F)
			distance = 1F;

        CustomModels customModel = Wrapper.getModule().get("CustomModels");
        boolean cm = customModel.isEnabled() && customModel.isValid(entity);
		boolean chinahat = Wrapper.getModule().get("chinahat").isEnabled() && entity == mc.player;
		float scale = (float) ((distance / 150F) * this.scale.getValue());
		float diff = 8;
		float scaleFactor = (float) ((distance / diff) * this.scale.getValue());
		scale = (float) MathUtil.clamp(scale, 0.012, 0.1);
		scaleFactor = (float) MathUtil.clamp(scaleFactor, 0.25, 3);
		double addY = (cm && customModel.mode.getCurrentMode().equals("Amogus") ? 0.14 : 
			cm && customModel.mode.getCurrentMode().equals("Demon") ? (0.3) + scaleFactor : 0.2 + scaleFactor);

		FontRenderer font = IFont.WEB_MIDDLE;

		GL11.glTranslated(
				entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
						- renderManager.getRenderPosX(),
				entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - renderManager.getRenderPosY()
						+ entity.height + (chinahat ? addY + 0.2 : addY),
				entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
						- renderManager.getRenderPosZ());

		boolean thirdPersonView = Wrapper.MC.getRenderManager().options.thirdPersonView == 2;

		GL11.glRotatef(screen ? 180 : -renderManager.playerViewY, 0F, 1F, 0F);
		GL11.glRotatef(screen ? 0 : (thirdPersonView ? -1 : 1) * renderManager.playerViewX, 1F, 0F, 0F);

		double width = font.getStringWidth(tag) / 2;

		GlStateManager.disableDepth();
		GL11.glScalef(-scale, -scale, scale);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawDefault(entity, tag, width);

		GlStateManager.enableDepth();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	private void drawDefault(Entity entity, String tag, double width) {
		FontRenderer font = IFont.WEB_MIDDLE;
		RoundedShader.drawRound((float) (-width - 3), -2, (float) (width + width + 6), font.getFontHeight() + 2, 3,
				new Color(21, 20, 22, 195));

		font.drawString(tag, (int) (1f + -width), 0, -1);

		float armorHeight = 22;
		List<ItemStack> items = Lists.newArrayList();
		if (this.items.isToggle() && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			for (int i = 0; i < 4; i++) {

				if (player.inventory.armorItemInSlot(i).isEmpty())
					continue;

				renderItem(player.inventory.armorItemInSlot(i), -18 + i * 18, -20);
				items.add(player.inventory.armorItemInSlot(i));
			}

			if (!player.getHeldItemMainhand().isEmpty())
			renderItem(player.getHeldItemMainhand(), -54, -20);
			
			if (!player.getHeldItemOffhand().isEmpty())
			renderItem(player.getHeldItemOffhand(), -36, -20);

			if (!player.getHeldItemMainhand().isEmpty())
				items.add(player.getHeldItemMainhand());

			if (!player.getHeldItemOffhand().isEmpty())
				items.add(player.getHeldItemOffhand());

			armorHeight = items.isEmpty() ? 22 : 38;
			if (itemEnchats.isToggle() && !items.isEmpty())
				armorHeight = 38 - (getBestHeight(items));
		}

		if (potions.isToggle() && entity instanceof EntityLivingBase) {
			EntityLivingBase livingBase = (EntityLivingBase) entity;
			Collection<PotionEffect> activeEffects = livingBase.getActivePotionEffects();

			float offsetY = -armorHeight;
			for (PotionEffect potionEffect : activeEffects) {
				Potion potion = potionEffect.getPotion();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));

				int xPos = -48;
				String potionName = "";

				if (potionsName.isToggle()) {
					potionName = I18n.translateToLocal(potion.getName());

					if (potionEffect.getAmplifier() == 1) {
						potionName = potionName + " II";
					} else if (potionEffect.getAmplifier() == 2) {
						potionName = potionName + " III";
					} else if (potionEffect.getAmplifier() == 3) {
						potionName = potionName + " IV";
					} else if (potionEffect.getAmplifier() == 4) {
						potionName = potionName + " V";
					} else if (potionEffect.getAmplifier() == 5) {
						potionName = potionName + " VI";
					} else if (potionEffect.getAmplifier() == 6) {
						potionName = potionName + " VII";
					} else if (potionEffect.getAmplifier() == 7) {
						potionName = potionName + " VIII";
					} else if (potionEffect.getAmplifier() == 8) {
						potionName = potionName + " IX";
					} else if (potionEffect.getAmplifier() == 9) {
						potionName = potionName + " X";
					} else if (potionEffect.getAmplifier() > 9) {
						potionName = potionName + " " + (potionEffect.getAmplifier() + 1);
					}
				}
				String effectDuration = Potion.getPotionDurationString(potionEffect, 1f);

				if (potion.hasStatusIcon()) {
					int potionStatusIcon = potion.getStatusIconIndex();
					Drawable.drawTexturedModalRect(xPos, (int) offsetY, potionStatusIcon % 8 * 18,
							166 + 32 + potionStatusIcon / 8 * 18, 18, 18);
				}
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				boolean minSeconds = (potionEffect.getDuration() / 60) < 4;
				mc.fontRendererObj.drawStringWithShadow("\247f" + potionName + "\247r",
						xPos + 23 + mc.fontRendererObj.getStringWidth(effectDuration), offsetY + 5, 0xffffff);
				mc.fontRendererObj.drawStringWithShadow(effectDuration + "\247r", xPos + 20, offsetY + 5,
						minSeconds ? 0xFFFE4656 : 0xffffff);

				offsetY -= 16;
			}
		}
	}

	private void renderItem(ItemStack stack, int x, int y) {
		RoundedShader.drawRound((float) (x), y, (float) (16f), 16, 3,
				new Color(21, 20, 22, 195));
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();
		mc.getRenderItem().zLevel = -147f;
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();

		if (itemEnchats.isToggle()) {
			renderEnchant(stack, x, y);
		}

		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
	}

	private void renderEnchant(ItemStack stack, int x, int y) {
		int offsetY = y - 20;
		GlStateManager.disableDepth();
		NBTTagList enchants = stack.getEnchantmentTagList();
		for (int i = 0; i < enchants.tagCount(); ++i) {
			short id = enchants.getCompoundTagAt(i).getShort("id");
			short level = enchants.getCompoundTagAt(i).getShort("lvl");
			Enchantment enchant = Enchantment.getEnchantmentByID(id);

			if (enchant == null)
				return;

			String enchantName = enchant.getTranslatedName(level).substring(0, 1).toLowerCase();
			enchantName = enchantName + level;
			
//			RoundedShader.drawRound(x + 4, offsetY, 8, 8, 1,
//					new Color(21, 20, 22, 195));
			IFont.WEB_MIDDLE.drawCenteredStringWithShadow(enchantName, x + 8, offsetY,
					level >= enchant.getMaxLevel() ? Colors.CLIENT_COLOR.getRGB() : -1, 0.94f);

			offsetY -= 14;
		}
	}

	private float getBestHeight(List<ItemStack> items) {
		float bestHeight = 0f, offsetY = 0f;
		for (ItemStack stack : items) {
			if (EnchantmentHelper.getEnchantments(stack).size() <= 0)
				continue;

			offsetY = (EnchantmentHelper.getEnchantments(stack).size() * 15) - 1;

			if (offsetY > bestHeight)
				bestHeight = offsetY;
		}
		return -bestHeight;
	}

}
