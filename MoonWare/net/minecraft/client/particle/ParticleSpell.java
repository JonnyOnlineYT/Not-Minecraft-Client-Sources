package net.minecraft.client.particle;

import java.util.Random;
import net.minecraft.world.World;

public class ParticleSpell extends Particle
{
    private static final Random RANDOM = new Random();

    /** Base spell texture index */
    private int baseSpellTextureIndex = 128;

    protected ParticleSpell(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1229_8_, double ySpeed, double p_i1229_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5D - RANDOM.nextDouble(), ySpeed, 0.5D - RANDOM.nextDouble());
        motionY *= 0.20000000298023224D;

        if (p_i1229_8_ == 0.0D && p_i1229_12_ == 0.0D)
        {
            motionX *= 0.10000000149011612D;
            motionZ *= 0.10000000149011612D;
        }

        particleScale *= 0.75F;
        particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
    }

    public boolean isTransparent()
    {
        return true;
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

        setParticleTextureIndex(baseSpellTextureIndex + (7 - particleAge * 8 / particleMaxAge));
        motionY += 0.004D;
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

    /**
     * Sets the base spell texture index
     */
    public void setBaseSpellTextureIndex(int baseSpellTextureIndexIn)
    {
        baseSpellTextureIndex = baseSpellTextureIndexIn;
    }

    public static class AmbientMobFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setAlphaF(0.15F);
            particle.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return particle;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

    public static class InstantFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((ParticleSpell)particle).setBaseSpellTextureIndex(144);
            return particle;
        }
    }

    public static class MobFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
            return particle;
        }
    }

    public static class WitchFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((ParticleSpell)particle).setBaseSpellTextureIndex(144);
            float f = worldIn.rand.nextFloat() * 0.5F + 0.35F;
            particle.setRBGColorF(1.0F * f, 0.0F * f, 1.0F * f);
            return particle;
        }
    }
}
