package org.moonware.client.feature.impl.combat.particle.zalupa;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import org.moonware.client.feature.impl.combat.particle.zalupa.Utils.Vec3;
import org.moonware.client.helpers.misc.TimerHelper;

public class Particle {

    private final TimerHelper removeTimer = new TimerHelper();

    public final Vec3 position;
    private final Vec3 delta;

    public Particle(Vec3 position) {
        this.position = position;
        delta = new Vec3((Math.random() * 0.5 - 0.25) * 0.01, (Math.random() * 0.25) * 0.01, (Math.random() * 0.5 - 0.25) * 0.01);
        removeTimer.reset();
    }

    public Particle(Vec3 position, Vec3 velocity) {
        this.position = position;
        delta = new Vec3(velocity.xCoord * 0.01, velocity.yCoord * 0.01, velocity.zCoord * 0.01);
        removeTimer.reset();
    }

    public void update() {
        Block block1 = PlayerParticles.getBlock(position.xCoord, position.yCoord, position.zCoord + delta.zCoord);
        if (!(block1 instanceof BlockAir || block1 instanceof BlockBush || block1 instanceof BlockLiquid))
            delta.zCoord *= -0.8;

        Block block2 = PlayerParticles.getBlock(position.xCoord, position.yCoord + delta.yCoord, position.zCoord);
        if (!(block2 instanceof BlockAir || block2 instanceof BlockBush || block2 instanceof BlockLiquid)) {
            delta.xCoord *= 0.999F;
            delta.zCoord *= 0.999F;

            delta.yCoord *= -0.6;
        }

        Block block3 = PlayerParticles.getBlock(position.xCoord + delta.xCoord, position.yCoord, position.zCoord);
        if (!(block3 instanceof BlockAir || block3 instanceof BlockBush || block3 instanceof BlockLiquid))
            delta.xCoord *= -0.8;

        updateWithoutPhysics();
    }

    public void updateWithoutPhysics() {
        position.xCoord += delta.xCoord;
        position.yCoord += delta.yCoord;
        position.zCoord += delta.zCoord;
        delta.xCoord /= 0.999998F;
        delta.yCoord -= 0.0000015;
        delta.zCoord /= 0.999998F;
    }
}
