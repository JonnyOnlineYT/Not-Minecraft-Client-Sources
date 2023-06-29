package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleLava extends Particle
{
    private final float lavaParticleScale;

    protected ParticleLava(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX *= 0.800000011920929D;
        motionY *= 0.800000011920929D;
        motionZ *= 0.800000011920929D;
        motionY = rand.nextFloat() * 0.4F + 0.05F;
        particleRed = 1.0F;
        particleGreen = 1.0F;
        particleBlue = 1.0F;
        particleScale *= rand.nextFloat() * 2.0F + 0.2F;
        lavaParticleScale = particleScale;
        particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        setParticleTextureIndex(49);
    }

    public int getBrightnessForRender(float p_189214_1_)
    {
        int i = super.getBrightnessForRender(p_189214_1_);
        int j = 240;
        int k = i >> 16 & 255;
        return 240 | k << 16;
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float) particleAge + partialTicks) / (float) particleMaxAge;
        particleScale = lavaParticleScale * (1.0F - f * f);
        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
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

        float f = (float) particleAge / (float) particleMaxAge;

        if (rand.nextFloat() > f)
        {
            worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, motionX, motionY, motionZ);
        }

        motionY -= 0.03D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.9990000128746033D;
        motionY *= 0.9990000128746033D;
        motionZ *= 0.9990000128746033D;

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
            return new ParticleLava(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
