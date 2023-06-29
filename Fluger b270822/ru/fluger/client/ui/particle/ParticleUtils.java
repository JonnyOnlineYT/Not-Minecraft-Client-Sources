/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.particle;

import ru.fluger.client.ui.particle.ParticleGenerator;

public final class ParticleUtils {
    private static final ParticleGenerator particleGenerator = new ParticleGenerator(100);

    public static void drawParticles(int mouseX, int mouseY) {
        particleGenerator.draw(mouseX, mouseY);
    }
}

