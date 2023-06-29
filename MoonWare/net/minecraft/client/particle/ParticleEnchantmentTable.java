package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleEnchantmentTable extends Particle
{
    private final float oSize;
    private final double coordX;
    private final double coordY;
    private final double coordZ;

    protected ParticleEnchantmentTable(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        motionX = xSpeedIn;
        motionY = ySpeedIn;
        motionZ = zSpeedIn;
        coordX = xCoordIn;
        coordY = yCoordIn;
        coordZ = zCoordIn;
        prevPosX = xCoordIn + xSpeedIn;
        prevPosY = yCoordIn + ySpeedIn;
        prevPosZ = zCoordIn + zSpeedIn;
        posX = prevPosX;
        posY = prevPosY;
        posZ = prevPosZ;
        float f = rand.nextFloat() * 0.6F + 0.4F;
        particleScale = rand.nextFloat() * 0.5F + 0.2F;
        oSize = particleScale;
        particleRed = 0.9F * f;
        particleGreen = 0.9F * f;
        particleBlue = f;
        particleMaxAge = (int)(Math.random() * 10.0D) + 30;
        setParticleTextureIndex((int)(Math.random() * 26.0D + 1.0D + 224.0D));
    }

    public void moveEntity(double x, double y, double z)
    {
        setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
        resetPositionToBB();
    }

    public int getBrightnessForRender(float p_189214_1_)
    {
        int i = super.getBrightnessForRender(p_189214_1_);
        float f = (float) particleAge / (float) particleMaxAge;
        f = f * f;
        f = f * f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k = k + (int)(f * 15.0F * 16.0F);

        if (k > 240)
        {
            k = 240;
        }

        return j | k << 16;
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        float f = (float) particleAge / (float) particleMaxAge;
        f = 1.0F - f;
        float f1 = 1.0F - f;
        f1 = f1 * f1;
        f1 = f1 * f1;
        posX = coordX + motionX * (double)f;
        posY = coordY + motionY * (double)f - (double)(f1 * 1.2F);
        posZ = coordZ + motionZ * (double)f;

        if (particleAge++ >= particleMaxAge)
        {
            setExpired();
        }
    }

    public static class EnchantmentTable implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleEnchantmentTable(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
