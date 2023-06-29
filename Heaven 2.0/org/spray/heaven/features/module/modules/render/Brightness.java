package org.spray.heaven.features.module.modules.render;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "NightVision", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class Brightness extends Module {

	@Override
	public void onDisable() {
		mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
	}

	@Override
	public void onUpdate() {
		if (mc.player != null && mc.world != null)
			mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
	}

}
