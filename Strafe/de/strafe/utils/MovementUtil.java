package de.strafe.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MovementUtil implements IMinecraft {

    public double setMotion(double speed) {
        double yaw = Math.toRadians(getDirection());
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
        return yaw;
    }

    public float getDirection() {
        float left = mc.gameSettings.keyBindForward.isPressed() && mc.gameSettings.keyBindLeft.isPressed() ? -45.0F : (mc.gameSettings.keyBindBack.isPressed() && mc.gameSettings.keyBindLeft.isPressed()) ? 45.0F : mc.gameSettings.keyBindLeft.isPressed() ? -90.0F : 0.0F;
        float right = mc.gameSettings.keyBindForward.isPressed() && mc.gameSettings.keyBindRight.isPressed() ? 45.0F : (mc.gameSettings.keyBindBack.isPressed() && mc.gameSettings.keyBindRight.isPressed()) ? -45.0F : mc.gameSettings.keyBindRight.isPressed() ? 90.0F : 0.0F;
        float back = mc.gameSettings.keyBindBack.isPressed() ? 180.0F : 0.0F;
        return mc.thePlayer.rotationYaw + left + right + back;
    }
}
