package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBoat extends Entity
{
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(EntityBoat.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> BOAT_TYPE = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean>[] DATA_ID_PADDLE = new DataParameter[] {EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN)};
    private final float[] paddlePositions;

    /** How much of current speed to retain. Value zero to one. */
    private float momentum;
    private float outOfControlTicks;
    private float deltaRotation;
    private int lerpSteps;
    private double boatPitch;
    private double lerpY;
    private double lerpZ;
    private double boatYaw;
    private double lerpXRot;
    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;
    private double waterLevel;

    /**
     * How much the boat should glide given the slippery blocks it's currently gliding over.
     * Halved every tick.
     */
    private float boatGlide;
    private EntityBoat.Status status;
    private EntityBoat.Status previousStatus;
    private double lastYd;

    public EntityBoat(World worldIn)
    {
        super(worldIn);
        paddlePositions = new float[2];
        preventEntitySpawning = true;
        setSize(1.375F, 0.5625F);
    }

    public EntityBoat(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        setPosition(x, y, z);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        dataManager.register(TIME_SINCE_HIT, Integer.valueOf(0));
        dataManager.register(FORWARD_DIRECTION, Integer.valueOf(1));
        dataManager.register(DAMAGE_TAKEN, Float.valueOf(0.0F));
        dataManager.register(BOAT_TYPE, Integer.valueOf(EntityBoat.Type.OAK.ordinal()));

        for (DataParameter<Boolean> dataparameter : DATA_ID_PADDLE)
        {
            dataManager.register(dataparameter, Boolean.valueOf(false));
        }
    }

    @Nullable

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }

    @Nullable

    /**
     * Returns the collision bounding box for this entity
     */
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return getEntityBoundingBox();
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return -0.1D;
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
        else if (!world.isRemote && !isDead)
        {
            if (source instanceof EntityDamageSourceIndirect && source.getEntity() != null && isPassenger(source.getEntity()))
            {
                return false;
            }
            else
            {
                setForwardDirection(-getForwardDirection());
                setTimeSinceHit(10);
                setDamageTaken(getDamageTaken() + amount * 10.0F);
                setBeenAttacked();
                boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;

                if (flag || getDamageTaken() > 40.0F)
                {
                    if (!flag && world.getGameRules().getBoolean("doEntityDrops"))
                    {
                        dropItemWithOffset(getItemBoat(), 1, 0.0F);
                    }

                    setDead();
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
        if (entityIn instanceof EntityBoat)
        {
            if (entityIn.getEntityBoundingBox().minY < getEntityBoundingBox().maxY)
            {
                super.applyEntityCollision(entityIn);
            }
        }
        else if (entityIn.getEntityBoundingBox().minY <= getEntityBoundingBox().minY)
        {
            super.applyEntityCollision(entityIn);
        }
    }

    public Item getItemBoat()
    {
        switch (getBoatType())
        {
            case OAK:
            default:
                return Items.BOAT;

            case SPRUCE:
                return Items.SPRUCE_BOAT;

            case BIRCH:
                return Items.BIRCH_BOAT;

            case JUNGLE:
                return Items.JUNGLE_BOAT;

            case ACACIA:
                return Items.ACACIA_BOAT;

            case DARK_OAK:
                return Items.DARK_OAK_BOAT;
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        setForwardDirection(-getForwardDirection());
        setTimeSinceHit(10);
        setDamageTaken(getDamageTaken() * 11.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        boatPitch = x;
        lerpY = y;
        lerpZ = z;
        boatYaw = yaw;
        lerpXRot = pitch;
        lerpSteps = 10;
    }

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into
     * account.
     */
    public EnumFacing getAdjustedHorizontalFacing()
    {
        return getHorizontalFacing().rotateY();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        previousStatus = status;
        status = getBoatStatus();

        if (status != EntityBoat.Status.UNDER_WATER && status != EntityBoat.Status.UNDER_FLOWING_WATER)
        {
            outOfControlTicks = 0.0F;
        }
        else
        {
            ++outOfControlTicks;
        }

        if (!world.isRemote && outOfControlTicks >= 60.0F)
        {
            removePassengers();
        }

        if (getTimeSinceHit() > 0)
        {
            setTimeSinceHit(getTimeSinceHit() - 1);
        }

        if (getDamageTaken() > 0.0F)
        {
            setDamageTaken(getDamageTaken() - 1.0F);
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        super.onUpdate();
        tickLerp();

        if (canPassengerSteer())
        {
            if (getPassengers().isEmpty() || !(getPassengers().get(0) instanceof EntityPlayer))
            {
                setPaddleState(false, false);
            }

            updateMotion();

            if (world.isRemote)
            {
                controlBoat();
                world.sendPacketToServer(new CPacketSteerBoat(getPaddleState(0), getPaddleState(1)));
            }

            moveEntity(MoverType.SELF, motionX, motionY, motionZ);
        }
        else
        {
            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
        }

        for (int i = 0; i <= 1; ++i)
        {
            if (getPaddleState(i))
            {
                if (!isSilent() && (double)(paddlePositions[i] % ((float)Math.PI * 2F)) <= (Math.PI / 4D) && ((double) paddlePositions[i] + 0.39269909262657166D) % (Math.PI * 2D) >= (Math.PI / 4D))
                {
                    SoundEvent soundevent = func_193047_k();

                    if (soundevent != null)
                    {
                        Vec3d vec3d = getLook(1.0F);
                        double d0 = i == 1 ? -vec3d.zCoord : vec3d.zCoord;
                        double d1 = i == 1 ? vec3d.xCoord : -vec3d.xCoord;
                        world.playSound(null, posX + d0, posY, posZ + d1, soundevent, getSoundCategory(), 1.0F, 0.8F + 0.4F * rand.nextFloat());
                    }
                }

                paddlePositions[i] = (float)((double) paddlePositions[i] + 0.39269909262657166D);
            }
            else
            {
                paddlePositions[i] = 0.0F;
            }
        }

        doBlockCollisions();
        List<Entity> list = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty())
        {
            boolean flag = !world.isRemote && !(getControllingPassenger() instanceof EntityPlayer);

            for (int j = 0; j < list.size(); ++j)
            {
                Entity entity = list.get(j);

                if (!entity.isPassenger(this))
                {
                    if (flag && getPassengers().size() < 2 && !entity.isRiding() && entity.width < width && entity instanceof EntityLivingBase && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityPlayer))
                    {
                        entity.startRiding(this);
                    }
                    else
                    {
                        applyEntityCollision(entity);
                    }
                }
            }
        }
    }

    @Nullable
    protected SoundEvent func_193047_k()
    {
        switch (getBoatStatus())
        {
            case IN_WATER:
            case UNDER_WATER:
            case UNDER_FLOWING_WATER:
                return SoundEvents.field_193779_I;

            case ON_LAND:
                return SoundEvents.field_193778_H;

            case IN_AIR:
            default:
                return null;
        }
    }

    private void tickLerp()
    {
        if (lerpSteps > 0 && !canPassengerSteer())
        {
            double d0 = posX + (boatPitch - posX) / (double) lerpSteps;
            double d1 = posY + (lerpY - posY) / (double) lerpSteps;
            double d2 = posZ + (lerpZ - posZ) / (double) lerpSteps;
            double d3 = MathHelper.wrapDegrees(boatYaw - (double) rotationYaw);
            rotationYaw = (float)((double) rotationYaw + d3 / (double) lerpSteps);
            rotationPitch = (float)((double) rotationPitch + (lerpXRot - (double) rotationPitch) / (double) lerpSteps);
            --lerpSteps;
            setPosition(d0, d1, d2);
            setRotation(rotationYaw, rotationPitch);
        }
    }

    public void setPaddleState(boolean p_184445_1_, boolean p_184445_2_)
    {
        dataManager.set(DATA_ID_PADDLE[0], Boolean.valueOf(p_184445_1_));
        dataManager.set(DATA_ID_PADDLE[1], Boolean.valueOf(p_184445_2_));
    }

    public float getRowingTime(int p_184448_1_, float limbSwing)
    {
        return getPaddleState(p_184448_1_) ? (float)MathHelper.clampedLerp((double) paddlePositions[p_184448_1_] - 0.39269909262657166D, paddlePositions[p_184448_1_], limbSwing) : 0.0F;
    }

    /**
     * Determines whether the boat is in water, gliding on land, or in air
     */
    private EntityBoat.Status getBoatStatus()
    {
        EntityBoat.Status entityboat$status = getUnderwaterStatus();

        if (entityboat$status != null)
        {
            waterLevel = getEntityBoundingBox().maxY;
            return entityboat$status;
        }
        else if (checkInWater())
        {
            return EntityBoat.Status.IN_WATER;
        }
        else
        {
            float f = getBoatGlide();

            if (f > 0.0F)
            {
                boatGlide = f;
                return EntityBoat.Status.ON_LAND;
            }
            else
            {
                return EntityBoat.Status.IN_AIR;
            }
        }
    }

    public float getWaterLevelAbove()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(axisalignedbb.maxY - lastYd);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try
        {
            label108:

            for (int k1 = k; k1 < l; ++k1)
            {
                float f = 0.0F;
                int l1 = i;

                while (true)
                {
                    if (l1 >= j)
                    {
                        if (f < 1.0F)
                        {
                            float f2 = (float)blockpos$pooledmutableblockpos.getY() + f;
                            return f2;
                        }

                        break;
                    }

                    for (int i2 = i1; i2 < j1; ++i2)
                    {
                        blockpos$pooledmutableblockpos.setPos(l1, k1, i2);
                        IBlockState iblockstate = world.getBlockState(blockpos$pooledmutableblockpos);

                        if (iblockstate.getMaterial() == Material.WATER)
                        {
                            f = Math.max(f, BlockLiquid.func_190973_f(iblockstate, world, blockpos$pooledmutableblockpos));
                        }

                        if (f >= 1.0F)
                        {
                            continue label108;
                        }
                    }

                    ++l1;
                }
            }

            float f1 = (float)(l + 1);
            return f1;
        }
        finally
        {
            blockpos$pooledmutableblockpos.release();
        }
    }

    /**
     * Decides how much the boat should be gliding on the land (based on any slippery blocks)
     */
    public float getBoatGlide()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001D, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        int i = MathHelper.floor(axisalignedbb1.minX) - 1;
        int j = MathHelper.ceil(axisalignedbb1.maxX) + 1;
        int k = MathHelper.floor(axisalignedbb1.minY) - 1;
        int l = MathHelper.ceil(axisalignedbb1.maxY) + 1;
        int i1 = MathHelper.floor(axisalignedbb1.minZ) - 1;
        int j1 = MathHelper.ceil(axisalignedbb1.maxZ) + 1;
        List<AxisAlignedBB> list = Lists.newArrayList();
        float f = 0.0F;
        int k1 = 0;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try
        {
            for (int l1 = i; l1 < j; ++l1)
            {
                for (int i2 = i1; i2 < j1; ++i2)
                {
                    int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);

                    if (j2 != 2)
                    {
                        for (int k2 = k; k2 < l; ++k2)
                        {
                            if (j2 <= 0 || k2 != k && k2 != l - 1)
                            {
                                blockpos$pooledmutableblockpos.setPos(l1, k2, i2);
                                IBlockState iblockstate = world.getBlockState(blockpos$pooledmutableblockpos);
                                iblockstate.addCollisionBoxToList(world, blockpos$pooledmutableblockpos, axisalignedbb1, list, this, false);

                                if (!list.isEmpty())
                                {
                                    f += iblockstate.getBlock().slipperiness;
                                    ++k1;
                                }

                                list.clear();
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            blockpos$pooledmutableblockpos.release();
        }

        return f / (float)k1;
    }

    private boolean checkInWater()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        waterLevel = Double.MIN_VALUE;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try
        {
            for (int k1 = i; k1 < j; ++k1)
            {
                for (int l1 = k; l1 < l; ++l1)
                {
                    for (int i2 = i1; i2 < j1; ++i2)
                    {
                        blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
                        IBlockState iblockstate = world.getBlockState(blockpos$pooledmutableblockpos);

                        if (iblockstate.getMaterial() == Material.WATER)
                        {
                            float f = BlockLiquid.func_190972_g(iblockstate, world, blockpos$pooledmutableblockpos);
                            waterLevel = Math.max(f, waterLevel);
                            flag |= axisalignedbb.minY < (double)f;
                        }
                    }
                }
            }
        }
        finally
        {
            blockpos$pooledmutableblockpos.release();
        }

        return flag;
    }

    @Nullable

    /**
     * Decides whether the boat is currently underwater.
     */
    private EntityBoat.Status getUnderwaterStatus()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001D;
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(d0);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try
        {
            for (int k1 = i; k1 < j; ++k1)
            {
                for (int l1 = k; l1 < l; ++l1)
                {
                    for (int i2 = i1; i2 < j1; ++i2)
                    {
                        blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
                        IBlockState iblockstate = world.getBlockState(blockpos$pooledmutableblockpos);

                        if (iblockstate.getMaterial() == Material.WATER && d0 < (double)BlockLiquid.func_190972_g(iblockstate, world, blockpos$pooledmutableblockpos))
                        {
                            if (iblockstate.getValue(BlockLiquid.LEVEL).intValue() != 0)
                            {
                                EntityBoat.Status entityboat$status = EntityBoat.Status.UNDER_FLOWING_WATER;
                                return entityboat$status;
                            }

                            flag = true;
                        }
                    }
                }
            }
        }
        finally
        {
            blockpos$pooledmutableblockpos.release();
        }

        return flag ? EntityBoat.Status.UNDER_WATER : null;
    }

    /**
     * Update the boat's speed, based on momentum.
     */
    private void updateMotion()
    {
        double d0 = -0.03999999910593033D;
        double d1 = hasNoGravity() ? 0.0D : -0.03999999910593033D;
        double d2 = 0.0D;
        momentum = 0.05F;

        if (previousStatus == EntityBoat.Status.IN_AIR && status != EntityBoat.Status.IN_AIR && status != EntityBoat.Status.ON_LAND)
        {
            waterLevel = getEntityBoundingBox().minY + (double) height;
            setPosition(posX, (double)(getWaterLevelAbove() - height) + 0.101D, posZ);
            motionY = 0.0D;
            lastYd = 0.0D;
            status = EntityBoat.Status.IN_WATER;
        }
        else
        {
            if (status == EntityBoat.Status.IN_WATER)
            {
                d2 = (waterLevel - getEntityBoundingBox().minY) / (double) height;
                momentum = 0.9F;
            }
            else if (status == EntityBoat.Status.UNDER_FLOWING_WATER)
            {
                d1 = -7.0E-4D;
                momentum = 0.9F;
            }
            else if (status == EntityBoat.Status.UNDER_WATER)
            {
                d2 = 0.009999999776482582D;
                momentum = 0.45F;
            }
            else if (status == EntityBoat.Status.IN_AIR)
            {
                momentum = 0.9F;
            }
            else if (status == EntityBoat.Status.ON_LAND)
            {
                momentum = boatGlide;

                if (getControllingPassenger() instanceof EntityPlayer)
                {
                    boatGlide /= 2.0F;
                }
            }

            motionX *= momentum;
            motionZ *= momentum;
            deltaRotation *= momentum;
            motionY += d1;

            if (d2 > 0.0D)
            {
                double d3 = 0.65D;
                motionY += d2 * 0.06153846016296973D;
                double d4 = 0.75D;
                motionY *= 0.75D;
            }
        }
    }

    private void controlBoat()
    {
        if (isBeingRidden())
        {
            float f = 0.0F;

            if (leftInputDown)
            {
                deltaRotation += -1.0F;
            }

            if (rightInputDown)
            {
                ++deltaRotation;
            }

            if (rightInputDown != leftInputDown && !forwardInputDown && !backInputDown)
            {
                f += 0.005F;
            }

            rotationYaw += deltaRotation;

            if (forwardInputDown)
            {
                f += 0.04F;
            }

            if (backInputDown)
            {
                f -= 0.005F;
            }

            motionX += MathHelper.sin(-rotationYaw * 0.017453292F) * f;
            motionZ += MathHelper.cos(rotationYaw * 0.017453292F) * f;
            setPaddleState(rightInputDown && !leftInputDown || forwardInputDown, leftInputDown && !rightInputDown || forwardInputDown);
        }
    }

    public void updatePassenger(Entity passenger)
    {
        if (isPassenger(passenger))
        {
            float f = 0.0F;
            float f1 = (float)((isDead ? 0.009999999776482582D : getMountedYOffset()) + passenger.getYOffset());

            if (getPassengers().size() > 1)
            {
                int i = getPassengers().indexOf(passenger);

                if (i == 0)
                {
                    f = 0.2F;
                }
                else
                {
                    f = -0.6F;
                }

                if (passenger instanceof EntityAnimal)
                {
                    f = (float)((double)f + 0.2D);
                }
            }

            Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateYaw(-rotationYaw * 0.017453292F - ((float)Math.PI / 2F));
            passenger.setPosition(posX + vec3d.xCoord, posY + (double)f1, posZ + vec3d.zCoord);
            passenger.rotationYaw += deltaRotation;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + deltaRotation);
            applyYawToEntity(passenger);

            if (passenger instanceof EntityAnimal && getPassengers().size() > 1)
            {
                int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
                passenger.setRenderYawOffset(((EntityAnimal)passenger).renderYawOffset + (float)j);
                passenger.setRotationYawHead(passenger.getRotationYawHead() + (float)j);
            }
        }
    }

    /**
     * Applies this boat's yaw to the given entity. Used to update the orientation of its passenger.
     */
    protected void applyYawToEntity(Entity entityToUpdate)
    {
        entityToUpdate.setRenderYawOffset(rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    /**
     * Applies this entity's orientation (pitch/yaw) to another entity. Used to update passenger orientation.
     */
    public void applyOrientationToEntity(Entity entityToUpdate)
    {
        applyYawToEntity(entityToUpdate);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setString("Type", getBoatType().getName());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        if (compound.hasKey("Type", 8))
        {
            setBoatType(EntityBoat.Type.getTypeFromString(compound.getString("Type")));
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand stack)
    {
        if (player.isSneaking())
        {
            return false;
        }
        else
        {
            if (!world.isRemote && outOfControlTicks < 60.0F)
            {
                player.startRiding(this);
            }

            return true;
        }
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
        lastYd = motionY;

        if (!isRiding())
        {
            if (onGroundIn)
            {
                if (fallDistance > 3.0F)
                {
                    if (status != EntityBoat.Status.ON_LAND)
                    {
                        fallDistance = 0.0F;
                        return;
                    }

                    fall(fallDistance, 1.0F);

                    if (!world.isRemote && !isDead)
                    {
                        setDead();

                        if (world.getGameRules().getBoolean("doEntityDrops"))
                        {
                            for (int i = 0; i < 3; ++i)
                            {
                                entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, getBoatType().getMetadata()), 0.0F);
                            }

                            for (int j = 0; j < 2; ++j)
                            {
                                dropItemWithOffset(Items.STICK, 1, 0.0F);
                            }
                        }
                    }
                }

                fallDistance = 0.0F;
            }
            else if (world.getBlockState((new BlockPos(this)).down()).getMaterial() != Material.WATER && y < 0.0D)
            {
                fallDistance = (float)((double) fallDistance - y);
            }
        }
    }

    public boolean getPaddleState(int p_184457_1_)
    {
        return dataManager.get(DATA_ID_PADDLE[p_184457_1_]).booleanValue() && getControllingPassenger() != null;
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(float damageTaken)
    {
        dataManager.set(DAMAGE_TAKEN, Float.valueOf(damageTaken));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamageTaken()
    {
        return dataManager.get(DAMAGE_TAKEN).floatValue();
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int timeSinceHit)
    {
        dataManager.set(TIME_SINCE_HIT, Integer.valueOf(timeSinceHit));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit()
    {
        return dataManager.get(TIME_SINCE_HIT).intValue();
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int forwardDirection)
    {
        dataManager.set(FORWARD_DIRECTION, Integer.valueOf(forwardDirection));
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection()
    {
        return dataManager.get(FORWARD_DIRECTION).intValue();
    }

    public void setBoatType(EntityBoat.Type boatType)
    {
        dataManager.set(BOAT_TYPE, Integer.valueOf(boatType.ordinal()));
    }

    public EntityBoat.Type getBoatType()
    {
        return EntityBoat.Type.byId(dataManager.get(BOAT_TYPE).intValue());
    }

    protected boolean canFitPassenger(Entity passenger)
    {
        return getPassengers().size() < 2;
    }

    @Nullable

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    public Entity getControllingPassenger()
    {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    public void updateInputs(boolean p_184442_1_, boolean p_184442_2_, boolean p_184442_3_, boolean p_184442_4_)
    {
        leftInputDown = p_184442_1_;
        rightInputDown = p_184442_2_;
        forwardInputDown = p_184442_3_;
        backInputDown = p_184442_4_;
    }

    public enum Status
    {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR
    }

    public enum Type
    {
        OAK(BlockPlanks.EnumType.OAK.getMetadata(), "oak"),
        SPRUCE(BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce"),
        BIRCH(BlockPlanks.EnumType.BIRCH.getMetadata(), "birch"),
        JUNGLE(BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle"),
        ACACIA(BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia"),
        DARK_OAK(BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak");

        private final String name;
        private final int metadata;

        Type(int metadataIn, String nameIn)
        {
            name = nameIn;
            metadata = metadataIn;
        }

        public String getName()
        {
            return name;
        }

        public int getMetadata()
        {
            return metadata;
        }

        public String toString()
        {
            return name;
        }

        public static EntityBoat.Type byId(int id)
        {
            if (id < 0 || id >= values().length)
            {
                id = 0;
            }

            return values()[id];
        }

        public static EntityBoat.Type getTypeFromString(String nameIn)
        {
            for (int i = 0; i < values().length; ++i)
            {
                if (values()[i].getName().equals(nameIn))
                {
                    return values()[i];
                }
            }

            return values()[0];
        }
    }
}
