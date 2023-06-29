package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleNote extends Particle
{
    float noteParticleScale;

    protected ParticleNote(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46353_8_, double p_i46353_10_, double p_i46353_12_)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46353_8_, p_i46353_10_, p_i46353_12_, 2.0F);
    }

    protected ParticleNote(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1217_8_, double p_i1217_10_, double p_i1217_12_, float p_i1217_14_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX *= 0.009999999776482582D;
        motionY *= 0.009999999776482582D;
        motionZ *= 0.009999999776482582D;
        motionY += 0.2D;
        particleRed = MathHelper.sin(((float)p_i1217_8_ + 0.0F) * ((float)Math.PI * 2F)) * 0.65F + 0.35F;
        particleGreen = MathHelper.sin(((float)p_i1217_8_ + 0.33333334F) * ((float)Math.PI * 2F)) * 0.65F + 0.35F;
        particleBlue = MathHelper.sin(((float)p_i1217_8_ + 0.6666667F) * ((float)Math.PI * 2F)) * 0.65F + 0.35F;
        particleScale *= 0.75F;
        particleScale *= p_i1217_14_;
        noteParticleScale = particleScale;
        particleMaxAge = 6;
        setParticleTextureIndex(64);
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float) particleAge + partialTicks) / (float) particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        particleScale = noteParticleScale * f;
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

        if (posY == prevPosY)
        {
            motionX *= 1.1D;
            motionZ *= 1.1D;
        }

        motionX *= 0.6600000262260437D;
        motionY *= 0.6600000262260437D;
        motionZ *= 0.6600000262260437D;

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
            return new ParticleNote(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
