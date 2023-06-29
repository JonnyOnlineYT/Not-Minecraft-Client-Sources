package org.spray.infinity.features.module.visual;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.font.IFont;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.Drawable;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@ModuleInfo(category = Category.VISUAL, desc = "Shows broken armor", key = -2, name = "ArmorHUD", visible = true)
public class ArmorHUD extends Module {

	public Setting scale = new Setting(this, "Scale", 1.0D, 0.4, 2.0D);
	private Setting damage = new Setting(this, "Damage Count", true);

	@Override
	public void onRender(MatrixStack matrices, float tickDelta, int width, int height) {
		if (Helper.MC.options.debugEnabled)
			return;

		float scale = (float) this.scale.getCurrentValueDouble();
		
		HUD hud = ((HUD)Infinity.getModuleManager().get(HUD.class));

		matrices.push();
		matrices.scale(scale, scale, 1);
		
		int y = hud.isEnabled() && hud.watermark.isToggle() ? 16 : 1;
		for (int i = Helper.getPlayer().getInventory().armor.size() - 1; i >= 0; i--) {
			ItemStack armor = Helper.getPlayer().getInventory().armor.get(i);

			if (armor.isDamageable())
				Drawable.horizontalGradient(matrices, 1, y, damage.isToggle() && armor.isDamageable() ? 44 : 17, y + 18,
						0x90000000, 0x00000000);
			RenderUtil.drawItem(Helper.getPlayer().getInventory().armor.get(i), 1, y, true);

			if (damage.isToggle() && armor.isDamageable()) {
				int damage = armor.getMaxDamage() - armor.getDamage();
				IFont.legacy17.drawString(matrices, String.valueOf(damage), 20, y + 4, 0xFFFFFFFF);
			}

			y += 17;
		}

		matrices.pop();
	}

}
