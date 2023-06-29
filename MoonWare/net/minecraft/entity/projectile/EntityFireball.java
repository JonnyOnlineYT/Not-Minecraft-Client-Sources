package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityFireball extends Entity
{
    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    public EntityFireball(World worldIn)
    {
        super(worldIn);
        setSize(1.0F, 1.0F);
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

    public EntityFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        setSize(1.0F, 1.0F);
        setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
        setPosition(x, y, z);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        accelerationX = accelX / d0 * 0.1D;
        accelerationY = accelY / d0 * 0.1D;
        accelerationZ = accelZ / d0 * 0.1D;
    }

    public EntityFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        shootingEntity = shooter;
        setSize(1.0F, 1.0F);
        setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        setPosition(posX, posY, posZ);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        accelX = accelX + rand.nextGaussian() * 0.4D;
        accelY = accelY + rand.nextGaussian() * 0.4D;
        accelZ = accelZ + rand.nextGaussian() * 0.4D;
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        accelerationX = accelX / d0 * 0.1D;
        accelerationY = accelY / d0 * 0.1D;
        accelerationZ = accelZ / d0 * 0.1D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (world.isRemote || (shootingEntity == null || !shootingEntity.isDead) && world.isBlockLoaded(new BlockPos(this)))
        {
            super.onUpdate();

            if (isFireballFiery())
            {
                setFire(1);
            }

            ++ticksInAir;
            RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, ticksInAir >= 25, shootingEntity);

            if (raytraceresult != null)
            {
                onImpact(raytraceresult);
            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = getMotionFactor();

            if (isInWater())
            {
                for (int i = 0; i < 4; ++i)
                {
                    float f1 = 0.25F;
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25D, posY - motionY * 0.25D, posZ - motionZ * 0.25D, motionX, motionY, motionZ);
                }

                f = 0.8F;
            }

            motionX += accelerationX;
            motionY += accelerationY;
            motionZ += accelerationZ;
            motionX *= f;
            motionY *= f;
            motionZ *= f;
            world.spawnParticle(getParticleType(), posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D);
            setPosition(posX, posY, posZ);
        }
        else
        {
            setDead();
        }
    }

    protected boolean isFireballFiery()
    {
        return true;
    }

    protected EnumParticleTypes getParticleType()
    {
        return EnumParticleTypes.SMOKE_NORMAL;
    }

    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */
    protected float getMotionFactor()
    {
        return 0.95F;
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected abstract void onImpact(RayTraceResult result);

    public static void registerFixesFireball(DataFixer fixer, String name)
    {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setTag("direction", newDoubleNBTList(motionX, motionY, motionZ));
        compound.setTag("power", newDoubleNBTList(accelerationX, accelerationY, accelerationZ));
        compound.setInteger("life", ticksAlive);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        if (compound.hasKey("power", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("power", 6);

            if (nbttaglist.tagCount() == 3)
            {
                accelerationX = nbttaglist.getDoubleAt(0);
                accelerationY = nbttaglist.getDoubleAt(1);
                accelerationZ = nbttaglist.getDoubleAt(2);
            }
        }

        ticksAlive = compound.getInteger("life");

        if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3)
        {
            NBTTagList nbttaglist1 = compound.getTagList("direction", 6);
            motionX = nbttaglist1.getDoubleAt(0);
            motionY = nbttaglist1.getDoubleAt(1);
            motionZ = nbttaglist1.getDoubleAt(2);
        }
        else
        {
            setDead();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    public float getCollisionBorderSize()
    {
        return 1.0F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            setBeenAttacked();

            if (source.getEntity() != null)
            {
                Vec3d vec3d = source.getEntity().getLookVec();

                if (vec3d != null)
                {
                    motionX = vec3d.xCoord;
                    motionY = vec3d.yCoord;
                    motionZ = vec3d.zCoord;
                    accelerationX = motionX * 0.1D;
                    accelerationY = motionY * 0.1D;
                    accelerationZ = motionZ * 0.1D;
                }

                if (source.getEntity() instanceof EntityLivingBase)
                {
                    shootingEntity = (EntityLivingBase)source.getEntity();
                }

                return true;
            }
            else
            {
                return false;
            }
        }
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
}
