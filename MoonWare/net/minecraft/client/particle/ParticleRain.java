package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleRain extends Particle
{
    protected ParticleRain(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX *= 0.30000001192092896D;
        motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
        motionZ *= 0.30000001192092896D;
        particleRed = 1.0F;
        particleGreen = 1.0F;
        particleBlue = 1.0F;
        setParticleTextureIndex(19 + rand.nextInt(4));
        setSize(0.01F, 0.01F);
        particleGravity = 0.06F;
        particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        motionY -= particleGravity;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.9800000190734863D;
        motionY *= 0.9800000190734863D;
        motionZ *= 0.9800000190734863D;

        if (particleMaxAge-- <= 0)
        {
            setExpired();
        }

        if (isCollided)
        {
            if (Math.random() < 0.5D)
            {
                setExpired();
            }

            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
        }

        BlockPos blockpos = new BlockPos(posX, posY, posZ);
        IBlockState iblockstate = worldObj.getBlockState(blockpos);
        Material material = iblockstate.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0;

            if (iblockstate.getBlock() instanceof BlockLiquid)
            {
                d0 = 1.0F - BlockLiquid.getLiquidHeightPercent(iblockstate.getValue(BlockLiquid.LEVEL).intValue());
            }
            else
            {
                d0 = iblockstate.getBoundingBox(worldObj, blockpos).maxY;
            }

            double d1 = (double)MathHelper.floor(posY) + d0;

            if (posY < d1)
            {
                setExpired();
            }
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleRain(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
