package org.spray.infinity.features.module.visual;

import java.awt.Color;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.font.IFont;
import org.spray.infinity.utils.MathAssist;
import org.spray.infinity.utils.entity.EntityUtil;
import org.spray.infinity.utils.render.Drawable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(category = Category.VISUAL, desc = "Shows information about the enemy ", key = -2, name = "TargetInfo", visible = true)
public class TargetInfo extends Module {

	private Setting friends = new Setting(this, "Friends", false);
	private Setting invisibles = new Setting(this, "Invisibles", true);

	private Setting range = new Setting(this, "Range", 6D, 1D, 12.0D);

	private float fade;
	
	private Entity target;

	@Override
	public void onRender(MatrixStack matrices, float tickDelta, int w, int h) {
		target = EntityUtil.getTarget(range.getCurrentValueDouble(), 360, true, friends.isToggle(),
				invisibles.isToggle(), false, false, true);

		int wc = w / 2 - (142 / 2);
		int hc = h / 2 + 60;

		if (target == null) {
			fade = Math.max(0, fade - 0.4F);
			return;
		} else {
			fade = Math.min(1F, fade + 0.4F);
		}

		PlayerEntity player = null;

		if (target instanceof PlayerEntity) {
			player = (PlayerEntity) target;
		}
		
		RenderSystem.setShaderColor(1f, 1f, 1f, fade);
		double inc = ((player.getHealth() + player.getAbsorptionAmount()) / player.getMaxHealth()) * 75;
		double end = Math.min(inc, 75);
		String name = player.getName().getString();
		if (name.length() > 19)
			name = name.substring(0, 19) + "...";

		Drawable.drawRectWH(matrices, wc, hc, 145, 51, new Color(0, 0, 0, 220).getRGB());

		Drawable.drawRectWH(matrices, wc + 2, hc + 2, 33, 47, 0xFF151515);
		Drawable.drawRectWH(matrices, wc + 37, hc + 2, 105, 34, 0xFF151515);

		InventoryScreen.drawEntity(wc + 18, hc + 45, 20, 45, -target.getPitch(), (LivingEntity) target);

		IFont.legacy14.drawString(matrices, "Name: ", wc + 39, hc + 4, 0xFFFFFFFF);
		IFont.legacy14.drawString(matrices, name, wc + 63, hc + 4, 0xFF448CBF);

		IFont.legacy14.drawString(matrices, "Ping: ", wc + 39, hc + 6 + IFont.legacy14.getFontHeight(), 0xFFFFFFFF);
		IFont.legacy14.drawString(matrices, EntityUtil.getPing(player) + " ms", wc + 59,
				hc + 6 + IFont.legacy14.getFontHeight(), 0xFF448CBF);

		IFont.legacy14.drawString(matrices,
				String.valueOf(MathAssist.round(player.getHealth() + player.getAbsorptionAmount(), 1)), wc + 49 + 75,
				hc + 39, 0xFFFFFFFF);

		Drawable.drawHRoundedRect(matrices, wc + 43, hc + 40, 75, 6, 0xFF0A0A0A);
		Drawable.drawHRoundedRect(matrices, wc + 43, hc + 40, end, 6, 0xFF59DFD8);

		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}

	public void drawFace(MatrixStack matrices, float tickDelta, int x, int y, int w, int h) {

	}
}
