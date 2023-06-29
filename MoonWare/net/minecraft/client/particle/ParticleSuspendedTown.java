package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSuspendedTown extends Particle
{
    protected ParticleSuspendedTown(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double speedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, speedIn);
        float f = rand.nextFloat() * 0.1F + 0.2F;
        particleRed = f;
        particleGreen = f;
        particleBlue = f;
        setParticleTextureIndex(0);
        setSize(0.02F, 0.02F);
        particleScale *= rand.nextFloat() * 0.6F + 0.5F;
        motionX *= 0.019999999552965164D;
        motionY *= 0.019999999552965164D;
        motionZ *= 0.019999999552965164D;
        particleMaxAge = (int)(20.0D / (Math.random() * 0.8D + 0.2D));
    }

    public void moveEntity(double x, double y, double z)
    {
        setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
        resetPositionToBB();
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.99D;
        motionY *= 0.99D;
        motionZ *= 0.99D;

        if (particleMaxAge-- <= 0)
        {
            setExpired();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleSuspendedTown(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

    public static class HappyVillagerFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            Particle particle = new ParticleSuspendedTown(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setParticleTextureIndex(82);
            particle.setRBGColorF(1.0F, 1.0F, 1.0F);
            return particle;
        }
    }
}
