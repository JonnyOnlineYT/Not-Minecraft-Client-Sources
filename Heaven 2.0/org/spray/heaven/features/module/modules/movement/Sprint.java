package org.spray.heaven.features.module.modules.movement;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "Sprint", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Sprint extends Module {
	
//	public Setting keepSprint = register(new Setting("Keep Sprint", false)); // EntityPlayer.java action

	@Override
	public void onUpdate() {
		if ((mc.gameSettings.keyBindForward.isKeyDown()) && !(mc.player.isSneaking()) && !(mc.player.isHandActive())
				&& !(mc.player.isCollidedHorizontally) && !(mc.player.getFoodStats().getFoodLevel() <= 6f)) {
			mc.player.setSprinting(true);
		}
	}

}
