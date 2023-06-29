package net.minecraft.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.*;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.motion.EventSafeWalk;
import org.moonware.client.event.events.impl.motion.EventStep;
import org.moonware.client.event.events.impl.motion.EventStrafe;
import org.moonware.client.event.events.impl.player.EventFullCube;
import org.moonware.client.feature.impl.combat.KillAuraUtilsi.VectorRotationEvent;
import org.moonware.client.feature.impl.movement.Scaffold;
import org.moonware.client.feature.impl.player.AntiPush;
import org.moonware.client.feature.impl.player.AntiWaterCollide;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Entity implements ICommandSender {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<ItemStack> field_190535_b = Collections.emptyList();
    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static double renderDistanceWeight = 1.0D;
    private static int nextEntityID;
    private int entityId;

    /**
     * Blocks entities from spawning when they do their AABB check to make sure the spot is clear of entities that can
     * prevent spawning.
     */
    public boolean preventEntitySpawning;
    private final List<Entity> riddenByEntities;
    protected int rideCooldown;
    public Entity ridingEntity;
    public boolean forceSpawn;

    /**
     * Reference to the World object.
     */
    public World world;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;

    /**
     * Entity position X
     */
    public double posX;

    /**
     * Entity position Y
     */
    public double posY;

    /**
     * Entity position Z
     */
    public double posZ;

    /**
     * Entity motion X
     */
    public double motionX;

    /**
     * Entity motion Y
     */
    public double motionY;

    /**
     * Entity motion Z
     */
    public double motionZ;

    /**
     * Entity rotation Yaw
     */
    public float rotationYaw;

    /**
     * Entity rotation Pitch
     */
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;

    /**
     * Axis aligned bounding box.
     */
    public AxisAlignedBB boundingBox;
    public boolean onGround;

    /**
     * True if after a move this entity has collided with something on X- or Z-axis
     */
    public boolean isCollidedHorizontally;

    /**
     * True if after a move this entity has collided with something on Y-axis
     */
    public boolean isCollidedVertically;

    /**
     * True if after a move this entity has collided with something either vertically or horizontally
     */
    public boolean isCollided;
    public boolean velocityChanged;
    public boolean isInWeb;
    public boolean isOutsideBorder;

    /**
     * gets set by setEntityDead, so this must be the flag whether an Entity is dead (inactive may be better term)
     */
    public boolean isDead;

    /**
     * How wide this entity is considered to be
     */
    public float width;

    /**
     * How high this entity is considered to be
     */
    public float height;

    /**
     * The previous ticks distance walked multiplied by 0.6
     */
    public float prevDistanceWalkedModified;

    /**
     * The distance walked multiplied by 0.6
     */
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;

    /**
     * The distance that has to be exceeded in order to triger a new step sound and an onEntityWalking event on a block
     */
    public int nextStepDistance;
    public float field_191959_ay;

    /**
     * The entity's X coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosX;

    /**
     * The entity's Y coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosY;

    /**
     * The entity's Z coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosZ;

    /**
     * How high this entity can step up when running into a block to try to get over it (currently make note the entity
     * will always step up this amount and not just the amount needed)
     */
    public float stepHeight;

    /**
     * Whether this entity won't clip with collision or not (make note it won't disable gravity)
     */
    public boolean noClip;

    /**
     * Reduces the velocity applied by entity collisions by the specified percent.
     */
    public float entityCollisionReduction;
    protected Random rand;

    /**
     * How many ticks has this entity had ran since being alive
     */
    public int ticksExisted;
    public int field_190534_ay;

    /**
     * Whether this entity is currently inside of water (if it handles water movement that is)
     */
    public boolean inWater;

    /**
     * Remaining time an entity will be "immune" to further damage after being hurt.
     */
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected EntityDataManager dataManager;
    protected static final DataParameter<Byte> FLAGS = EntityDataManager.createKey(Entity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> AIR = EntityDataManager.createKey(Entity.class, DataSerializers.VARINT);
    private static final DataParameter<String> CUSTOM_NAME = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> CUSTOM_NAME_VISIBLE = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SILENT = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> NO_GRAVITY = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);

    /**
     * Has this entity been added to the chunk its within
     */
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public long serverPosX;
    public long serverPosY;
    public long serverPosZ;

    /**
     * Render entity even if it is outside the camera frustum. Only true in EntityFish for now. Used in RenderGlobal:
     * render if ignoreFrustumCheck or in frustum.
     */
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;

    /**
     * Whether the entity is inside a Portal
     */
    protected boolean inPortal;
    protected int portalCounter;

    /**
     * Which dimension the player is in (-1 = the Nether, 0 = normal world)
     */
    public int dimension;

    /**
     * The position of the last portal the entity was in
     */
    protected BlockPos lastPortalPos;

    /**
     * A horizontal vector related to the position of the last portal the entity was in
     */
    protected Vec3d lastPortalVec;

    /**
     * A direction related to the position of the last portal the entity was in
     */
    protected EnumFacing teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    protected String cachedUniqueIdString;

    /**
     * The command result statistics for this Entity.
     */
    private final CommandResultStats cmdResultStats;
    protected boolean glowing;
    private final Set<String> tags;
    private boolean isPositionDirty;
    private final double[] field_191505_aI;
    private long field_191506_aJ;

    public Entity(World worldIn) {
        entityId = nextEntityID++;
        riddenByEntities = Lists.newArrayList();
        boundingBox = ZERO_AABB;
        width = 0.6F;
        height = 1.8F;
        nextStepDistance = 1;
        field_191959_ay = 1.0F;
        rand = new Random();
        field_190534_ay = -func_190531_bD();
        firstUpdate = true;
        entityUniqueID = MathHelper.getRandomUUID(rand);
        cachedUniqueIdString = entityUniqueID.toString();
        cmdResultStats = new CommandResultStats();
        tags = Sets.newHashSet();
        field_191505_aI = new double[]{0.0D, 0.0D, 0.0D};
        world = worldIn;
        setPosition(0.0D, 0.0D, 0.0D);

        if (worldIn != null) {
            dimension = worldIn.provider.getDimensionType().getId();
        }

        dataManager = new EntityDataManager(this);
        dataManager.register(FLAGS, Byte.valueOf((byte) 0));
        dataManager.register(AIR, Integer.valueOf(300));
        dataManager.register(CUSTOM_NAME_VISIBLE, Boolean.valueOf(false));
        dataManager.register(CUSTOM_NAME, "");
        dataManager.register(SILENT, Boolean.valueOf(false));
        dataManager.register(NO_GRAVITY, Boolean.valueOf(false));
        entityInit();
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int id) {
        entityId = id;
    }

    public Set<String> getTags() {
        return tags;
    }

    public boolean addTag(String tag) {
        if (tags.size() >= 1024) {
            return false;
        } else {
            tags.add(tag);
            return true;
        }
    }

    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    /**
     * Called by the /kill command.
     */
    public void onKillCommand() {
        setDead();
    }

    protected abstract void entityInit();

    public EntityDataManager getDataManager() {
        return dataManager;
    }

    public boolean equals(Object p_equals_1_) {
        if (p_equals_1_ instanceof Entity) {
            return ((Entity) p_equals_1_).entityId == entityId;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return entityId;
    }

    /**
     * Keeps moving the entity up so it isn't colliding with blocks and other requirements for this entity to be spawned
     * (only actually used on players though its also on Entity)
     */
    protected void preparePlayerToSpawn() {
        if (world != null) {
            while (posY > 0.0D && posY < 256.0D) {
                setPosition(posX, posY, posZ);

                if (world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty()) {
                    break;
                }

                ++posY;
            }

            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
            rotationPitch = 0.0F;
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead() {
        isDead = true;
    }

    /**
     * Sets whether this entity should drop its items when setDead() is called. This applies to container minecarts.
     */
    public void setDropItemsWhenDead(boolean dropWhenDead) {
    }

    /**
     * Sets the width and height of the entity.
     */
    protected void setSize(float width, float height) {
        if (width != this.width || height != this.height) {
            float f = this.width;
            this.width = width;
            this.height = height;

            if (this.width < f) {
                double d0 = (double) width / 2.0D;
                setEntityBoundingBox(new AxisAlignedBB(posX - d0, posY, posZ - d0, posX + d0, posY + (double) this.height, posZ + d0));
                return;
            }

            AxisAlignedBB axisalignedbb = getEntityBoundingBox();
            setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) this.width, axisalignedbb.minY + (double) this.height, axisalignedbb.minZ + (double) this.width));

            if (this.width > f && !firstUpdate && !world.isRemote) {
                moveEntity(MoverType.SELF, f - this.width, 0.0D, f - this.width);
            }
        }
    }

    /**
     * Sets the rotation of the entity.
     */
    protected void setRotation(float yaw, float pitch) {
        rotationYaw = yaw % 360.0F;
        rotationPitch = pitch % 360.0F;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z) {
        posX = x;
        posY = y;
        posZ = z;
        float f = (width) / 2.0F;
        float f1 = height;
        setEntityBoundingBox(new AxisAlignedBB(x - (double) f, y, z - (double) f, x + (double) f, y + (double) f1, z + (double) f));
    }

    /**
     * Adds 15% to the entity's yaw and subtracts 15% from the pitch. Clamps pitch from -90 to 90. Both arguments in
     * degrees.
     */
    public void setAngles(float yaw, float pitch) {
        float f = rotationPitch;
        float f1 = rotationYaw;
        rotationYaw = (float) ((double) rotationYaw + (double) yaw * 0.15D);
        rotationPitch = (float) ((double) rotationPitch - (double) pitch * 0.15D);
        rotationPitch = MathHelper.clamp(rotationPitch, -90.0f, 90.0f);
        //this.rotationPitch = MathHelper.clamp(this.rotationPitch, -90.0F, 90.0F);
        prevRotationPitch += rotationPitch - f;
        prevRotationYaw += rotationYaw - f1;

        if (ridingEntity != null) {
            ridingEntity.applyOrientationToEntity(this);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        if (!world.isRemote) {
            setFlag(6, isGlowing());
        }

        onEntityUpdate();
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate() {
        world.theProfiler.startSection("entityBaseTick");

        if (isRiding() && getRidingEntity().isDead) {
            dismountRidingEntity();
        }

        if (rideCooldown > 0) {
            --rideCooldown;
        }

        prevDistanceWalkedModified = distanceWalkedModified;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        prevRotationPitch = rotationPitch;
        prevRotationYaw = rotationYaw;

        if (!world.isRemote && world instanceof WorldServer) {
            world.theProfiler.startSection("portal");

            if (inPortal) {
                MinecraftServer minecraftserver = world.getInstanceServer();

                if (minecraftserver.getAllowNether()) {
                    if (!isRiding()) {
                        int i = getMaxInPortalTime();

                        if (portalCounter++ >= i) {
                            portalCounter = i;
                            timeUntilPortal = getPortalCooldown();
                            int j;

                            if (world.provider.getDimensionType().getId() == -1) {
                                j = 0;
                            } else {
                                j = -1;
                            }

                            changeDimension(j);
                        }
                    }

                    inPortal = false;
                }
            } else {
                if (portalCounter > 0) {
                    portalCounter -= 4;
                }

                if (portalCounter < 0) {
                    portalCounter = 0;
                }
            }

            decrementTimeUntilPortal();
            world.theProfiler.endSection();
        }

        spawnRunningParticles();
        handleWaterMovement();

        if (world.isRemote) {
            extinguish();
        } else if (field_190534_ay > 0) {
            if (isImmuneToFire) {
                field_190534_ay -= 4;

                if (field_190534_ay < 0) {
                    extinguish();
                }
            } else {
                if (field_190534_ay % 20 == 0) {
                    attackEntityFrom(DamageSource.onFire, 1.0F);
                }

                --field_190534_ay;
            }
        }

        if (isInLava()) {
            setOnFireFromLava();
            fallDistance *= 0.5F;
        }

        if (posY < -64.0D) {
            kill();
        }

        if (!world.isRemote) {
            setFlag(0, field_190534_ay > 0);
        }

        firstUpdate = false;
        world.theProfiler.endSection();
    }

    /**
     * Decrements the counter for the remaining time until the entity may use a portal again.
     */
    protected void decrementTimeUntilPortal() {
        if (timeUntilPortal > 0) {
            --timeUntilPortal;
        }
    }

    /**
     * Return the amount of time this entity should stay in a portal before being transported.
     */
    public int getMaxInPortalTime() {
        return 1;
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    protected void setOnFireFromLava() {
        if (!isImmuneToFire) {
            attackEntityFrom(DamageSource.lava, 4.0F);
            setFire(15);
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    public void setFire(int seconds) {
        int i = seconds * 20;

        if (this instanceof EntityLivingBase) {
            i = EnchantmentProtection.getFireTimeForEntity((EntityLivingBase) this, i);
        }

        if (field_190534_ay < i) {
            field_190534_ay = i;
        }
    }

    /**
     * Removes fire from entity.
     */
    public void extinguish() {
        field_190534_ay = 0;
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill() {
        setDead();
    }

    /**
     * Checks if the offset position from the entity's current position is inside of a liquid.
     */
    public boolean isOffsetPositionInLiquid(double x, double y, double z) {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(x, y, z);
        return isLiquidPresentInAABB(axisalignedbb);
    }

    /**
     * Determines if a liquid is present within the specified AxisAlignedBB.
     */
    private boolean isLiquidPresentInAABB(AxisAlignedBB bb) {
        return world.getCollisionBoxes(this, bb).isEmpty() && !world.containsAnyLiquid(bb);
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
        EventFullCube eventFullCube = new EventFullCube();
        EventManager.call(eventFullCube);
        if (noClip || eventFullCube.isCancelled()) {
            setEntityBoundingBox(getEntityBoundingBox().offset(p_70091_2_, p_70091_4_, p_70091_6_));
            resetPositionToBB();
        } else {
            if (x == MoverType.PISTON) {
                long i = world.getTotalWorldTime();

                if (i != field_191506_aJ) {
                    Arrays.fill(field_191505_aI, 0.0D);
                    field_191506_aJ = i;
                }

                if (p_70091_2_ != 0.0D) {
                    int j = EnumFacing.Axis.X.ordinal();
                    double d0 = MathHelper.clamp(p_70091_2_ + field_191505_aI[j], -0.51D, 0.51D);
                    p_70091_2_ = d0 - field_191505_aI[j];
                    field_191505_aI[j] = d0;

                    if (Math.abs(p_70091_2_) <= 9.999999747378752E-6D) {
                        return;
                    }
                } else if (p_70091_4_ != 0.0D) {
                    int l4 = EnumFacing.Axis.Y.ordinal();
                    double d12 = MathHelper.clamp(p_70091_4_ + field_191505_aI[l4], -0.51D, 0.51D);
                    p_70091_4_ = d12 - field_191505_aI[l4];
                    field_191505_aI[l4] = d12;

                    if (Math.abs(p_70091_4_) <= 9.999999747378752E-6D) {
                        return;
                    }
                } else {
                    if (p_70091_6_ == 0.0D) {
                        return;
                    }

                    int i5 = EnumFacing.Axis.Z.ordinal();
                    double d13 = MathHelper.clamp(p_70091_6_ + field_191505_aI[i5], -0.51D, 0.51D);
                    p_70091_6_ = d13 - field_191505_aI[i5];
                    field_191505_aI[i5] = d13;

                    if (Math.abs(p_70091_6_) <= 9.999999747378752E-6D) {
                        return;
                    }
                }
            }

            world.theProfiler.startSection("move");
            double d10 = posX;
            double d11 = posY;
            double d1 = posZ;

            if (isInWeb) {
                isInWeb = false;
                p_70091_2_ *= 0.25D;
                p_70091_4_ *= 0.05000000074505806D;
                p_70091_6_ *= 0.25D;
                motionX = 0.0D;
                motionY = 0.0D;
                motionZ = 0.0D;
            }

            double d2 = p_70091_2_;
            double d3 = p_70091_4_;
            double d4 = p_70091_6_;

            EventSafeWalk safeEvent = new EventSafeWalk();
            EventManager.call(safeEvent);

            if (((x == MoverType.SELF || x == MoverType.PLAYER) && onGround && isSneaking() && !Scaffold.isSneaking) || (safeEvent.isCancelled())) {
                for (double d5 = 0.05D; p_70091_2_ != 0.0D && world.getCollisionBoxes(this, getEntityBoundingBox().offset(p_70091_2_, -stepHeight, 0.0D)).isEmpty(); d2 = p_70091_2_) {
                    if (p_70091_2_ < 0.05D && p_70091_2_ >= -0.05D) {
                        p_70091_2_ = 0.0D;
                    } else if (p_70091_2_ > 0.0D) {
                        p_70091_2_ -= 0.05D;
                    } else {
                        p_70091_2_ += 0.05D;
                    }
                }

                for (; p_70091_6_ != 0.0D && world.getCollisionBoxes(this, getEntityBoundingBox().offset(0.0D, -stepHeight, p_70091_6_)).isEmpty(); d4 = p_70091_6_) {
                    if (p_70091_6_ < 0.05D && p_70091_6_ >= -0.05D) {
                        p_70091_6_ = 0.0D;
                    } else if (p_70091_6_ > 0.0D) {
                        p_70091_6_ -= 0.05D;
                    } else {
                        p_70091_6_ += 0.05D;
                    }
                }

                for (; p_70091_2_ != 0.0D && p_70091_6_ != 0.0D && world.getCollisionBoxes(this, getEntityBoundingBox().offset(p_70091_2_, -stepHeight, p_70091_6_)).isEmpty(); d4 = p_70091_6_) {
                    if (p_70091_2_ < 0.05D && p_70091_2_ >= -0.05D) {
                        p_70091_2_ = 0.0D;
                    } else if (p_70091_2_ > 0.0D) {
                        p_70091_2_ -= 0.05D;
                    } else {
                        p_70091_2_ += 0.05D;
                    }

                    d2 = p_70091_2_;

                    if (p_70091_6_ < 0.05D && p_70091_6_ >= -0.05D) {
                        p_70091_6_ = 0.0D;
                    } else if (p_70091_6_ > 0.0D) {
                        p_70091_6_ -= 0.05D;
                    } else {
                        p_70091_6_ += 0.05D;
                    }
                }
            }

            List<AxisAlignedBB> list1 = world.getCollisionBoxes(this, getEntityBoundingBox().addCoord(p_70091_2_, p_70091_4_, p_70091_6_));
            AxisAlignedBB axisalignedbb = getEntityBoundingBox();

            if (p_70091_4_ != 0.0D) {
                int k = 0;

                for (int l = list1.size(); k < l; ++k) {
                    p_70091_4_ = list1.get(k).calculateYOffset(getEntityBoundingBox(), p_70091_4_);
                }

                setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, p_70091_4_, 0.0D));
            }

            if (p_70091_2_ != 0.0D) {
                int j5 = 0;

                for (int l5 = list1.size(); j5 < l5; ++j5) {
                    p_70091_2_ = list1.get(j5).calculateXOffset(getEntityBoundingBox(), p_70091_2_);
                }

                if (p_70091_2_ != 0.0D) {
                    setEntityBoundingBox(getEntityBoundingBox().offset(p_70091_2_, 0.0D, 0.0D));
                }
            }

            if (p_70091_6_ != 0.0D) {
                int k5 = 0;

                for (int i6 = list1.size(); k5 < i6; ++k5) {
                    p_70091_6_ = list1.get(k5).calculateZOffset(getEntityBoundingBox(), p_70091_6_);
                }

                if (p_70091_6_ != 0.0D) {
                    setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, p_70091_6_));
                }
            }

            boolean flag = onGround || d3 != p_70091_4_ && d3 < 0.0D;

            EventStep eventStep = new EventStep(true, stepHeight);
            if (this == Minecraft.player) {
                EventManager.call(eventStep);
            }
            if (eventStep.getStepHeight() > 0.0F && flag && (d2 != p_70091_2_ || d4 != p_70091_6_)) {
                double d14 = p_70091_2_;
                double d6 = p_70091_4_;
                double d7 = p_70091_6_;
                AxisAlignedBB axisalignedbb1 = getEntityBoundingBox();
                setEntityBoundingBox(axisalignedbb);
                p_70091_4_ = eventStep.getStepHeight();
                List<AxisAlignedBB> list = world.getCollisionBoxes(this, getEntityBoundingBox().addCoord(d2, p_70091_4_, d4));
                AxisAlignedBB axisalignedbb2 = getEntityBoundingBox();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.addCoord(d2, 0.0D, d4);
                double d8 = p_70091_4_;
                int j1 = 0;

                for (int k1 = list.size(); j1 < k1; ++j1) {
                    d8 = list.get(j1).calculateYOffset(axisalignedbb3, d8);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, d8, 0.0D);
                double d18 = d2;
                int l1 = 0;

                for (int i2 = list.size(); l1 < i2; ++l1) {
                    d18 = list.get(l1).calculateXOffset(axisalignedbb2, d18);
                }

                axisalignedbb2 = axisalignedbb2.offset(d18, 0.0D, 0.0D);
                double d19 = d4;
                int j2 = 0;

                for (int k2 = list.size(); j2 < k2; ++j2) {
                    d19 = list.get(j2).calculateZOffset(axisalignedbb2, d19);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, 0.0D, d19);
                AxisAlignedBB axisalignedbb4 = getEntityBoundingBox();
                double d20 = p_70091_4_;
                int l2 = 0;

                for (int i3 = list.size(); l2 < i3; ++l2) {
                    d20 = list.get(l2).calculateYOffset(axisalignedbb4, d20);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d20, 0.0D);
                double d21 = d2;
                int j3 = 0;

                for (int k3 = list.size(); j3 < k3; ++j3) {
                    d21 = list.get(j3).calculateXOffset(axisalignedbb4, d21);
                }

                axisalignedbb4 = axisalignedbb4.offset(d21, 0.0D, 0.0D);
                double d22 = d4;
                int l3 = 0;

                for (int i4 = list.size(); l3 < i4; ++l3) {
                    d22 = list.get(l3).calculateZOffset(axisalignedbb4, d22);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d22);
                double d23 = d18 * d18 + d19 * d19;
                double d9 = d21 * d21 + d22 * d22;

                if (d23 > d9) {
                    p_70091_2_ = d18;
                    p_70091_6_ = d19;
                    p_70091_4_ = -d8;
                    setEntityBoundingBox(axisalignedbb2);
                } else {
                    p_70091_2_ = d21;
                    p_70091_6_ = d22;
                    p_70091_4_ = -d20;
                    setEntityBoundingBox(axisalignedbb4);
                }

                int j4 = 0;

                for (int k4 = list.size(); j4 < k4; ++j4) {
                    p_70091_4_ = list.get(j4).calculateYOffset(getEntityBoundingBox(), p_70091_4_);
                }

                setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, p_70091_4_, 0.0D));

                if (d14 * d14 + d7 * d7 >= p_70091_2_ * p_70091_2_ + p_70091_6_ * p_70091_6_) {
                    p_70091_2_ = d14;
                    p_70091_4_ = d6;
                    p_70091_6_ = d7;
                    setEntityBoundingBox(axisalignedbb1);
                }

                EventStep eventStep1 = new EventStep(false, stepHeight);
                if (this == Minecraft.player) {
                    EventManager.call(eventStep1);
                }
            }

            world.theProfiler.endSection();
            world.theProfiler.startSection("rest");
            resetPositionToBB();
            isCollidedHorizontally = d2 != p_70091_2_ || d4 != p_70091_6_;
            isCollidedVertically = d3 != p_70091_4_;
            onGround = isCollidedVertically && d3 < 0.0D;
            isCollided = isCollidedHorizontally || isCollidedVertically;
            int j6 = MathHelper.floor(posX);
            int i1 = MathHelper.floor(posY - 0.20000000298023224D);
            int k6 = MathHelper.floor(posZ);
            BlockPos blockpos = new BlockPos(j6, i1, k6);
            IBlockState iblockstate = world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == Material.AIR) {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate1 = world.getBlockState(blockpos1);
                Block block1 = iblockstate1.getBlock();

                if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate) {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            updateFallState(p_70091_4_, onGround, iblockstate, blockpos);

            if (d2 != p_70091_2_) {
                motionX = 0.0D;
            }

            if (d4 != p_70091_6_) {
                motionZ = 0.0D;
            }

            Block block = iblockstate.getBlock();

            if (d3 != p_70091_4_) {
                block.onLanded(world, this);
            }

            if (canTriggerWalking() && (!onGround || !isSneaking() || !(this instanceof EntityPlayer)) && !isRiding()) {
                double d15 = posX - d10;
                double d16 = posY - d11;
                double d17 = posZ - d1;

                if (block != Blocks.LADDER) {
                    d16 = 0.0D;
                }

                if (block != null && onGround) {
                    block.onEntityWalk(world, blockpos, this);
                }

                distanceWalkedModified = (float) ((double) distanceWalkedModified + (double) MathHelper.sqrt(d15 * d15 + d17 * d17) * 0.6D);
                distanceWalkedOnStepModified = (float) ((double) distanceWalkedOnStepModified + (double) MathHelper.sqrt(d15 * d15 + d16 * d16 + d17 * d17) * 0.6D);

                if (distanceWalkedOnStepModified > (float) nextStepDistance && iblockstate.getMaterial() != Material.AIR) {
                    nextStepDistance = (int) distanceWalkedOnStepModified + 1;

                    if (isInWater()) {
                        Entity entity = isBeingRidden() && getControllingPassenger() != null ? getControllingPassenger() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        float f1 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224D + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224D) * f;

                        if (f1 > 1.0F) {
                            f1 = 1.0F;
                        }

                        playSound(getSwimSound(), f1, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                    } else {
                        playStepSound(blockpos, block);
                    }
                } else if (distanceWalkedOnStepModified > field_191959_ay && func_191957_ae() && iblockstate.getMaterial() == Material.AIR) {
                    field_191959_ay = func_191954_d(distanceWalkedOnStepModified);
                }
            }

            try {
                doBlockCollisions();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag1 = isWet();

            if (world.isFlammableWithin(getEntityBoundingBox().contract(0.001D))) {
                dealFireDamage(1);

                if (!flag1) {
                    ++field_190534_ay;

                    if (field_190534_ay == 0) {
                        setFire(8);
                    }
                }
            } else if (field_190534_ay <= 0) {
                field_190534_ay = -func_190531_bD();
            }

            if (flag1 && isBurning()) {
                playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
                field_190534_ay = -func_190531_bD();
            }

            world.theProfiler.endSection();
        }
    }

    /**
     * Resets the entity's position to the center (planar) and bottom (vertical) points of its bounding box.
     */
    public void resetPositionToBB() {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        posY = axisalignedbb.minY;
        posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_GENERIC_SWIM;
    }

    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_GENERIC_SPLASH;
    }

    protected void doBlockCollisions() {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain();

        if (world.isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1)) {
            for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); ++i) {
                for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); ++j) {
                    for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1.getZ(); ++k) {
                        blockpos$pooledmutableblockpos2.setPos(i, j, k);
                        IBlockState iblockstate = world.getBlockState(blockpos$pooledmutableblockpos2);

                        try {
                            iblockstate.getBlock().onEntityCollidedWithBlock(world, blockpos$pooledmutableblockpos2, iblockstate, this);
                            func_191955_a(iblockstate);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.release();
        blockpos$pooledmutableblockpos1.release();
        blockpos$pooledmutableblockpos2.release();
    }

    protected void func_191955_a(IBlockState p_191955_1_) {
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        SoundType soundtype = blockIn.getSoundType();

        if (world.getBlockState(pos.up()).getBlock() == Blocks.SNOW_LAYER) {
            soundtype = Blocks.SNOW_LAYER.getSoundType();
            playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
        } else if (!blockIn.getDefaultState().getMaterial().isLiquid()) {
            playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }

    protected float func_191954_d(float p_191954_1_) {
        return 0.0F;
    }

    protected boolean func_191957_ae() {
        return false;
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!isSilent()) {
            world.playSound(null, posX, posY, posZ, soundIn, getSoundCategory(), volume, pitch);
        }
    }

    /**
     * @return True if this entity will not play sounds
     */
    public boolean isSilent() {
        return dataManager.get(SILENT).booleanValue();
    }

    /**
     * When set to true the entity will not play sounds.
     */
    public void setSilent(boolean isSilent) {
        dataManager.set(SILENT, Boolean.valueOf(isSilent));
    }

    public boolean hasNoGravity() {
        return dataManager.get(NO_GRAVITY).booleanValue();
    }

    public void setNoGravity(boolean noGravity) {
        dataManager.set(NO_GRAVITY, Boolean.valueOf(noGravity));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return true;
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if (onGroundIn) {
            if (fallDistance > 0.0F) {
                state.getBlock().onFallenUpon(world, pos, this, fallDistance);
            }

            fallDistance = 0.0F;
        } else if (y < 0.0D) {
            fallDistance = (float) ((double) fallDistance - y);
        }
    }

    @Nullable

    /**
     * Returns the collision bounding box for this entity
     */
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    /**
     * Will deal the specified amount of fire damage to the entity if the entity isn't immune to fire damage.
     */
    protected void dealFireDamage(int amount) {
        if (!isImmuneToFire) {
            attackEntityFrom(DamageSource.inFire, (float) amount);
        }
    }

    public final boolean isImmuneToFire() {
        return isImmuneToFire;
    }

    public void fall(float distance, float damageMultiplier) {
        if (isBeingRidden()) {
            for (Entity entity : getPassengers()) {
                entity.fall(distance, damageMultiplier);
            }
        }
    }

    /**
     * Checks if this entity is either in water or on an open air block in rain (used in wolves).
     */
    public boolean isWet() {
        if (inWater) {
            return true;
        } else {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(posX, posY, posZ);

            if (!world.isRainingAt(blockpos$pooledmutableblockpos) && !world.isRainingAt(blockpos$pooledmutableblockpos.setPos(posX, posY + (double) height, posZ))) {
                blockpos$pooledmutableblockpos.release();
                return false;
            } else {
                blockpos$pooledmutableblockpos.release();
                return true;
            }
        }
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater() {
        boolean a;
        if (MoonWare.featureManager.getFeatureByClass(AntiWaterCollide.class).getState()) {
            a =false;
        }else{
            a = inWater;
        }
        return a;
    }

    public boolean func_191953_am() {
        return world.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -20.0D, 0.0D).contract(0.001D), Material.WATER, this);
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement() {
        if (getRidingEntity() instanceof EntityBoat) {
            inWater = false;
        } else if (world.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D), Material.WATER, this)) {
            if (!inWater && !firstUpdate) {
                resetHeight();
            }

            fallDistance = 0.0F;
            inWater = true;
            extinguish();
        } else {
            inWater = false;
        }

        return inWater;
    }

    /**
     * sets the players height back to normal after doing things like sleeping and dieing
     */
    protected void resetHeight() {
        Entity entity = isBeingRidden() && getControllingPassenger() != null ? getControllingPassenger() : this;
        float f = entity == this ? 0.2F : 0.9F;
        float f1 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224D + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224D) * f;

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        playSound(getSplashSound(), f1, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
        float f2 = (float) MathHelper.floor(getEntityBoundingBox().minY);

        for (int i = 0; (float) i < 1.0F + width * 20.0F; ++i) {
            float f3 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            float f4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + (double) f3, f2 + 1.0F, posZ + (double) f4, motionX, motionY - (double) (rand.nextFloat() * 0.2F), motionZ);
        }

        for (int j = 0; (float) j < 1.0F + width * 20.0F; ++j) {
            float f5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            float f6 = (rand.nextFloat() * 2.0F - 1.0F) * width;
            world.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX + (double) f5, f2 + 1.0F, posZ + (double) f6, motionX, motionY, motionZ);
        }
    }

    /**
     * Attempts to create sprinting particles if the entity is sprinting and not in water.
     */
    public void spawnRunningParticles() {
        if (isSprinting() && !isInWater()) {
            createRunningParticles();
        }
    }

    protected void createRunningParticles() {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(posY - 0.20000000298023224D);
        int k = MathHelper.floor(posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = world.getBlockState(blockpos);

        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + ((double) rand.nextFloat() - 0.5D) * (double) width, getEntityBoundingBox().minY + 0.1D, posZ + ((double) rand.nextFloat() - 0.5D) * (double) width, -motionX * 4.0D, 1.5D, -motionZ * 4.0D, Block.getStateId(iblockstate));
        }
    }

    /**
     * Checks if the current block the entity is within of the specified material type
     */
    public boolean isInsideOfMaterial(Material materialIn) {
        if (getRidingEntity() instanceof EntityBoat) {
            return false;
        } else {
            double d0 = posY + (double) getEyeHeight();
            BlockPos blockpos = new BlockPos(posX, d0, posZ);
            IBlockState iblockstate = world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == materialIn) {
                float f = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111F;
                float f1 = (float) (blockpos.getY() + 1) - f;
                boolean flag = d0 < (double) f1;
                return (flag || !(this instanceof EntityPlayer)) && flag;
            } else {
                return false;
            }
        }
    }

    public boolean isInLava() {
        return world.isMaterialInBB(getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
    }

    public void moveFlying(float p_191958_1_, float p_191958_2_, float p_191958_3_, float p_191958_4_) {
        EventStrafe eventStrafe = new EventStrafe(rotationYaw, p_191958_1_, p_191958_3_, p_191958_4_);
        EventManager.call(eventStrafe);
        if (eventStrafe.isCancelled())
            return;
        float f = p_191958_1_ * p_191958_1_ + p_191958_2_ * p_191958_2_ + p_191958_3_ * p_191958_3_;
        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt(f);
            if (f < 1.0F) {
                f = 1.0F;
            }
            f = p_191958_4_ / f;
            p_191958_1_ = p_191958_1_ * f;
            p_191958_2_ = p_191958_2_ * f;
            p_191958_3_ = p_191958_3_ * f;
            float f1 = MathHelper.sin(eventStrafe.getYaw() * (float) Math.PI / 180.0F);
            float f2 = MathHelper.cos(eventStrafe.getYaw() * (float) Math.PI / 180.0F);
            motionX += p_191958_1_ * f2 - p_191958_3_ * f1;
            motionY += p_191958_2_;
            motionZ += p_191958_3_ * f2 + p_191958_1_ * f1;
        }
    }

    public int getBrightnessForRender() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(posX), 0, MathHelper.floor(posZ));

        if (world.isBlockLoaded(blockpos$mutableblockpos)) {
            blockpos$mutableblockpos.setY(MathHelper.floor(posY + (double) getEyeHeight()));
            return world.getCombinedLight(blockpos$mutableblockpos, 0);
        } else {
            return 0;
        }
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(posX), 0, MathHelper.floor(posZ));

        if (world.isBlockLoaded(blockpos$mutableblockpos)) {
            blockpos$mutableblockpos.setY(MathHelper.floor(posY + (double) getEyeHeight()));
            return world.getLightBrightness(blockpos$mutableblockpos);
        } else {
            return 0.0F;
        }
    }

    /**
     * Sets the reference to the World object.
     */
    public void setWorld(World worldIn) {
        world = worldIn;
    }

    /**
     * Sets the entity's position and rotation.
     */
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        posX = MathHelper.clamp(x, -3.0E7D, 3.0E7D);
        posY = y;
        posZ = MathHelper.clamp(z, -3.0E7D, 3.0E7D);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        rotationYaw = yaw;
        rotationPitch = pitch;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        double d0 = prevRotationYaw - yaw;

        if (d0 < -180.0D) {
            prevRotationYaw += 360.0F;
        }

        if (d0 >= 180.0D) {
            prevRotationYaw -= 360.0F;
        }

        setPosition(posX, posY, posZ);
        setRotation(yaw, pitch);
    }

    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {
        setLocationAndAngles((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        posX = x;
        posY = y;
        posZ = z;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        rotationYaw = yaw;
        rotationPitch = pitch;
        setPosition(posX, posY, posZ);
    }

    /**
     * Returns the distance to the entity.
     */
    public float getDistanceToEntity(Entity entityIn) {
        float f = (float) (posX - entityIn.posX);
        float f1 = (float) (posY - entityIn.posY);
        float f2 = (float) (posZ - entityIn.posZ);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public float getDistanceToTileEntity(TileEntity entityIn) {
        float f = (float) (posX - entityIn.getPos().getX());
        float f1 = (float) (posY - entityIn.getPos().getY());
        float f2 = (float) (posZ - entityIn.getPos().getZ());
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    /**
     * Gets the squared distance to the position.
     */
    public double getDistanceSq(double x, double y, double z) {
        double d0 = posX - x;
        double d1 = posY - y;
        double d2 = posZ - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getDistanceSq(BlockPos pos) {
        return pos.distanceSq(posX, posY, posZ);
    }

    public double getDistanceSqToCenter(BlockPos pos) {
        return pos.distanceSqToCenter(posX, posY, posZ);
    }

    /**
     * Gets the distance to the position.
     */
    public double getDistance(double x, double y, double z) {
        double d0 = posX - x;
        double d1 = posY - y;
        double d2 = posZ - z;
        return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * Returns the squared distance to the entity.
     */
    public double getDistanceSqToEntity(Entity entityIn) {
        double d0 = posX - entityIn.posX;
        double d1 = posY - entityIn.posY;
        double d2 = posZ - entityIn.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn) {
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn) {
        if (MoonWare.featureManager.getFeatureByClass(AntiPush.class).getState() && AntiPush.players.getBoolValue())
            return;

        if (!isRidingSameEntity(entityIn)) {
            if (!entityIn.noClip && !noClip) {
                double d0 = entityIn.posX - posX;
                double d1 = entityIn.posZ - posZ;
                double d2 = MathHelper.absMax(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = MathHelper.sqrt(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (double) (1.0F - entityCollisionReduction);
                    d1 = d1 * (double) (1.0F - entityCollisionReduction);

                    if (!isBeingRidden()) {
                        addVelocity(-d0, 0.0D, -d1);
                    }

                    if (!entityIn.isBeingRidden()) {
                        entityIn.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }

    /**
     * Adds to the current velocity of the entity.
     */
    public void addVelocity(double x, double y, double z) {
        motionX += x;
        motionY += y;
        motionZ += z;
        isAirBorne = true;
    }

    /**
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked() {
        velocityChanged = true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        } else {
            setBeenAttacked();
            return false;
        }
    }

    /**
     * interpolated look vector
     */
    public Vec3d getLook(float partialTicks) {
        if (partialTicks == 1.0F) {
            return getVectorForRotation(rotationPitch, rotationYaw);
        } else {
            float f = prevRotationPitch + (rotationPitch - prevRotationPitch) * partialTicks;
            float f1 = prevRotationYaw + (rotationYaw - prevRotationYaw) * partialTicks;
            return getVectorForRotation(f, f1);
        }
    }

    /**
     * Creates a Vec3 using the pitch and yaw of the entities rotation.
     */
    public static final Vec3d getVectorForRotation(float pitch, float yaw) {
        VectorRotationEvent event = new VectorRotationEvent(yaw, pitch);
        EventManager.call(event);
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public Vec3d getPositionEyes(float partialTicks) {
        if (partialTicks == 1.0F) {
            return new Vec3d(posX, posY + (double) getEyeHeight(), posZ);
        } else {
            double d0 = prevPosX + (posX - prevPosX) * (double) partialTicks;
            double d1 = prevPosY + (posY - prevPosY) * (double) partialTicks + (double) getEyeHeight();
            double d2 = prevPosZ + (posZ - prevPosZ) * (double) partialTicks;
            return new Vec3d(d0, d1, d2);
        }
    }

    @Nullable
    public RayTraceResult rayTrace(double blockReachDistance, float partialTicks) {
        Vec3d vec3d = getPositionEyes(partialTicks);
        Vec3d vec3d1 = getLook(partialTicks);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * blockReachDistance, vec3d1.yCoord * blockReachDistance, vec3d1.zCoord * blockReachDistance);
        return world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }
    public boolean canEntityBeSeen(Entity entityIn)
    {
        return world.rayTraceBlocks(new Vec3d(posX, posY + (double) getEyeHeight(), posZ), new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null;
    }
    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed() {
        return false;
    }

    public void func_191956_a(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_) {
        if (p_191956_1_ instanceof EntityPlayerMP) {
            CriteriaTriggers.field_192123_c.func_192211_a((EntityPlayerMP) p_191956_1_, this, p_191956_3_);
        }
    }

    public boolean isInRangeToRender3d(double x, double y, double z) {
        double d0 = posX - x;
        double d1 = posY - y;
        double d2 = posZ - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return isInRangeToRenderDist(d3);
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * renderDistanceWeight;
        return distance < d0 * d0;
    }

    /**
     * Attempts to write this Entity to the given NBTTagCompound. Returns false if the entity is dead or its string
     * representation is null. In this event, the given NBTTagCompound is not modified.
     * <p>
     * Similar to writeToNBTOptional, but does not check whether this Entity is a passenger of another.
     */
    public boolean writeToNBTAtomically(NBTTagCompound compound) {
        String s = getEntityString();

        if (!isDead && s != null) {
            compound.setString("id", s);
            writeToNBT(compound);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Either write this entity to the NBT tag given and return true, or return false without doing anything. If this
     * returns false the entity is not saved on disk. Ridden entities return false here as they are saved with their
     * rider.
     */
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        String s = getEntityString();

        if (!isDead && s != null && !isRiding()) {
            compound.setString("id", s);
            writeToNBT(compound);
            return true;
        } else {
            return false;
        }
    }

    public static void func_190533_a(DataFixer p_190533_0_) {
        p_190533_0_.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
                if (compound.hasKey("Passengers", 9)) {
                    NBTTagList nbttaglist = compound.getTagList("Passengers", 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        nbttaglist.set(i, fixer.process(FixTypes.ENTITY, nbttaglist.getCompoundTagAt(i), versionIn));
                    }
                }

                return compound;
            }
        });
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        try {
            compound.setTag("Pos", newDoubleNBTList(posX, posY, posZ));
            compound.setTag("Motion", newDoubleNBTList(motionX, motionY, motionZ));
            compound.setTag("Rotation", newFloatNBTList(rotationYaw, rotationPitch));
            compound.setFloat("FallDistance", fallDistance);
            compound.setShort("Fire", (short) field_190534_ay);
            compound.setShort("Air", (short) getAir());
            compound.setBoolean("OnGround", onGround);
            compound.setInteger("Dimension", dimension);
            compound.setBoolean("Invulnerable", invulnerable);
            compound.setInteger("PortalCooldown", timeUntilPortal);
            compound.setUniqueId("UUID", getUniqueID());

            if (hasCustomName()) {
                compound.setString("CustomName", getCustomNameTag());
            }

            if (getAlwaysRenderNameTag()) {
                compound.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
            }

            cmdResultStats.writeStatsToNBT(compound);

            if (isSilent()) {
                compound.setBoolean("Silent", isSilent());
            }

            if (hasNoGravity()) {
                compound.setBoolean("NoGravity", hasNoGravity());
            }

            if (glowing) {
                compound.setBoolean("Glowing", glowing);
            }

            if (!tags.isEmpty()) {
                NBTTagList nbttaglist = new NBTTagList();

                for (String s : tags) {
                    nbttaglist.appendTag(new NBTTagString(s));
                }

                compound.setTag("Tags", nbttaglist);
            }

            writeEntityToNBT(compound);

            if (isBeingRidden()) {
                NBTTagList nbttaglist1 = new NBTTagList();

                for (Entity entity : getPassengers()) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    if (entity.writeToNBTAtomically(nbttagcompound)) {
                        nbttaglist1.appendTag(nbttagcompound);
                    }
                }

                if (!nbttaglist1.hasNoTags()) {
                    compound.setTag("Passengers", nbttaglist1);
                }
            }

            return compound;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
            addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readFromNBT(NBTTagCompound compound) {
        try {
            NBTTagList nbttaglist = compound.getTagList("Pos", 6);
            NBTTagList nbttaglist2 = compound.getTagList("Motion", 6);
            NBTTagList nbttaglist3 = compound.getTagList("Rotation", 5);
            motionX = nbttaglist2.getDoubleAt(0);
            motionY = nbttaglist2.getDoubleAt(1);
            motionZ = nbttaglist2.getDoubleAt(2);

            if (Math.abs(motionX) > 10.0D) {
                motionX = 0.0D;
            }

            if (Math.abs(motionY) > 10.0D) {
                motionY = 0.0D;
            }

            if (Math.abs(motionZ) > 10.0D) {
                motionZ = 0.0D;
            }

            posX = nbttaglist.getDoubleAt(0);
            posY = nbttaglist.getDoubleAt(1);
            posZ = nbttaglist.getDoubleAt(2);
            lastTickPosX = posX;
            lastTickPosY = posY;
            lastTickPosZ = posZ;
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            rotationYaw = nbttaglist3.getFloatAt(0);
            rotationPitch = nbttaglist3.getFloatAt(1);
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
            setRotationYawHead(rotationYaw);
            setRenderYawOffset(rotationYaw);
            fallDistance = compound.getFloat("FallDistance");
            field_190534_ay = compound.getShort("Fire");
            setAir(compound.getShort("Air"));
            onGround = compound.getBoolean("OnGround");

            if (compound.hasKey("Dimension")) {
                dimension = compound.getInteger("Dimension");
            }

            invulnerable = compound.getBoolean("Invulnerable");
            timeUntilPortal = compound.getInteger("PortalCooldown");

            if (compound.hasUniqueId("UUID")) {
                entityUniqueID = compound.getUniqueId("UUID");
                cachedUniqueIdString = entityUniqueID.toString();
            }

            setPosition(posX, posY, posZ);
            setRotation(rotationYaw, rotationPitch);

            if (compound.hasKey("CustomName", 8)) {
                setCustomNameTag(compound.getString("CustomName"));
            }

            setAlwaysRenderNameTag(compound.getBoolean("CustomNameVisible"));
            cmdResultStats.readStatsFromNBT(compound);
            setSilent(compound.getBoolean("Silent"));
            setNoGravity(compound.getBoolean("NoGravity"));
            setGlowing(compound.getBoolean("Glowing"));

            if (compound.hasKey("Tags", 9)) {
                tags.clear();
                NBTTagList nbttaglist1 = compound.getTagList("Tags", 8);
                int i = Math.min(nbttaglist1.tagCount(), 1024);

                for (int j = 0; j < i; ++j) {
                    tags.add(nbttaglist1.getStringTagAt(j));
                }
            }

            readEntityFromNBT(compound);

            if (shouldSetPosAfterLoading()) {
                setPosition(posX, posY, posZ);
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
            addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean shouldSetPosAfterLoading() {
        return true;
    }

    @Nullable

    /**
     * Returns the string that identifies this Entity's class
     */
    protected final String getEntityString() {
        Namespaced resourcelocation = EntityList.func_191301_a(this);
        return resourcelocation == null ? null : resourcelocation.toString();
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected abstract void readEntityFromNBT(NBTTagCompound compound);

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected abstract void writeEntityToNBT(NBTTagCompound compound);

    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    protected NBTTagList newDoubleNBTList(double... numbers) {
        NBTTagList nbttaglist = new NBTTagList();

        for (double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    protected NBTTagList newFloatNBTList(float... numbers) {
        NBTTagList nbttaglist = new NBTTagList();

        for (float f : numbers) {
            nbttaglist.appendTag(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    @Nullable
    public EntityItem dropItem(Item itemIn, int size) {
        return dropItemWithOffset(itemIn, size, 0.0F);
    }

    @Nullable
    public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
        return entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
    }

    @Nullable

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack stack, float offsetY) {
        if (stack.isEmpty()) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(world, posX, posY + (double) offsetY, posZ, stack);
            entityitem.setDefaultPickupDelay();
            world.spawnEntityInWorld(entityitem);
            return entityitem;
        }
    }

    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive() {
        return !isDead;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock() {
        if (noClip) {
            return false;
        } else {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int i = 0; i < 8; ++i) {
                int j = MathHelper.floor(posY + (double) (((float) ((i >> 0) % 2) - 0.5F) * 0.1F) + (double) getEyeHeight());
                int k = MathHelper.floor(posX + (double) (((float) ((i >> 1) % 2) - 0.5F) * width * 0.8F));
                int l = MathHelper.floor(posZ + (double) (((float) ((i >> 2) % 2) - 0.5F) * width * 0.8F));

                if (blockpos$pooledmutableblockpos.getX() != k || blockpos$pooledmutableblockpos.getY() != j || blockpos$pooledmutableblockpos.getZ() != l) {
                    blockpos$pooledmutableblockpos.setPos(k, j, l);

                    if (world.getBlockState(blockpos$pooledmutableblockpos).func_191058_s()) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();
            return false;
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        return false;
    }

    @Nullable

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null;
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden() {
        Entity entity = getRidingEntity();

        if (isRiding() && entity.isDead) {
            dismountRidingEntity();
        } else {
            motionX = 0.0D;
            motionY = 0.0D;
            motionZ = 0.0D;
            onUpdate();

            if (isRiding()) {
                entity.updatePassenger(this);
            }
        }
    }

    public void updatePassenger(Entity passenger) {
        if (isPassenger(passenger)) {
            passenger.setPosition(posX, posY + getMountedYOffset() + passenger.getYOffset(), posZ);
        }
    }

    /**
     * Applies this entity's orientation (pitch/yaw) to another entity. Used to update passenger orientation.
     */
    public void applyOrientationToEntity(Entity entityToUpdate) {
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset() {
        return 0.0D;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset() {
        return (double) height * 0.75D;
    }

    public boolean startRiding(Entity entityIn) {
        return startRiding(entityIn, false);
    }

    public boolean startRiding(Entity entityIn, boolean force) {
        for (Entity entity = entityIn; entity.ridingEntity != null; entity = entity.ridingEntity) {
            if (entity.ridingEntity == this) {
                return false;
            }
        }

        if (force || canBeRidden(entityIn) && entityIn.canFitPassenger(this)) {
            if (isRiding()) {
                dismountRidingEntity();
            }

            ridingEntity = entityIn;
            ridingEntity.addPassenger(this);
            return true;
        } else {
            return false;
        }
    }

    protected boolean canBeRidden(Entity entityIn) {
        return rideCooldown <= 0;
    }

    public void removePassengers() {
        for (int i = riddenByEntities.size() - 1; i >= 0; --i) {
            riddenByEntities.get(i).dismountRidingEntity();
        }
    }

    public void dismountRidingEntity() {
        if (ridingEntity != null) {
            Entity entity = ridingEntity;
            ridingEntity = null;
            entity.removePassenger(this);
        }
    }

    protected void addPassenger(Entity passenger) {
        if (passenger.getRidingEntity() != this) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        } else {
            if (!world.isRemote && passenger instanceof EntityPlayer && !(getControllingPassenger() instanceof EntityPlayer)) {
                riddenByEntities.add(0, passenger);
            } else {
                riddenByEntities.add(passenger);
            }
        }
    }

    protected void removePassenger(Entity passenger) {
        if (passenger.getRidingEntity() == this) {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        } else {
            riddenByEntities.remove(passenger);
            passenger.rideCooldown = 60;
        }
    }

    protected boolean canFitPassenger(Entity passenger) {
        return getPassengers().size() < 1;
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        setPosition(x, y, z);
        setRotation(yaw, pitch);
    }

    public float getCollisionBorderSize() {
        return 0.0F;
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3d getLookVec() {
        return getVectorForRotation(rotationPitch, rotationYaw);
    }

    /**
     * returns the Entity's pitch and yaw as a Vec2f
     */
    public Vec2f getPitchYaw() {
        return new Vec2f(rotationPitch, rotationYaw);
    }

    public Vec3d getForward() {
        return Vec3d.fromPitchYawVector(getPitchYaw());
    }

    /**
     * Marks the entity as being inside a portal, activating teleportation logic in onEntityUpdate() in the following
     * tick(s).
     */
    public void setPortal(BlockPos pos) {
        if (timeUntilPortal > 0) {
            timeUntilPortal = getPortalCooldown();
        } else {
            if (!world.isRemote && !pos.equals(lastPortalPos)) {
                lastPortalPos = new BlockPos(pos);
                BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.PORTAL.createPatternHelper(world, lastPortalPos);
                double d0 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? (double) blockpattern$patternhelper.getFrontTopLeft().getZ() : (double) blockpattern$patternhelper.getFrontTopLeft().getX();
                double d1 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? posZ : posX;
                d1 = Math.abs(MathHelper.pct(d1 - (double) (blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - (double) blockpattern$patternhelper.getWidth()));
                double d2 = MathHelper.pct(posY - 1.0D, blockpattern$patternhelper.getFrontTopLeft().getY(), blockpattern$patternhelper.getFrontTopLeft().getY() - blockpattern$patternhelper.getHeight());
                lastPortalVec = new Vec3d(d1, d2, 0.0D);
                teleportDirection = blockpattern$patternhelper.getForwards();
            }

            inPortal = true;
        }
    }

    /**
     * Return the amount of cooldown before this entity can use a portal again.
     */
    public int getPortalCooldown() {
        return 300;
    }

    /**
     * Updates the velocity of the entity to a new value.
     */
    public void setVelocity(double x, double y, double z) {
        motionX = x;
        motionY = y;
        motionZ = z;
    }

    public void handleStatusUpdate(byte id) {
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation() {
    }

    public Iterable<ItemStack> getHeldEquipment() {
        return field_190535_b;
    }

    public Iterable<ItemStack> getArmorInventoryList() {
        return field_190535_b;
    }

    public Iterable<ItemStack> getEquipmentAndArmor() {
        return Iterables.concat(getHeldEquipment(), getArmorInventoryList());
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning() {
        boolean flag = world != null && world.isRemote;
        return !isImmuneToFire && (field_190534_ay > 0 || flag && getFlag(0));
    }

    public boolean isRiding() {
        return getRidingEntity() != null;
    }

    /**
     * If at least 1 entity is riding this one
     */
    public boolean isBeingRidden() {
        return !getPassengers().isEmpty();
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking() {
        return getFlag(1);
    }

    /**
     * Sets the sneaking flag.
     */
    public void setSneaking(boolean sneaking) {
        setFlag(1, sneaking);
    }

    /**
     * Get if the Entity is sprinting.
     */
    public boolean isSprinting() {
        return getFlag(3);
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        setFlag(3, sprinting);
    }

    public boolean isGlowing() {
        return glowing || world.isRemote && getFlag(6);
    }

    public void setGlowing(boolean glowingIn) {
        glowing = glowingIn;

        if (!world.isRemote) {
            setFlag(6, glowing);
        }
    }

    public boolean isInvisible() {
        return getFlag(5);
    }

    /**
     * Only used by renderer in EntityLivingBase subclasses.
     * Determines if an entity is visible or not to a specfic player, if the entity is normally invisible.
     * For EntityLivingBase subclasses, returning false when invisible will render the entity semitransparent.
     */
    public boolean isInvisibleToPlayer(EntityPlayer player) {
        if (player.isSpectator()) {
            return false;
        } else {
            Team team = getTeam();
            return (team == null || player == null || player.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled()) && isInvisible();
        }
    }

    @Nullable
    public Team getTeam() {
        return world.getScoreboard().getPlayersTeam(getCachedUniqueIdString());
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn) {
        return isOnScoreboardTeam(entityIn.getTeam());
    }

    /**
     * Returns whether this Entity is on the given scoreboard team.
     */
    public boolean isOnScoreboardTeam(Team teamIn) {
        return getTeam() != null && getTeam().isSameTeam(teamIn);
    }

    public void setInvisible(boolean invisible) {
        setFlag(5, invisible);
    }

    /**
     * Returns true if the flag is active for the entity. Known flags: 0) is burning; 1) is sneaking; 2) is riding
     * something; 3) is sprinting; 4) is eating
     */
    protected boolean getFlag(int flag) {
        return (dataManager.get(FLAGS).byteValue() & 1 << flag) != 0;
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    protected void setFlag(int flag, boolean set) {
        byte b0 = dataManager.get(FLAGS).byteValue();

        if (set) {
            dataManager.set(FLAGS, Byte.valueOf((byte) (b0 | 1 << flag)));
        } else {
            dataManager.set(FLAGS, Byte.valueOf((byte) (b0 & ~(1 << flag))));
        }
    }

    public int getAir() {
        return dataManager.get(AIR).intValue();
    }

    public void setAir(int air) {
        dataManager.set(AIR, Integer.valueOf(air));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        attackEntityFrom(DamageSource.lightningBolt, 5.0F);
        ++field_190534_ay;

        if (field_190534_ay == 0) {
            setFire(8);
        }
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn) {
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = x - (double) blockpos.getX();
        double d1 = y - (double) blockpos.getY();
        double d2 = z - (double) blockpos.getZ();

        if (!world.collidesWithAnyBlock(getEntityBoundingBox())) {
            return false;
        } else {
            EnumFacing enumfacing = EnumFacing.UP;
            double d3 = Double.MAX_VALUE;

            if (!world.isBlockFullCube(blockpos.west()) && d0 < d3) {
                d3 = d0;
                enumfacing = EnumFacing.WEST;
            }

            if (!world.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
                d3 = 1.0D - d0;
                enumfacing = EnumFacing.EAST;
            }

            if (!world.isBlockFullCube(blockpos.north()) && d2 < d3) {
                d3 = d2;
                enumfacing = EnumFacing.NORTH;
            }

            if (!world.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
                d3 = 1.0D - d2;
                enumfacing = EnumFacing.SOUTH;
            }

            if (!world.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
                d3 = 1.0D - d1;
                enumfacing = EnumFacing.UP;
            }

            float f = rand.nextFloat() * 0.2F + 0.1F;
            float f1 = (float) enumfacing.getAxisDirection().getOffset();

            if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                motionX = f1 * f;
                motionY *= 0.75D;
                motionZ *= 0.75D;
            } else if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
                motionX *= 0.75D;
                motionY = f1 * f;
                motionZ *= 0.75D;
            } else if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
                motionX *= 0.75D;
                motionY *= 0.75D;
                motionZ = f1 * f;
            }

            return true;
        }
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {
        isInWeb = true;
        fallDistance = 0.0F;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName() {
        if (hasCustomName()) {
            return getCustomNameTag();
        } else {
            String s = EntityList.getEntityString(this);

            if (s == null) {
                s = "generic";
            }

            return I18n.translateToLocal("entity." + s + ".name");
        }
    }

    @Nullable

    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts() {
        return null;
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn) {
        return this == entityIn;
    }

    public float getRotationYawHead() {
        return 0.0F;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation) {
    }

    /**
     * Set the render yaw offset
     */
    public void setRenderYawOffset(float offset) {
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem() {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn) {
        return false;
    }

    public String toString() {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", getClass().getSimpleName(), getName(), entityId, world == null ? "~NULL~" : world.getWorldInfo().getWorldName(), posX, posY, posZ);
    }

    /**
     * Returns whether this Entity is invulnerable to the given DamageSource.
     */
    public boolean isEntityInvulnerable(DamageSource source) {
        return invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer();
    }

    public boolean func_190530_aW() {
        return invulnerable;
    }

    /**
     * Sets whether this Entity is invulnerable.
     */
    public void setEntityInvulnerable(boolean isInvulnerable) {
        invulnerable = isInvulnerable;
    }

    /**
     * Sets this entity's location and angles to the location and angles of the passed in entity.
     */
    public void copyLocationAndAnglesFrom(Entity entityIn) {
        setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }

    /**
     * Prepares this entity in new dimension by copying NBT data from entity in old dimension
     */
    private void copyDataFromOld(Entity entityIn) {
        NBTTagCompound nbttagcompound = entityIn.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("Dimension");
        readFromNBT(nbttagcompound);
        timeUntilPortal = entityIn.timeUntilPortal;
        lastPortalPos = entityIn.lastPortalPos;
        lastPortalVec = entityIn.lastPortalVec;
        teleportDirection = entityIn.teleportDirection;
    }

    @Nullable
    public Entity changeDimension(int dimensionIn) {
        if (!world.isRemote && !isDead) {
            world.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = getServer();
            int i = dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(i);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionIn);
            dimension = dimensionIn;

            if (i == 1 && dimensionIn == 1) {
                worldserver1 = minecraftserver.worldServerForDimension(0);
                dimension = 0;
            }

            world.removeEntity(this);
            isDead = false;
            world.theProfiler.startSection("reposition");
            BlockPos blockpos;

            if (dimensionIn == 1) {
                blockpos = worldserver1.getSpawnCoordinate();
            } else {
                double d0 = posX;
                double d1 = posZ;
                double d2 = 8.0D;

                if (dimensionIn == -1) {
                    d0 = MathHelper.clamp(d0 / 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp(d1 / 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
                } else if (dimensionIn == 0) {
                    d0 = MathHelper.clamp(d0 * 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp(d1 * 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
                }

                d0 = MathHelper.clamp((int) d0, -29999872, 29999872);
                d1 = MathHelper.clamp((int) d1, -29999872, 29999872);
                float f = rotationYaw;
                setLocationAndAngles(d0, posY, d1, 90.0F, 0.0F);
                Teleporter teleporter = worldserver1.getDefaultTeleporter();
                teleporter.placeInExistingPortal(this, f);
                blockpos = new BlockPos(this);
            }

            worldserver.updateEntityWithOptionalForce(this, false);
            world.theProfiler.endStartSection("reloading");
            Entity entity = EntityList.func_191304_a(getClass(), worldserver1);

            if (entity != null) {
                entity.copyDataFromOld(this);

                if (i == 1 && dimensionIn == 1) {
                    BlockPos blockpos1 = worldserver1.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
                } else {
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
                }

                boolean flag = entity.forceSpawn;
                entity.forceSpawn = true;
                worldserver1.spawnEntityInWorld(entity);
                entity.forceSpawn = flag;
                worldserver1.updateEntityWithOptionalForce(entity, false);
            }

            isDead = true;
            world.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            world.theProfiler.endSection();
            return entity;
        } else {
            return null;
        }
    }

    /**
     * Returns false if this Entity is a boss, true otherwise.
     */
    public boolean isNonBoss() {
        return true;
    }

    /**
     * Explosion resistance of a block relative to this entity
     */
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlock().getExplosionResistance(this);
    }

    public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_) {
        return true;
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight() {
        return 3;
    }

    public Vec3d getLastPortalVec() {
        return lastPortalVec;
    }

    public EnumFacing getTeleportDirection() {
        return teleportDirection;
    }

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category) {
        category.setDetail("Entity Type", new ICrashReportDetail<String>() {
            public String call() throws Exception {
                return EntityList.func_191301_a(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", Integer.valueOf(entityId));
        category.setDetail("Entity Name", new ICrashReportDetail<String>() {
            public String call() throws Exception {
                return getName();
            }
        });
        category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", posX, posY, posZ));
        category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ)));
        category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", motionX, motionY, motionZ));
        category.setDetail("Entity's Passengers", new ICrashReportDetail<String>() {
            public String call() throws Exception {
                return getPassengers().toString();
            }
        });
        category.setDetail("Entity's Vehicle", new ICrashReportDetail<String>() {
            public String call() throws Exception {
                return getRidingEntity().toString();
            }
        });
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire() {
        return isBurning();
    }

    public void setUniqueId(UUID uniqueIdIn) {
        entityUniqueID = uniqueIdIn;
        cachedUniqueIdString = entityUniqueID.toString();
    }

    /**
     * Returns the UUID of this entity.
     */
    public UUID getUniqueID() {
        return entityUniqueID;
    }

    public String getCachedUniqueIdString() {
        return cachedUniqueIdString;
    }

    public boolean isPushedByWater() {
        return true;
    }

    public static double getRenderDistanceWeight() {
        return renderDistanceWeight;
    }

    public static void setRenderDistanceWeight(double renderDistWeight) {
        renderDistanceWeight = renderDistWeight;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName() {
        TextComponent textcomponentstring = new TextComponent(ScorePlayerTeam.formatPlayerName(getTeam(), getName()));
        textcomponentstring.getStyle().setHoverEvent(getHoverEvent());
        textcomponentstring.getStyle().setInsertion(getCachedUniqueIdString());
        return textcomponentstring;
    }

    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String name) {
        dataManager.set(CUSTOM_NAME, name);
    }

    public String getCustomNameTag() {
        return dataManager.get(CUSTOM_NAME);
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return !dataManager.get(CUSTOM_NAME).isEmpty();
    }

    public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {
        dataManager.set(CUSTOM_NAME_VISIBLE, Boolean.valueOf(alwaysRenderNameTag));
    }

    public boolean getAlwaysRenderNameTag() {
        return dataManager.get(CUSTOM_NAME_VISIBLE).booleanValue();
    }

    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    public void setPositionAndUpdate(double x, double y, double z) {
        isPositionDirty = true;
        setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
        world.updateEntityWithOptionalForce(this, false);
    }

    public boolean getAlwaysRenderNameTagForRender() {
        return getAlwaysRenderNameTag();
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
    }

    /**
     * Gets the horizontal facing direction of this Entity.
     */
    public EnumFacing getHorizontalFacing() {
        return EnumFacing.getHorizontal(MathHelper.floor((double) (rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
    }

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into
     * account.
     */
    public EnumFacing getAdjustedHorizontalFacing() {
        return getHorizontalFacing();
    }

    protected HoverEvent getHoverEvent() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Namespaced resourcelocation = EntityList.func_191301_a(this);
        nbttagcompound.setString("id", getCachedUniqueIdString());

        if (resourcelocation != null) {
            nbttagcompound.setString("type", resourcelocation.toString());
        }

        nbttagcompound.setString("name", getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new TextComponent(nbttagcompound.toString()));
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP player) {
        return true;
    }

    public AxisAlignedBB getEntityBoundingBox() {
        return boundingBox;
    }

    /**
     * Gets the bounding box of this Entity, adjusted to take auxiliary entities into account (e.g. the tile contained
     * by a minecart, such as a command block).
     */
    public AxisAlignedBB getRenderBoundingBox() {
        return getEntityBoundingBox();
    }

    public void setEntityBoundingBox(AxisAlignedBB bb) {
        boundingBox = bb;
    }

    public float getEyeHeight() {
        return height * 0.85F;
    }

    public boolean isOutsideBorder() {
        return isOutsideBorder;
    }

    public void setOutsideBorder(boolean outsideBorder) {
        isOutsideBorder = outsideBorder;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        return false;
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(Component component) {
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true;
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition() {
        return new BlockPos(posX, posY + 0.5D, posZ);
    }

    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    public Vec3d getPositionVector() {
        return new Vec3d(posX, posY, posZ);
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld() {
        return world;
    }

    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    public Entity getCommandSenderEntity() {
        return this;
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    public boolean sendCommandFeedback() {
        return false;
    }

    public void setCommandStat(CommandResultStats.Type type, int amount) {
        if (world != null && !world.isRemote) {
            cmdResultStats.setCommandStatForSender(world.getInstanceServer(), this, type, amount);
        }
    }

    @Nullable

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer() {
        return world.getInstanceServer();
    }

    public CommandResultStats getCommandStats() {
        return cmdResultStats;
    }

    /**
     * Set the CommandResultStats from the entity
     */
    public void setCommandStats(Entity entityIn) {
        cmdResultStats.addAllStats(entityIn.getCommandStats());
    }

    /**
     * Applies the given player interaction to this Entity.
     */
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand stack) {
        return EnumActionResult.PASS;
    }

    public boolean isImmuneToExplosions() {
        return false;
    }

    protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entityIn, entityLivingBaseIn);
        }

        EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
    }

    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    public void addTrackingPlayer(EntityPlayerMP player) {
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player) {
    }

    /**
     * Transforms the entity's current yaw with the given Rotation and returns it. This does not have a side-effect.
     */
    public float getRotatedYaw(Rotation transformRotation) {
        float f = MathHelper.wrapDegrees(rotationYaw);

        switch (transformRotation) {
            case CLOCKWISE_180:
                return f + 180.0F;

            case COUNTERCLOCKWISE_90:
                return f + 270.0F;

            case CLOCKWISE_90:
                return f + 90.0F;

            default:
                return f;
        }
    }

    /**
     * Transforms the entity's current yaw with the given Mirror and returns it. This does not have a side-effect.
     */
    public float getMirroredYaw(Mirror transformMirror) {
        float f = MathHelper.wrapDegrees(rotationYaw);

        switch (transformMirror) {
            case LEFT_RIGHT:
                return -f;

            case FRONT_BACK:
                return 180.0F - f;

            default:
                return f;
        }
    }

    public boolean ignoreItemEntityData() {
        return false;
    }

    public boolean setPositionNonDirty() {
        boolean flag = isPositionDirty;
        isPositionDirty = false;
        return flag;
    }

    @Nullable

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    public Entity getControllingPassenger() {
        return null;
    }

    public List<Entity> getPassengers() {
        return riddenByEntities.isEmpty() ? Collections.emptyList() : Lists.newArrayList(riddenByEntities);
    }

    public boolean isPassenger(Entity entityIn) {
        for (Entity entity : getPassengers()) {
            if (entity.equals(entityIn)) {
                return true;
            }
        }

        return false;
    }

    public Collection<Entity> getRecursivePassengers() {
        Set<Entity> set = Sets.newHashSet();
        getRecursivePassengersByType(Entity.class, set);
        return set;
    }

    public <T extends Entity> Collection<T> getRecursivePassengersByType(Class<T> entityClass) {
        Set<T> set = Sets.newHashSet();
        getRecursivePassengersByType(entityClass, set);
        return set;
    }

    private <T extends Entity> void getRecursivePassengersByType(Class<T> entityClass, Set<T> theSet) {
        for (Entity entity : getPassengers()) {
            if (entityClass.isAssignableFrom(entity.getClass())) {
                theSet.add((T) entity);
            }

            entity.getRecursivePassengersByType(entityClass, theSet);
        }
    }

    public Entity getLowestRidingEntity() {
        Entity entity;

        for (entity = this; entity.isRiding(); entity = entity.getRidingEntity()) {
        }

        return entity;
    }

    public boolean isRidingSameEntity(Entity entityIn) {
        return getLowestRidingEntity() == entityIn.getLowestRidingEntity();
    }

    public boolean isRidingOrBeingRiddenBy(Entity entityIn) {
        for (Entity entity : getPassengers()) {
            if (entity.equals(entityIn)) {
                return true;
            }

            if (entity.isRidingOrBeingRiddenBy(entityIn)) {
                return true;
            }
        }

        return false;
    }

    public boolean canPassengerSteer() {
        Entity entity = getControllingPassenger();

        if (entity instanceof EntityPlayer) {
            return ((EntityPlayer) entity).isUser();
        } else {
            return !world.isRemote;
        }
    }

    @Nullable

    /**
     * Get entity this is riding
     */
    public Entity getRidingEntity() {
        return ridingEntity;
    }

    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.NORMAL;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    protected int func_190531_bD() {
        return 1;
    }

}
