package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityArrow extends Entity implements IProjectile
{
    private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_.canBeCollidedWith();
        }
    });
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityArrow.class, DataSerializers.BYTE);
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int inData;
    protected boolean inGround;
    protected int timeInGround;

    /** 1 if the player can pick up the arrow */
    public EntityArrow.PickupStatus pickupStatus;

    /** Seems to be some sort of timer for animating an arrow. */
    public int arrowShake;

    /** The owner of this arrow. */
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;

    /** The amount of knockback an arrow applies when it hits a mob. */
    private int knockbackStrength;

    public EntityArrow(World worldIn)
    {
        super(worldIn);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        damage = 2.0D;
        setSize(0.5F, 0.5F);
    }

    public EntityArrow(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        setPosition(x, y, z);
    }

    public EntityArrow(World worldIn, EntityLivingBase shooter)
    {
        this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
        shootingEntity = shooter;

        if (shooter instanceof EntityPlayer)
        {
            pickupStatus = EntityArrow.PickupStatus.ALLOWED;
        }
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = getEntityBoundingBox().getAverageEdgeLength() * 10.0D;

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * Entity.getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    protected void entityInit()
    {
        dataManager.register(CRITICAL, Byte.valueOf((byte)0));
    }

    public void setAim(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        setThrowableHeading(f, f1, f2, velocity, inaccuracy);
        motionX += shooter.motionX;
        motionZ += shooter.motionZ;

        if (!shooter.onGround)
        {
            motionY += shooter.motionY;
        }
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        motionX = x;
        motionY = y;
        motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        rotationPitch = (float)(MathHelper.atan2(y, f1) * (180D / Math.PI));
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        ticksInGround = 0;
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        setPosition(x, y, z);
        setRotation(yaw, pitch);
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
            rotationPitch = (float)(MathHelper.atan2(y, f) * (180D / Math.PI));
            rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));
            rotationPitch = (float)(MathHelper.atan2(motionY, f) * (180D / Math.PI));
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
        }

        BlockPos blockpos = new BlockPos(xTile, yTile, zTile);
        IBlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR)
        {
            AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(world, blockpos);

            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).isVecInside(new Vec3d(posX, posY, posZ)))
            {
                inGround = true;
            }
        }

        if (arrowShake > 0)
        {
            --arrowShake;
        }

        if (inGround)
        {
            int j = block.getMetaFromState(iblockstate);

            if ((block != inTile || j != inData) && !world.collidesWithAnyBlock(getEntityBoundingBox().expandXyz(0.05D)))
            {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                ticksInGround = 0;
                ticksInAir = 0;
            }
            else
            {
                ++ticksInGround;

                if (ticksInGround >= 1200)
                {
                    setDead();
                }
            }

            ++timeInGround;
        }
        else
        {
            timeInGround = 0;
            ++ticksInAir;
            Vec3d vec3d1 = new Vec3d(posX, posY, posZ);
            Vec3d vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(posX, posY, posZ);
            vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

            if (raytraceresult != null)
            {
                vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
            }

            Entity entity = findEntityOnPath(vec3d1, vec3d);

            if (entity != null)
            {
                raytraceresult = new RayTraceResult(entity);
            }

            if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;

                if (shootingEntity instanceof EntityPlayer && !((EntityPlayer) shootingEntity).canAttackPlayer(entityplayer))
                {
                    raytraceresult = null;
                }
            }

            if (raytraceresult != null)
            {
                onHit(raytraceresult);
            }

            if (getIsCritical())
            {
                for (int k = 0; k < 4; ++k)
                {
                    world.spawnParticle(EnumParticleTypes.CRIT, posX + motionX * (double)k / 4.0D, posY + motionY * (double)k / 4.0D, posZ + motionZ * (double)k / 4.0D, -motionX, -motionY + 0.2D, -motionZ);
                }
            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float f4 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            rotationYaw = (float)(MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));

            for (rotationPitch = (float)(MathHelper.atan2(motionY, f4) * (180D / Math.PI)); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F)
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
            float f1 = 0.99F;
            float f2 = 0.05F;

            if (isInWater())
            {
                for (int i = 0; i < 4; ++i)
                {
                    float f3 = 0.25F;
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25D, posY - motionY * 0.25D, posZ - motionZ * 0.25D, motionX, motionY, motionZ);
                }

                f1 = 0.6F;
            }

            if (isWet())
            {
                extinguish();
            }

            motionX *= f1;
            motionY *= f1;
            motionZ *= f1;

            if (!hasNoGravity())
            {
                motionY -= 0.05000000074505806D;
            }

            setPosition(posX, posY, posZ);
            doBlockCollisions();
        }
    }

    /**
     * Called when the arrow hits a block or an entity
     */
    protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;

        if (entity != null)
        {
            float f = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            int i = MathHelper.ceil((double)f * damage);

            if (getIsCritical())
            {
                i += rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (shootingEntity == null)
            {
                damagesource = DamageSource.causeArrowDamage(this, this);
            }
            else
            {
                damagesource = DamageSource.causeArrowDamage(this, shootingEntity);
            }

            if (isBurning() && !(entity instanceof EntityEnderman))
            {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damagesource, (float)i))
            {
                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

                    if (!world.isRemote)
                    {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }

                    if (knockbackStrength > 0)
                    {
                        float f1 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);

                        if (f1 > 0.0F)
                        {
                            entitylivingbase.addVelocity(motionX * (double) knockbackStrength * 0.6000000238418579D / (double)f1, 0.1D, motionZ * (double) knockbackStrength * 0.6000000238418579D / (double)f1);
                        }
                    }

                    if (shootingEntity instanceof EntityLivingBase)
                    {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) shootingEntity, entitylivingbase);
                    }

                    arrowHit(entitylivingbase);

                    if (shootingEntity != null && entitylivingbase != shootingEntity && entitylivingbase instanceof EntityPlayer && shootingEntity instanceof EntityPlayerMP)
                    {
                        ((EntityPlayerMP) shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }

                playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));

                if (!(entity instanceof EntityEnderman))
                {
                    setDead();
                }
            }
            else
            {
                motionX *= -0.10000000149011612D;
                motionY *= -0.10000000149011612D;
                motionZ *= -0.10000000149011612D;
                rotationYaw += 180.0F;
                prevRotationYaw += 180.0F;
                ticksInAir = 0;

                if (!world.isRemote && motionX * motionX + motionY * motionY + motionZ * motionZ < 0.0010000000474974513D)
                {
                    if (pickupStatus == EntityArrow.PickupStatus.ALLOWED)
                    {
                        entityDropItem(getArrowStack(), 0.1F);
                    }

                    setDead();
                }
            }
        }
        else
        {
            BlockPos blockpos = raytraceResultIn.getBlockPos();
            xTile = blockpos.getX();
            yTile = blockpos.getY();
            zTile = blockpos.getZ();
            IBlockState iblockstate = world.getBlockState(blockpos);
            inTile = iblockstate.getBlock();
            inData = inTile.getMetaFromState(iblockstate);
            motionX = (float)(raytraceResultIn.hitVec.xCoord - posX);
            motionY = (float)(raytraceResultIn.hitVec.yCoord - posY);
            motionZ = (float)(raytraceResultIn.hitVec.zCoord - posZ);
            float f2 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            posX -= motionX / (double)f2 * 0.05000000074505806D;
            posY -= motionY / (double)f2 * 0.05000000074505806D;
            posZ -= motionZ / (double)f2 * 0.05000000074505806D;
            playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
            inGround = true;
            arrowShake = 7;
            setIsCritical(false);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                inTile.onEntityCollidedWithBlock(world, blockpos, iblockstate, this);
            }
        }
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);

        if (inGround)
        {
            xTile = MathHelper.floor(posX);
            yTile = MathHelper.floor(posY);
            zTile = MathHelper.floor(posZ);
        }
    }

    protected void arrowHit(EntityLivingBase living)
    {
    }

    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expandXyz(1.0D), ARROW_TARGETS);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1 != shootingEntity || ticksInAir >= 5)
            {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null)
                {
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    public static void registerFixesArrow(DataFixer fixer, String name)
    {
    }

    public static void registerFixesArrow(DataFixer fixer)
    {
        registerFixesArrow(fixer, "Arrow");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("xTile", xTile);
        compound.setInteger("yTile", yTile);
        compound.setInteger("zTile", zTile);
        compound.setShort("life", (short) ticksInGround);
        Namespaced resourcelocation = Block.REGISTRY.getNameForObject(inTile);
        compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("inData", (byte) inData);
        compound.setByte("shake", (byte) arrowShake);
        compound.setByte("inGround", (byte)(inGround ? 1 : 0));
        compound.setByte("pickup", (byte) pickupStatus.ordinal());
        compound.setDouble("damage", damage);
        compound.setBoolean("crit", getIsCritical());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        xTile = compound.getInteger("xTile");
        yTile = compound.getInteger("yTile");
        zTile = compound.getInteger("zTile");
        ticksInGround = compound.getShort("life");

        if (compound.hasKey("inTile", 8))
        {
            inTile = Block.getBlockFromName(compound.getString("inTile"));
        }
        else
        {
            inTile = Block.getBlockById(compound.getByte("inTile") & 255);
        }

        inData = compound.getByte("inData") & 255;
        arrowShake = compound.getByte("shake") & 255;
        inGround = compound.getByte("inGround") == 1;

        if (compound.hasKey("damage", 99))
        {
            damage = compound.getDouble("damage");
        }

        if (compound.hasKey("pickup", 99))
        {
            pickupStatus = EntityArrow.PickupStatus.getByOrdinal(compound.getByte("pickup"));
        }
        else if (compound.hasKey("player", 99))
        {
            pickupStatus = compound.getBoolean("player") ? EntityArrow.PickupStatus.ALLOWED : EntityArrow.PickupStatus.DISALLOWED;
        }

        setIsCritical(compound.getBoolean("crit"));
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!world.isRemote && inGround && arrowShake <= 0)
        {
            boolean flag = pickupStatus == EntityArrow.PickupStatus.ALLOWED || pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode;

            if (pickupStatus == EntityArrow.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(getArrowStack()))
            {
                flag = false;
            }

            if (flag)
            {
                entityIn.onItemPickup(this, 1);
                setDead();
            }
        }
    }

    protected abstract ItemStack getArrowStack();

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public void setDamage(double damageIn)
    {
        damage = damageIn;
    }

    public double getDamage()
    {
        return damage;
    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int knockbackStrengthIn)
    {
        knockbackStrength = knockbackStrengthIn;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public void setIsCritical(boolean critical)
    {
        byte b0 = dataManager.get(CRITICAL).byteValue();

        if (critical)
        {
            dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public boolean getIsCritical()
    {
        byte b0 = dataManager.get(CRITICAL).byteValue();
        return (b0 & 1) != 0;
    }

    public void func_190547_a(EntityLivingBase p_190547_1_, float p_190547_2_)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_);
        setDamage((double)(p_190547_2_ * 2.0F) + rand.nextGaussian() * 0.25D + (double)((float) world.getDifficulty().getDifficultyId() * 0.11F));

        if (i > 0)
        {
            setDamage(getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0)
        {
            setFire(100);
        }
    }

    public enum PickupStatus
    {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        public static EntityArrow.PickupStatus getByOrdinal(int ordinal)
        {
            if (ordinal < 0 || ordinal > values().length)
            {
                ordinal = 0;
            }

            return values()[ordinal];
        }
    }
}
