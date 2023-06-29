package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public abstract class AbstractHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount
{
    private static final Predicate<Entity> IS_HORSE_BREEDING = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof AbstractHorse && ((AbstractHorse)p_apply_1_).isBreeding();
        }
    };
    protected static final IAttribute JUMP_STRENGTH = (new RangedAttribute(null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    private static final DataParameter<Byte> STATUS = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private int field_190689_bJ;
    private int openMouthCounter;
    private int jumpRearingCounter;
    public int tailCounter;
    public int sprintCounter;
    protected boolean horseJumping;
    protected ContainerHorseChest horseChest;

    /**
     * "The higher this value, the more likely the horse is to be tamed next time a player rides it."
     */
    protected int temper;
    protected float jumpPower;
    private boolean allowStandSliding;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;
    protected boolean field_190688_bE = true;

    /** Used to determine the sound that the horse should make when it steps */
    protected int gallopTime;

    public AbstractHorse(World p_i47299_1_)
    {
        super(p_i47299_1_);
        setSize(1.3964844F, 1.6F);
        stepHeight = 1.0F;
        initHorseChest();
    }

    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.2D));
        tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        tasks.addTask(2, new EntityAIMate(this, 1.0D, AbstractHorse.class));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
        tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(STATUS, Byte.valueOf((byte)0));
        dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
    }

    protected boolean getHorseWatchableBoolean(int p_110233_1_)
    {
        return (dataManager.get(STATUS).byteValue() & p_110233_1_) != 0;
    }

    protected void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_)
    {
        byte b0 = dataManager.get(STATUS).byteValue();

        if (p_110208_2_)
        {
            dataManager.set(STATUS, Byte.valueOf((byte)(b0 | p_110208_1_)));
        }
        else
        {
            dataManager.set(STATUS, Byte.valueOf((byte)(b0 & ~p_110208_1_)));
        }
    }

    public boolean isTame()
    {
        return getHorseWatchableBoolean(2);
    }

    @Nullable
    public UUID getOwnerUniqueId()
    {
        return (UUID)((Optional) dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerUniqueId(@Nullable UUID uniqueId)
    {
        dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(uniqueId));
    }

    public float getHorseSize()
    {
        return 0.5F;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    public void setScaleForAge(boolean child)
    {
        setScale(child ? getHorseSize() : 1.0F);
    }

    public boolean isHorseJumping()
    {
        return horseJumping;
    }

    public void setHorseTamed(boolean tamed)
    {
        setHorseWatchableBoolean(2, tamed);
    }

    public void setHorseJumping(boolean jumping)
    {
        horseJumping = jumping;
    }

    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return super.canBeLeashedTo(player) && getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
    }

    protected void onLeashDistance(float p_142017_1_)
    {
        if (p_142017_1_ > 6.0F && isEatingHaystack())
        {
            setEatingHaystack(false);
        }
    }

    public boolean isEatingHaystack()
    {
        return getHorseWatchableBoolean(16);
    }

    public boolean isRearing()
    {
        return getHorseWatchableBoolean(32);
    }

    public boolean isBreeding()
    {
        return getHorseWatchableBoolean(8);
    }

    public void setBreeding(boolean breeding)
    {
        setHorseWatchableBoolean(8, breeding);
    }

    public void setHorseSaddled(boolean saddled)
    {
        setHorseWatchableBoolean(4, saddled);
    }

    public int getTemper()
    {
        return temper;
    }

    public void setTemper(int temperIn)
    {
        temper = temperIn;
    }

    public int increaseTemper(int p_110198_1_)
    {
        int i = MathHelper.clamp(getTemper() + p_110198_1_, 0, func_190676_dC());
        setTemper(i);
        return i;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        Entity entity = source.getEntity();
        return (!isBeingRidden() || entity == null || !isRidingOrBeingRiddenBy(entity)) && super.attackEntityFrom(source, amount);
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return !isBeingRidden();
    }

    private void eatingHorse()
    {
        openHorseMouth();

        if (!isSilent())
        {
            world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_HORSE_EAT, getSoundCategory(), 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
        }
    }

    public void fall(float distance, float damageMultiplier)
    {
        if (distance > 1.0F)
        {
            playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F);
        }

        int i = MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);

        if (i > 0)
        {
            attackEntityFrom(DamageSource.fall, (float)i);

            if (isBeingRidden())
            {
                for (Entity entity : getRecursivePassengers())
                {
                    entity.attackEntityFrom(DamageSource.fall, (float)i);
                }
            }

            IBlockState iblockstate = world.getBlockState(new BlockPos(posX, posY - 0.2D - (double) prevRotationYaw, posZ));
            Block block = iblockstate.getBlock();

            if (iblockstate.getMaterial() != Material.AIR && !isSilent())
            {
                SoundType soundtype = block.getSoundType();
                world.playSound(null, posX, posY, posZ, soundtype.getStepSound(), getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    protected int func_190686_di()
    {
        return 2;
    }

    protected void initHorseChest()
    {
        ContainerHorseChest containerhorsechest = horseChest;
        horseChest = new ContainerHorseChest("HorseChest", func_190686_di());
        horseChest.setCustomName(getName());

        if (containerhorsechest != null)
        {
            containerhorsechest.removeInventoryChangeListener(this);
            int i = Math.min(containerhorsechest.getSizeInventory(), horseChest.getSizeInventory());

            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = containerhorsechest.getStackInSlot(j);

                if (!itemstack.isEmpty())
                {
                    horseChest.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }

        horseChest.addInventoryChangeListener(this);
        updateHorseSlots();
    }

    /**
     * Updates the items in the saddle and armor slots of the horse's inventory.
     */
    protected void updateHorseSlots()
    {
        if (!world.isRemote)
        {
            setHorseSaddled(!horseChest.getStackInSlot(0).isEmpty() && func_190685_dA());
        }
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    public void onInventoryChanged(IInventory invBasic)
    {
        boolean flag = isHorseSaddled();
        updateHorseSlots();

        if (ticksExisted > 20 && !flag && isHorseSaddled())
        {
            playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
        }
    }

    @Nullable
    protected AbstractHorse getClosestHorse(Entity entityIn, double distance)
    {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;

        for (Entity entity1 : world.getEntitiesInAABBexcluding(entityIn, entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), IS_HORSE_BREEDING))
        {
            double d1 = entity1.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);

            if (d1 < d0)
            {
                entity = entity1;
                d0 = d1;
            }
        }

        return (AbstractHorse)entity;
    }

    public double getHorseJumpStrength()
    {
        return getEntityAttribute(JUMP_STRENGTH).getAttributeValue();
    }

    @Nullable
    protected SoundEvent getDeathSound()
    {
        openHorseMouth();
        return null;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        openHorseMouth();

        if (rand.nextInt(3) == 0)
        {
            makeHorseRear();
        }

        return null;
    }

    @Nullable
    protected SoundEvent getAmbientSound()
    {
        openHorseMouth();

        if (rand.nextInt(10) == 0 && !isMovementBlocked())
        {
            makeHorseRear();
        }

        return null;
    }

    public boolean func_190685_dA()
    {
        return true;
    }

    public boolean isHorseSaddled()
    {
        return getHorseWatchableBoolean(4);
    }

    @Nullable
    protected SoundEvent getAngrySound()
    {
        openHorseMouth();
        makeHorseRear();
        return null;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        if (!blockIn.getDefaultState().getMaterial().isLiquid())
        {
            SoundType soundtype = blockIn.getSoundType();

            if (world.getBlockState(pos.up()).getBlock() == Blocks.SNOW_LAYER)
            {
                soundtype = Blocks.SNOW_LAYER.getSoundType();
            }

            if (isBeingRidden() && field_190688_bE)
            {
                ++gallopTime;

                if (gallopTime > 5 && gallopTime % 3 == 0)
                {
                    func_190680_a(soundtype);
                }
                else if (gallopTime <= 5)
                {
                    playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
            }
            else if (soundtype == SoundType.WOOD)
            {
                playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
            else
            {
                playSound(SoundEvents.ENTITY_HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
        }
    }

    protected void func_190680_a(SoundType p_190680_1_)
    {
        playSound(SoundEvents.ENTITY_HORSE_GALLOP, p_190680_1_.getVolume() * 0.15F, p_190680_1_.getPitch());
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(JUMP_STRENGTH);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22499999403953552D);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 6;
    }

    public int func_190676_dC()
    {
        return 100;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.8F;
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 400;
    }

    public void openGUI(EntityPlayer playerEntity)
    {
        if (!world.isRemote && (!isBeingRidden() || isPassenger(playerEntity)) && isTame())
        {
            horseChest.setCustomName(getName());
            playerEntity.openGuiHorseInventory(this, horseChest);
        }
    }

    protected boolean func_190678_b(EntityPlayer p_190678_1_, ItemStack p_190678_2_)
    {
        boolean flag = false;
        float f = 0.0F;
        int i = 0;
        int j = 0;
        Item item = p_190678_2_.getItem();

        if (item == Items.WHEAT)
        {
            f = 2.0F;
            i = 20;
            j = 3;
        }
        else if (item == Items.SUGAR)
        {
            f = 1.0F;
            i = 30;
            j = 3;
        }
        else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK))
        {
            f = 20.0F;
            i = 180;
        }
        else if (item == Items.APPLE)
        {
            f = 3.0F;
            i = 60;
            j = 3;
        }
        else if (item == Items.GOLDEN_CARROT)
        {
            f = 4.0F;
            i = 60;
            j = 5;

            if (isTame() && getGrowingAge() == 0 && !isInLove())
            {
                flag = true;
                setInLove(p_190678_1_);
            }
        }
        else if (item == Items.GOLDEN_APPLE)
        {
            f = 10.0F;
            i = 240;
            j = 10;

            if (isTame() && getGrowingAge() == 0 && !isInLove())
            {
                flag = true;
                setInLove(p_190678_1_);
            }
        }

        if (getHealth() < getMaxHealth() && f > 0.0F)
        {
            heal(f);
            flag = true;
        }

        if (isChild() && i > 0)
        {
            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, 0.0D, 0.0D, 0.0D);

            if (!world.isRemote)
            {
                addGrowth(i);
            }

            flag = true;
        }

        if (j > 0 && (flag || !isTame()) && getTemper() < func_190676_dC())
        {
            flag = true;

            if (!world.isRemote)
            {
                increaseTemper(j);
            }
        }

        if (flag)
        {
            eatingHorse();
        }

        return flag;
    }

    protected void mountTo(EntityPlayer player)
    {
        player.rotationYaw = rotationYaw;
        player.rotationPitch = rotationPitch;
        setEatingHaystack(false);
        setRearing(false);

        if (!world.isRemote)
        {
            player.startRiding(this);
        }
    }

    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked()
    {
        return super.isMovementBlocked() && isBeingRidden() && isHorseSaddled() || isEatingHaystack() || isRearing();
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return false;
    }

    private void moveTail()
    {
        tailCounter = 1;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (!world.isRemote && horseChest != null)
        {
            for (int i = 0; i < horseChest.getSizeInventory(); ++i)
            {
                ItemStack itemstack = horseChest.getStackInSlot(i);

                if (!itemstack.isEmpty())
                {
                    entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (rand.nextInt(200) == 0)
        {
            moveTail();
        }

        super.onLivingUpdate();

        if (!world.isRemote)
        {
            if (rand.nextInt(900) == 0 && deathTime == 0)
            {
                heal(1.0F);
            }

            if (func_190684_dE())
            {
                if (!isEatingHaystack() && !isBeingRidden() && rand.nextInt(300) == 0 && world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY) - 1, MathHelper.floor(posZ))).getBlock() == Blocks.GRASS)
                {
                    setEatingHaystack(true);
                }

                if (isEatingHaystack() && ++field_190689_bJ > 50)
                {
                    field_190689_bJ = 0;
                    setEatingHaystack(false);
                }
            }

            func_190679_dD();
        }
    }

    protected void func_190679_dD()
    {
        if (isBreeding() && isChild() && !isEatingHaystack())
        {
            AbstractHorse abstracthorse = getClosestHorse(this, 16.0D);

            if (abstracthorse != null && getDistanceSqToEntity(abstracthorse) > 4.0D)
            {
                navigator.getPathToEntityLiving(abstracthorse);
            }
        }
    }

    public boolean func_190684_dE()
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (openMouthCounter > 0 && ++openMouthCounter > 30)
        {
            openMouthCounter = 0;
            setHorseWatchableBoolean(64, false);
        }

        if (canPassengerSteer() && jumpRearingCounter > 0 && ++jumpRearingCounter > 20)
        {
            jumpRearingCounter = 0;
            setRearing(false);
        }

        if (tailCounter > 0 && ++tailCounter > 8)
        {
            tailCounter = 0;
        }

        if (sprintCounter > 0)
        {
            ++sprintCounter;

            if (sprintCounter > 300)
            {
                sprintCounter = 0;
            }
        }

        prevHeadLean = headLean;

        if (isEatingHaystack())
        {
            headLean += (1.0F - headLean) * 0.4F + 0.05F;

            if (headLean > 1.0F)
            {
                headLean = 1.0F;
            }
        }
        else
        {
            headLean += (0.0F - headLean) * 0.4F - 0.05F;

            if (headLean < 0.0F)
            {
                headLean = 0.0F;
            }
        }

        prevRearingAmount = rearingAmount;

        if (isRearing())
        {
            headLean = 0.0F;
            prevHeadLean = headLean;
            rearingAmount += (1.0F - rearingAmount) * 0.4F + 0.05F;

            if (rearingAmount > 1.0F)
            {
                rearingAmount = 1.0F;
            }
        }
        else
        {
            allowStandSliding = false;
            rearingAmount += (0.8F * rearingAmount * rearingAmount * rearingAmount - rearingAmount) * 0.6F - 0.05F;

            if (rearingAmount < 0.0F)
            {
                rearingAmount = 0.0F;
            }
        }

        prevMouthOpenness = mouthOpenness;

        if (getHorseWatchableBoolean(64))
        {
            mouthOpenness += (1.0F - mouthOpenness) * 0.7F + 0.05F;

            if (mouthOpenness > 1.0F)
            {
                mouthOpenness = 1.0F;
            }
        }
        else
        {
            mouthOpenness += (0.0F - mouthOpenness) * 0.7F - 0.05F;

            if (mouthOpenness < 0.0F)
            {
                mouthOpenness = 0.0F;
            }
        }
    }

    private void openHorseMouth()
    {
        if (!world.isRemote)
        {
            openMouthCounter = 1;
            setHorseWatchableBoolean(64, true);
        }
    }

    public void setEatingHaystack(boolean p_110227_1_)
    {
        setHorseWatchableBoolean(16, p_110227_1_);
    }

    public void setRearing(boolean rearing)
    {
        if (rearing)
        {
            setEatingHaystack(false);
        }

        setHorseWatchableBoolean(32, rearing);
    }

    private void makeHorseRear()
    {
        if (canPassengerSteer())
        {
            jumpRearingCounter = 1;
            setRearing(true);
        }
    }

    public void func_190687_dF()
    {
        makeHorseRear();
        SoundEvent soundevent = getAngrySound();

        if (soundevent != null)
        {
            playSound(soundevent, getSoundVolume(), getSoundPitch());
        }
    }

    public boolean setTamedBy(EntityPlayer player)
    {
        setOwnerUniqueId(player.getUniqueID());
        setHorseTamed(true);

        if (player instanceof EntityPlayerMP)
        {
            CriteriaTriggers.field_193136_w.func_193178_a((EntityPlayerMP)player, this);
        }

        world.setEntityState(this, (byte)7);
        return true;
    }

    public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        if (isBeingRidden() && canBeSteered() && isHorseSaddled())
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase) getControllingPassenger();
            rotationYaw = entitylivingbase.rotationYaw;
            prevRotationYaw = rotationYaw;
            rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            setRotation(rotationYaw, rotationPitch);
            renderYawOffset = rotationYaw;
            rotationYawHead = renderYawOffset;
            p_191986_1_ = entitylivingbase.moveStrafing * 0.5F;
            p_191986_3_ = entitylivingbase.field_191988_bg;

            if (p_191986_3_ <= 0.0F)
            {
                p_191986_3_ *= 0.25F;
                gallopTime = 0;
            }

            if (onGround && jumpPower == 0.0F && isRearing() && !allowStandSliding)
            {
                p_191986_1_ = 0.0F;
                p_191986_3_ = 0.0F;
            }

            if (jumpPower > 0.0F && !isHorseJumping() && onGround)
            {
                motionY = getHorseJumpStrength() * (double) jumpPower;

                if (isPotionActive(MobEffects.JUMP_BOOST))
                {
                    motionY += (float)(getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
                }

                setHorseJumping(true);
                isAirBorne = true;

                if (p_191986_3_ > 0.0F)
                {
                    float f = MathHelper.sin(rotationYaw * 0.017453292F);
                    float f1 = MathHelper.cos(rotationYaw * 0.017453292F);
                    motionX += -0.4F * f * jumpPower;
                    motionZ += 0.4F * f1 * jumpPower;
                    playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
                }

                jumpPower = 0.0F;
            }

            jumpMovementFactor = getAIMoveSpeed() * 0.1F;

            if (canPassengerSteer())
            {
                setAIMoveSpeed((float) getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
            }
            else if (entitylivingbase instanceof EntityPlayer)
            {
                motionX = 0.0D;
                motionY = 0.0D;
                motionZ = 0.0D;
            }

            if (onGround)
            {
                jumpPower = 0.0F;
                setHorseJumping(false);
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
        else
        {
            jumpMovementFactor = 0.02F;
            super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
        }
    }

    public static void func_190683_c(DataFixer p_190683_0_, Class<?> p_190683_1_)
    {
        EntityLiving.registerFixesMob(p_190683_0_, p_190683_1_);
        p_190683_0_.registerWalker(FixTypes.ENTITY, new ItemStackData(p_190683_1_, "SaddleItem"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("EatingHaystack", isEatingHaystack());
        compound.setBoolean("Bred", isBreeding());
        compound.setInteger("Temper", getTemper());
        compound.setBoolean("Tame", isTame());

        if (getOwnerUniqueId() != null)
        {
            compound.setString("OwnerUUID", getOwnerUniqueId().toString());
        }

        if (!horseChest.getStackInSlot(0).isEmpty())
        {
            compound.setTag("SaddleItem", horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setEatingHaystack(compound.getBoolean("EatingHaystack"));
        setBreeding(compound.getBoolean("Bred"));
        setTemper(compound.getInteger("Temper"));
        setHorseTamed(compound.getBoolean("Tame"));
        String s;

        if (compound.hasKey("OwnerUUID", 8))
        {
            s = compound.getString("OwnerUUID");
        }
        else
        {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(getServer(), s1);
        }

        if (!s.isEmpty())
        {
            setOwnerUniqueId(UUID.fromString(s));
        }

        IAttributeInstance iattributeinstance = getAttributeMap().getAttributeInstanceByName("Speed");

        if (iattributeinstance != null)
        {
            getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(iattributeinstance.getBaseValue() * 0.25D);
        }

        if (compound.hasKey("SaddleItem", 10))
        {
            ItemStack itemstack = new ItemStack(compound.getCompoundTag("SaddleItem"));

            if (itemstack.getItem() == Items.SADDLE)
            {
                horseChest.setInventorySlotContents(0, itemstack);
            }
        }

        updateHorseSlots();
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        return false;
    }

    /**
     * Return true if the horse entity ready to mate. (no rider, not riding, tame, adult, not steril...)
     */
    protected boolean canMate()
    {
        return !isBeingRidden() && !isRiding() && isTame() && !isChild() && getHealth() >= getMaxHealth() && isInLove();
    }

    @Nullable
    public EntityAgeable createChild(EntityAgeable ageable)
    {
        return null;
    }

    protected void func_190681_a(EntityAgeable p_190681_1_, AbstractHorse p_190681_2_)
    {
        double d0 = getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + (double) getModifiedMaxHealth();
        p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(d0 / 3.0D);
        double d1 = getEntityAttribute(JUMP_STRENGTH).getBaseValue() + p_190681_1_.getEntityAttribute(JUMP_STRENGTH).getBaseValue() + getModifiedJumpStrength();
        p_190681_2_.getEntityAttribute(JUMP_STRENGTH).setBaseValue(d1 / 3.0D);
        double d2 = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + getModifiedMovementSpeed();
        p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(d2 / 3.0D);
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
        return getControllingPassenger() instanceof EntityLivingBase;
    }

    public float getGrassEatingAmount(float p_110258_1_)
    {
        return prevHeadLean + (headLean - prevHeadLean) * p_110258_1_;
    }

    public float getRearingAmount(float p_110223_1_)
    {
        return prevRearingAmount + (rearingAmount - prevRearingAmount) * p_110223_1_;
    }

    public float getMouthOpennessAngle(float p_110201_1_)
    {
        return prevMouthOpenness + (mouthOpenness - prevMouthOpenness) * p_110201_1_;
    }

    public void setJumpPower(int jumpPowerIn)
    {
        if (isHorseSaddled())
        {
            if (jumpPowerIn < 0)
            {
                jumpPowerIn = 0;
            }
            else
            {
                allowStandSliding = true;
                makeHorseRear();
            }

            if (jumpPowerIn >= 90)
            {
                jumpPower = 1.0F;
            }
            else
            {
                jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
            }
        }
    }

    public boolean canJump()
    {
        return isHorseSaddled();
    }

    public void handleStartJump(int p_184775_1_)
    {
        allowStandSliding = true;
        makeHorseRear();
    }

    public void handleStopJump()
    {
    }

    /**
     * "Spawns particles for the horse entity. par1 tells whether to spawn hearts. If it is false, it spawns smoke."
     */
    protected void spawnHorseParticles(boolean p_110216_1_)
    {
        EnumParticleTypes enumparticletypes = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;

        for (int i = 0; i < 7; ++i)
        {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            world.spawnParticle(enumparticletypes, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + 0.5D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, d0, d1, d2);
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 7)
        {
            spawnHorseParticles(true);
        }
        else if (id == 6)
        {
            spawnHorseParticles(false);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public void updatePassenger(Entity passenger)
    {
        super.updatePassenger(passenger);

        if (passenger instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)passenger;
            renderYawOffset = entityliving.renderYawOffset;
        }

        if (prevRearingAmount > 0.0F)
        {
            float f3 = MathHelper.sin(renderYawOffset * 0.017453292F);
            float f = MathHelper.cos(renderYawOffset * 0.017453292F);
            float f1 = 0.7F * prevRearingAmount;
            float f2 = 0.15F * prevRearingAmount;
            passenger.setPosition(posX + (double)(f1 * f3), posY + getMountedYOffset() + passenger.getYOffset() + (double)f2, posZ - (double)(f1 * f));

            if (passenger instanceof EntityLivingBase)
            {
                ((EntityLivingBase)passenger).renderYawOffset = renderYawOffset;
            }
        }
    }

    /**
     * Returns randomized max health
     */
    protected float getModifiedMaxHealth()
    {
        return 15.0F + (float) rand.nextInt(8) + (float) rand.nextInt(9);
    }

    /**
     * Returns randomized jump strength
     */
    protected double getModifiedJumpStrength()
    {
        return 0.4000000059604645D + rand.nextDouble() * 0.2D + rand.nextDouble() * 0.2D + rand.nextDouble() * 0.2D;
    }

    /**
     * Returns randomized movement speed
     */
    protected double getModifiedMovementSpeed()
    {
        return (0.44999998807907104D + rand.nextDouble() * 0.3D + rand.nextDouble() * 0.3D + rand.nextDouble() * 0.3D) * 0.25D;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return false;
    }

    public float getEyeHeight()
    {
        return height;
    }

    public boolean func_190677_dK()
    {
        return false;
    }

    public boolean func_190682_f(ItemStack p_190682_1_)
    {
        return false;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        int i = inventorySlot - 400;

        if (i >= 0 && i < 2 && i < horseChest.getSizeInventory())
        {
            if (i == 0 && itemStackIn.getItem() != Items.SADDLE)
            {
                return false;
            }
            else if (i != 1 || func_190677_dK() && func_190682_f(itemStackIn))
            {
                horseChest.setInventorySlotContents(i, itemStackIn);
                updateHorseSlots();
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            int j = inventorySlot - 500 + 2;

            if (j >= 2 && j < horseChest.getSizeInventory())
            {
                horseChest.setInventorySlotContents(j, itemStackIn);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Nullable

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    public Entity getControllingPassenger()
    {
        return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (rand.nextInt(5) == 0)
        {
            setGrowingAge(-24000);
        }

        return livingdata;
    }
}
