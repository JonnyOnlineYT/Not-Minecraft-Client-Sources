package org.spray.heaven.features.module.modules.movement;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.EntityUtil;

@ModuleInfo(name = "LongJump", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class LongJump extends Module {
	
	private Setting autoPearl = register(new Setting("Auto throw pearl", true));
	
	@Override
	public void onEnable() {
		if (autoPearl.isToggle())
			EntityUtil.throwPearl();
	}

	@Override
	public void onDisable() {
		mc.player.jumpMovementFactor = 0.02F;
		mc.player.speedInAir = 0.02f;
		mc.getTimer().reset();
	}

	@Override
	public void onUpdate() {
		if (mc.player.hurtTime > 0) {
			if (mc.player.onGround) {
				mc.player.jump();
			} else {
				mc.getTimer().setSpeed(1.5f);
				mc.player.speedInAir = 0.04f;
				mc.player.jumpMovementFactor = 0.999f;
			}
		}
	}
}
