package org.spray.infinity.features.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.mixin.IKeyBinding;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MathAssist;
import org.spray.infinity.utils.MoveUtil;
import org.spray.infinity.utils.block.BlockUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(category = Category.MOVEMENT, desc = "Lets you walk on water", key = -2, name = "Jesus", visible = true)
public class Jesus extends Module {

	private Setting mode = new Setting(this, "Mode", "Swing",
			new ArrayList<>(Arrays.asList("Swing", "Matrix 6.0.6", "Bhop")));

	private Setting speed = new Setting(this, "Speed", 0.32, 0.2, 1)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Matrix 6.0.6")
					|| mode.getCurrentMode().equalsIgnoreCase("Bhop"));

	private boolean water;

	private double dir;

	@Override
	public void onEnable() {
		if (Infinity.getModuleManager().get(AntiWaterPush.class).isEnabled()) {
			Helper.message("Jesus will not work stably, please turn off AntiWaterPush module");
		}
	}

	@Override
	public void onDisable() {
		KeyBinding.setKeyPressed(((IKeyBinding) Helper.MC.options.keyJump).getBoundKey(), false);
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
		if (Infinity.getModuleManager().get(AntiWaterPush.class).isEnabled())
			return;

		double offsetY = MathAssist.random(0.01, 0.3);
		double bhopY = MathAssist.random(0.1, 0.34);
		switch (mode.getCurrentMode()) {
		case "Matrix 6.0.6":
			if (BlockUtil.isFluid(new BlockPos(Helper.getPlayer().getX(), Helper.getPlayer().getY() + offsetY,
					Helper.getPlayer().getZ()))) {

				Helper.getPlayer().setVelocity(Helper.getPlayer().getVelocity().x, 0,
						Helper.getPlayer().getVelocity().z);
			}

			if (Helper.getPlayer().isTouchingWater()) {
				Helper.getPlayer().setSprinting(false);
				MoveUtil.strafe(MoveUtil.calcMoveYaw(), speed.getCurrentValueDouble());
				KeyBinding.setKeyPressed(((IKeyBinding) Helper.MC.options.keyJump).getBoundKey(), true);
			}
			break;
		case "Swing":
			if (Helper.getPlayer().isTouchingWater() || Helper.getPlayer().isInLava() || water) {
				if (Helper.getPlayer().age % 3 == 0) {
					if (Helper.MC.options.keyForward.isPressed()) {
						dir = Math.toRadians(Helper.getPlayer().getYaw());
						Helper.getPlayer().setVelocity((-Math.sin(dir) * 0.7), 0.2, Math.cos(dir) * 0.7);

						Helper.getPlayer().setVelocity((-Math.sin(dir) * 0.4), 0.2, Math.cos(dir) * 0.4);
					}
					if (Helper.MC.options.keyLeft.isPressed()) {
						dir = Math.toRadians(Helper.getPlayer().getYaw() - 90);
						Helper.getPlayer().setVelocity((-Math.sin(dir) * 0.6), 0.2, Math.cos(dir) * 0.6);

						Helper.getPlayer().setVelocity((-Math.sin(dir) * 0.3), 0.2, Math.cos(dir) * 0.3);
					}
					if (Helper.MC.options.keyRight.isPressed()) {
						dir = Math.toRadians(Helper.getPlayer().getYaw() + 90);
						Helper.getPlayer().setVelocity((-Math.sin(dir) * 0.6), 0.2, Math.cos(dir) * 0.6);

						Helper.getPlayer().setVelocity((-Math.sin(dir) * 0.3), 0.2, Math.cos(dir) * 0.3);
					}
				}
			}
			break;
		case "Bhop":
			if (BlockUtil.isFluid(new BlockPos(Helper.getPlayer().getX(), Helper.getPlayer().getY() + bhopY,
					Helper.getPlayer().getZ()))) {
				MoveUtil.setYVelocity(0.4);
				MoveUtil.strafe(MoveUtil.calcMoveYaw(), speed.getCurrentValueDouble());
			}

			break;
		}
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (Infinity.getModuleManager().get(AntiWaterPush.class).isEnabled())
			return;
		
		if (event.getType().equals(EventType.PRE)) {
			if (mode.getCurrentMode().equalsIgnoreCase("Swing")) {

				if (Helper.getPlayer().isTouchingWater() || Helper.getPlayer().isInLava() || water) {
					if (Helper.getPlayer().age % 2 == 0) {
						if (Helper.MC.options.keyRight.isPressed()
								|| Helper.MC.options.keyLeft.isPressed()
								|| Helper.MC.options.keyForward.isPressed())
							Helper.getPlayer().setVelocity(Helper.getPlayer().getVelocity().x, -0.2,
									Helper.getPlayer().getVelocity().z);
					}
				}
			}
		} else if (event.getType().equals(EventType.POST)) {
			water = Helper.getPlayer().isTouchingWater() || Helper.getPlayer().isInLava();
			if (mode.getCurrentMode().equalsIgnoreCase("Swing")) {
				if (Helper.getPlayer().isTouchingWater() || Helper.getPlayer().isInLava()) {
					if (Helper.MC.options.keyRight.isPressed()
							|| Helper.MC.options.keyLeft.isPressed()
							|| Helper.MC.options.keyForward.isPressed())
						return;
					Helper.getPlayer().setVelocity(Helper.getPlayer().getVelocity().x, 0.2,
							Helper.getPlayer().getVelocity().z);
				}
			}
		}

	}

}
