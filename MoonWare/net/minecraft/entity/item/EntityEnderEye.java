package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity
{
    /** 'x' location the eye should float towards. */
    private double targetX;

    /** 'y' location the eye should float towards. */
    private double targetY;

    /** 'z' location the eye should float towards. */
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;

    public EntityEnderEye(World worldIn)
    {
        super(worldIn);
        setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    public EntityEnderEye(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        despawnTimer = 0;
        setSize(0.25F, 0.25F);
        setPosition(x, y, z);
    }

    public void moveTowards(BlockPos pos)
    {
        double d0 = pos.getX();
        int i = pos.getY();
        double d1 = pos.getZ();
        double d2 = d0 - posX;
        double d3 = d1 - posZ;
        float f = MathHelper.sqrt(d2 * d2 + d3 * d3);

        if (f > 12.0F)
        {
            targetX = posX + d2 / (double)f * 12.0D;
            targetZ = posZ + d3 / (double)f * 12.0D;
            targetY = posY + 8.0D;
        }
        else
        {
            targetX = d0;
            targetY = i;
            targetZ = d1;
        }

        despawnTimer = 0;
        shatterOrDrop = rand.nextInt(5) > 0;
    }

    /**
     * Updates the velocity of the entity to a new value.
     */
    public void setVelocity(double x, double y, double z)
    {
        motionX = x;
        motionY = y;
        motionZ = z;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            rotationPitch = (float)(MathHelper.atan2(y, f) * (180D / Math.PI));
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        super.onUpdate();
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));

        for (rotationPitch = (float)(MathHelper.atan2(motionY, f) * (180D / Math.PI)); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F)
        {
        }

        while (rotationPitch - prevRotationPitch >= 180.0F)
        {
            prevRotationPitch += 360.0F;
        }

        while (rotationYaw - prevRotationYaw < -180.0F)
        {
            prevRotationYaw -= 360.0F;
        }

        while (rotationYaw - prevRotationYaw >= 180.0F)
        {
            prevRotationYaw += 360.0F;
        }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;

        if (!world.isRemote)
        {
            double d0 = targetX - posX;
            double d1 = targetZ - posZ;
            float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
            float f2 = (float)MathHelper.atan2(d1, d0);
            double d2 = (double)f + (double)(f1 - f) * 0.0025D;

            if (f1 < 1.0F)
            {
                d2 *= 0.8D;
                motionY *= 0.8D;
            }

            motionX = Math.cos(f2) * d2;
            motionZ = Math.sin(f2) * d2;

            if (posY < targetY)
            {
                motionY += (1.0D - motionY) * 0.014999999664723873D;
            }
            else
            {
                motionY += (-1.0D - motionY) * 0.014999999664723873D;
            }
        }

        float f3 = 0.25F;

        if (isInWater())
        {
            for (int i = 0; i < 4; ++i)
            {
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25D, posY - motionY * 0.25D, posZ - motionZ * 0.25D, motionX, motionY, motionZ);
            }
        }
        else
        {
            world.spawnParticle(EnumParticleTypes.PORTAL, posX - motionX * 0.25D + rand.nextDouble() * 0.6D - 0.3D, posY - motionY * 0.25D - 0.5D, posZ - motionZ * 0.25D + rand.nextDouble() * 0.6D - 0.3D, motionX, motionY, motionZ);
        }

        if (!world.isRemote)
        {
            setPosition(posX, posY, posZ);
            ++despawnTimer;

            if (despawnTimer > 80 && !world.isRemote)
            {
                playSound(SoundEvents.field_193777_bb, 1.0F, 1.0F);
                setDead();

                if (shatterOrDrop)
                {
                    world.spawnEntityInWorld(new EntityItem(world, posX, posY, posZ, new ItemStack(Items.ENDER_EYE)));
                }
                else
                {
                    world.playEvent(2003, new BlockPos(this), 0);
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    public int getBrightnessForRender()
    {
        return 15728880;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
}
