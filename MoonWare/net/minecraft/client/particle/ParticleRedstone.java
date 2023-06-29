package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleRedstone extends Particle
{
    float reddustParticleScale;

    protected ParticleRedstone(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1.0F, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }

    protected ParticleRedstone(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46350_8_, float p_i46350_9_, float p_i46350_10_, float p_i46350_11_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX *= 0.10000000149011612D;
        motionY *= 0.10000000149011612D;
        motionZ *= 0.10000000149011612D;

        if (p_i46350_9_ == 0.0F)
        {
            p_i46350_9_ = 1.0F;
        }

        float f = (float)Math.random() * 0.4F + 0.6F;
        particleRed = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_9_ * f;
        particleGreen = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_10_ * f;
        particleBlue = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_11_ * f;
        particleScale *= 0.75F;
        particleScale *= p_i46350_8_;
        reddustParticleScale = particleScale;
        particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        particleMaxAge = (int)((float) particleMaxAge * p_i46350_8_);
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float) particleAge + partialTicks) / (float) particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        particleScale = reddustParticleScale * f;
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
        moveEntity(motionX, motionY, motionZ);

        if (posY == prevPosY)
        {
            motionX *= 1.1D;
            motionZ *= 1.1D;
        }

        motionX *= 0.9599999785423279D;
        motionY *= 0.9599999785423279D;
        motionZ *= 0.9599999785423279D;

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
            return new ParticleRedstone(worldIn, xCoordIn, yCoordIn, zCoordIn, (float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
        }
    }
}
