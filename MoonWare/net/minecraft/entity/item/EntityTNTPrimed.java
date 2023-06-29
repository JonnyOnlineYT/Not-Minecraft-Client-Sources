package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityTNTPrimed extends Entity
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;

    /** How long the fuse is */
    private int fuse;

    public EntityTNTPrimed(World worldIn)
    {
        super(worldIn);
        fuse = 80;
        preventEntitySpawning = true;
        isImmuneToFire = true;
        setSize(0.98F, 0.98F);
    }

    public EntityTNTPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter)
    {
        this(worldIn);
        setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        motionX = -((float)Math.sin(f)) * 0.02F;
        motionY = 0.20000000298023224D;
        motionZ = -((float)Math.cos(f)) * 0.02F;
        setFuse(80);
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        tntPlacedBy = igniter;
    }

    protected void entityInit()
    {
        dataManager.register(FUSE, Integer.valueOf(80));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (!hasNoGravity())
        {
            motionY -= 0.03999999910593033D;
        }

        moveEntity(MoverType.SELF, motionX, motionY, motionZ);
        motionX *= 0.9800000190734863D;
        motionY *= 0.9800000190734863D;
        motionZ *= 0.9800000190734863D;

        if (onGround)
        {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
            motionY *= -0.5D;
        }

        --fuse;

        if (fuse <= 0)
        {
            setDead();

            if (!world.isRemote)
            {
                explode();
            }
        }
        else
        {
            handleWaterMovement();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode()
    {
        float f = 4.0F;
        world.createExplosion(this, posX, posY + (double)(height / 16.0F), posZ, 4.0F, true);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Fuse", (short) getFuse());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        setFuse(compound.getShort("Fuse"));
    }

    @Nullable

    /**
     * returns null or the entityliving it was placed or ignited by
     */
    public EntityLivingBase getTntPlacedBy()
    {
        return tntPlacedBy;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void setFuse(int fuseIn)
    {
        dataManager.set(FUSE, Integer.valueOf(fuseIn));
        fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FUSE.equals(key))
        {
            fuse = getFuseDataManager();
        }
    }

    /**
     * Gets the fuse from the data manager
     */
    public int getFuseDataManager()
    {
        return dataManager.get(FUSE).intValue();
    }

    public int getFuse()
    {
        return fuse;
    }
}
