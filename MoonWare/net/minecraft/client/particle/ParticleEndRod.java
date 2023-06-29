package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleEndRod extends ParticleSimpleAnimated
{
    public ParticleEndRod(World p_i46580_1_, double p_i46580_2_, double p_i46580_4_, double p_i46580_6_, double p_i46580_8_, double p_i46580_10_, double p_i46580_12_)
    {
        super(p_i46580_1_, p_i46580_2_, p_i46580_4_, p_i46580_6_, 176, 8, -5.0E-4F);
        motionX = p_i46580_8_;
        motionY = p_i46580_10_;
        motionZ = p_i46580_12_;
        particleScale *= 0.75F;
        particleMaxAge = 60 + rand.nextInt(12);
        setColorFade(15916745);
    }

    public void moveEntity(double x, double y, double z)
    {
        setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
        resetPositionToBB();
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleEndRod(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
