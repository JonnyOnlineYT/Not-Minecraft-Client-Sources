package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Namespaced;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityMinecart extends Entity implements IWorldNameable
{
    private static final DataParameter<Integer> ROLLING_AMPLITUDE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ROLLING_DIRECTION = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> DISPLAY_TILE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DISPLAY_TILE_OFFSET = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SHOW_BLOCK = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.BOOLEAN);
    private boolean isInReverse;

    /** Minecart rotational logic matrix */
    private static final int[][][] MATRIX = {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

    /** appears to be the progress of the turn */
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public EntityMinecart(World worldIn)
    {
        super(worldIn);
        preventEntitySpawning = true;
        setSize(0.98F, 0.7F);
    }

    public static EntityMinecart create(World worldIn, double x, double y, double z, EntityMinecart.Type typeIn)
    {
        switch (typeIn)
        {
            case CHEST:
                return new EntityMinecartChest(worldIn, x, y, z);

            case FURNACE:
                return new EntityMinecartFurnace(worldIn, x, y, z);

            case TNT:
                return new EntityMinecartTNT(worldIn, x, y, z);

            case SPAWNER:
                return new EntityMinecartMobSpawner(worldIn, x, y, z);

            case HOPPER:
                return new EntityMinecartHopper(worldIn, x, y, z);

            case COMMAND_BLOCK:
                return new EntityMinecartCommandBlock(worldIn, x, y, z);

            default:
                return new EntityMinecartEmpty(worldIn, x, y, z);
        }
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
        dataManager.register(ROLLING_AMPLITUDE, Integer.valueOf(0));
        dataManager.register(ROLLING_DIRECTION, Integer.valueOf(1));
        dataManager.register(DAMAGE, Float.valueOf(0.0F));
        dataManager.register(DISPLAY_TILE, Integer.valueOf(0));
        dataManager.register(DISPLAY_TILE_OFFSET, Integer.valueOf(6));
        dataManager.register(SHOW_BLOCK, Boolean.valueOf(false));
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
        return null;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    public EntityMinecart(World worldIn, double x, double y, double z)
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
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 0.0D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!world.isRemote && !isDead)
        {
            if (isEntityInvulnerable(source))
            {
                return false;
            }
            else
            {
                setRollingDirection(-getRollingDirection());
                setRollingAmplitude(10);
                setBeenAttacked();
                setDamage(getDamage() + amount * 10.0F);
                boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;

                if (flag || getDamage() > 40.0F)
                {
                    removePassengers();

                    if (flag && !hasCustomName())
                    {
                        setDead();
                    }
                    else
                    {
                        killMinecart(source);
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void killMinecart(DamageSource source)
    {
        setDead();

        if (world.getGameRules().getBoolean("doEntityDrops"))
        {
            ItemStack itemstack = new ItemStack(Items.MINECART, 1);

            if (hasCustomName())
            {
                itemstack.setStackDisplayName(getCustomNameTag());
            }

            entityDropItem(itemstack, 0.0F);
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        setRollingDirection(-getRollingDirection());
        setRollingAmplitude(10);
        setDamage(getDamage() + getDamage() * 10.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into
     * account.
     */
    public EnumFacing getAdjustedHorizontalFacing()
    {
        return isInReverse ? getHorizontalFacing().getOpposite().rotateY() : getHorizontalFacing().rotateY();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (getRollingAmplitude() > 0)
        {
            setRollingAmplitude(getRollingAmplitude() - 1);
        }

        if (getDamage() > 0.0F)
        {
            setDamage(getDamage() - 1.0F);
        }

        if (posY < -64.0D)
        {
            kill();
        }

        if (!world.isRemote && world instanceof WorldServer)
        {
            world.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = world.getInstanceServer();
            int i = getMaxInPortalTime();

            if (inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (!isRiding() && portalCounter++ >= i)
                    {
                        portalCounter = i;
                        timeUntilPortal = getPortalCooldown();
                        int j;

                        if (world.provider.getDimensionType().getId() == -1)
                        {
                            j = 0;
                        }
                        else
                        {
                            j = -1;
                        }

                        changeDimension(j);
                    }

                    inPortal = false;
                }
            }
            else
            {
                if (portalCounter > 0)
                {
                    portalCounter -= 4;
                }

                if (portalCounter < 0)
                {
                    portalCounter = 0;
                }
            }

            if (timeUntilPortal > 0)
            {
                --timeUntilPortal;
            }

            world.theProfiler.endSection();
        }

        if (world.isRemote)
        {
            if (turnProgress > 0)
            {
                double d4 = posX + (minecartX - posX) / (double) turnProgress;
                double d5 = posY + (minecartY - posY) / (double) turnProgress;
                double d6 = posZ + (minecartZ - posZ) / (double) turnProgress;
                double d1 = MathHelper.wrapDegrees(minecartYaw - (double) rotationYaw);
                rotationYaw = (float)((double) rotationYaw + d1 / (double) turnProgress);
                rotationPitch = (float)((double) rotationPitch + (minecartPitch - (double) rotationPitch) / (double) turnProgress);
                --turnProgress;
                setPosition(d4, d5, d6);
                setRotation(rotationYaw, rotationPitch);
            }
            else
            {
                setPosition(posX, posY, posZ);
                setRotation(rotationYaw, rotationPitch);
            }
        }
        else
        {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;

            if (!hasNoGravity())
            {
                motionY -= 0.03999999910593033D;
            }

            int k = MathHelper.floor(posX);
            int l = MathHelper.floor(posY);
            int i1 = MathHelper.floor(posZ);

            if (BlockRailBase.isRailBlock(world, new BlockPos(k, l - 1, i1)))
            {
                --l;
            }

            BlockPos blockpos = new BlockPos(k, l, i1);
            IBlockState iblockstate = world.getBlockState(blockpos);

            if (BlockRailBase.isRailBlock(iblockstate))
            {
                moveAlongTrack(blockpos, iblockstate);

                if (iblockstate.getBlock() == Blocks.ACTIVATOR_RAIL)
                {
                    onActivatorRailPass(k, l, i1, iblockstate.getValue(BlockRailPowered.POWERED).booleanValue());
                }
            }
            else
            {
                moveDerailedMinecart();
            }

            doBlockCollisions();
            rotationPitch = 0.0F;
            double d0 = prevPosX - posX;
            double d2 = prevPosZ - posZ;

            if (d0 * d0 + d2 * d2 > 0.001D)
            {
                rotationYaw = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI);

                if (isInReverse)
                {
                    rotationYaw += 180.0F;
                }
            }

            double d3 = MathHelper.wrapDegrees(rotationYaw - prevRotationYaw);

            if (d3 < -170.0D || d3 >= 170.0D)
            {
                rotationYaw += 180.0F;
                isInReverse = !isInReverse;
            }

            setRotation(rotationYaw, rotationPitch);

            if (getType() == EntityMinecart.Type.RIDEABLE && motionX * motionX + motionZ * motionZ > 0.01D)
            {
                List<Entity> list = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D), EntitySelectors.getTeamCollisionPredicate(this));

                if (!list.isEmpty())
                {
                    for (int j1 = 0; j1 < list.size(); ++j1)
                    {
                        Entity entity1 = list.get(j1);

                        if (!(entity1 instanceof EntityPlayer) && !(entity1 instanceof EntityIronGolem) && !(entity1 instanceof EntityMinecart) && !isBeingRidden() && !entity1.isRiding())
                        {
                            entity1.startRiding(this);
                        }
                        else
                        {
                            entity1.applyEntityCollision(this);
                        }
                    }
                }
            }
            else
            {
                for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D)))
                {
                    if (!isPassenger(entity) && entity.canBePushed() && entity instanceof EntityMinecart)
                    {
                        entity.applyEntityCollision(this);
                    }
                }
            }

            handleWaterMovement();
        }
    }

    /**
     * Get's the maximum speed for a minecart
     */
    protected double getMaximumSpeed()
    {
        return 0.4D;
    }

    /**
     * Called every tick the minecart is on an activator rail.
     */
    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
    }

    /**
     * Moves a minecart that is not attached to a rail
     */
    protected void moveDerailedMinecart()
    {
        double d0 = getMaximumSpeed();
        motionX = MathHelper.clamp(motionX, -d0, d0);
        motionZ = MathHelper.clamp(motionZ, -d0, d0);

        if (onGround)
        {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveEntity(MoverType.SELF, motionX, motionY, motionZ);

        if (!onGround)
        {
            motionX *= 0.949999988079071D;
            motionY *= 0.949999988079071D;
            motionZ *= 0.949999988079071D;
        }
    }

    @SuppressWarnings("incomplete-switch")
    protected void moveAlongTrack(BlockPos pos, IBlockState state)
    {
        fallDistance = 0.0F;
        Vec3d vec3d = getPos(posX, posY, posZ);
        posY = pos.getY();
        boolean flag = false;
        boolean flag1 = false;
        BlockRailBase blockrailbase = (BlockRailBase)state.getBlock();

        if (blockrailbase == Blocks.GOLDEN_RAIL)
        {
            flag = state.getValue(BlockRailPowered.POWERED).booleanValue();
            flag1 = !flag;
        }

        double d0 = 0.0078125D;
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(blockrailbase.getShapeProperty());

        switch (blockrailbase$enumraildirection)
        {
            case ASCENDING_EAST:
                motionX -= 0.0078125D;
                ++posY;
                break;

            case ASCENDING_WEST:
                motionX += 0.0078125D;
                ++posY;
                break;

            case ASCENDING_NORTH:
                motionZ += 0.0078125D;
                ++posY;
                break;

            case ASCENDING_SOUTH:
                motionZ -= 0.0078125D;
                ++posY;
        }

        int[][] aint = MATRIX[blockrailbase$enumraildirection.getMetadata()];
        double d1 = aint[1][0] - aint[0][0];
        double d2 = aint[1][2] - aint[0][2];
        double d3 = Math.sqrt(d1 * d1 + d2 * d2);
        double d4 = motionX * d1 + motionZ * d2;

        if (d4 < 0.0D)
        {
            d1 = -d1;
            d2 = -d2;
        }

        double d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (d5 > 2.0D)
        {
            d5 = 2.0D;
        }

        motionX = d5 * d1 / d3;
        motionZ = d5 * d2 / d3;
        Entity entity = getPassengers().isEmpty() ? null : getPassengers().get(0);

        if (entity instanceof EntityLivingBase)
        {
            double d6 = ((EntityLivingBase)entity).field_191988_bg;

            if (d6 > 0.0D)
            {
                double d7 = -Math.sin(entity.rotationYaw * 0.017453292F);
                double d8 = Math.cos(entity.rotationYaw * 0.017453292F);
                double d9 = motionX * motionX + motionZ * motionZ;

                if (d9 < 0.01D)
                {
                    motionX += d7 * 0.1D;
                    motionZ += d8 * 0.1D;
                    flag1 = false;
                }
            }
        }

        if (flag1)
        {
            double d17 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d17 < 0.03D)
            {
                motionX *= 0.0D;
                motionY *= 0.0D;
                motionZ *= 0.0D;
            }
            else
            {
                motionX *= 0.5D;
                motionY *= 0.0D;
                motionZ *= 0.5D;
            }
        }

        double d18 = (double)pos.getX() + 0.5D + (double)aint[0][0] * 0.5D;
        double d19 = (double)pos.getZ() + 0.5D + (double)aint[0][2] * 0.5D;
        double d20 = (double)pos.getX() + 0.5D + (double)aint[1][0] * 0.5D;
        double d21 = (double)pos.getZ() + 0.5D + (double)aint[1][2] * 0.5D;
        d1 = d20 - d18;
        d2 = d21 - d19;
        double d10;

        if (d1 == 0.0D)
        {
            posX = (double)pos.getX() + 0.5D;
            d10 = posZ - (double)pos.getZ();
        }
        else if (d2 == 0.0D)
        {
            posZ = (double)pos.getZ() + 0.5D;
            d10 = posX - (double)pos.getX();
        }
        else
        {
            double d11 = posX - d18;
            double d12 = posZ - d19;
            d10 = (d11 * d1 + d12 * d2) * 2.0D;
        }

        posX = d18 + d1 * d10;
        posZ = d19 + d2 * d10;
        setPosition(posX, posY, posZ);
        double d22 = motionX;
        double d23 = motionZ;

        if (isBeingRidden())
        {
            d22 *= 0.75D;
            d23 *= 0.75D;
        }

        double d13 = getMaximumSpeed();
        d22 = MathHelper.clamp(d22, -d13, d13);
        d23 = MathHelper.clamp(d23, -d13, d13);
        moveEntity(MoverType.SELF, d22, 0.0D, d23);

        if (aint[0][1] != 0 && MathHelper.floor(posX) - pos.getX() == aint[0][0] && MathHelper.floor(posZ) - pos.getZ() == aint[0][2])
        {
            setPosition(posX, posY + (double)aint[0][1], posZ);
        }
        else if (aint[1][1] != 0 && MathHelper.floor(posX) - pos.getX() == aint[1][0] && MathHelper.floor(posZ) - pos.getZ() == aint[1][2])
        {
            setPosition(posX, posY + (double)aint[1][1], posZ);
        }

        applyDrag();
        Vec3d vec3d1 = getPos(posX, posY, posZ);

        if (vec3d1 != null && vec3d != null)
        {
            double d14 = (vec3d.yCoord - vec3d1.yCoord) * 0.05D;
            d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d5 > 0.0D)
            {
                motionX = motionX / d5 * (d5 + d14);
                motionZ = motionZ / d5 * (d5 + d14);
            }

            setPosition(posX, vec3d1.yCoord, posZ);
        }

        int j = MathHelper.floor(posX);
        int i = MathHelper.floor(posZ);

        if (j != pos.getX() || i != pos.getZ())
        {
            d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            motionX = d5 * (double)(j - pos.getX());
            motionZ = d5 * (double)(i - pos.getZ());
        }

        if (flag)
        {
            double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d15 > 0.01D)
            {
                double d16 = 0.06D;
                motionX += motionX / d15 * 0.06D;
                motionZ += motionZ / d15 * 0.06D;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (world.getBlockState(pos.west()).isNormalCube())
                {
                    motionX = 0.02D;
                }
                else if (world.getBlockState(pos.east()).isNormalCube())
                {
                    motionX = -0.02D;
                }
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (world.getBlockState(pos.north()).isNormalCube())
                {
                    motionZ = 0.02D;
                }
                else if (world.getBlockState(pos.south()).isNormalCube())
                {
                    motionZ = -0.02D;
                }
            }
        }
    }

    protected void applyDrag()
    {
        if (isBeingRidden())
        {
            motionX *= 0.996999979019165D;
            motionY *= 0.0D;
            motionZ *= 0.996999979019165D;
        }
        else
        {
            motionX *= 0.9599999785423279D;
            motionY *= 0.0D;
            motionZ *= 0.9599999785423279D;
        }
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        posX = x;
        posY = y;
        posZ = z;
        float f = width / 2.0F;
        float f1 = height;
        setEntityBoundingBox(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
    }

    @Nullable
    public Vec3d getPosOffset(double x, double y, double z, double offset)
    {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);

        if (BlockRailBase.isRailBlock(world, new BlockPos(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = world.getBlockState(new BlockPos(i, j, k));

        if (BlockRailBase.isRailBlock(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            y = j;

            if (blockrailbase$enumraildirection.isAscending())
            {
                y = j + 1;
            }

            int[][] aint = MATRIX[blockrailbase$enumraildirection.getMetadata()];
            double d0 = aint[1][0] - aint[0][0];
            double d1 = aint[1][2] - aint[0][2];
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            d0 = d0 / d2;
            d1 = d1 / d2;
            x = x + d0 * offset;
            z = z + d1 * offset;

            if (aint[0][1] != 0 && MathHelper.floor(x) - i == aint[0][0] && MathHelper.floor(z) - k == aint[0][2])
            {
                y += aint[0][1];
            }
            else if (aint[1][1] != 0 && MathHelper.floor(x) - i == aint[1][0] && MathHelper.floor(z) - k == aint[1][2])
            {
                y += aint[1][1];
            }

            return getPos(x, y, z);
        }
        else
        {
            return null;
        }
    }

    @Nullable
    public Vec3d getPos(double p_70489_1_, double p_70489_3_, double p_70489_5_)
    {
        int i = MathHelper.floor(p_70489_1_);
        int j = MathHelper.floor(p_70489_3_);
        int k = MathHelper.floor(p_70489_5_);

        if (BlockRailBase.isRailBlock(world, new BlockPos(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = world.getBlockState(new BlockPos(i, j, k));

        if (BlockRailBase.isRailBlock(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            int[][] aint = MATRIX[blockrailbase$enumraildirection.getMetadata()];
            double d0 = (double)i + 0.5D + (double)aint[0][0] * 0.5D;
            double d1 = (double)j + 0.0625D + (double)aint[0][1] * 0.5D;
            double d2 = (double)k + 0.5D + (double)aint[0][2] * 0.5D;
            double d3 = (double)i + 0.5D + (double)aint[1][0] * 0.5D;
            double d4 = (double)j + 0.0625D + (double)aint[1][1] * 0.5D;
            double d5 = (double)k + 0.5D + (double)aint[1][2] * 0.5D;
            double d6 = d3 - d0;
            double d7 = (d4 - d1) * 2.0D;
            double d8 = d5 - d2;
            double d9;

            if (d6 == 0.0D)
            {
                d9 = p_70489_5_ - (double)k;
            }
            else if (d8 == 0.0D)
            {
                d9 = p_70489_1_ - (double)i;
            }
            else
            {
                double d10 = p_70489_1_ - d0;
                double d11 = p_70489_5_ - d2;
                d9 = (d10 * d6 + d11 * d8) * 2.0D;
            }

            p_70489_1_ = d0 + d6 * d9;
            p_70489_3_ = d1 + d7 * d9;
            p_70489_5_ = d2 + d8 * d9;

            if (d7 < 0.0D)
            {
                ++p_70489_3_;
            }

            if (d7 > 0.0D)
            {
                p_70489_3_ += 0.5D;
            }

            return new Vec3d(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets the bounding box of this Entity, adjusted to take auxiliary entities into account (e.g. the tile contained
     * by a minecart, such as a command block).
     */
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        return hasDisplayTile() ? axisalignedbb.expandXyz((double)Math.abs(getDisplayTileOffset()) / 16.0D) : axisalignedbb;
    }

    public static void registerFixesMinecart(DataFixer fixer, Class<?> name)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        if (compound.getBoolean("CustomDisplayTile"))
        {
            Block block;

            if (compound.hasKey("DisplayTile", 8))
            {
                block = Block.getBlockFromName(compound.getString("DisplayTile"));
            }
            else
            {
                block = Block.getBlockById(compound.getInteger("DisplayTile"));
            }

            int i = compound.getInteger("DisplayData");
            setDisplayTile(block == null ? Blocks.AIR.getDefaultState() : block.getStateFromMeta(i));
            setDisplayTileOffset(compound.getInteger("DisplayOffset"));
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        if (hasDisplayTile())
        {
            compound.setBoolean("CustomDisplayTile", true);
            IBlockState iblockstate = getDisplayTile();
            Namespaced resourcelocation = Block.REGISTRY.getNameForObject(iblockstate.getBlock());
            compound.setString("DisplayTile", resourcelocation == null ? "" : resourcelocation.toString());
            compound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
            compound.setInteger("DisplayOffset", getDisplayTileOffset());
        }
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
        if (!world.isRemote)
        {
            if (!entityIn.noClip && !noClip)
            {
                if (!isPassenger(entityIn))
                {
                    double d0 = entityIn.posX - posX;
                    double d1 = entityIn.posZ - posZ;
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 >= 9.999999747378752E-5D)
                    {
                        d2 = MathHelper.sqrt(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;

                        if (d3 > 1.0D)
                        {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * 0.10000000149011612D;
                        d1 = d1 * 0.10000000149011612D;
                        d0 = d0 * (double)(1.0F - entityCollisionReduction);
                        d1 = d1 * (double)(1.0F - entityCollisionReduction);
                        d0 = d0 * 0.5D;
                        d1 = d1 * 0.5D;

                        if (entityIn instanceof EntityMinecart)
                        {
                            double d4 = entityIn.posX - posX;
                            double d5 = entityIn.posZ - posZ;
                            Vec3d vec3d = (new Vec3d(d4, 0.0D, d5)).normalize();
                            Vec3d vec3d1 = (new Vec3d(MathHelper.cos(rotationYaw * 0.017453292F), 0.0D, MathHelper.sin(rotationYaw * 0.017453292F))).normalize();
                            double d6 = Math.abs(vec3d.dotProduct(vec3d1));

                            if (d6 < 0.800000011920929D)
                            {
                                return;
                            }

                            double d7 = entityIn.motionX + motionX;
                            double d8 = entityIn.motionZ + motionZ;

                            if (((EntityMinecart)entityIn).getType() == EntityMinecart.Type.FURNACE && getType() != EntityMinecart.Type.FURNACE)
                            {
                                motionX *= 0.20000000298023224D;
                                motionZ *= 0.20000000298023224D;
                                addVelocity(entityIn.motionX - d0, 0.0D, entityIn.motionZ - d1);
                                entityIn.motionX *= 0.949999988079071D;
                                entityIn.motionZ *= 0.949999988079071D;
                            }
                            else if (((EntityMinecart)entityIn).getType() != EntityMinecart.Type.FURNACE && getType() == EntityMinecart.Type.FURNACE)
                            {
                                entityIn.motionX *= 0.20000000298023224D;
                                entityIn.motionZ *= 0.20000000298023224D;
                                entityIn.addVelocity(motionX + d0, 0.0D, motionZ + d1);
                                motionX *= 0.949999988079071D;
                                motionZ *= 0.949999988079071D;
                            }
                            else
                            {
                                d7 = d7 / 2.0D;
                                d8 = d8 / 2.0D;
                                motionX *= 0.20000000298023224D;
                                motionZ *= 0.20000000298023224D;
                                addVelocity(d7 - d0, 0.0D, d8 - d1);
                                entityIn.motionX *= 0.20000000298023224D;
                                entityIn.motionZ *= 0.20000000298023224D;
                                entityIn.addVelocity(d7 + d0, 0.0D, d8 + d1);
                            }
                        }
                        else
                        {
                            addVelocity(-d0, 0.0D, -d1);
                            entityIn.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
                        }
                    }
                }
            }
        }
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        minecartX = x;
        minecartY = y;
        minecartZ = z;
        minecartYaw = yaw;
        minecartPitch = pitch;
        turnProgress = posRotationIncrements + 2;
        motionX = velocityX;
        motionY = velocityY;
        motionZ = velocityZ;
    }

    /**
     * Updates the velocity of the entity to a new value.
     */
    public void setVelocity(double x, double y, double z)
    {
        motionX = x;
        motionY = y;
        motionZ = z;
        velocityX = motionX;
        velocityY = motionY;
        velocityZ = motionZ;
    }

    /**
     * Sets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public void setDamage(float damage)
    {
        dataManager.set(DAMAGE, Float.valueOf(damage));
    }

    /**
     * Gets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public float getDamage()
    {
        return dataManager.get(DAMAGE).floatValue();
    }

    /**
     * Sets the rolling amplitude the cart rolls while being attacked.
     */
    public void setRollingAmplitude(int rollingAmplitude)
    {
        dataManager.set(ROLLING_AMPLITUDE, Integer.valueOf(rollingAmplitude));
    }

    /**
     * Gets the rolling amplitude the cart rolls while being attacked.
     */
    public int getRollingAmplitude()
    {
        return dataManager.get(ROLLING_AMPLITUDE).intValue();
    }

    /**
     * Sets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public void setRollingDirection(int rollingDirection)
    {
        dataManager.set(ROLLING_DIRECTION, Integer.valueOf(rollingDirection));
    }

    /**
     * Gets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public int getRollingDirection()
    {
        return dataManager.get(ROLLING_DIRECTION).intValue();
    }

    public abstract EntityMinecart.Type getType();

    public IBlockState getDisplayTile()
    {
        return !hasDisplayTile() ? getDefaultDisplayTile() : Block.getStateById(getDataManager().get(DISPLAY_TILE).intValue());
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.AIR.getDefaultState();
    }

    public int getDisplayTileOffset()
    {
        return !hasDisplayTile() ? getDefaultDisplayTileOffset() : getDataManager().get(DISPLAY_TILE_OFFSET).intValue();
    }

    public int getDefaultDisplayTileOffset()
    {
        return 6;
    }

    public void setDisplayTile(IBlockState displayTile)
    {
        getDataManager().set(DISPLAY_TILE, Integer.valueOf(Block.getStateId(displayTile)));
        setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int displayTileOffset)
    {
        getDataManager().set(DISPLAY_TILE_OFFSET, Integer.valueOf(displayTileOffset));
        setHasDisplayTile(true);
    }

    public boolean hasDisplayTile()
    {
        return getDataManager().get(SHOW_BLOCK).booleanValue();
    }

    public void setHasDisplayTile(boolean showBlock)
    {
        getDataManager().set(SHOW_BLOCK, Boolean.valueOf(showBlock));
    }

    public enum Type
    {
        RIDEABLE(0, "MinecartRideable"),
        CHEST(1, "MinecartChest"),
        FURNACE(2, "MinecartFurnace"),
        TNT(3, "MinecartTNT"),
        SPAWNER(4, "MinecartSpawner"),
        HOPPER(5, "MinecartHopper"),
        COMMAND_BLOCK(6, "MinecartCommandBlock");

        private static final Map<Integer, EntityMinecart.Type> BY_ID = Maps.newHashMap();
        private final int id;
        private final String name;

        Type(int idIn, String nameIn)
        {
            id = idIn;
            name = nameIn;
        }

        public int getId()
        {
            return id;
        }

        public String getName()
        {
            return name;
        }

        public static EntityMinecart.Type getById(int idIn)
        {
            EntityMinecart.Type entityminecart$type = BY_ID.get(Integer.valueOf(idIn));
            return entityminecart$type == null ? RIDEABLE : entityminecart$type;
        }

        static {
            for (EntityMinecart.Type entityminecart$type : values())
            {
                BY_ID.put(Integer.valueOf(entityminecart$type.getId()), entityminecart$type);
            }
        }
    }
}
