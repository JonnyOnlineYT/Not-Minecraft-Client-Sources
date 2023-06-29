package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity
{
    private static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private static final DataParameter<Integer> field_191512_b = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.VARINT);

    /** The age of the firework in ticks. */
    private int fireworkAge;

    /**
     * The lifetime of the firework in ticks. When the age reaches the lifetime the firework explodes.
     */
    private int lifetime;
    private EntityLivingBase field_191513_e;

    public EntityFireworkRocket(World worldIn)
    {
        super(worldIn);
        setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
        dataManager.register(FIREWORK_ITEM, ItemStack.EMPTY);
        dataManager.register(field_191512_b, Integer.valueOf(0));
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 4096.0D && !func_191511_j();
    }

    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        return super.isInRangeToRender3d(x, y, z) && !func_191511_j();
    }

    public EntityFireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem)
    {
        super(worldIn);
        fireworkAge = 0;
        setSize(0.25F, 0.25F);
        setPosition(x, y, z);
        int i = 1;

        if (!givenItem.isEmpty() && givenItem.hasTagCompound())
        {
            dataManager.set(FIREWORK_ITEM, givenItem.copy());
            NBTTagCompound nbttagcompound = givenItem.getTagCompound();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
            i += nbttagcompound1.getByte("Flight");
        }

        motionX = rand.nextGaussian() * 0.001D;
        motionZ = rand.nextGaussian() * 0.001D;
        motionY = 0.05D;
        lifetime = 10 * i + rand.nextInt(6) + rand.nextInt(7);
    }

    public EntityFireworkRocket(World p_i47367_1_, ItemStack p_i47367_2_, EntityLivingBase p_i47367_3_)
    {
        this(p_i47367_1_, p_i47367_3_.posX, p_i47367_3_.posY, p_i47367_3_.posZ, p_i47367_2_);
        dataManager.set(field_191512_b, Integer.valueOf(p_i47367_3_.getEntityId()));
        field_191513_e = p_i47367_3_;
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

        if (func_191511_j())
        {
            if (field_191513_e == null)
            {
                Entity entity = world.getEntityByID(dataManager.get(field_191512_b).intValue());

                if (entity instanceof EntityLivingBase)
                {
                    field_191513_e = (EntityLivingBase)entity;
                }
            }

            if (field_191513_e != null)
            {
                if (field_191513_e.isElytraFlying())
                {
                    Vec3d vec3d = field_191513_e.getLookVec();
                    double d0 = 1.5D;
                    double d1 = 0.1D;
                    field_191513_e.motionX += vec3d.xCoord * 0.1D + (vec3d.xCoord * 1.5D - field_191513_e.motionX) * 0.5D;
                    field_191513_e.motionY += vec3d.yCoord * 0.1D + (vec3d.yCoord * 1.5D - field_191513_e.motionY) * 0.5D;
                    field_191513_e.motionZ += vec3d.zCoord * 0.1D + (vec3d.zCoord * 1.5D - field_191513_e.motionZ) * 0.5D;
                }

                setPosition(field_191513_e.posX, field_191513_e.posY, field_191513_e.posZ);
                motionX = field_191513_e.motionX;
                motionY = field_191513_e.motionY;
                motionZ = field_191513_e.motionZ;
            }
        }
        else
        {
            motionX *= 1.15D;
            motionZ *= 1.15D;
            motionY += 0.04D;
            moveEntity(MoverType.SELF, motionX, motionY, motionZ);
        }

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

        if (fireworkAge == 0 && !isSilent())
        {
            world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++fireworkAge;

        if (world.isRemote && fireworkAge % 2 < 2)
        {
            world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, posX, posY - 0.3D, posZ, rand.nextGaussian() * 0.05D, -motionY * 0.5D, rand.nextGaussian() * 0.05D);
        }

        if (!world.isRemote && fireworkAge > lifetime)
        {
            world.setEntityState(this, (byte)17);
            func_191510_k();
            setDead();
        }
    }

    private void func_191510_k()
    {
        float f = 0.0F;
        ItemStack itemstack = dataManager.get(FIREWORK_ITEM);
        NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getTagList("Explosions", 10) : null;

        if (nbttaglist != null && !nbttaglist.hasNoTags())
        {
            f = (float)(5 + nbttaglist.tagCount() * 2);
        }

        if (f > 0.0F)
        {
            if (field_191513_e != null)
            {
                field_191513_e.attackEntityFrom(DamageSource.field_191552_t, (float)(5 + nbttaglist.tagCount() * 2));
            }

            double d0 = 5.0D;
            Vec3d vec3d = new Vec3d(posX, posY, posZ);

            for (EntityLivingBase entitylivingbase : world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expandXyz(5.0D)))
            {
                if (entitylivingbase != field_191513_e && getDistanceSqToEntity(entitylivingbase) <= 25.0D)
                {
                    boolean flag = false;

                    for (int i = 0; i < 2; ++i)
                    {
                        RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, new Vec3d(entitylivingbase.posX, entitylivingbase.posY + (double)entitylivingbase.height * 0.5D * (double)i, entitylivingbase.posZ), false, true, false);

                        if (raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS)
                        {
                            flag = true;
                            break;
                        }
                    }

                    if (flag)
                    {
                        float f1 = f * (float)Math.sqrt((5.0D - (double) getDistanceToEntity(entitylivingbase)) / 5.0D);
                        entitylivingbase.attackEntityFrom(DamageSource.field_191552_t, f1);
                    }
                }
            }
        }
    }

    public boolean func_191511_j()
    {
        return dataManager.get(field_191512_b).intValue() > 0;
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 17 && world.isRemote)
        {
            ItemStack itemstack = dataManager.get(FIREWORK_ITEM);
            NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
            world.makeFireworks(posX, posY, posZ, motionX, motionY, motionZ, nbttagcompound);
        }

        super.handleStatusUpdate(id);
    }

    public static void registerFixesFireworkRocket(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityFireworkRocket.class, "FireworksItem"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Life", fireworkAge);
        compound.setInteger("LifeTime", lifetime);
        ItemStack itemstack = dataManager.get(FIREWORK_ITEM);

        if (!itemstack.isEmpty())
        {
            compound.setTag("FireworksItem", itemstack.writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        fireworkAge = compound.getInteger("Life");
        lifetime = compound.getInteger("LifeTime");
        NBTTagCompound nbttagcompound = compound.getCompoundTag("FireworksItem");

        if (nbttagcompound != null)
        {
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.isEmpty())
            {
                dataManager.set(FIREWORK_ITEM, itemstack);
            }
        }
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
}
