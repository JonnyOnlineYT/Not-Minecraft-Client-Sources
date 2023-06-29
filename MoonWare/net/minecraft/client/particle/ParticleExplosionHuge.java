package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleExplosionHuge extends Particle
{
    private int timeSinceStart;

    /** the maximum time for the explosion */
    private final int maximumTime = 8;

    protected ParticleExplosionHuge(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1214_8_, double p_i1214_10_, double p_i1214_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
    }

    public void onUpdate()
    {
        for (int i = 0; i < 6; ++i)
        {
            double d0 = posX + (rand.nextDouble() - rand.nextDouble()) * 4.0D;
            double d1 = posY + (rand.nextDouble() - rand.nextDouble()) * 4.0D;
            double d2 = posZ + (rand.nextDouble() - rand.nextDouble()) * 4.0D;
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, (float) timeSinceStart / (float) maximumTime, 0.0D, 0.0D);
        }

        ++timeSinceStart;

        if (timeSinceStart == maximumTime)
        {
            setExpired();
        }
    }

    /**
     * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite sheet,
     * 1 for the main Texture atlas, and 3 for a custom texture
     */
    public int getFXLayer()
    {
        return 1;
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleExplosionHuge(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
