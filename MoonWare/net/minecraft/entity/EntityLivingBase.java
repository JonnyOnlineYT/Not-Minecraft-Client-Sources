package net.minecraft.entity;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.RotationMoveEvent;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.motion.EventJump;
import org.moonware.client.feature.impl.visual.Animations;
import org.moonware.client.feature.impl.visual.NoRender;

public abstract class EntityLivingBase extends Entity
{
    private static final Logger field_190632_a = LogManager.getLogger();
    private static final UUID SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
    protected static final DataParameter<Byte> HAND_STATES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BYTE);
    private static final DataParameter<Float> HEALTH = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> POTION_EFFECTS = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HIDE_PARTICLES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private AbstractAttributeMap attributeMap;
    private final CombatTracker _combatTracker = new CombatTracker(this);
    private final Map<Potion, PotionEffect> activePotionsMap = Maps.newHashMap();
    private final NonNullList<ItemStack> handInventory = NonNullList.func_191197_a(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> armorArray = NonNullList.func_191197_a(4, ItemStack.EMPTY);

    /** Whether an arm swing is currently in progress. */
    public boolean isSwingInProgress;
    public EnumHand swingingHand;
    public int swingProgressInt;
    public int arrowHitTimer;

    /**
     * The amount of time remaining this entity should act 'hurt'. (Visual appearance of red tint)
     */
    public int hurtTime;

    /** What the hurt time was max set to last. */
    public int maxHurtTime;

    /** The yaw at which this entity was last attacked from. */
    public float attackedAtYaw;

    /**
     * The amount of time remaining this entity should act 'dead', i.e. have a corpse in the world.
     */
    public int deathTime;
    public float prevSwingProgress;
    public float swingProgress;
    protected int ticksSinceLastSwing;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    public float limbSwing;
    public int maxHurtResistantTime = 20;
    public float prevCameraPitch;
    public float cameraPitch;
    public float randomUnused2;
    public float randomUnused1;
    public float renderYawOffset;
    public float prevRenderYawOffset;

    public float rotationPitchHead;
    public float prevRotationPitchHead;

    /** Entity head rotation yaw */
    public float rotationYawHead;

    /** Entity head rotation yaw at previous tick */
    public float prevRotationYawHead;

    /**
     * A factor used to determine how far this entity will move each tick if it is jumping or falling.
     */
    public float jumpMovementFactor = 0.02F;

    /** The most recent player that has attacked this entity */
    public EntityPlayer attackingPlayer;

    /**
     * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity
     * should drop items on death.
     */
    public int recentlyHit;

    /**
     * This gets set on entity death, but never used. Looks like a duplicate of isDead
     */
    protected boolean dead;

    /** The age of this EntityLiving (used to determine when it dies) */
    public int entityAge;
    public float prevOnGroundSpeedFactor;
    public float onGroundSpeedFactor;
    public float movedDistance;
    public float prevMovedDistance;
    public float unused180;

    /** The score value of the Mob, the amount of points the mob is worth. */
    protected int scoreValue;

    /**
     * Damage taken in the last hit. Mobs are resistant to damage less than this for a short time after taking damage.
     */
    public float lastDamage;

    /** used to check whether entity is jumping. */
    public boolean isJumping;
    public float moveStrafing;
    public float moveForward;
    public float field_191988_bg;
    public float randomYawVelocity;

    /**
     * The number of updates over which the new position and rotation are to be applied to the entity.
     */
    public int newPosRotationIncrements;

    /**
     * The X position the entity will be interpolated to. Used for teleporting.
     */
    protected double interpTargetX;

    /**
     * The Y position the entity will be interpolated to. Used for teleporting.
     */
    protected double interpTargetY;

    /**
     * The Z position the entity will be interpolated to. Used for teleporting.
     */
    protected double interpTargetZ;

    /**
     * The yaw rotation the entity will be interpolated to. Used for teleporting.
     */
    protected double interpTargetYaw;

    /**
     * The pitch rotation the entity will be interpolated to. Used for teleporting.
     */
    protected double interpTargetPitch;

    /** Whether the DataWatcher needs to be updated with the active potions */
    private boolean potionsNeedUpdate = true;

    /** is only being set, has no uses as of MC 1.1 */
    public EntityLivingBase entityLivingToAttack;
    public int revengeTimer;
    public EntityLivingBase lastAttacker;

    /** Holds the value of ticksExisted when setLastAttacker was last called. */
    private int lastAttackerTime;

    /**
     * A factor used to determine how far this entity will move each tick if it is walking on land. Adjusted by speed,
     * and slipperiness of the current block.
     */
    public float landMovementFactor;

    /** Number of ticks since last jump */
    public int jumpTicks;
    public float absorptionAmount;
    public ItemStack activeItemStack = ItemStack.EMPTY;
    public int activeItemStackUseCount;
    public int ticksElytraFlying;

    /** The BlockPos the entity had during the previous tick. */
    private BlockPos prevBlockpos;
    public DamageSource lastDamageSource;
    public long lastDamageStamp;

    /**
     * Called by the /kill command.
     */
    public void onKillCommand()
    {
        attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
    }

    public EntityLivingBase(World worldIn)
    {
        super(worldIn);
        applyEntityAttributes();
        setHealth(getMaxHealth());
        preventEntitySpawning = true;
        randomUnused1 = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
        setPosition(posX, posY, posZ);
        randomUnused2 = (float)Math.random() * 12398.0F;
        rotationYaw = (float)(Math.random() * (Math.PI * 2D));
        rotationYawHead = rotationYaw;
        stepHeight = 0.6F;
    }

    protected void entityInit()
    {
        dataManager.register(HAND_STATES, Byte.valueOf((byte)0));
        dataManager.register(POTION_EFFECTS, Integer.valueOf(0));
        dataManager.register(HIDE_PARTICLES, Boolean.valueOf(false));
        dataManager.register(ARROW_COUNT_IN_ENTITY, Integer.valueOf(0));
        dataManager.register(HEALTH, Float.valueOf(1.0F));
    }

    protected void applyEntityAttributes()
    {
        getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
        if (!isInWater())
        {
            handleWaterMovement();
        }

        if (!world.isRemote && fallDistance > 3.0F && onGroundIn)
        {
            float f = (float)MathHelper.ceil(fallDistance - 3.0F);

            if (state.getMaterial() != Material.AIR)
            {
                double d0 = Math.min(0.2F + f / 15.0F, 2.5D);
                int i = (int)(150.0D * d0);
                ((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_DUST, posX, posY, posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(state));
            }
        }

        super.updateFallState(y, onGroundIn, state, pos);
    }

    public boolean canBreatheUnderwater()
    {
        return false;
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        prevSwingProgress = swingProgress;
        super.onEntityUpdate();
        world.theProfiler.startSection("livingEntityBaseTick");
        boolean flag = this instanceof EntityPlayer;

        if (isEntityAlive())
        {
            if (isEntityInsideOpaqueBlock())
            {
                attackEntityFrom(DamageSource.inWall, 1.0F);
            }
            else if (flag && !world.getWorldBorder().contains(getEntityBoundingBox()))
            {
                double d0 = world.getWorldBorder().getClosestDistance(this) + world.getWorldBorder().getDamageBuffer();

                if (d0 < 0.0D)
                {
                    double d1 = world.getWorldBorder().getDamageAmount();

                    if (d1 > 0.0D)
                    {
                        attackEntityFrom(DamageSource.inWall, (float)Math.max(1, MathHelper.floor(-d0 * d1)));
                    }
                }
            }
        }

        if (isImmuneToFire() || world.isRemote)
        {
            extinguish();
        }

        boolean flag1 = flag && ((EntityPlayer)this).capabilities.disableDamage;

        if (isEntityAlive())
        {
            if (!isInsideOfMaterial(Material.WATER))
            {
                setAir(300);
            }
            else
            {
                if (!canBreatheUnderwater() && !isPotionActive(MobEffects.WATER_BREATHING) && !flag1)
                {
                    setAir(decreaseAirSupply(getAir()));

                    if (getAir() == -20)
                    {
                        setAir(0);

                        for (int i = 0; i < 8; ++i)
                        {
                            float f2 = rand.nextFloat() - rand.nextFloat();
                            float f = rand.nextFloat() - rand.nextFloat();
                            float f1 = rand.nextFloat() - rand.nextFloat();
                            world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + (double)f2, posY + (double)f, posZ + (double)f1, motionX, motionY, motionZ);
                        }

                        attackEntityFrom(DamageSource.drown, 2.0F);
                    }
                }

                if (!world.isRemote && isRiding() && getRidingEntity() instanceof EntityLivingBase)
                {
                    dismountRidingEntity();
                }
            }

            if (!world.isRemote)
            {
                BlockPos blockpos = new BlockPos(this);

                if (!Objects.equal(prevBlockpos, blockpos))
                {
                    prevBlockpos = blockpos;
                    frostWalk(blockpos);
                }
            }
        }

        if (isEntityAlive() && isWet())
        {
            extinguish();
        }

        prevCameraPitch = cameraPitch;

        if (hurtTime > 0)
        {
            --hurtTime;
        }

        if (hurtResistantTime > 0 && !(this instanceof EntityPlayerMP))
        {
            --hurtResistantTime;
        }

        if (getHealth() <= 0.0F)
        {
            onDeathUpdate();
        }

        if (recentlyHit > 0)
        {
            --recentlyHit;
        }
        else
        {
            attackingPlayer = null;
        }

        if (lastAttacker != null && !lastAttacker.isEntityAlive())
        {
            lastAttacker = null;
        }

        if (entityLivingToAttack != null)
        {
            if (!entityLivingToAttack.isEntityAlive())
            {
                setRevengeTarget(null);
            }
            else if (ticksExisted - revengeTimer > 100)
            {
                setRevengeTarget(null);
            }
        }

        updatePotionEffects();
        prevMovedDistance = movedDistance;
        prevRenderYawOffset = renderYawOffset;
        prevRotationYawHead = rotationYawHead;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        prevRotationPitchHead = rotationPitchHead;
        world.theProfiler.endSection();
    }

    protected void frostWalk(BlockPos pos)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this);

        if (i > 0)
        {
            EnchantmentFrostWalker.freezeNearby(this, world, pos, i);
        }
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return false;
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        ++deathTime;

        if (deathTime == 20)
        {
            if (!world.isRemote && (isPlayer() || recentlyHit > 0 && canDropLoot() && world.getGameRules().getBoolean("doMobLoot")))
            {
                int i = getExperiencePoints(attackingPlayer);

                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    world.spawnEntityInWorld(new EntityXPOrb(world, posX, posY, posZ, j));
                }
            }

            setDead();

            for (int k = 0; k < 20; ++k)
            {
                double d2 = rand.nextGaussian() * 0.02D;
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, d2, d0, d1);
            }
        }
    }

    /**
     * Entity won't drop items or experience points if this returns false
     */
    protected boolean canDropLoot()
    {
        return !isChild();
    }

    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int air)
    {
        int i = EnchantmentHelper.getRespirationModifier(this);
        return i > 0 && rand.nextInt(i + 1) > 0 ? air : air - 1;
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        return 0;
    }

    /**
     * Only use is to identify if class is an instance of player for experience dropping
     */
    protected boolean isPlayer()
    {
        return false;
    }

    public Random getRNG()
    {
        return rand;
    }

    @Nullable
    public EntityLivingBase getAITarget()
    {
        return entityLivingToAttack;
    }

    public int getRevengeTimer()
    {
        return revengeTimer;
    }

    public void setRevengeTarget(@Nullable EntityLivingBase livingBase)
    {
        entityLivingToAttack = livingBase;
        revengeTimer = ticksExisted;
    }

    public EntityLivingBase getLastAttacker()
    {
        return lastAttacker;
    }

    public int getLastAttackerTime()
    {
        return lastAttackerTime;
    }

    public void setLastAttacker(Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase)
        {
            lastAttacker = (EntityLivingBase)entityIn;
        }
        else
        {
            lastAttacker = null;
        }

        lastAttackerTime = ticksExisted;
    }

    public int getAge()
    {
        return entityAge;
    }

    protected void playEquipSound(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            Item item = stack.getItem();

            if (item instanceof ItemArmor)
            {
                soundevent = ((ItemArmor)item).getArmorMaterial().getSoundEvent();
            }
            else if (item == Items.ELYTRA)
            {
                soundevent = SoundEvents.field_191258_p;
            }

            playSound(soundevent, 1.0F, 1.0F);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setFloat("Health", getHealth());
        compound.setShort("HurtTime", (short) hurtTime);
        compound.setInteger("HurtByTimestamp", revengeTimer);
        compound.setShort("DeathTime", (short) deathTime);
        compound.setFloat("AbsorptionAmount", getAbsorptionAmount());

        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
        {
            ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);

            if (!itemstack.isEmpty())
            {
                getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
            }
        }

        compound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(getAttributeMap()));

        for (EntityEquipmentSlot entityequipmentslot1 : EntityEquipmentSlot.values())
        {
            ItemStack itemstack1 = getItemStackFromSlot(entityequipmentslot1);

            if (!itemstack1.isEmpty())
            {
                getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot1));
            }
        }

        if (!activePotionsMap.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : activePotionsMap.values())
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            compound.setTag("ActiveEffects", nbttaglist);
        }

        compound.setBoolean("FallFlying", isElytraFlying());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        setAbsorptionAmount(compound.getFloat("AbsorptionAmount"));

        if (compound.hasKey("Attributes", 9) && world != null && !world.isRemote)
        {
            SharedMonsterAttributes.setAttributeModifiers(getAttributeMap(), compound.getTagList("Attributes", 10));
        }

        if (compound.hasKey("ActiveEffects", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("ActiveEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

                if (potioneffect != null)
                {
                    activePotionsMap.put(potioneffect.getPotion(), potioneffect);
                }
            }
        }

        if (compound.hasKey("Health", 99))
        {
            setHealth(compound.getFloat("Health"));
        }

        hurtTime = compound.getShort("HurtTime");
        deathTime = compound.getShort("DeathTime");
        revengeTimer = compound.getInteger("HurtByTimestamp");

        if (compound.hasKey("Team", 8))
        {
            String s = compound.getString("Team");
            boolean flag = world.getScoreboard().addPlayerToTeam(getCachedUniqueIdString(), s);

            if (!flag)
            {
                field_190632_a.warn("Unable to add mob to team \"" + s + "\" (that team probably doesn't exist)");
            }
        }

        if (compound.getBoolean("FallFlying"))
        {
            setFlag(7, true);
        }
    }

    protected void updatePotionEffects()
    {
        Iterator<Potion> iterator = activePotionsMap.keySet().iterator();

        try
        {
            while (iterator.hasNext())
            {
                Potion potion = iterator.next();
                PotionEffect potioneffect = activePotionsMap.get(potion);

                if (!potioneffect.onUpdate(this))
                {
                    if (!world.isRemote)
                    {
                        iterator.remove();
                        onFinishedPotionEffect(potioneffect);
                    }
                }
                else if (potioneffect.getDuration() % 600 == 0)
                {
                    onChangedPotionEffect(potioneffect, false);
                }
            }
        }
        catch (ConcurrentModificationException var11)
        {
        }

        if (potionsNeedUpdate)
        {
            if (!world.isRemote)
            {
                updatePotionMetadata();
            }

            potionsNeedUpdate = false;
        }

        int i = dataManager.get(POTION_EFFECTS).intValue();
        boolean flag1 = dataManager.get(HIDE_PARTICLES).booleanValue();

        if (i > 0)
        {
            boolean flag;

            if (isInvisible())
            {
                flag = rand.nextInt(15) == 0;
            }
            else
            {
                flag = rand.nextBoolean();
            }

            if (flag1)
            {
                flag &= rand.nextInt(5) == 0;
            }

            if (flag && i > 0)
            {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;
                world.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, posX + (rand.nextDouble() - 0.5D) * (double) width, posY + rand.nextDouble() * (double) height, posZ + (rand.nextDouble() - 0.5D) * (double) width, d0, d1, d2);
            }
        }
    }

    /**
     * Clears potion metadata values if the entity has no potion effects. Otherwise, updates potion effect color,
     * ambience, and invisibility metadata values
     */
    protected void updatePotionMetadata()
    {
        if (activePotionsMap.isEmpty())
        {
            resetPotionEffectMetadata();
            setInvisible(false);
        }
        else
        {
            Collection<PotionEffect> collection = activePotionsMap.values();
            dataManager.set(HIDE_PARTICLES, Boolean.valueOf(areAllPotionsAmbient(collection)));
            dataManager.set(POTION_EFFECTS, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(collection)));
            setInvisible(isPotionActive(MobEffects.INVISIBILITY));
        }
    }

    /**
     * Returns true if all of the potion effects in the specified collection are ambient.
     */
    public static boolean areAllPotionsAmbient(Collection<PotionEffect> potionEffects)
    {
        for (PotionEffect potioneffect : potionEffects)
        {
            if (!potioneffect.getIsAmbient())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Resets the potion effect color and ambience metadata values
     */
    protected void resetPotionEffectMetadata()
    {
        dataManager.set(HIDE_PARTICLES, Boolean.valueOf(false));
        dataManager.set(POTION_EFFECTS, Integer.valueOf(0));
    }

    public void clearActivePotions()
    {
        if (!world.isRemote)
        {
            Iterator<PotionEffect> iterator = activePotionsMap.values().iterator();

            while (iterator.hasNext())
            {
                onFinishedPotionEffect(iterator.next());
                iterator.remove();
            }
        }
    }

    public Collection<PotionEffect> getActivePotionEffects()
    {
        return activePotionsMap.values();
    }

    public Map<Potion, PotionEffect> func_193076_bZ()
    {
        return activePotionsMap;
    }

    public boolean isPotionActive(Potion potionIn)
    {
        return activePotionsMap.containsKey(potionIn);
    }

    @Nullable

    /**
     * returns the PotionEffect for the supplied Potion if it is active, null otherwise.
     */
    public PotionEffect getActivePotionEffect(Potion potionIn)
    {
        return activePotionsMap.get(potionIn);
    }

    /**
     * adds a PotionEffect to the entity
     */
    public void addPotionEffect(PotionEffect potioneffectIn)
    {
        if (isPotionApplicable(potioneffectIn))
        {
            PotionEffect potioneffect = activePotionsMap.get(potioneffectIn.getPotion());

            if (potioneffect == null)
            {
                activePotionsMap.put(potioneffectIn.getPotion(), potioneffectIn);
                onNewPotionEffect(potioneffectIn);
            }
            else
            {
                potioneffect.combine(potioneffectIn);
                onChangedPotionEffect(potioneffect, true);
            }
        }
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
        {
            Potion potion = potioneffectIn.getPotion();

            return potion != MobEffects.REGENERATION && potion != MobEffects.POISON;
        }

        return true;
    }

    /**
     * Returns true if this entity is undead.
     */
    public boolean isEntityUndead()
    {
        return getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    @Nullable

    /**
     * Removes the given potion effect from the active potion map and returns it. Does not call cleanup callbacks for
     * the end of the potion effect.
     */
    public PotionEffect removeActivePotionEffect(@Nullable Potion potioneffectin)
    {
        return activePotionsMap.remove(potioneffectin);
    }

    /**
     * Removes the given potion effect.
     */
    public void removePotionEffect(Potion potionIn)
    {
        PotionEffect potioneffect = removeActivePotionEffect(potionIn);

        if (potioneffect != null)
        {
            onFinishedPotionEffect(potioneffect);
        }
    }

    protected void onNewPotionEffect(PotionEffect id)
    {
        potionsNeedUpdate = true;

        if (!world.isRemote)
        {
            id.getPotion().applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
        }
    }

    protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_)
    {
        potionsNeedUpdate = true;

        if (p_70695_2_ && !world.isRemote)
        {
            Potion potion = id.getPotion();
            potion.removeAttributesModifiersFromEntity(this, getAttributeMap(), id.getAmplifier());
            potion.applyAttributesModifiersToEntity(this, getAttributeMap(), id.getAmplifier());
        }
    }

    protected void onFinishedPotionEffect(PotionEffect effect)
    {
        potionsNeedUpdate = true;

        if (!world.isRemote)
        {
            effect.getPotion().removeAttributesModifiersFromEntity(this, getAttributeMap(), effect.getAmplifier());
        }
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float healAmount)
    {
        float f = getHealth();

        if (f > 0.0F)
        {
            setHealth(f + healAmount);
        }
    }

    public final float getHealth()
    {
        return dataManager.get(HEALTH).floatValue();
    }

    public void setHealth(float health)
    {
        dataManager.set(HEALTH, Float.valueOf(MathHelper.clamp(health, 0.0F, getMaxHealth())));
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
        else if (world.isRemote)
        {
            return false;
        }
        else
        {
            entityAge = 0;

            if (getHealth() <= 0.0F)
            {
                return false;
            }
            else if (source.isFireDamage() && isPotionActive(MobEffects.FIRE_RESISTANCE))
            {
                return false;
            }
            else
            {
                float f = amount;

                if ((source == DamageSource.anvil || source == DamageSource.fallingBlock) && !getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
                {
                    getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int)(amount * 4.0F + rand.nextFloat() * amount * 2.0F), this);
                    amount *= 0.75F;
                }

                boolean flag = false;

                if (amount > 0.0F && canBlockDamageSource(source))
                {
                    damageShield(amount);
                    amount = 0.0F;

                    if (!source.isProjectile())
                    {
                        Entity entity = source.getSourceOfDamage();

                        if (entity instanceof EntityLivingBase)
                        {
                            func_190629_c((EntityLivingBase)entity);
                        }
                    }

                    flag = true;
                }

                limbSwingAmount = 1.5F;
                boolean flag1 = true;

                if ((float) hurtResistantTime > (float) maxHurtResistantTime / 2.0F)
                {
                    if (amount <= lastDamage)
                    {
                        return false;
                    }

                    damageEntity(source, amount - lastDamage);
                    lastDamage = amount;
                    flag1 = false;
                }
                else
                {
                    lastDamage = amount;
                    hurtResistantTime = maxHurtResistantTime;
                    damageEntity(source, amount);
                    maxHurtTime = 10;
                    hurtTime = maxHurtTime;
                }

                attackedAtYaw = 0.0F;
                Entity entity1 = source.getEntity();

                if (entity1 != null)
                {
                    if (entity1 instanceof EntityLivingBase)
                    {
                        setRevengeTarget((EntityLivingBase)entity1);
                    }

                    if (entity1 instanceof EntityPlayer)
                    {
                        recentlyHit = 100;
                        attackingPlayer = (EntityPlayer)entity1;
                    }
                    else if (entity1 instanceof EntityWolf)
                    {
                        EntityWolf entitywolf = (EntityWolf)entity1;

                        if (entitywolf.isTamed())
                        {
                            recentlyHit = 100;
                            attackingPlayer = null;
                        }
                    }
                }

                if (flag1)
                {
                    if (flag)
                    {
                        world.setEntityState(this, (byte)29);
                    }
                    else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
                    {
                        world.setEntityState(this, (byte)33);
                    }
                    else
                    {
                        byte b0;

                        if (source == DamageSource.drown)
                        {
                            b0 = 36;
                        }
                        else if (source.isFireDamage())
                        {
                            b0 = 37;
                        }
                        else
                        {
                            b0 = 2;
                        }

                        world.setEntityState(this, b0);
                    }

                    if (source != DamageSource.drown && (!flag || amount > 0.0F))
                    {
                        setBeenAttacked();
                    }

                    if (entity1 != null)
                    {
                        double d1 = entity1.posX - posX;
                        double d0;

                        for (d0 = entity1.posZ - posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                        {
                            d1 = (Math.random() - Math.random()) * 0.01D;
                        }

                        attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (180D / Math.PI) - (double) rotationYaw);
                        knockBack(entity1, 0.4F, d1, d0);
                    }
                    else
                    {
                        attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
                    }
                }

                if (getHealth() <= 0.0F)
                {
                    if (!func_190628_d(source))
                    {
                        SoundEvent soundevent = getDeathSound();

                        if (flag1 && soundevent != null)
                        {
                            playSound(soundevent, getSoundVolume(), getSoundPitch());
                        }

                        onDeath(source);
                    }
                }
                else if (flag1)
                {
                    playHurtSound(source);
                }

                boolean flag2 = !flag || amount > 0.0F;

                if (flag2)
                {
                    lastDamageSource = source;
                    lastDamageStamp = world.getTotalWorldTime();
                }

                if (this instanceof EntityPlayerMP)
                {
                    CriteriaTriggers.field_192128_h.func_192200_a((EntityPlayerMP)this, source, f, amount, flag);
                }

                if (entity1 instanceof EntityPlayerMP)
                {
                    CriteriaTriggers.field_192127_g.func_192220_a((EntityPlayerMP)entity1, this, source, f, amount, flag);
                }

                return flag2;
            }
        }
    }

    protected void func_190629_c(EntityLivingBase p_190629_1_)
    {
        p_190629_1_.knockBack(this, 0.5F, posX - p_190629_1_.posX, posZ - p_190629_1_.posZ);
    }

    private boolean func_190628_d(DamageSource p_190628_1_)
    {
        if (p_190628_1_.canHarmInCreative())
        {
            return false;
        }
        else
        {
            ItemStack itemstack = null;

            for (EnumHand enumhand : EnumHand.values())
            {
                ItemStack itemstack1 = getHeldItem(enumhand);

                if (itemstack1.getItem() == Items.TOTEM_OF_UNDYING)
                {
                    itemstack = itemstack1.copy();
                    itemstack1.func_190918_g(1);
                    break;
                }
            }

            if (itemstack != null)
            {
                if (this instanceof EntityPlayerMP)
                {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)this;
                    entityplayermp.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.field_193130_A.func_193187_a(entityplayermp, itemstack);
                }

                setHealth(1.0F);
                clearActivePotions();
                addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
                addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
                world.setEntityState(this, (byte)35);
            }

            return itemstack != null;
        }
    }

    @Nullable
    public DamageSource getLastDamageSource()
    {
        if (world.getTotalWorldTime() - lastDamageStamp > 40L)
        {
            lastDamageSource = null;
        }

        return lastDamageSource;
    }

    protected void playHurtSound(DamageSource source)
    {
        SoundEvent soundevent = getHurtSound(source);

        if (soundevent != null)
        {
            playSound(soundevent, getSoundVolume(), getSoundPitch());
        }
    }

    /**
     * Determines whether the entity can block the damage source based on the damage source's location, whether the
     * damage source is blockable, and whether the entity is blocking.
     */
    private boolean canBlockDamageSource(DamageSource damageSourceIn)
    {
        if (!damageSourceIn.isUnblockable() && isActiveItemStackBlocking())
        {
            Vec3d vec3d = damageSourceIn.getDamageLocation();

            if (vec3d != null)
            {
                Vec3d vec3d1 = getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(posX, posY, posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.xCoord, 0.0D, vec3d2.zCoord);

                return vec3d2.dotProduct(vec3d1) < 0.0D;
            }
        }

        return false;
    }

    /**
     * Renders broken item particles using the given ItemStack
     */
    public void renderBrokenItemStack(ItemStack stack)
    {
        playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + world.rand.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i)
        {
            Vec3d vec3d = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.rotatePitch(-rotationPitch * 0.017453292F);
            vec3d = vec3d.rotateYaw(-rotationYaw * 0.017453292F);
            double d0 = (double)(-rand.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d1 = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec3d1 = vec3d1.rotatePitch(-rotationPitch * 0.017453292F);
            vec3d1 = vec3d1.rotateYaw(-rotationYaw * 0.017453292F);
            vec3d1 = vec3d1.addVector(posX, posY + (double) getEyeHeight(), posZ);
            world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, Item.getIdFromItem(stack.getItem()));
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        if (!dead)
        {
            Entity entity = cause.getEntity();
            EntityLivingBase entitylivingbase = getAttackingEntity();

            if (scoreValue >= 0 && entitylivingbase != null)
            {
                entitylivingbase.func_191956_a(this, scoreValue, cause);
            }

            if (entity != null)
            {
                entity.onKillEntity(this);
            }

            dead = true;
            getCombatTracker().reset();

            if (!world.isRemote)
            {
                int i = 0;

                if (entity instanceof EntityPlayer)
                {
                    i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
                }

                if (canDropLoot() && world.getGameRules().getBoolean("doMobLoot"))
                {
                    boolean flag = recentlyHit > 0;
                    dropLoot(flag, i, cause);
                }
            }

            world.setEntityState(this, (byte)3);
        }
    }

    /**
     * drops the loot of this entity upon death
     */
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        dropFewItems(wasRecentlyHit, lootingModifier);
        dropEquipment(wasRecentlyHit, lootingModifier);
    }

    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
    {
    }

    /**
     * Constructs a knockback vector from the given direction ratio and magnitude and adds it to the entity's velocity.
     * If it is on the ground (i.e. {@code this.onGround}), the Y-velocity is increased as well, clamping it to {@code
     * .4}.

     * The entity's existing horizontal velocity is halved, and if the entity is on the ground the Y-velocity is too.
     *
     * @param strength Magnitude of the knockback vector, and also the Y-velocity to add if the entity is on the ground.
     * @param xRatio The X part of the direction ratio of the knockback vector.
     * @param zRatio The Z part of the direction ratio of the knockback vector.
     */
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio)
    {
        if (rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue())
        {
            isAirBorne = true;
            float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
            motionX /= 2.0D;
            motionZ /= 2.0D;
            motionX -= xRatio / (double)f * (double)strength;
            motionZ -= zRatio / (double)f * (double)strength;

            if (onGround)
            {
                motionY /= 2.0D;
                motionY += strength;

                if (motionY > 0.4000000059604645D)
                {
                    motionY = 0.4000000059604645D;
                }
            }
        }
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_GENERIC_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }

    protected SoundEvent getFallSound(int heightIn)
    {
        return heightIn > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getEntityBoundingBox().minY);
        int k = MathHelper.floor(posZ);

        if (this instanceof EntityPlayer && ((EntityPlayer)this).isSpectator())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (block != Blocks.LADDER && block != Blocks.VINE)
            {
                return block instanceof BlockTrapDoor && canGoThroughtTrapDoorOnLadder(blockpos, iblockstate);
            }
            else
            {
                return true;
            }
        }
    }

    private boolean canGoThroughtTrapDoorOnLadder(BlockPos pos, IBlockState state)
    {
        if (state.getValue(BlockTrapDoor.OPEN).booleanValue())
        {
            IBlockState iblockstate = world.getBlockState(pos.down());

            return iblockstate.getBlock() == Blocks.LADDER && iblockstate.getValue(BlockLadder.FACING) == state.getValue(BlockTrapDoor.FACING);
        }

        return false;
    }

    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive()
    {
        return !isDead && getHealth() > 0.0F;
    }

    public void fall(float distance, float damageMultiplier)
    {
        super.fall(distance, damageMultiplier);
        PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f = potioneffect == null ? 0.0F : (float)(potioneffect.getAmplifier() + 1);
        int i = MathHelper.ceil((distance - 3.0F - f) * damageMultiplier);

        if (i > 0)
        {
            playSound(getFallSound(i), 1.0F, 1.0F);
            attackEntityFrom(DamageSource.fall, (float)i);
            int j = MathHelper.floor(posX);
            int k = MathHelper.floor(posY - 0.20000000298023224D);
            int l = MathHelper.floor(posZ);
            IBlockState iblockstate = world.getBlockState(new BlockPos(j, k, l));

            if (iblockstate.getMaterial() != Material.AIR)
            {
                SoundType soundtype = iblockstate.getBlock().getSoundType();
                playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        maxHurtTime = 10;
        hurtTime = maxHurtTime;
        attackedAtYaw = 0.0F;
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.ARMOR);
        return MathHelper.floor(iattributeinstance.getAttributeValue());
    }

    protected void damageArmor(float damage)
    {
    }

    protected void damageShield(float damage)
    {
    }

    /**
     * Reduces damage, depending on armor
     */
    protected float applyArmorCalculations(DamageSource source, float damage)
    {
        if (!source.isUnblockable())
        {
            damageArmor(damage);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) getTotalArmorValue(), (float) getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }

        return damage;
    }

    /**
     * Reduces damage, depending on potions
     */
    protected float applyPotionDamageCalculations(DamageSource source, float damage)
    {
        if (source.isDamageAbsolute())
        {
            return damage;
        }
        else
        {
            if (isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.outOfWorld)
            {
                int i = (getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damage * (float)j;
                damage = f / 25.0F;
            }

            if (damage <= 0.0F)
            {
                return 0.0F;
            }
            else
            {
                int k = EnchantmentHelper.getEnchantmentModifierDamage(getArmorInventoryList(), source);

                if (k > 0)
                {
                    damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
                }

                return damage;
            }
        }
    }

    /**
     * Deals damage to the entity. This will take the armor of the entity into consideration before damaging the health
     * bar.
     */
    protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
        if (!isEntityInvulnerable(damageSrc))
        {
            damageAmount = applyArmorCalculations(damageSrc, damageAmount);
            damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
            float f = damageAmount;
            damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
            setAbsorptionAmount(getAbsorptionAmount() - (f - damageAmount));

            if (damageAmount != 0.0F)
            {
                float f1 = getHealth();
                setHealth(f1 - damageAmount);
                getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
                setAbsorptionAmount(getAbsorptionAmount() - damageAmount);
            }
        }
    }

    /**
     * 1.8.9
     */
    public CombatTracker getCombatTracker()
    {
        return _combatTracker;
    }

    @Nullable
    public EntityLivingBase getAttackingEntity()
    {
        if (_combatTracker.getBestAttacker() != null)
        {
            return _combatTracker.getBestAttacker();
        }
        else if (attackingPlayer != null)
        {
            return attackingPlayer;
        }
        else
        {
            return entityLivingToAttack != null ? entityLivingToAttack : null;
        }
    }

    /**
     * Returns the maximum health of the entity (what it is able to regenerate up to, what it spawned with, etc)
     */
    public final float getMaxHealth()
    {
        return (float) getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }

    /**
     * counts the amount of arrows stuck in the entity. getting hit by arrows increases this, used in rendering
     */
    public final int getArrowCountInEntity()
    {
        return dataManager.get(ARROW_COUNT_IN_ENTITY).intValue();
    }

    /**
     * sets the amount of arrows stuck in the entity. used for rendering those
     */
    public final void setArrowCountInEntity(int count)
    {
        dataManager.set(ARROW_COUNT_IN_ENTITY, Integer.valueOf(count));
    }

    /**
     * Returns an integer indicating the end point of the swing animation, used by {@link #swingProgress} to provide a
     * progress indicator. Takes dig speed enchantments into account.
     */
    private int getArmSwingAnimationEnd() {
        if (MoonWare.featureManager.getFeatureByClass(Animations.class).getState()) {
            int speed = (int) Animations.speed.getNumberValue();
            return isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : speed;
        } else {
            return isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
        }
    }

    public void swingArm(EnumHand hand)
    {
        if (MoonWare.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.swing.getBoolValue()) {
            return;
        }
        if (!isSwingInProgress || swingProgressInt >= getArmSwingAnimationEnd() / 2 || swingProgressInt < 0)
        {
            swingProgressInt = -1;
            isSwingInProgress = true;
            swingingHand = hand;

            if (world instanceof WorldServer)
            {
                ((WorldServer) world).getEntityTracker().sendToAllTrackingEntity(this, new SPacketAnimation(this, hand == EnumHand.MAIN_HAND ? 0 : 3));
            }
        }
    }

    public void handleStatusUpdate(byte id)
    {
        boolean flag = id == 33;
        boolean flag1 = id == 36;
        boolean flag2 = id == 37;

        if (id != 2 && !flag && !flag1 && !flag2)
        {
            if (id == 3)
            {
                SoundEvent soundevent1 = getDeathSound();

                if (soundevent1 != null)
                {
                    playSound(soundevent1, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }

                setHealth(0.0F);
                onDeath(DamageSource.generic);
            }
            else if (id == 30)
            {
                playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + world.rand.nextFloat() * 0.4F);
            }
            else if (id == 29)
            {
                playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + world.rand.nextFloat() * 0.4F);
            }
            else
            {
                super.handleStatusUpdate(id);
            }
        }
        else
        {
            limbSwingAmount = 1.5F;
            hurtResistantTime = maxHurtResistantTime;
            maxHurtTime = 10;
            hurtTime = maxHurtTime;
            attackedAtYaw = 0.0F;

            if (flag)
            {
                playSound(SoundEvents.ENCHANT_THORNS_HIT, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            DamageSource damagesource;

            if (flag2)
            {
                damagesource = DamageSource.onFire;
            }
            else if (flag1)
            {
                damagesource = DamageSource.drown;
            }
            else
            {
                damagesource = DamageSource.generic;
            }

            SoundEvent soundevent = getHurtSound(damagesource);

            if (soundevent != null)
            {
                playSound(soundevent, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            attackEntityFrom(DamageSource.generic, 0.0F);
        }
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill()
    {
        attackEntityFrom(DamageSource.outOfWorld, 4.0F);
    }

    /**
     * Updates the arm swing progress counters and animation progress
     */
    protected void updateArmSwingProgress()
    {
        int i = getArmSwingAnimationEnd();

        if (isSwingInProgress)
        {
            ++swingProgressInt;

            if (swingProgressInt >= i)
            {
                swingProgressInt = 0;
                isSwingInProgress = false;
            }
        }
        else
        {
            swingProgressInt = 0;
        }

        swingProgress = (float) swingProgressInt / (float)i;
    }

    public IAttributeInstance getEntityAttribute(IAttribute attribute)
    {
        return getAttributeMap().getAttributeInstance(attribute);
    }

    /**
     * Returns this entity's attribute map (where all its attributes are stored)
     */
    public AbstractAttributeMap getAttributeMap()
    {
        if (attributeMap == null)
        {
            attributeMap = new AttributeMap();
        }

        return attributeMap;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public ItemStack getHeldItemMainhand()
    {
        return getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    }

    public ItemStack getHeldItemOffhand()
    {
        return getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
    }

    public ItemStack getHeldItem(EnumHand hand)
    {
        if (hand == EnumHand.MAIN_HAND)
        {
            return getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        }
        else if (hand == EnumHand.OFF_HAND)
        {
            return getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        }
        else
        {
            throw new IllegalArgumentException("Invalid hand " + hand);
        }
    }

    public void setHeldItem(EnumHand hand, ItemStack stack)
    {
        if (hand == EnumHand.MAIN_HAND)
        {
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
        }
        else
        {
            if (hand != EnumHand.OFF_HAND)
            {
                throw new IllegalArgumentException("Invalid hand " + hand);
            }

            setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
        }
    }

    public boolean func_190630_a(EntityEquipmentSlot p_190630_1_)
    {
        return !getItemStackFromSlot(p_190630_1_).isEmpty();
    }

    public abstract Iterable<ItemStack> getArmorInventoryList();

    public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn);

    public abstract void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack);

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting)
    {
        super.setSprinting(sprinting);
        IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (iattributeinstance.getModifier(SPRINTING_SPEED_BOOST_ID) != null)
        {
            iattributeinstance.removeModifier(SPRINTING_SPEED_BOOST);
        }

        if (sprinting)
        {
            iattributeinstance.applyModifier(SPRINTING_SPEED_BOOST);
        }
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 1.0F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
    {
        return isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
    }

    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked()
    {
        return getHealth() <= 0.0F;
    }

    /**
     * Moves the entity to a position out of the way of its mount.
     */
    public void dismountEntity(Entity entityIn)
    {
        if (!(entityIn instanceof EntityBoat) && !(entityIn instanceof AbstractHorse))
        {
            double d1 = entityIn.posX;
            double d13 = entityIn.getEntityBoundingBox().minY + (double)entityIn.height;
            double d14 = entityIn.posZ;
            EnumFacing enumfacing1 = entityIn.getAdjustedHorizontalFacing();

            if (enumfacing1 != null)
            {
                EnumFacing enumfacing = enumfacing1.rotateY();
                int[][] aint1 = {{0, 1}, {0, -1}, { -1, 1}, { -1, -1}, {1, 1}, {1, -1}, { -1, 0}, {1, 0}, {0, 1}};
                double d5 = Math.floor(posX) + 0.5D;
                double d6 = Math.floor(posZ) + 0.5D;
                double d7 = getEntityBoundingBox().maxX - getEntityBoundingBox().minX;
                double d8 = getEntityBoundingBox().maxZ - getEntityBoundingBox().minZ;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(d5 - d7 / 2.0D, entityIn.getEntityBoundingBox().minY, d6 - d8 / 2.0D, d5 + d7 / 2.0D, Math.floor(entityIn.getEntityBoundingBox().minY) + (double) height, d6 + d8 / 2.0D);

                for (int[] aint : aint1)
                {
                    double d9 = enumfacing1.getFrontOffsetX() * aint[0] + enumfacing.getFrontOffsetX() * aint[1];
                    double d10 = enumfacing1.getFrontOffsetZ() * aint[0] + enumfacing.getFrontOffsetZ() * aint[1];
                    double d11 = d5 + d9;
                    double d12 = d6 + d10;
                    AxisAlignedBB axisalignedbb1 = axisalignedbb.offset(d9, 0.0D, d10);

                    if (!world.collidesWithAnyBlock(axisalignedbb1))
                    {
                        if (world.getBlockState(new BlockPos(d11, posY, d12)).isFullyOpaque())
                        {
                            setPositionAndUpdate(d11, posY + 1.0D, d12);
                            return;
                        }

                        BlockPos blockpos = new BlockPos(d11, posY - 1.0D, d12);

                        if (world.getBlockState(blockpos).isFullyOpaque() || world.getBlockState(blockpos).getMaterial() == Material.WATER)
                        {
                            d1 = d11;
                            d13 = posY + 1.0D;
                            d14 = d12;
                        }
                    }
                    else if (!world.collidesWithAnyBlock(axisalignedbb1.offset(0.0D, 1.0D, 0.0D)) && world.getBlockState(new BlockPos(d11, posY + 1.0D, d12)).isFullyOpaque())
                    {
                        d1 = d11;
                        d13 = posY + 2.0D;
                        d14 = d12;
                    }
                }
            }

            setPositionAndUpdate(d1, d13, d14);
        }
        else
        {
            double d0 = (double)(width / 2.0F + entityIn.width / 2.0F) + 0.4D;
            float f;

            if (entityIn instanceof EntityBoat)
            {
                f = 0.0F;
            }
            else
            {
                f = ((float)Math.PI / 2F) * (float)(getPrimaryHand() == EnumHandSide.RIGHT ? -1 : 1);
            }

            float f1 = -MathHelper.sin(-rotationYaw * 0.017453292F - (float)Math.PI + f);
            float f2 = -MathHelper.cos(-rotationYaw * 0.017453292F - (float)Math.PI + f);
            double d2 = Math.abs(f1) > Math.abs(f2) ? d0 / (double)Math.abs(f1) : d0 / (double)Math.abs(f2);
            double d3 = posX + (double)f1 * d2;
            double d4 = posZ + (double)f2 * d2;
            setPosition(d3, entityIn.posY + (double)entityIn.height + 0.001D, d4);

            if (world.collidesWithAnyBlock(getEntityBoundingBox()))
            {
                setPosition(d3, entityIn.posY + (double)entityIn.height + 1.001D, d4);

                if (world.collidesWithAnyBlock(getEntityBoundingBox()))
                {
                    setPosition(entityIn.posX, entityIn.posY + (double) height + 0.001D, entityIn.posZ);
                }
            }
        }
    }

    public boolean getAlwaysRenderNameTagForRender()
    {
        return getAlwaysRenderNameTag();
    }

    protected float getJumpUpwardsMotion()
    {
        return 0.42F;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {

        if (this instanceof EntityPlayerSP) {
            IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer((EntityPlayerSP) this);
            if (baritone != null) {
                RotationMoveEvent jumpRotationEvent = new RotationMoveEvent(RotationMoveEvent.Type.JUMP, rotationYaw);
                baritone.getGameEventHandler().onPlayerRotationMove(jumpRotationEvent);
            }
        }

        EventJump eventJumping = new EventJump(rotationYaw);
        EventManager.call(eventJumping);
        if (eventJumping.isCancelled())
            return;

        motionY = getJumpUpwardsMotion();

        if (isPotionActive(MobEffects.JUMP_BOOST)) {
            motionY += (float) (getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (isSprinting()) {
            float f = eventJumping.getYaw() * 0.017453292F;
            motionX -= MathHelper.sin(f) * 0.2F;
            motionZ += MathHelper.cos(f) * 0.2F;
        }

        isAirBorne = true;
    }

    /**
     * Handles the jump when the entity is in water
     */
    protected void handleJumpWater()
    {
        motionY += 0.03999999910593033D;
    }

    protected void handleJumpLava()
    {
        motionY += 0.03999999910593033D;
    }

    protected float getWaterSlowDown()
    {
        return 0.8F;
    }

    public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        if (isServerWorld() || canPassengerSteer())
        {
            if (!isInWater() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)
            {
                if (!isInLava() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)
                {
                    if (isElytraFlying())
                    {
                        if (motionY > -0.5D)
                        {
                            fallDistance = 1.0F;
                        }

                        Vec3d vec3d = getLookVec();
                        float f = rotationPitch * 0.017453292F;
                        double d6 = Math.sqrt(vec3d.xCoord * vec3d.xCoord + vec3d.zCoord * vec3d.zCoord);
                        double d8 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                        double d1 = vec3d.lengthVector();
                        float f4 = MathHelper.cos(f);
                        f4 = (float)((double)f4 * (double)f4 * Math.min(1.0D, d1 / 0.4D));
                        motionY += -0.08D + (double)f4 * 0.06D;

                        if (motionY < 0.0D && d6 > 0.0D)
                        {
                            double d2 = motionY * -0.1D * (double)f4;
                            motionY += d2;
                            motionX += vec3d.xCoord * d2 / d6;
                            motionZ += vec3d.zCoord * d2 / d6;
                        }

                        if (f < 0.0F)
                        {
                            double d10 = d8 * (double)(-MathHelper.sin(f)) * 0.04D;
                            motionY += d10 * 3.2D;
                            motionX -= vec3d.xCoord * d10 / d6;
                            motionZ -= vec3d.zCoord * d10 / d6;
                        }

                        if (d6 > 0.0D)
                        {
                            motionX += (vec3d.xCoord / d6 * d8 - motionX) * 0.1D;
                            motionZ += (vec3d.zCoord / d6 * d8 - motionZ) * 0.1D;
                        }

                        motionX *= 0.9900000095367432D;
                        motionY *= 0.9800000190734863D;
                        motionZ *= 0.9900000095367432D;
                        moveEntity(MoverType.SELF, motionX, motionY, motionZ);

                        if (isCollidedHorizontally && !world.isRemote)
                        {
                            double d11 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                            double d3 = d8 - d11;
                            float f5 = (float)(d3 * 10.0D - 3.0D);

                            if (f5 > 0.0F)
                            {
                                playSound(getFallSound((int)f5), 1.0F, 1.0F);
                                attackEntityFrom(DamageSource.flyIntoWall, f5);
                            }
                        }

                        if (onGround && !world.isRemote)
                        {
                            setFlag(7, false);
                        }
                    }
                    else
                    {
                        float f6 = 0.91F;
                        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(posX, getEntityBoundingBox().minY - 1.0D, posZ);

                        if (onGround)
                        {
                            f6 = world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
                        }

                        float f7 = 0.16277136F / (f6 * f6 * f6);
                        float f8;

                        if (onGround)
                        {
                            f8 = getAIMoveSpeed() * f7;
                        }
                        else
                        {
                            f8 = jumpMovementFactor;
                        }

                        moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, f8);
                        f6 = 0.91F;

                        if (onGround)
                        {
                            f6 = world.getBlockState(blockpos$pooledmutableblockpos.setPos(posX, getEntityBoundingBox().minY - 1.0D, posZ)).getBlock().slipperiness * 0.91F;
                        }

                        if (isOnLadder())
                        {
                            float f9 = 0.15F;
                            motionX = MathHelper.clamp(motionX, -0.15000000596046448D, 0.15000000596046448D);
                            motionZ = MathHelper.clamp(motionZ, -0.15000000596046448D, 0.15000000596046448D);
                            fallDistance = 0.0F;

                            if (motionY < -0.15D)
                            {
                                motionY = -0.15D;
                            }

                            boolean flag = isSneaking() && this instanceof EntityPlayer;

                            if (flag && motionY < 0.0D)
                            {
                                motionY = 0.0D;
                            }
                        }

                        moveEntity(MoverType.SELF, motionX, motionY, motionZ);

                        if (isCollidedHorizontally && isOnLadder())
                        {
                            motionY = 0.2D;
                        }

                        if (isPotionActive(MobEffects.LEVITATION))
                        {
                            motionY += (0.05D * (double)(getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - motionY) * 0.2D;
                        }
                        else
                        {
                            blockpos$pooledmutableblockpos.setPos(posX, 0.0D, posZ);

                            if (!world.isRemote || world.isBlockLoaded(blockpos$pooledmutableblockpos) && world.getChunkFromBlockCoords(blockpos$pooledmutableblockpos).isLoaded())
                            {
                                if (!hasNoGravity())
                                {
                                    motionY -= 0.08D;
                                }
                            }
                            else if (posY > 0.0D)
                            {
                                motionY = -0.1D;
                            }
                            else
                            {
                                motionY = 0.0D;
                            }
                        }

                        motionY *= 0.9800000190734863D;
                        motionX *= f6;
                        motionZ *= f6;
                        blockpos$pooledmutableblockpos.release();
                    }
                }
                else
                {
                    double d4 = posY;
                    moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
                    moveEntity(MoverType.SELF, motionX, motionY, motionZ);
                    motionX *= 0.5D;
                    motionY *= 0.5D;
                    motionZ *= 0.5D;

                    if (!hasNoGravity())
                    {
                        motionY -= 0.02D;
                    }

                    if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d4, motionZ))
                    {
                        motionY = 0.30000001192092896D;
                    }
                }
            }
            else
            {
                double d0 = posY;
                float f1 = getWaterSlowDown();
                float f2 = 0.02F;
                float f3 = (float)EnchantmentHelper.getDepthStriderModifier(this);

                if (f3 > 3.0F)
                {
                    f3 = 3.0F;
                }

                if (!onGround)
                {
                    f3 *= 0.5F;
                }

                if (f3 > 0.0F)
                {
                    f1 += (0.54600006F - f1) * f3 / 3.0F;
                    f2 += (getAIMoveSpeed() - f2) * f3 / 3.0F;
                }

                moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, f2);
                moveEntity(MoverType.SELF, motionX, motionY, motionZ);
                motionX *= f1;
                motionY *= 0.800000011920929D;
                motionZ *= f1;

                if (!hasNoGravity())
                {
                    motionY -= 0.02D;
                }

                if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d0, motionZ))
                {
                    motionY = 0.30000001192092896D;
                }
            }
        }

        prevLimbSwingAmount = limbSwingAmount;
        double d5 = posX - prevPosX;
        double d7 = posZ - prevPosZ;
        double d9 = this instanceof net.minecraft.entity.passive.EntityFlying ? posY - prevPosY : 0.0D;
        float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;

        if (f10 > 1.0F)
        {
            f10 = 1.0F;
        }

        limbSwingAmount += (f10 - limbSwingAmount) * 0.4F;
        limbSwing += limbSwingAmount;
    }

    /**
     * the movespeed used for the new AI system
     */
    public float getAIMoveSpeed()
    {
        return landMovementFactor;
    }

    /**
     * set the movespeed used for the new AI system
     */
    public void setAIMoveSpeed(float speedIn)
    {
        landMovementFactor = speedIn;
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        setLastAttacker(entityIn);
        return false;
    }

    /**
     * Returns whether player is sleeping or not
     */
    public boolean isPlayerSleeping()
    {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        updateActiveHand();

        if (!world.isRemote)
        {
            int i = getArrowCountInEntity();

            if (i > 0)
            {
                if (arrowHitTimer <= 0)
                {
                    arrowHitTimer = 20 * (30 - i);
                }

                --arrowHitTimer;

                if (arrowHitTimer <= 0)
                {
                    setArrowCountInEntity(i - 1);
                }
            }

            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
            {
                ItemStack itemstack;

                switch (entityequipmentslot.getSlotType())
                {
                    case HAND:
                        itemstack = handInventory.get(entityequipmentslot.getIndex());
                        break;

                    case ARMOR:
                        itemstack = armorArray.get(entityequipmentslot.getIndex());
                        break;

                    default:
                        continue;
                }

                ItemStack itemstack1 = getItemStackFromSlot(entityequipmentslot);

                if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
                {
                    ((WorldServer) world).getEntityTracker().sendToAllTrackingEntity(this, new SPacketEntityEquipment(getEntityId(), entityequipmentslot, itemstack1));

                    if (!itemstack.isEmpty())
                    {
                        getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
                    }

                    if (!itemstack1.isEmpty())
                    {
                        getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot));
                    }

                    switch (entityequipmentslot.getSlotType())
                    {
                        case HAND:
                            handInventory.set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
                            break;

                        case ARMOR:
                            armorArray.set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
                    }
                }
            }

            if (ticksExisted % 20 == 0)
            {
                getCombatTracker().reset();
            }

            if (!glowing)
            {
                boolean flag = isPotionActive(MobEffects.GLOWING);

                if (getFlag(6) != flag)
                {
                    setFlag(6, flag);
                }
            }
        }

        onLivingUpdate();
        double d0 = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f3 = (float)(d0 * d0 + d1 * d1);
        float f4 = renderYawOffset;
        float f5 = 0.0F;
        prevOnGroundSpeedFactor = onGroundSpeedFactor;
        float f = 0.0F;

        if (f3 > 0.0025000002F)
        {
            f = 1.0F;
            f5 = (float)Math.sqrt(f3) * 3.0F;
            float f1 = (float)MathHelper.atan2(d1, d0) * (180F / (float)Math.PI) - 90.0F;
            float f2 = MathHelper.abs(MathHelper.wrapDegrees(rotationYaw) - f1);

            if (95.0F < f2 && f2 < 265.0F)
            {
                f4 = f1 - 180.0F;
            }
            else
            {
                f4 = f1;
            }
        }

        if (swingProgress > 0.0F)
        {
            f4 = rotationYaw;
        }

        if (!onGround)
        {
            f = 0.0F;
        }

        onGroundSpeedFactor += (f - onGroundSpeedFactor) * 0.3F;
        world.theProfiler.startSection("headTurn");
        f5 = updateDistance(f4, f5);
        world.theProfiler.endSection();
        world.theProfiler.startSection("rangeChecks");

        while (rotationYaw - prevRotationYaw < -180.0F)
        {
            prevRotationYaw -= 360.0F;
        }

        while (rotationYaw - prevRotationYaw >= 180.0F)
        {
            prevRotationYaw += 360.0F;
        }

        while (renderYawOffset - prevRenderYawOffset < -180.0F)
        {
            prevRenderYawOffset -= 360.0F;
        }

        while (renderYawOffset - prevRenderYawOffset >= 180.0F)
        {
            prevRenderYawOffset += 360.0F;
        }

        while (rotationPitch - prevRotationPitch < -180.0F)
        {
            prevRotationPitch -= 360.0F;
        }

        while (rotationPitch - prevRotationPitch >= 180.0F)
        {
            prevRotationPitch += 360.0F;
        }

        while (rotationYawHead - prevRotationYawHead < -180.0F)
        {
            prevRotationYawHead -= 360.0F;
        }

        while (rotationYawHead - prevRotationYawHead >= 180.0F)
        {
            prevRotationYawHead += 360.0F;
        }

        world.theProfiler.endSection();
        movedDistance += f5;

        if (isElytraFlying())
        {
            ++ticksElytraFlying;
        }
        else
        {
            ticksElytraFlying = 0;
        }
    }

    protected float updateDistance(float p_110146_1_, float p_110146_2_)
    {
        float f = MathHelper.wrapDegrees(p_110146_1_ - renderYawOffset);
        renderYawOffset += f * 0.3F;
        float f1 = MathHelper.wrapDegrees(rotationYaw - renderYawOffset);
        boolean flag = f1 < -90.0F || f1 >= 90.0F;

        if (f1 < -75.0F)
        {
            f1 = -75.0F;
        }

        if (f1 >= 75.0F)
        {
            f1 = 75.0F;
        }

        renderYawOffset = rotationYaw - f1;

        if (f1 * f1 > 2500.0F)
        {
            renderYawOffset += f1 * 0.2F;
        }

        if (flag)
        {
            p_110146_2_ *= -1.0F;
        }

        return p_110146_2_;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (jumpTicks > 0)
        {
            --jumpTicks;
        }

        if (newPosRotationIncrements > 0 && !canPassengerSteer())
        {
            double d0 = posX + (interpTargetX - posX) / (double) newPosRotationIncrements;
            double d1 = posY + (interpTargetY - posY) / (double) newPosRotationIncrements;
            double d2 = posZ + (interpTargetZ - posZ) / (double) newPosRotationIncrements;
            double d3 = MathHelper.wrapDegrees(interpTargetYaw - (double) rotationYaw);
            rotationYaw = (float)((double) rotationYaw + d3 / (double) newPosRotationIncrements);
            rotationPitch = (float)((double) rotationPitch + (interpTargetPitch - (double) rotationPitch) / (double) newPosRotationIncrements);
            --newPosRotationIncrements;
            setPosition(d0, d1, d2);
            setRotation(rotationYaw, rotationPitch);
        }
        else if (!isServerWorld())
        {
            motionX *= 0.98D;
            motionY *= 0.98D;
            motionZ *= 0.98D;
        }

        if (Math.abs(motionX) < 0.003D)
        {
            motionX = 0.0D;
        }

        if (Math.abs(motionY) < 0.003D)
        {
            motionY = 0.0D;
        }

        if (Math.abs(motionZ) < 0.003D)
        {
            motionZ = 0.0D;
        }

        world.theProfiler.startSection("ai");

        if (isMovementBlocked())
        {
            isJumping = false;
            moveStrafing = 0.0F;
            field_191988_bg = 0.0F;
            randomYawVelocity = 0.0F;
        }
        else if (isServerWorld())
        {
            world.theProfiler.startSection("newAi");
            updateEntityActionState();
            world.theProfiler.endSection();
        }

        world.theProfiler.endSection();
        world.theProfiler.startSection("jump");

        if (isJumping)
        {
            if (isInWater())
            {
                handleJumpWater();
            }
            else if (isInLava())
            {
                handleJumpLava();
            }
            else if (onGround && jumpTicks == 0)
            {
                jump();
                jumpTicks = 10;
            }
        }
        else
        {
            jumpTicks = 0;
        }

        world.theProfiler.endSection();
        world.theProfiler.startSection("travel");
        moveStrafing *= 0.98F;
        field_191988_bg *= 0.98F;
        randomYawVelocity *= 0.9F;
        updateElytra();
        func_191986_a(moveStrafing, moveForward, field_191988_bg);
        world.theProfiler.endSection();
        world.theProfiler.startSection("push");
        collideWithNearbyEntities();
        world.theProfiler.endSection();
    }

    /**
     * Called each tick. Updates state for the elytra.
     */
    private void updateElytra()
    {
        boolean flag = getFlag(7);

        if (flag && !onGround && !isRiding())
        {
            ItemStack itemstack = getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack))
            {
                flag = true;

                if (!world.isRemote && (ticksElytraFlying + 1) % 20 == 0)
                {
                    itemstack.damageItem(1, this);
                }
            }
            else
            {
                flag = false;
            }
        }
        else
        {
            flag = false;
        }

        if (!world.isRemote)
        {
            setFlag(7, flag);
        }
    }

    protected void updateEntityActionState()
    {
    }

    protected void collideWithNearbyEntities()
    {
        List<Entity> list = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty())
        {
            int i = world.getGameRules().getInt("maxEntityCramming");

            if (i > 0 && list.size() > i - 1 && rand.nextInt(4) == 0)
            {
                int j = 0;

                for (int k = 0; k < list.size(); ++k)
                {
                    if (!list.get(k).isRiding())
                    {
                        ++j;
                    }
                }

                if (j > i - 1)
                {
                    attackEntityFrom(DamageSource.field_191291_g, 6.0F);
                }
            }

            for (int l = 0; l < list.size(); ++l)
            {
                Entity entity = list.get(l);
                collideWithEntity(entity);
            }
        }
    }

    protected void collideWithEntity(Entity entityIn)
    {
        entityIn.applyEntityCollision(this);
    }

    public void dismountRidingEntity()
    {
        Entity entity = getRidingEntity();
        super.dismountRidingEntity();

        if (entity != null && entity != getRidingEntity() && !world.isRemote)
        {
            dismountEntity(entity);
        }
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        super.updateRidden();
        prevOnGroundSpeedFactor = onGroundSpeedFactor;
        onGroundSpeedFactor = 0.0F;
        fallDistance = 0.0F;
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        interpTargetX = x;
        interpTargetY = y;
        interpTargetZ = z;
        interpTargetYaw = yaw;
        interpTargetPitch = pitch;
        newPosRotationIncrements = posRotationIncrements;
    }

    public void setJumping(boolean jumping)
    {
        isJumping = jumping;
    }

    /**
     * Called when the entity picks up an item.
     */
    public void onItemPickup(Entity entityIn, int quantity)
    {
        if (!entityIn.isDead && !world.isRemote)
        {
            EntityTracker entitytracker = ((WorldServer) world).getEntityTracker();

            if (entityIn instanceof EntityItem || entityIn instanceof EntityArrow || entityIn instanceof EntityXPOrb)
            {
                entitytracker.sendToAllTrackingEntity(entityIn, new SPacketCollectItem(entityIn.getEntityId(), getEntityId(), quantity));
            }
        }
    }

    /**
     * returns true if the entity provided in the argument can be seen. (Raytrace)
     */
    public boolean canEntityBeSeen(Entity entityIn)
    {
        return world.rayTraceBlocks(new Vec3d(posX, posY + (double) getEyeHeight(), posZ), new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null;
    }

    /**
     * interpolated look vector
     */
    public Vec3d getLook(float partialTicks)
    {
        if (partialTicks == 1.0F)
        {
            return getVectorForRotation(rotationPitch, rotationYawHead);
        }
        else
        {
            float f = prevRotationPitch + (rotationPitch - prevRotationPitch) * partialTicks;
            float f1 = prevRotationYawHead + (rotationYawHead - prevRotationYawHead) * partialTicks;
            return getVectorForRotation(f, f1);
        }
    }

    /**
     * Gets the progression of the swing animation, ranges from 0.0 to 1.0.
     */
    public float getSwingProgress(float partialTickTime)
    {
        float f = swingProgress - prevSwingProgress;

        if (f < 0.0F)
        {
            ++f;
        }

        return prevSwingProgress + f * partialTickTime;
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
        return !world.isRemote;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return isEntityAlive() && !isOnLadder();
    }

    /**
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked()
    {
        velocityChanged = rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
    }

    public float getRotationYawHead()
    {
        return rotationYawHead;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation)
    {
        rotationYawHead = rotation;
    }

    /**
     * Set the render yaw offset
     */
    public void setRenderYawOffset(float offset)
    {
        renderYawOffset = offset;
    }

    /**
     * Returns the amount of health added by the Absorption effect.
     */
    public float getAbsorptionAmount()
    {
        return absorptionAmount;
    }

    public void setAbsorptionAmount(float amount)
    {
        if (amount < 0.0F)
        {
            amount = 0.0F;
        }

        absorptionAmount = amount;
    }

    /**
     * Sends an ENTER_COMBAT packet to the client
     */
    public void sendEnterCombat()
    {
    }

    /**
     * Sends an END_COMBAT packet to the client
     */
    public void sendEndCombat()
    {
    }

    protected void markPotionsDirty()
    {
        potionsNeedUpdate = true;
    }

    public abstract EnumHandSide getPrimaryHand();

    public boolean isHandActive()
    {
        return (dataManager.get(HAND_STATES).byteValue() & 1) > 0;
    }

    public EnumHand getActiveHand()
    {
        return (dataManager.get(HAND_STATES).byteValue() & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    public boolean isInLiquid() {
        return Minecraft.player.isInWater() || Minecraft.player.isInLava();
    }

    public boolean isBlocking() {
        return isHandActive() && activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.BLOCK;
    }

    public boolean isEating() {
        return isHandActive() && activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.EAT;
    }

    public boolean isUsingItem() {
        return isHandActive() && (activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.EAT || activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.BLOCK || activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.BOW || activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.DRINK);
    }

    public boolean isDrinking() {
        return isHandActive() && activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.DRINK;
    }

    public boolean isBowing() {
        return isHandActive() && activeItemStack.getItem().getItemUseAction(activeItemStack) == EnumAction.BOW;
    }

    protected void updateActiveHand()
    {
        if (isHandActive())
        {
            ItemStack itemstack = getHeldItem(getActiveHand());

            if (itemstack == activeItemStack)
            {
                if (getItemInUseCount() <= 25 && getItemInUseCount() % 4 == 0)
                {
                    updateItemUse(activeItemStack, 5);
                }

                if (--activeItemStackUseCount == 0 && !world.isRemote)
                {
                    onItemUseFinish();
                }
            }
            else
            {
                resetActiveHand();
            }
        }
    }

    public void setActiveHand(EnumHand hand)
    {
        ItemStack itemstack = getHeldItem(hand);

        if (!itemstack.isEmpty() && !isHandActive())
        {
            activeItemStack = itemstack;
            activeItemStackUseCount = itemstack.getMaxItemUseDuration();

            if (!world.isRemote)
            {
                int i = 1;

                if (hand == EnumHand.OFF_HAND)
                {
                    i |= 2;
                }

                dataManager.set(HAND_STATES, Byte.valueOf((byte)i));
            }
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);

        if (HAND_STATES.equals(key) && world.isRemote)
        {
            if (isHandActive() && activeItemStack.isEmpty())
            {
                activeItemStack = getHeldItem(getActiveHand());

                if (!activeItemStack.isEmpty())
                {
                    activeItemStackUseCount = activeItemStack.getMaxItemUseDuration();
                }
            }
            else if (!isHandActive() && !activeItemStack.isEmpty())
            {
                activeItemStack = ItemStack.EMPTY;
                activeItemStackUseCount = 0;
            }
        }
    }

    /**
     * Plays sounds and makes particles for item in use state
     */
    protected void updateItemUse(ItemStack stack, int eatingParticleCount)
    {
        if (!stack.isEmpty() && isHandActive())
        {
            if (stack.getItemUseAction() == EnumAction.DRINK)
            {
                playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (stack.getItemUseAction() == EnumAction.EAT)
            {
                for (int i = 0; i < eatingParticleCount; ++i)
                {
                    Vec3d vec3d = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                    vec3d = vec3d.rotatePitch(-rotationPitch * 0.017453292F);
                    vec3d = vec3d.rotateYaw(-rotationYaw * 0.017453292F);
                    double d0 = (double)(-rand.nextFloat()) * 0.6D - 0.3D;
                    Vec3d vec3d1 = new Vec3d(((double) rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
                    vec3d1 = vec3d1.rotatePitch(-rotationPitch * 0.017453292F);
                    vec3d1 = vec3d1.rotateYaw(-rotationYaw * 0.017453292F);
                    vec3d1 = vec3d1.addVector(posX, posY + (double) getEyeHeight(), posZ);

                    if (stack.getHasSubtypes())
                    {
                        world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
                    }
                    else
                    {
                        world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord, Item.getIdFromItem(stack.getItem()));
                    }
                }

                playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * (float) rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    /**
     * Used for when item use count runs out, ie: eating completed
     */
    protected void onItemUseFinish()
    {
        if (!activeItemStack.isEmpty() && isHandActive())
        {
            updateItemUse(activeItemStack, 16);
            setHeldItem(getActiveHand(), activeItemStack.onItemUseFinish(world, this));
            resetActiveHand();
        }
    }

    public ItemStack getActiveItemStack()
    {
        return activeItemStack;
    }

    public int getItemInUseCount()
    {
        return activeItemStackUseCount;
    }

    public int getItemInUseMaxCount()
    {
        return isHandActive() ? activeItemStack.getMaxItemUseDuration() - getItemInUseCount() : 0;
    }

    public void stopActiveHand()
    {
        if (!activeItemStack.isEmpty())
        {
            activeItemStack.onPlayerStoppedUsing(world, this, getItemInUseCount());
        }

        resetActiveHand();
    }

    public void resetActiveHand()
    {
        if (!world.isRemote)
        {
            dataManager.set(HAND_STATES, Byte.valueOf((byte)0));
        }

        activeItemStack = ItemStack.EMPTY;
        activeItemStackUseCount = 0;
    }

    public boolean isActiveItemStackBlocking()
    {
        if (isHandActive() && !activeItemStack.isEmpty())
        {
            Item item = activeItemStack.getItem();

            if (item.getItemUseAction(activeItemStack) != EnumAction.BLOCK)
            {
                return false;
            }
            else
            {
                return item.getMaxItemUseDuration(activeItemStack) - activeItemStackUseCount >= 5;
            }
        }
        else
        {
            return false;
        }
    }
    public boolean isActiveItemStackBlocking(int delay) {
        if (isHandActive() && !activeItemStack.isEmpty()) {
            Item item = activeItemStack.getItem();
            if (item.getItemUseAction(activeItemStack) != EnumAction.BLOCK) {
                return false;
            }
            return item.getMaxItemUseDuration(activeItemStack) - activeItemStackUseCount >= delay;
        }
        return false;
    }

    public boolean isElytraFlying()
    {
        return getFlag(7);
    }

    public int getTicksElytraFlying()
    {
        return ticksElytraFlying;
    }

    /**
     * Teleports the entity to the specified location. Used for Enderman and Chorus Fruit teleportation
     */
    public boolean attemptTeleport(double x, double y, double z)
    {
        double d0 = posX;
        double d1 = posY;
        double d2 = posZ;
        posX = x;
        posY = y;
        posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this);
        World world = this.world;
        Random random = getRNG();

        if (world.isBlockLoaded(blockpos))
        {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0)
            {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement())
                {
                    flag1 = true;
                }
                else
                {
                    --posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1)
            {
                setPositionAndUpdate(posX, posY, posZ);

                if (world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox()))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            setPositionAndUpdate(d0, d1, d2);
            return false;
        }
        else
        {
            int i = 128;

            for (int j = 0; j < 128; ++j)
            {
                double d6 = (double)j / 127.0D;
                float f = (random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                double d3 = d0 + (posX - d0) * d6 + (random.nextDouble() - 0.5D) * (double) width * 2.0D;
                double d4 = d1 + (posY - d1) * d6 + random.nextDouble() * (double) height;
                double d5 = d2 + (posZ - d2) * d6 + (random.nextDouble() - 0.5D) * (double) width * 2.0D;
                world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
            }

            if (this instanceof EntityCreature)
            {
                ((EntityCreature)this).getNavigator().clearPathEntity();
            }

            return true;
        }
    }

    /**
     * Returns false if the entity is an armor stand. Returns true for all other entity living bases.
     */
    public boolean canBeHitWithPotion()
    {
        return true;
    }

    public boolean func_190631_cK()
    {
        return true;
    }

    public void func_191987_a(BlockPos p_191987_1_, boolean p_191987_2_)
    {
    }

    public int getItemInUseDuration() {
        return isUsingItem() ? (activeItemStack.getMaxItemUseDuration() - getItemInUseCount()) : 0;
    }

    public int getHurtTime() {
        return hurtTime;
    }
}
