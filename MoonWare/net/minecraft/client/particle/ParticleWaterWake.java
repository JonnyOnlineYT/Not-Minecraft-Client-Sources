package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleWaterWake extends Particle
{
    protected ParticleWaterWake(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i45073_8_, double p_i45073_10_, double p_i45073_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX *= 0.30000001192092896D;
        motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
        motionZ *= 0.30000001192092896D;
        particleRed = 1.0F;
        particleGreen = 1.0F;
        particleBlue = 1.0F;
        setParticleTextureIndex(19);
        setSize(0.01F, 0.01F);
        particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        particleGravity = 0.0F;
        motionX = p_i45073_8_;
        motionY = p_i45073_10_;
        motionZ = p_i45073_12_;
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= particleGravity;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.9800000190734863D;
        motionY *= 0.9800000190734863D;
        motionZ *= 0.9800000190734863D;
        int i = 60 - particleMaxAge;
        float f = (float)i * 0.001F;
        setSize(f, f);
        setParticleTextureIndex(19 + i % 4);

        if (particleMaxAge-- <= 0)
        {
            setExpired();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleWaterWake(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
