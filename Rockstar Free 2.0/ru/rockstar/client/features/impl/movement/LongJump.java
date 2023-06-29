package ru.rockstar.client.features.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class LongJump extends Feature {

    public ListSetting mode;
    public NumberSetting boostMultiplier;
    public NumberSetting speed = new NumberSetting("Speed", 1F, 0.1F, 7F, 0.1F, () -> mode.currentMode.equals("StormHVH NoDamage"));;
    public NumberSetting motionBoost;
    public BooleanSetting motionYBoost = new BooleanSetting("MotionY boost", false, () -> true);

    public LongJump() {
        super("LongJump", "Длинный прыжок",0, Category.MOVEMENT);
        mode = new ListSetting("LongJump Mode", "Matrix Pearle", () -> true, "Redesky", "Matrix Pearle", "StormHVH NoDamage");
        boostMultiplier = new NumberSetting("Boost Speed", 0.3F, 0.1F, 1F, 0.1F, () -> mode.currentMode.equals("Matrix Pearle"));
        motionBoost = new NumberSetting("Motion Boost", 0.6F, 0.1F, 8F, 0.1F, () -> mode.currentMode.equals("Matrix Pearle") && motionYBoost.getBoolValue());
        addSettings(mode, speed);
    }

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdate event) {
        String longMode = mode.getOptions();
        this.setSuffix(longMode, true);
            
        if (longMode.equalsIgnoreCase("Redesky")) {
            if (mc.player.hurtTime > 0) {
                mc.timer.timerSpeed = 1F;
                if (mc.player.fallDistance != 0.0f) {
                    mc.player.motionY += 0.039;
                }
                if (mc.player.onGround) {
                    mc.player.jump();
                    mc.player.jumpMovementFactor = 0.01f;
                } else {
                    mc.timer.timerSpeed = 0.2f;
                    mc.player.motionY += 0.075;
                    mc.player.motionX *= 1.065f;
                    mc.player.motionZ *= 1.065f;
                }
            }
        } else if (longMode.equalsIgnoreCase("Matrix Pearle")) {
        	if (mc.player.hurtTime > 0) {
        		mc.player.motionY += 0.13;
        		if (mc.gameSettings.keyBindForward.pressed && !mc.player.onGround) {
        		mc.player.motionX -= MathHelper.sin((float) Math.toRadians(mc.player.rotationYaw));
        		mc.player.motionZ += MathHelper.cos((float) Math.toRadians(mc.player.rotationYaw));
        		event.setGround(true);
        		return;
        		}
        	}
        } else if (longMode.equalsIgnoreCase("StormHVH NoDamage")) {
        	block13: {
        		mc.player.jumpMovementFactor = 0.01f;
				BlockPos feet = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                BlockPos newPos = new BlockPos(feet.getX(),feet.getY() - 1, feet.getZ());
                if (mc.world.getBlockState(newPos).getBlock() instanceof BlockAir) {
                    return;
                }
				if (mc.player.onGround) {
					mc.player.jump();
				}
				if (mc.player.isInWater()) break block13;
				if (mc.player.onGround) {
					if (!MovementHelper.isMoving())
						if(mc.player.ticksExisted %1==0 && mc.player.speedInAir < 0.03 ) {
							mc.player.jumpMovementFactor = 0.01f;
							mc.player.speedInAir *= 2f + 1.32412f ;
						}
				}
				if (!MovementHelper.isMoving()) {
					mc.player.speedInAir = 0.02f;
					return;
				}
				if (mc.player.onGround != true) {
					mc.player.speedInAir = 0.0f;
					MovementHelper.strafe(speed.getNumberValue());
				}
			}
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }
}
