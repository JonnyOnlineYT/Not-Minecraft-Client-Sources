package org.spray.infinity.features.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;
import org.spray.infinity.utils.RotationUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;

@ModuleInfo(category = Category.MOVEMENT, desc = "Whirls in a circle of entity", key = -2, name = "TargetStrafe", visible = true)
public class TargetStrafe extends Module {

	private Setting mode = new Setting(this, "Mode", "Basic", new ArrayList<>(Arrays.asList("Basic", "Scroll")));

	private Setting players = new Setting(this, "Players", true);
	private Setting friends = new Setting(this, "Friends", false).setVisible(() -> players.isToggle());
	private Setting invisibles = new Setting(this, "Invisibles", true);
	private Setting mobs = new Setting(this, "Mobs", true);
	private Setting animals = new Setting(this, "Animals", true);

	private Setting distance = new Setting(this, "Entity Distance", 7.0D, 6.1D, 15.0D);

	private Setting radius = new Setting(this, "Strafing radius", 1.9D, 0.0D, 6.0D);
	private Setting speed = new Setting(this, "Speed", 0.24D, 0.0D, 1.0D);

	private Setting scrollSpeed = new Setting(this, "Speed to Entity", 0.26D, 0.0D, 1.0D)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Scroll"));

	private Setting damageBoost = new Setting(this, "Damage Boost", false);
	private Setting boost = new Setting(this, "Boost Value", 0.09D, 0.0D, 0.8D)
			.setVisible(() -> damageBoost.isToggle());

	public static Entity target;

	private int direction = 1;

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		setSuffix(mode.getCurrentMode());

		target = EntityUtil.getTarget(distance.getCurrentValueDouble(), 360, players.isToggle(), friends.isToggle(),
				invisibles.isToggle(), mobs.isToggle(), animals.isToggle(), true);

		if (target == null)
			return;

		if (Helper.getPlayer().isOnGround())
			Helper.getPlayer().jump();

		if (Helper.MC.options.keyLeft.isPressed())
			direction = 1;
		else if (Helper.MC.options.keyRight.isPressed())
			direction = -1;

		if (Helper.getPlayer().horizontalCollision)
			direction = direction == 1 ? -1 : 1;

		double speed = damageBoost.isToggle() && Helper.getPlayer().hurtTime != 0
				? this.speed.getCurrentValueDouble() + boost.getCurrentValueDouble()
				: this.speed.getCurrentValueDouble();
		double forward = Helper.getPlayer().distanceTo(target) > radius.getCurrentValueDouble() ? 1 : 0;

		float yaw = RotationUtil.lookAtEntity(target)[0];
		
		Helper.getPlayer().bodyYaw = yaw;
		Helper.getPlayer().headYaw = yaw;

		if (mode.getCurrentMode().equalsIgnoreCase("Basic"))
			getBasic(yaw, speed, forward, direction);
		else if (mode.getCurrentMode().equalsIgnoreCase("Scroll"))
			getScroll(target, speed);
	}

	private void getScroll(Entity target, double speed) {
		double c1 = (Helper.getPlayer().getX() - target.getX())
				/ (Math.sqrt(Math.pow(Helper.getPlayer().getX() - target.getX(), 2)
						+ Math.pow(Helper.getPlayer().getZ() - target.getZ(), 2)));
		double s1 = (Helper.getPlayer().getZ() - target.getZ())
				/ (Math.sqrt(Math.pow(Helper.getPlayer().getX() - target.getX(), 2)
						+ Math.pow(Helper.getPlayer().getZ() - target.getZ(), 2)));

		double x = speed * s1 * direction - scrollSpeed.getCurrentValueDouble() * speed * c1;
		double z = -speed * c1 * direction - scrollSpeed.getCurrentValueDouble() * speed * s1;

		MoveUtil.setHVelocity(x, z);
	}

	private void getBasic(float yaw, double speed, double forward, double direction) {
		if (forward != 0.0D) {
			if (direction > 0.0D) {
				yaw += (float) (forward > 0.0D ? -45 : 45);
			} else if (direction < 0.0D) {
				yaw += (float) (forward > 0.0D ? 45 : -45);
			}
			direction = 0.0D;
			if (forward > 0.0D) {
				forward = 1.0D;
			} else if (forward < 0.0D) {
				forward = -1.0D;
			}
		}

		double x = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F)))
				+ direction * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
		double z = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F)))
				- direction * speed * Math.cos(Math.toRadians((yaw + 90.0F)));

		MoveUtil.setHVelocity(x, z);
	}
}
