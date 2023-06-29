package org.spray.infinity.features.module.movement;

import org.spray.infinity.event.PlayerInWaterEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventTarget;

@ModuleInfo(category = Category.MOVEMENT, desc = "You can walk underwater", key = -2, name = "AntiWaterPush", visible = true)
public class AntiWaterPush extends Module {

	@EventTarget
	public void onPlayerInWater(PlayerInWaterEvent event) {
		event.setInWater(false);
	}
	
	@Override
	public void onUpdate() {
		Helper.getPlayer().setSprinting(false);
	}

}
