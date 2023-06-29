package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleSnowShovel extends Particle
{
    float snowDigParticleScale;

    protected ParticleSnowShovel(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 1.0F);
    }

    protected ParticleSnowShovel(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float p_i1228_14_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        motionX *= 0.10000000149011612D;
        motionY *= 0.10000000149011612D;
        motionZ *= 0.10000000149011612D;
        motionX += xSpeedIn;
        motionY += ySpeedIn;
        motionZ += zSpeedIn;
        float f = 1.0F - (float)(Math.random() * 0.30000001192092896D);
        particleRed = f;
        particleGreen = f;
        particleBlue = f;
        particleScale *= 0.75F;
        particleScale *= p_i1228_14_;
        snowDigParticleScale = particleScale;
        particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        particleMaxAge = (int)((float) particleMaxAge * p_i1228_14_);
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float) particleAge + partialTicks) / (float) particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        particleScale = snowDigParticleScale * f;
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

        setParticleTextureIndex(7 - particleAge * 8 / particleMaxAge);
        motionY -= 0.03D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.9900000095367432D;
        motionY *= 0.9900000095367432D;
        motionZ *= 0.9900000095367432D;

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
            return new ParticleSnowShovel(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
