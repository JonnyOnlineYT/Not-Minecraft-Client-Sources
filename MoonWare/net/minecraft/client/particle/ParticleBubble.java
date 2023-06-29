package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleBubble extends Particle
{
    protected ParticleBubble(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        particleRed = 1.0F;
        particleGreen = 1.0F;
        particleBlue = 1.0F;
        setParticleTextureIndex(32);
        setSize(0.02F, 0.02F);
        particleScale *= rand.nextFloat() * 0.6F + 0.2F;
        motionX = xSpeedIn * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        motionY = ySpeedIn * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        motionZ = zSpeedIn * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
        particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY += 0.002D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.8500000238418579D;
        motionY *= 0.8500000238418579D;
        motionZ *= 0.8500000238418579D;

        if (worldObj.getBlockState(new BlockPos(posX, posY, posZ)).getMaterial() != Material.WATER)
        {
            setExpired();
        }

        if (particleMaxAge-- <= 0)
        {
            setExpired();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleBubble(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
