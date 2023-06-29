package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleExplosion extends Particle
{
    protected ParticleExplosion(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        motionX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
        motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
        motionZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
        float f = rand.nextFloat() * 0.3F + 0.7F;
        particleRed = f;
        particleGreen = f;
        particleBlue = f;
        particleScale = rand.nextFloat() * rand.nextFloat() * 6.0F + 1.0F;
        particleMaxAge = (int)(16.0D / ((double) rand.nextFloat() * 0.8D + 0.2D)) + 2;
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge)
        {
            setExpired();
        }

        setParticleTextureIndex(7 - particleAge * 8 / particleMaxAge);
        motionY += 0.004D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.8999999761581421D;
        motionY *= 0.8999999761581421D;
        motionZ *= 0.8999999761581421D;

        if (isCollided)
        {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleExplosion(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
