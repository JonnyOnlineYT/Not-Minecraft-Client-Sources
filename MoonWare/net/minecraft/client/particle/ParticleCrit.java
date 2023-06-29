package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleCrit extends Particle
{
    float oSize;

    protected ParticleCrit(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46284_8_, double p_i46284_10_, double p_i46284_12_)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46284_8_, p_i46284_10_, p_i46284_12_, 1.0F);
    }

    protected ParticleCrit(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46285_8_, double p_i46285_10_, double p_i46285_12_, float p_i46285_14_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX *= 0.10000000149011612D;
        motionY *= 0.10000000149011612D;
        motionZ *= 0.10000000149011612D;
        motionX += p_i46285_8_ * 0.4D;
        motionY += p_i46285_10_ * 0.4D;
        motionZ += p_i46285_12_ * 0.4D;
        float f = (float)(Math.random() * 0.30000001192092896D + 0.6000000238418579D);
        particleRed = f;
        particleGreen = f;
        particleBlue = f;
        particleScale *= 0.75F;
        particleScale *= p_i46285_14_;
        oSize = particleScale;
        particleMaxAge = (int)(6.0D / (Math.random() * 0.8D + 0.6D));
        particleMaxAge = (int)((float) particleMaxAge * p_i46285_14_);
        setParticleTextureIndex(65);
        onUpdate();
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float) particleAge + partialTicks) / (float) particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        particleScale = oSize * f;
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

        moveEntity(motionX, motionY, motionZ);
        particleGreen = (float)((double) particleGreen * 0.96D);
        particleBlue = (float)((double) particleBlue * 0.9D);
        motionX *= 0.699999988079071D;
        motionY *= 0.699999988079071D;
        motionZ *= 0.699999988079071D;
        motionY -= 0.019999999552965164D;

        if (isCollided)
        {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
        }
    }

    public static class DamageIndicatorFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            Particle particle = new ParticleCrit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn + 1.0D, zSpeedIn, 1.0F);
            particle.setMaxAge(20);
            particle.setParticleTextureIndex(67);
            return particle;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleCrit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

    public static class MagicFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            Particle particle = new ParticleCrit(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setRBGColorF(particle.getRedColorF() * 0.3F, particle.getGreenColorF() * 0.8F, particle.getBlueColorF());
            particle.nextTextureIndexX();
            return particle;
        }
    }
}
