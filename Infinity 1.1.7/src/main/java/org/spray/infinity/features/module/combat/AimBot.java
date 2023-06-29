package org.spray.infinity.features.module.combat;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.ClickEvent;
import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MathAssist;
import org.spray.infinity.utils.MoveUtil;
import org.spray.infinity.utils.RotationUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

@ModuleInfo(category = Category.COMBAT, desc = "Automatically aim at target", key = -2, name = "AimBot", visible = true)
public class AimBot extends Module {

	private Setting look = new Setting(this, "Look", "HEAD", new ArrayList<>(Arrays.asList("HEAD", "BODY", "LEGS")));

	// targets
	private Setting players = new Setting(this, "Players", true);
	private Setting friends = new Setting(this, "Friends", false).setVisible(() -> players.isToggle());
	private Setting invisibles = new Setting(this, "Invisibles", true);
	private Setting mobs = new Setting(this, "Mobs", true);
	private Setting animals = new Setting(this, "Animals", false);

	private Setting throughWalls = new Setting(this, "Through Walls", false);

	private Setting whenClick = new Setting(this, "When Click", false);

	private Setting whenMoving = new Setting(this, "When Moving", false);

	private Setting range = new Setting(this, "Range", 3.5D, 0D, 6D);

	private Setting fov = new Setting(this, "FOV", 120D, 0D, 360D);

	private Setting maxSpeed = new Setting(this, "Max Speed %", 60D, 0D, 100D);
	private Setting minSpeed = new Setting(this, "Min Speed %", 20D, 0D, 100D);

	private Entity target;
	private float[] lookPos;
	private float speed;

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		target = EntityUtil.getTarget(this.range.getCurrentValueDouble(), fov.getCurrentValueDouble(),
				players.isToggle(), friends.isToggle(), invisibles.isToggle(), mobs.isToggle(), animals.isToggle(),
				throughWalls.isToggle());

		if (target == null || whenClick.isToggle())
			return;

		double min = minSpeed.getCurrentValueDouble();
		double max = maxSpeed.getCurrentValueDouble();
		speed = (float) MathAssist.random(min, max);

		lookPos = getLookPos(target);

		if (whenMoving.isToggle() && !MoveUtil.isMoving())
			return;

		event.setRotation(RotationUtil.limitAngleChange(event.getYaw(), lookPos[0], speed),
				RotationUtil.limitAngleChange(event.getPitch(), lookPos[1], speed), true);

	}

	@EventTarget
	public void onClick(ClickEvent event) {
		if (!whenClick.isToggle() && target == null)
			return;

		Helper.getPlayer().setYaw(RotationUtil.limitAngleChange(Helper.getPlayer().getYaw(), lookPos[0], speed));
		Helper.getPlayer().setPitch(RotationUtil.limitAngleChange(Helper.getPlayer().getPitch(), lookPos[1], speed));

	}

	private float[] getLookPos(Entity target) {

		double d = target.getPos().x - Helper.getPlayer().getPos().x;
		double e = target.getPos().z - Helper.getPlayer().getPos().z;
		double pitchPos = 0;

		if (look.getCurrentMode().equalsIgnoreCase("HEAD"))
			pitchPos = 0;
		else if (look.getCurrentMode().equalsIgnoreCase("BODY"))
			pitchPos = 0.5;
		else if (look.getCurrentMode().equalsIgnoreCase("LEGS"))
			pitchPos = 1.4;

		double g;
		if (target instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) target;
			g = livingEntity.getEyeY() - Helper.getPlayer().getEyeY() - pitchPos;
		} else {
			g = (target.getBoundingBox().minY + target.getBoundingBox().maxY) / 2.0D - Helper.getPlayer().getY()
					+ Helper.getPlayer().getEyeY() - pitchPos;
		}

		double h = (double) Math.sqrt(d * d + e * e);
		float i = (float) (Math.atan2(e, d) * 180.0D / Math.PI) - 90.0F;
		float j = (float) (-(Math.atan2(g, h) * 180.0D / Math.PI));

		return new float[] { i, j };
	}
}
