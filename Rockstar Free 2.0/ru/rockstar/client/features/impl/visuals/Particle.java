package ru.rockstar.client.features.impl.visuals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.rockstar.api.utils.world.TimerHelper;

public class Particle {

    private final TimerHelper removeTimer = new TimerHelper();

    public final Vec3d position;
    private final Vec3d delta;

    public Particle(final Vec3d position) {
        this.position = position;
        this.delta = new Vec3d((Math.random() * 0.5 - 0.25) * 0.01, (Math.random() * 0.25) * 0.01, (Math.random() * 0.5 - 0.25) * 0.01);
        this.removeTimer.reset();
    }

    public Particle(final Vec3d position, final Vec3d velocity) {
        this.position = position;
        this.delta = new Vec3d(velocity.xCoord * 0.01, velocity.yCoord * 0.01, velocity.zCoord * 0.01);
        this.removeTimer.reset();
    }
    
    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return Minecraft.getMinecraft().world.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }

    public void update() {
        final Block block1 = getBlock(this.position.xCoord, this.position.yCoord, this.position.zCoord + this.delta.zCoord);
        if (!(block1 instanceof BlockAir || block1 instanceof BlockBush || block1 instanceof BlockLiquid))
            this.delta.zCoord *= -0.8;

        final Block block2 = getBlock(this.position.xCoord, this.position.yCoord + this.delta.yCoord, this.position.zCoord);
        if (!(block2 instanceof BlockAir || block2 instanceof BlockBush || block2 instanceof BlockLiquid)) {
            this.delta.xCoord *= 0.999F;
            this.delta.zCoord *= 0.999F;

            this.delta.yCoord *= -0.6;
        }

        final Block block3 = getBlock(this.position.xCoord + this.delta.xCoord, this.position.yCoord, this.position.zCoord);
        if (!(block3 instanceof BlockAir || block3 instanceof BlockBush || block3 instanceof BlockLiquid))
            this.delta.xCoord *= -0.8;

        this.updateWithoutPhysics();
    }

    public void updateWithoutPhysics() {
        this.position.xCoord += this.delta.xCoord;
        this.position.yCoord += this.delta.yCoord;
        this.position.zCoord += this.delta.zCoord;
        this.delta.xCoord /= 0.999998F;
        this.delta.yCoord -= 0.0000015;
        this.delta.zCoord /= 0.999998F;
    }

	public Vec3d getPosition() {
		return delta;
	}
}