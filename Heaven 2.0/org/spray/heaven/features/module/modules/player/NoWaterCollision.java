package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PlayerInWaterEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import com.darkmagician6.eventapi.EventTarget;

@ModuleInfo(name = "NoWaterCollision", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class NoWaterCollision extends Module {

	private final Setting ignoreGround = register(new Setting("Ignore Ground", true));
	
	@EventTarget
	public void onWater(PlayerInWaterEvent event) {
		if (ignoreGround.isToggle() && !mc.player.onGround)
			return;
		
		event.setInWater(false);
	}
}
