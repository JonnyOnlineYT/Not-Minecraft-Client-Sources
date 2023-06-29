package net.minecraft.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying extends EntityLiving
{
    public EntityFlying(World worldIn)
    {
        super(worldIn);
    }

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
    }

    public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        if (isInWater())
        {
            moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
            moveEntity(MoverType.SELF, motionX, motionY, motionZ);
            motionX *= 0.800000011920929D;
            motionY *= 0.800000011920929D;
            motionZ *= 0.800000011920929D;
        }
        else if (isInLava())
        {
            moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
            moveEntity(MoverType.SELF, motionX, motionY, motionZ);
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }
        else
        {
            float f = 0.91F;

            if (onGround)
            {
                f = world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(getEntityBoundingBox().minY) - 1, MathHelper.floor(posZ))).getBlock().slipperiness * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (onGround)
            {
                f = world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(getEntityBoundingBox().minY) - 1, MathHelper.floor(posZ))).getBlock().slipperiness * 0.91F;
            }

            moveEntity(MoverType.SELF, motionX, motionY, motionZ);
            motionX *= f;
            motionY *= f;
            motionZ *= f;
        }

        prevLimbSwingAmount = limbSwingAmount;
        double d1 = posX - prevPosX;
        double d0 = posZ - prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        limbSwingAmount += (f2 - limbSwingAmount) * 0.4F;
        limbSwing += limbSwingAmount;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return false;
    }
}
