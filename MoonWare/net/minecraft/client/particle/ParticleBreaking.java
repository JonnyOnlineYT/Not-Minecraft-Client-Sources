package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ParticleBreaking extends Particle
{
    protected ParticleBreaking(World worldIn, double posXIn, double posYIn, double posZIn, Item itemIn)
    {
        this(worldIn, posXIn, posYIn, posZIn, itemIn, 0);
    }

    protected ParticleBreaking(World worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, Item itemIn, int meta)
    {
        this(worldIn, posXIn, posYIn, posZIn, itemIn, meta);
        motionX *= 0.10000000149011612D;
        motionY *= 0.10000000149011612D;
        motionZ *= 0.10000000149011612D;
        motionX += xSpeedIn;
        motionY += ySpeedIn;
        motionZ += zSpeedIn;
    }

    protected ParticleBreaking(World worldIn, double posXIn, double posYIn, double posZIn, Item itemIn, int meta)
    {
        super(worldIn, posXIn, posYIn, posZIn, 0.0D, 0.0D, 0.0D);
        setParticleTexture(Minecraft.getRenderItem().getItemModelMesher().getParticleIcon(itemIn, meta));
        particleRed = 1.0F;
        particleGreen = 1.0F;
        particleBlue = 1.0F;
        particleGravity = Blocks.SNOW.blockParticleGravity;
        particleScale /= 2.0F;
    }

    /**
     * Retrieve what effect layer (what texture) the particle should be rendered with. 0 for the particle sprite sheet,
     * 1 for the main Texture atlas, and 3 for a custom texture
     */
    public int getFXLayer()
    {
        return 1;
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float) particleTextureIndexX + particleTextureJitterX / 4.0F) / 16.0F;
        float f1 = f + 0.015609375F;
        float f2 = ((float) particleTextureIndexY + particleTextureJitterY / 4.0F) / 16.0F;
        float f3 = f2 + 0.015609375F;
        float f4 = 0.1F * particleScale;

        if (particleTexture != null)
        {
            f = particleTexture.getInterpolatedU(particleTextureJitterX / 4.0F * 16.0F);
            f1 = particleTexture.getInterpolatedU((particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
            f2 = particleTexture.getInterpolatedV(particleTextureJitterY / 4.0F * 16.0F);
            f3 = particleTexture.getInterpolatedV((particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
        }

        float f5 = (float)(prevPosX + (posX - prevPosX) * (double)partialTicks - Particle.interpPosX);
        float f6 = (float)(prevPosY + (posY - prevPosY) * (double)partialTicks - Particle.interpPosY);
        float f7 = (float)(prevPosZ + (posZ - prevPosZ) * (double)partialTicks - Particle.interpPosZ);
        int i = getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        worldRendererIn.pos(f5 - rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 - rotationYZ * f4 - rotationXZ * f4).tex(f, f3).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        worldRendererIn.pos(f5 - rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 - rotationYZ * f4 + rotationXZ * f4).tex(f, f2).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        worldRendererIn.pos(f5 + rotationX * f4 + rotationXY * f4, f6 + rotationZ * f4, f7 + rotationYZ * f4 + rotationXZ * f4).tex(f1, f2).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        worldRendererIn.pos(f5 + rotationX * f4 - rotationXY * f4, f6 - rotationZ * f4, f7 + rotationYZ * f4 - rotationXZ * f4).tex(f1, f3).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            int i = p_178902_15_.length > 1 ? p_178902_15_[1] : 0;
            return new ParticleBreaking(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Item.getItemById(p_178902_15_[0]), i);
        }
    }

    public static class SlimeFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleBreaking(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.SLIME_BALL);
        }
    }

    public static class SnowballFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleBreaking(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.SNOWBALL);
        }
    }
}
