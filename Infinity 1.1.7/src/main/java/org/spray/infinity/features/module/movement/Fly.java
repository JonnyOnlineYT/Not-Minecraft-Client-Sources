package org.spray.infinity.features.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

@ModuleInfo(category = Category.MOVEMENT, desc = "Lets fly in survival", key = -2, name = "Fly", visible = true)
public class Fly extends Module {

	private Setting mode = new Setting(this, "Mode", "Motion", new ArrayList<>(Arrays.asList("Motion", "Glide")));

	private Setting speed = new Setting(this, "Speed", 0.26, 0.15, 0.5)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Motion"));

	private int airTicks;

	@Override
	public void onDisable() {
		Infinity.resetTimer();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {

			if (mode.getCurrentMode().equalsIgnoreCase("Motion")) {
				MoveUtil.setYVelocity(0);

				if (Helper.MC.options.keyJump.isPressed()) {
					MoveUtil.setYVelocity(Helper.getPlayer().getVelocity().getY() + speed.getCurrentValueDouble());
				}
				if (Helper.MC.options.keySneak.isPressed()) {
					MoveUtil.setYVelocity(Helper.getPlayer().getVelocity().getY() - speed.getCurrentValueDouble());
				}

				MoveUtil.strafe(MoveUtil.calcMoveYaw(), speed.getCurrentValueDouble());

			} else if (mode.getCurrentMode().equalsIgnoreCase("Glide")) {

				if (!Helper.getPlayer().isOnGround()) {
					airTicks++;
				}

				if (airTicks == 2) {
					Infinity.resetTimer();
				}

				if (airTicks == 6) {
					Infinity.TIMER = 0.7f;
					MoveUtil.setYVelocity(0.1);
				}

				if (airTicks >= 6 && !Helper.getPlayer().isOnGround()) {
					airTicks = 0;
					MoveUtil.vClip(-0.2);
				}
			}
		}
	}

}
