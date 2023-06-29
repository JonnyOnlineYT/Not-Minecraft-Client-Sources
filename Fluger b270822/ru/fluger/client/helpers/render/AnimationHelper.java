/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.helpers.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class AnimationHelper {
    public static float delta;

    public static float customAnim(float current, float add, float min, float max) {
        if ((current += add) > max) {
           current = max;
        }

        if (current < min) {
           current = min;
        }

        return current;
     }

    public static float Move(float from, float to, float minstep, float maxstep, float factor) {
        float f = (to - from) * MathHelper.clamp(factor, 0.0f, 1.0f);
        f = f < 0.0f ? MathHelper.clamp(f, -maxstep, -minstep) : MathHelper.clamp(f, minstep, maxstep);
        if (Math.abs(f) > Math.abs(to - from)) {
            return to;
        }
        return from + f;
    }

    public static double easeInOutQuart(double x) {
        return x < 0.5 ? 8.0 * x * x * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, 4.0) / 2.0;
    }

    public static float animation2(float animation, float target, float speed) {
        float dif = (target - animation) / Math.max((float)Minecraft.getDebugFPS(), 5.0f) * 15.0f;
        if (dif > 0.0f) {
            dif = Math.max(speed, dif);
            dif = Math.min(target - animation, dif);
        } else if (dif < 0.0f) {
            dif = Math.min(-speed, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }

    public static float animation(float animation, float target, float speedTarget) {
        float dif = (target - animation) / Math.max((float)Minecraft.getDebugFPS(), 5.0f) * speedTarget;
        if (dif > 0.0f) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        } else if (dif < 0.0f) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }

    public static double animation(double animation, double target, double speedTarget) {
        double dif = (target - animation) / (double)Math.max(Minecraft.getDebugFPS(), 5) * speedTarget;
        if (dif > 0.0) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        } else if (dif < 0.0) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }

    public static float calculateCompensation(float target, float current, float delta, double speed) {
        float diff = current - target;
        if (delta < 1.0F) {
           delta = 1.0F;
        }

        if (delta > 1000.0F) {
           delta = 16.0F;
        }

        double dif = Math.max(speed * (double)delta / 16.66666603088379, 0.5);
        if ((double)diff > speed) {
           if ((current = (float)((double)current - dif)) < target) {
              current = target;
           }
        } else if ((double)diff < -speed) {
           if ((current = (float)((double)current + dif)) > target) {
              current = target;
           }
        } else {
           current = target;
        }

        return current;
     }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)((double)delta * speed);
        animation = animation < finalState ? (animation + (double)add < finalState ? (animation = animation + (double)add) : finalState) : (animation - (double)add > finalState ? (animation = animation - (double)add) : finalState);
        return animation;
    }
}

