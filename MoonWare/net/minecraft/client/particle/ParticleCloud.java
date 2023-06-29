package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleCloud extends Particle
{
    float oSize;

    protected ParticleCloud(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1221_8_, double p_i1221_10_, double p_i1221_12_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        float f = 2.5F;
        motionX *= 0.10000000149011612D;
        motionY *= 0.10000000149011612D;
        motionZ *= 0.10000000149011612D;
        motionX += p_i1221_8_;
        motionY += p_i1221_10_;
        motionZ += p_i1221_12_;
        float f1 = 1.0F - (float)(Math.random() * 0.30000001192092896D);
        particleRed = f1;
        particleGreen = f1;
        particleBlue = f1;
        particleScale *= 0.75F;
        particleScale *= 2.5F;
        oSize = particleScale;
        particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.3D));
        particleMaxAge = (int)((float) particleMaxAge * 2.5F);
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

        setParticleTextureIndex(7 - particleAge * 8 / particleMaxAge);
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.9599999785423279D;
        motionY *= 0.9599999785423279D;
        motionZ *= 0.9599999785423279D;
        EntityPlayer entityplayer = worldObj.getClosestPlayer(posX, posY, posZ, 2.0D, false);

        if (entityplayer != null)
        {
            AxisAlignedBB axisalignedbb = entityplayer.getEntityBoundingBox();

            if (posY > axisalignedbb.minY)
            {
                posY += (axisalignedbb.minY - posY) * 0.2D;
                motionY += (entityplayer.motionY - motionY) * 0.2D;
                setPosition(posX, posY, posZ);
            }
        }

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
            return new ParticleCloud(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
