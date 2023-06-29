package ru.rockstar.api.utils.render;

import net.minecraft.client.Minecraft;
import ru.rockstar.api.utils.math.MathHelper;

public class AnimationHelper {
    public static int deltaTime;
    public static float speedTarget = 0.125f;
    private int time;
    private State previousState = State.STATIC;
    private State currentState = State.STATIC;
    private long currentStateStart = 0;
    private boolean initialState;

    public AnimationHelper(int time, boolean initialState) {
        this.time = time;
        this.initialState = initialState;

        if (initialState) {
            previousState = State.EXPANDING;
        }
    }

    public static float animation(float current, float targetAnimation, float speedTarget) {
        float da = (targetAnimation - current) / Minecraft.getDebugFPS() * 15.0f;
        if (da > 0.0f) {
            da = Math.max(speedTarget, da);
            da = Math.min(targetAnimation - current, da);
        } else if (da < 0.0f) {
            da = Math.min(-speedTarget, da);
            da = Math.max(targetAnimation - current, da);
        }
        return current + da;
    }

    public double getAnimationFactor() {
        if (currentState == State.EXPANDING) {
            return (System.currentTimeMillis() - currentStateStart) / (double) time;
        }

        if (currentState == State.RETRACTING) {
            return ((long) time - (System.currentTimeMillis() - currentStateStart)) / (double) time;
        }

        return previousState == State.EXPANDING ? 1 : 0;
    }

    public static double animate(double target, double current, double speed) {
        boolean bl = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = bl ? (current += factor) : (current -= factor);
        return current;
    }
    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1) {
            delta = 1;
        }
        if (delta > 1000) {
            delta = 16;
        }
        if (diff > speed) {
            double xD = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current -= xD;
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            double xD = (speed * delta / (1000 / 60) < 0.5 ? 0.5 : speed * delta / (1000 / 60));
            current += xD;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }
    public enum State {
        EXPANDING, RETRACTING, STATIC
    }
	
    public static float move(float from, float to, float minstep, float maxstep, float factor) {

        float f = (to - from) * MathHelper.clamp(factor,0,1);

        if (f < 0)
            f = MathHelper.clamp(f, -maxstep, -minstep);
        else
            f = MathHelper.clamp(f, minstep, maxstep);

        if(Math.abs(f) > Math.abs(to - from))
            return to;

        return from + f;
    }
}

