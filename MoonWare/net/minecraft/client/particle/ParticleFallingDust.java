package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFallingDust extends Particle
{
    float oSize;
    final float rotSpeed;

    protected ParticleFallingDust(World p_i47135_1_, double p_i47135_2_, double p_i47135_4_, double p_i47135_6_, float p_i47135_8_, float p_i47135_9_, float p_i47135_10_)
    {
        super(p_i47135_1_, p_i47135_2_, p_i47135_4_, p_i47135_6_, 0.0D, 0.0D, 0.0D);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        particleRed = p_i47135_8_;
        particleGreen = p_i47135_9_;
        particleBlue = p_i47135_10_;
        float f = 0.9F;
        particleScale *= 0.75F;
        particleScale *= 0.9F;
        oSize = particleScale;
        particleMaxAge = (int)(32.0D / (Math.random() * 0.8D + 0.2D));
        particleMaxAge = (int)((float) particleMaxAge * 0.9F);
        rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
        particleAngle = (float)Math.random() * ((float)Math.PI * 2F);
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

        prevParticleAngle = particleAngle;
        particleAngle += (float)Math.PI * rotSpeed * 2.0F;

        if (isCollided)
        {
            prevParticleAngle = particleAngle = 0.0F;
        }

        setParticleTextureIndex(7 - particleAge * 8 / particleMaxAge);
        moveEntity(motionX, motionY, motionZ);
        motionY -= 0.003000000026077032D;
        motionY = Math.max(motionY, -0.14000000059604645D);
    }

    public static class Factory implements IParticleFactory
    {
        @Nullable
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            IBlockState iblockstate = Block.getStateById(p_178902_15_[0]);

            if (iblockstate.getBlock() != Blocks.AIR && iblockstate.getRenderType() == EnumBlockRenderType.INVISIBLE)
            {
                return null;
            }
            else
            {
                int i = Minecraft.getBlockColors().getColor(iblockstate, worldIn, new BlockPos(xCoordIn, yCoordIn, zCoordIn));

                if (iblockstate.getBlock() instanceof BlockFalling)
                {
                    i = ((BlockFalling)iblockstate.getBlock()).getDustColor(iblockstate);
                }

                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;
                return new ParticleFallingDust(worldIn, xCoordIn, yCoordIn, zCoordIn, f, f1, f2);
            }
        }
    }
}
