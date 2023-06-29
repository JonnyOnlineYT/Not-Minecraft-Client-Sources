package ru.rockstar.api.utils.other;

import java.util.Random;
import net.minecraft.client.gui.ScaledResolution;

public class Particle {
    public float x;

    public float y;

    public float radius;

    public float speed;

    public float ticks;

    public float opacity;

    public Particle(ScaledResolution sr, float radius, float speed) {
        this.x = (new Random()).nextFloat() * sr.getScaledWidth();
        this.y = (new Random()).nextFloat() * sr.getScaledHeight();
        this.ticks = (new Random()).nextFloat() * sr.getScaledHeight() / 2.0F;
        this.radius = radius;
        this.speed = speed;
    }
}
