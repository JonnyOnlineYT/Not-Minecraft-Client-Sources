package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleDrip extends Particle
{
    /** the material type for dropped items/blocks */
    private final Material materialType;

    /** The height of the current bob */
    private int bobTimer;

    protected ParticleDrip(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, Material p_i1203_8_)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;

        if (p_i1203_8_ == Material.WATER)
        {
            particleRed = 0.0F;
            particleGreen = 0.0F;
            particleBlue = 1.0F;
        }
        else
        {
            particleRed = 1.0F;
            particleGreen = 0.0F;
            particleBlue = 0.0F;
        }

        setParticleTextureIndex(113);
        setSize(0.01F, 0.01F);
        particleGravity = 0.06F;
        materialType = p_i1203_8_;
        bobTimer = 40;
        particleMaxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
    }

    public int getBrightnessForRender(float p_189214_1_)
    {
        return materialType == Material.WATER ? super.getBrightnessForRender(p_189214_1_) : 257;
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (materialType == Material.WATER)
        {
            particleRed = 0.2F;
            particleGreen = 0.3F;
            particleBlue = 1.0F;
        }
        else
        {
            particleRed = 1.0F;
            particleGreen = 16.0F / (float)(40 - bobTimer + 16);
            particleBlue = 4.0F / (float)(40 - bobTimer + 8);
        }

        motionY -= particleGravity;

        if (bobTimer-- > 0)
        {
            motionX *= 0.02D;
            motionY *= 0.02D;
            motionZ *= 0.02D;
            setParticleTextureIndex(113);
        }
        else
        {
            setParticleTextureIndex(112);
        }

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
            if (materialType == Material.WATER)
            {
                setExpired();
                worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                setParticleTextureIndex(114);
            }

            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
        }

        BlockPos blockpos = new BlockPos(posX, posY, posZ);
        IBlockState iblockstate = worldObj.getBlockState(blockpos);
        Material material = iblockstate.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = 0.0D;

            if (iblockstate.getBlock() instanceof BlockLiquid)
            {
                d0 = BlockLiquid.getLiquidHeightPercent(iblockstate.getValue(BlockLiquid.LEVEL).intValue());
            }

            double d1 = (double)(MathHelper.floor(posY) + 1) - d0;

            if (posY < d1)
            {
                setExpired();
            }
        }
    }

    public static class LavaFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleDrip(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.LAVA);
        }
    }

    public static class WaterFactory implements IParticleFactory
    {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleDrip(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.WATER);
        }
    }
}
