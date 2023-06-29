package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleDragonBreath extends Particle
{
    private final float oSize;
    private boolean hasHitGround;

    protected ParticleDragonBreath(World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
    {
        super(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        motionX = xSpeed;
        motionY = ySpeed;
        motionZ = zSpeed;
        particleRed = MathHelper.nextFloat(rand, 0.7176471F, 0.8745098F);
        particleGreen = MathHelper.nextFloat(rand, 0.0F, 0.0F);
        particleBlue = MathHelper.nextFloat(rand, 0.8235294F, 0.9764706F);
        particleScale *= 0.75F;
        oSize = particleScale;
        particleMaxAge = (int)(20.0D / ((double) rand.nextFloat() * 0.8D + 0.2D));
        hasHitGround = false;
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
        else
        {
            setParticleTextureIndex(3 * particleAge / particleMaxAge + 5);

            if (isCollided)
            {
                motionY = 0.0D;
                hasHitGround = true;
            }

            if (hasHitGround)
            {
                motionY += 0.002D;
            }

            moveEntity(motionX, motionY, motionZ);

            if (posY == prevPosY)
            {
                motionX *= 1.1D;
                motionZ *= 1.1D;
            }

            motionX *= 0.9599999785423279D;
            motionZ *= 0.9599999785423279D;

            if (hasHitGround)
            {
                motionY *= 0.9599999785423279D;
            }
        }
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        particleScale = oSize * MathHelper.clamp(((float) particleAge + partialTicks) / (float) particleMaxAge * 32.0F, 0.0F, 1.0F);
        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleDragonBreath(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
