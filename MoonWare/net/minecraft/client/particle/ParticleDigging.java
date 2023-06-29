package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleDigging extends Particle
{
    private final IBlockState sourceState;
    private BlockPos sourcePos;

    protected ParticleDigging(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        sourceState = state;
        setParticleTexture(Minecraft.getBlockRenderDispatcher().getBlockModelShapes().getTexture(state));
        particleGravity = state.getBlock().blockParticleGravity;
        particleRed = 0.6F;
        particleGreen = 0.6F;
        particleBlue = 0.6F;
        particleScale /= 2.0F;
    }

    /**
     * Sets the position of the block that this particle came from. Used for calculating texture and color multiplier.
     */
    public ParticleDigging setBlockPos(BlockPos pos)
    {
        sourcePos = pos;

        if (sourceState.getBlock() == Blocks.GRASS)
        {
            return this;
        }
        else
        {
            multiplyColor(pos);
            return this;
        }
    }

    public ParticleDigging init()
    {
        sourcePos = new BlockPos(posX, posY, posZ);
        Block block = sourceState.getBlock();

        if (block == Blocks.GRASS)
        {
            return this;
        }
        else
        {
            multiplyColor(sourcePos);
            return this;
        }
    }

    protected void multiplyColor(@Nullable BlockPos p_187154_1_)
    {
        int i = Minecraft.getBlockColors().colorMultiplier(sourceState, worldObj, p_187154_1_, 0);
        particleRed *= (float)(i >> 16 & 255) / 255.0F;
        particleGreen *= (float)(i >> 8 & 255) / 255.0F;
        particleBlue *= (float)(i & 255) / 255.0F;
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

    public int getBrightnessForRender(float p_189214_1_)
    {
        int i = super.getBrightnessForRender(p_189214_1_);
        int j = 0;

        if (worldObj.isBlockLoaded(sourcePos))
        {
            j = worldObj.getCombinedLight(sourcePos, 0);
        }

        return i == 0 ? j : i;
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return (new ParticleDigging(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Block.getStateById(p_178902_15_[0]))).init();
        }
    }
}
