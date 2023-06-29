package net.minecraft.client.renderer.vec;

public class Vec2f {
    public static final net.minecraft.util.math.Vec2f ZERO = new net.minecraft.util.math.Vec2f(0.0f, 0.0f);
    public static final net.minecraft.util.math.Vec2f ONE = new net.minecraft.util.math.Vec2f(1.0f, 1.0f);
    public static final net.minecraft.util.math.Vec2f UNIT_X = new net.minecraft.util.math.Vec2f(1.0f, 0.0f);
    public static final net.minecraft.util.math.Vec2f NEGATIVE_UNIT_X = new net.minecraft.util.math.Vec2f(-1.0f, 0.0f);
    public static final net.minecraft.util.math.Vec2f UNIT_Y = new net.minecraft.util.math.Vec2f(0.0f, 1.0f);
    public static final net.minecraft.util.math.Vec2f NEGATIVE_UNIT_Y = new net.minecraft.util.math.Vec2f(0.0f, -1.0f);
    public static final net.minecraft.util.math.Vec2f MAX = new net.minecraft.util.math.Vec2f(Float.MAX_VALUE, Float.MAX_VALUE);
    public static final net.minecraft.util.math.Vec2f MIN = new net.minecraft.util.math.Vec2f(Float.MIN_VALUE, Float.MIN_VALUE);
    public final float x;
    public final float y;

    public Vec2f(float xIn, float yIn) {
        x = xIn;
        y = yIn;
    }
}
