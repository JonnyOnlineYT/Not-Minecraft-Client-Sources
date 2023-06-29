package org.moonware.client.helpers.render;

import net.minecraft.client.Minecraft;
import org.moonware.client.helpers.math.MathematicHelper;

public class ScreenHelper {

    private float x;
    private float y;
    private long lastMS;

    public ScreenHelper(float x, float y) {
        this.x = x;
        this.y = y;
        lastMS = System.currentTimeMillis();
    }

    public static double animate(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1D) {
            factor = 0.1D;
        }

        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }

        return current;
    }

    public void interpolate(float targetX, float targetY, double speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - lastMS;
        lastMS = currentMS;
        double deltaX = 0;
        double deltaY = 0;
        if (speed != 0) {
            deltaX = (Math.abs(targetX - x) * 0.35f) / (10 / speed);
            deltaY = (Math.abs(targetY - y) * 0.35f) / (10 / speed);
        }
        x = AnimationHelper.calculateCompensation(targetX, x, delta, deltaX);
        y = AnimationHelper.calculateCompensation(targetY, y, delta, deltaY);
    }

    public void calculateCompensation(float targetX, float targetY, float xSpeed, float ySpeed) {
        int deltaX = (int) (Math.abs(targetX - x) * xSpeed);
        int deltaY = (int) (Math.abs(targetY - y) * ySpeed);
        x = AnimationHelper.calculateCompensation(targetX, x, (long) Minecraft.frameTime, deltaX);
        y = AnimationHelper.calculateCompensation(targetY, y, (long) Minecraft.frameTime, deltaY);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void calculateCompensation(float targetX, float targetY, double xSpeed, double ySpeed) {
        int deltaX = (int)((double)Math.abs(targetX - x) * xSpeed);
        int deltaY = (int)((double)Math.abs(targetY - y) * ySpeed);
        x = AnimationHelper.calculateCompensation(targetX, x, (long) (Minecraft.frameTime * 0.5), deltaX);
        y = AnimationHelper.calculateCompensation(targetY, y, (long) (Minecraft.frameTime * 0.5), deltaY);
    }
}
