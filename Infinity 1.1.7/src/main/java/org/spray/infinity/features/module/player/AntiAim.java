package org.spray.infinity.features.module.player;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.features.module.combat.KillAura;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MathAssist;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

@ModuleInfo(category = Category.PLAYER, desc = "Moves differently to make it harder for the enemy to hit", key = -2, name = "AntiAim", visible = true)
public class AntiAim extends Module {

	private Setting yawMode = new Setting(this, "Yaw Mode", "Spin", new ArrayList<>(Arrays.asList("Spin", "Random")));

	private Setting spinSpeed = new Setting(this, "Spin Speed", 20D, 0D, 60D)
			.setVisible(() -> yawMode.getCurrentMode().equalsIgnoreCase("Spin"));

	private Setting pitchMode = new Setting(this, "Pitch Mode", "Down",
			new ArrayList<>(Arrays.asList("Down", "Up", "Random", "None")));

	private float yaw;
	private float pitch;

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {

			// yaw
			if (yawMode.getCurrentMode().equalsIgnoreCase("Spin")) {
				yaw += spinSpeed.getCurrentValueDouble();

				if (yaw > 180)
					yaw = -180;

				if (yaw < -180)
					yaw = 180;
			} else if (yawMode.getCurrentMode().equalsIgnoreCase("Random")) {
				double randomYaw = Math.min(MathAssist.random(-180, 180), spinSpeed.getCurrentValueDouble());
				yaw = (float) randomYaw;
			}

			// pitch

			if (pitchMode.getCurrentMode().equalsIgnoreCase("Down")) {
				pitch = 90f;
			} else if (pitchMode.getCurrentMode().equalsIgnoreCase("Up")) {
				pitch = -90f;
			} else if (pitchMode.getCurrentMode().equalsIgnoreCase("None")) {
				pitch = Helper.getPlayer().getPitch();
			} else if (pitchMode.getCurrentMode().equalsIgnoreCase("Random")) {
				pitch = (float) MathAssist.random(-90, 90);
			}

			float f = (float) (Helper.MC.options.mouseSensitivity * 0.6F + 0.2F);
			float gcd = f * f * f * 1.2F;

			yaw -= yaw % gcd;
			pitch -= pitch % gcd;

			if (KillAura.target != null || Helper.getPlayer().isUsingItem())
				return;

			if (!Float.isNaN(yaw) || !Float.isNaN(pitch)) {
				event.setYaw(yaw);
				Helper.getPlayer().bodyYaw = yaw;
				Helper.getPlayer().headYaw = yaw;
				event.setPitch(pitch);
			}

		}
	}
}
