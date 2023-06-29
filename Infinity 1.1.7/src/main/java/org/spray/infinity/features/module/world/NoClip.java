package org.spray.infinity.features.module.world;

import org.spray.infinity.event.ClipEvent;
import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.PlayerInWaterEvent;
import org.spray.infinity.event.PlayerMoveEvent;
import org.spray.infinity.event.PushOutBlockEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;

import com.darkmagician6.eventapi.EventTarget;

@ModuleInfo(category = Category.WORLD, desc = "Allows you to walk through blocks", key = -2, name = "NoClip", visible = true)
public class NoClip extends Module {

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		MoveUtil.setYVelocity(0);

		if (Helper.MC.options.keySneak.isPressed()) {
			MoveUtil.setYVelocity(Helper.getPlayer().getVelocity().y - 0.2);
		} else if (Helper.MC.options.keyJump.isPressed()) {
			MoveUtil.setYVelocity(Helper.getPlayer().getVelocity().y + 0.2);
		}
	}

	@Override
	public void onUpdate() {
	}

	@EventTarget
	public void onPlayerInWater(PlayerInWaterEvent event) {
	}

	@EventTarget
	public void onMove(PlayerMoveEvent event) {

	}

	@EventTarget
	public void onPushBlock(PushOutBlockEvent event) {
		event.cancel();
	}

	@EventTarget
	public void onClip(ClipEvent event) {
		event.cancel();
	}

}
