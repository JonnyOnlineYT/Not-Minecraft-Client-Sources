package org.spray.heaven.features.module.modules.display;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.font.IFont;
import org.spray.heaven.util.render.Drawable;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

@ModuleInfo(name = "PotionEffects", category = Category.DISPLAY, visible = true, key = Keyboard.KEY_NONE)
public class PotionEffects extends Module {

	private Setting effectName = register(new Setting("Effects Name", true));
	private Map<PotionEffect, Integer> potionMaxDurationMap = new HashMap<PotionEffect, Integer>();

	@Override
	public void onRender(int scaledWidth, int scaledHeight, float delta) {
		Collection<?> activeEffects = mc.player.getActivePotionEffects();

		if (!activeEffects.isEmpty()) {
			int yOffset = 26;

			int yBase = getY(activeEffects.size(), scaledHeight, yOffset);

			for (Iterator<?> iteratorPotionEffect = activeEffects.iterator(); iteratorPotionEffect
					.hasNext(); yBase += yOffset) {
				PotionEffect potionEffect = (PotionEffect) iteratorPotionEffect.next();

				if (!potionMaxDurationMap.containsKey(potionEffect)
						|| potionMaxDurationMap.get(potionEffect) < potionEffect.getDuration())
					potionMaxDurationMap.put(potionEffect, potionEffect.getDuration());

				Potion potion = potionEffect.getPotion();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
				int xBase = 2;
				String potionName = "";

				if (effectName.isToggle()) {
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

//				Drawable.drawBlurredShadow(xBase, yBase - 4,
//						(int) IFont.WEB_LIST.getStringWidth(potionName + " " + effectDuration), yOffset, 4,
//						new Color(14, 14, 14, 190));
//				Drawable.drawRectWH(xBase, yBase - 4,
//						(int) IFont.WEB_LIST.getStringWidth(potionName + " " + effectDuration), yOffset,
//						new Color(14, 14, 14, 190).getRGB());
//				
				if (potion.hasStatusIcon()) {
					int potionStatusIcon = potion.getStatusIconIndex();
					Drawable.drawTexturedModalRect(xBase, yBase, potionStatusIcon % 8 * 18,
							166 + 32 + potionStatusIcon / 8 * 18, 18, 18, -150f);
				}
				IFont.WEB_LIST.drawStringWithShadow("\247f" + potionName + "\247r", xBase + 4 + 18, yBase, 0xffffff);

				if (shouldRender(potionEffect, potionEffect.getDuration(), 10))
					IFont.WEB_LIST.drawStringWithShadow("\247f" + effectDuration + "\247r", xBase + 4 + 18,
							yBase + (effectName.isToggle() ? 10 : 5), 0xffffff);
			}
		}

		List<PotionEffect> toRemove = new LinkedList<PotionEffect>();

		for (PotionEffect pe : potionMaxDurationMap.keySet())
			if (!activeEffects.contains(pe))
				toRemove.add(pe);

		for (PotionEffect pe : toRemove)
			potionMaxDurationMap.remove(pe);
	}

	private boolean shouldRender(PotionEffect pe, int ticksLeft, int thresholdSeconds) {
		if (potionMaxDurationMap.get(pe) > 400)
			if (ticksLeft / 20 <= thresholdSeconds)
				return ticksLeft % 20 < 10;

		return true;
	}

	private int getY(int rowCount, int scaledHeight, int height) {
		return (scaledHeight / 2) - ((rowCount * height) / 2);

	}

}
