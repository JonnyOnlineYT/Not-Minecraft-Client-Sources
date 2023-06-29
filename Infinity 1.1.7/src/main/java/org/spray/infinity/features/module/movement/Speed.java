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
import org.spray.infinity.utils.Timer;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

@ModuleInfo(category = Category.MOVEMENT, desc = "Make you faster", key = -2, name = "Speed", visible = true)
public class Speed extends Module {

	private Setting mode = new Setting(this, "Mode", "Strafe",
			new ArrayList<>(Arrays.asList("Strafe", "Sentiel Ground", "onGround")));

	private Setting strafeSpeed = new Setting(this, "Strafe Speed", 0.24, 0.2, 0.5)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Strafe"));

	private Timer timer = new Timer();

	@Override
	public void onDisable() {
		Infinity.resetTimer();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

		if (mode.getCurrentMode().equalsIgnoreCase("Strafe")) {
			if (MoveUtil.isMoving() && Helper.getPlayer().isOnGround()) {
				if (Helper.getPlayer().isSprinting())
					Helper.getPlayer().setSprinting(false);
				Helper.getPlayer().jump();
			}
		}
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {
			if (mode.getCurrentMode().equalsIgnoreCase("Strafe")) {
				MoveUtil.strafe(MoveUtil.calcMoveYaw(), strafeSpeed.getCurrentValueDouble());

			} else if (mode.getCurrentMode().equalsIgnoreCase("Sentiel Ground")) {

				if (!Helper.getPlayer().isOnGround())
					return;

				if (MoveUtil.isMoving()) {
					if (timer.hasReached(380)) {
						Infinity.TIMER = 0.45f;
						MoveUtil.hClip(3);
						timer.reset();
					} else {
						Infinity.resetTimer();
					}

				} else
					Infinity.resetTimer();

			} else if (mode.getCurrentMode().equalsIgnoreCase("onGround")) {
				if (!Helper.getPlayer().isOnGround())
					return;

				Helper.getPlayer().velocityDirty = false;

				Helper.getPlayer().getAbilities().allowFlying = false;

				MoveUtil.setHVelocity(0, 0);
				MoveUtil.strafe(MoveUtil.calcMoveYaw(), 0.4);

			}
		}
	}
}
