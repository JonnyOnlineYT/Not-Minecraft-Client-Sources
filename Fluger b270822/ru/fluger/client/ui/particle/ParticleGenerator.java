/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.ui.particle.Particle;

public class ParticleGenerator {
    private final List<Particle> particles = new ArrayList<Particle>();
    private final int amount;
    private int prevWidth;
    private int prevHeight;

    public ParticleGenerator(int amount) {
        this.amount = amount;
    }

    public void draw(int mouseX, int mouseY) {
        if (this.particles.isEmpty() || this.prevWidth != Minecraft.getMinecraft().displayWidth || this.prevHeight != Minecraft.getMinecraft().displayHeight) {
            this.particles.clear();
            this.create();
        }
        this.prevWidth = Minecraft.getMinecraft().displayWidth;
        this.prevHeight = Minecraft.getMinecraft().displayHeight;
        for (Particle particle : this.particles) {
            particle.fall();
            particle.interpolation();
            int range = 110;
            RenderHelper.drawCircle(particle.getX(), particle.getY(), particle.size, -1);
        }
    }

    private void create() {
        Random random = new Random();
        for (int i = 0; i < this.amount; ++i) {
            this.particles.add(new Particle(random.nextInt(Minecraft.getMinecraft().displayWidth), random.nextInt(Minecraft.getMinecraft().displayHeight)));
        }
    }
}

