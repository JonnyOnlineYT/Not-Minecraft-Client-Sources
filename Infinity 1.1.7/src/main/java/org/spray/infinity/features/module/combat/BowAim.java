package org.spray.infinity.features.module.combat;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.RotationUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.Entity;
import net.minecraft.item.BowItem;

@ModuleInfo(category = Category.COMBAT, desc = "Aimbot for bow to target", key = -2, name = "BowAim", visible = true)
public class BowAim extends Module {

	private Setting mode = new Setting(this, "Mode", "Packet", new ArrayList<>(Arrays.asList("Packet", "Client")));
	private Setting players = new Setting(this, "Players", true);
	private Setting friends = new Setting(this, "Friends", false).setVisible(() -> players.isToggle());
	private Setting invisibles = new Setting(this, "Invisibles", true);
	private Setting mobs = new Setting(this, "Mobs", true);
	private Setting animals = new Setting(this, "Animals", true);

	private Setting throughWalls = new Setting(this, "Through Walls", false);

	private Setting fov = new Setting(this, "FOV", 120D, 0D, 360D);
	private Setting range = new Setting(this, "Range", 40, 1, 80);
	private Setting speed = new Setting(this, "Speed", 90F, 1F, 180F);

	public static Entity target;

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {
			if (Helper.getPlayer().getMainHandStack().getItem() instanceof BowItem && Helper.getPlayer().isUsingItem()
					&& Helper.getPlayer().getItemUseTime() >= 3) {
				target = EntityUtil.getTarget(range.getCurrentValueInt(), fov.getCurrentValueDouble(),
						players.isToggle(), friends.isToggle(), invisibles.isToggle(), mobs.isToggle(),
						animals.isToggle(), throughWalls.isToggle());
				if (target == null)
					return;
				float[] look = RotationUtil.bowAimRotation(target);

				look[0] = RotationUtil.limitAngleChange(event.getYaw(), look[0], speed.getCurrentValueFloat());
				look[1] = RotationUtil.limitAngleChange(event.getPitch(), look[1], speed.getCurrentValueFloat());

				event.setRotation(look[0], look[1], mode.getCurrentMode().equalsIgnoreCase("Client"));

			}
		}
	}
}
