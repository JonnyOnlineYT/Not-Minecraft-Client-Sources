package org.spray.heaven.features.module.modules.render;

import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import java.util.Objects;

@ModuleInfo(name = "NoRender", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class NoRender extends Module {

	public Setting noFire = register(new Setting("No Visual Fire", true));
	
	// EntityRenderer.hurtCameraEffect()V method callback
	public Setting noHurtCam = register(new Setting("No HurtCam", true));
	
	// Returnable callback in GuiIngame class , renderGameOverlay()V method
	public Setting noScoreboard = register(new Setting("No Scoreboard", false)); 
	
	public Setting noBadEffects = register(new Setting("No Bad Effects", true));

	@Override
	public void onUpdate() {
		if (noBadEffects.isToggle()) {
			if (mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(15))))
				mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(15)));

			if (mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(9))))
				mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(9)));

			mc.entityRenderer.func_190565_a(null);

			if (mc.world != null)
				mc.world.setRainStrength(0.0f);
		}
	}
}
