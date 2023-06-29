package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWolf extends EntityTameable
{
    private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.createKey(EntityWolf.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(EntityWolf.class, DataSerializers.VARINT);

    /** Float used to smooth the rotation of the wolf head */
    private float headRotationCourse;
    private float headRotationCourseOld;

    /** true is the wolf is wet else false */
    private boolean isWet;

    /** True if the wolf is shaking else False */
    private boolean isShaking;

    /**
     * This time increases while wolf is shaking and emitting water particles.
     */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    public EntityWolf(World worldIn)
    {
        super(worldIn);
        setSize(0.6F, 0.85F);
        setTamed(false);
    }

    protected void initEntityAI()
    {
        aiSit = new EntityAISit(this);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, aiSit);
        tasks.addTask(3, new EntityWolf.AIAvoidEntity(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
        tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
        tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
        tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        tasks.addTask(7, new EntityAIMate(this, 1.0D));
        tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
        tasks.addTask(9, new EntityAIBeg(this, 8.0F));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(10, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit;
            }
        }));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, AbstractSkeleton.class, false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);

        if (isTamed())
        {
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        }
        else
        {
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        super.setAttackTarget(entitylivingbaseIn);

        if (entitylivingbaseIn == null)
        {
            setAngry(false);
        }
        else if (!isTamed())
        {
            setAngry(true);
        }
    }

    protected void updateAITasks()
    {
        dataManager.set(DATA_HEALTH_ID, Float.valueOf(getHealth()));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(DATA_HEALTH_ID, Float.valueOf(getHealth()));
        dataManager.register(BEGGING, Boolean.valueOf(false));
        dataManager.register(COLLAR_COLOR, Integer.valueOf(EnumDyeColor.RED.getDyeDamage()));
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    public static void registerFixesWolf(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityWolf.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Angry", isAngry());
        compound.setByte("CollarColor", (byte) getCollarColor().getDyeDamage());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setAngry(compound.getBoolean("Angry"));

        if (compound.hasKey("CollarColor", 99))
        {
            setCollarColor(EnumDyeColor.byDyeDamage(compound.getByte("CollarColor")));
        }
    }

    protected SoundEvent getAmbientSound()
    {
        if (isAngry())
        {
            return SoundEvents.ENTITY_WOLF_GROWL;
        }
        else if (rand.nextInt(3) == 0)
        {
            return isTamed() && dataManager.get(DATA_HEALTH_ID).floatValue() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        }
        else
        {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_WOLF;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!world.isRemote && isWet && !isShaking && !hasPath() && onGround)
        {
            isShaking = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
            world.setEntityState(this, (byte)8);
        }

        if (!world.isRemote && getAttackTarget() == null && isAngry())
        {
            setAngry(false);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        headRotationCourseOld = headRotationCourse;

        if (isBegging())
        {
            headRotationCourse += (1.0F - headRotationCourse) * 0.4F;
        }
        else
        {
            headRotationCourse += (0.0F - headRotationCourse) * 0.4F;
        }

        if (isWet())
        {
            isWet = true;
            isShaking = false;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
        }
        else if ((isWet || isShaking) && isShaking)
        {
            if (timeWolfIsShaking == 0.0F)
            {
                playSound(SoundEvents.ENTITY_WOLF_SHAKE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            prevTimeWolfIsShaking = timeWolfIsShaking;
            timeWolfIsShaking += 0.05F;

            if (prevTimeWolfIsShaking >= 2.0F)
            {
                isWet = false;
                isShaking = false;
                prevTimeWolfIsShaking = 0.0F;
                timeWolfIsShaking = 0.0F;
            }

            if (timeWolfIsShaking > 0.4F)
            {
                float f = (float) getEntityBoundingBox().minY;
                int i = (int)(MathHelper.sin((timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

                for (int j = 0; j < i; ++j)
                {
                    float f1 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX + (double)f1, f + 0.8F, posZ + (double)f2, motionX, motionY, motionZ);
                }
            }
        }
    }

    /**
     * True if the wolf is wet
     */
    public boolean isWolfWet()
    {
        return isWet;
    }

    /**
     * Used when calculating the amount of shading to apply while the wolf is wet.
     */
    public float getShadingWhileWet(float p_70915_1_)
    {
        return 0.75F + (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
    }

    public float getShakeAngle(float p_70923_1_, float p_70923_2_)
    {
        float f = (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;

        if (f < 0.0F)
        {
            f = 0.0F;
        }
        else if (f > 1.0F)
        {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    public float getInterestedAngle(float p_70917_1_)
    {
        return (headRotationCourseOld + (headRotationCourse - headRotationCourseOld) * p_70917_1_) * 0.15F * (float)Math.PI;
    }

    public float getEyeHeight()
    {
        return height * 0.8F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return isSitting() ? 20 : super.getVerticalFaceSpeed();
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
            Entity entity = source.getEntity();

            if (aiSit != null)
            {
                aiSit.setSitting(false);
            }

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag)
        {
            applyEnchantments(this, entityIn);
        }

        return flag;
    }

    public void setTamed(boolean tamed)
    {
        super.setTamed(tamed);

        if (tamed)
        {
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        }
        else
        {
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (isTamed())
        {
            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() instanceof ItemFood)
                {
                    ItemFood itemfood = (ItemFood)itemstack.getItem();

                    if (itemfood.isWolfsFavoriteMeat() && dataManager.get(DATA_HEALTH_ID).floatValue() < 20.0F)
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            itemstack.func_190918_g(1);
                        }

                        heal((float)itemfood.getHealAmount(itemstack));
                        return true;
                    }
                }
                else if (itemstack.getItem() == Items.DYE)
                {
                    EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

                    if (enumdyecolor != getCollarColor())
                    {
                        setCollarColor(enumdyecolor);

                        if (!player.capabilities.isCreativeMode)
                        {
                            itemstack.func_190918_g(1);
                        }

                        return true;
                    }
                }
            }

            if (isOwner(player) && !world.isRemote && !isBreedingItem(itemstack))
            {
                aiSit.setSitting(!isSitting());
                isJumping = false;
                navigator.clearPathEntity();
                setAttackTarget(null);
            }
        }
        else if (itemstack.getItem() == Items.BONE && !isAngry())
        {
            if (!player.capabilities.isCreativeMode)
            {
                itemstack.func_190918_g(1);
            }

            if (!world.isRemote)
            {
                if (rand.nextInt(3) == 0)
                {
                    func_193101_c(player);
                    navigator.clearPathEntity();
                    setAttackTarget(null);
                    aiSit.setSitting(true);
                    setHealth(20.0F);
                    playTameEffect(true);
                    world.setEntityState(this, (byte)7);
                }
                else
                {
                    playTameEffect(false);
                    world.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 8)
        {
            isShaking = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public float getTailRotation()
    {
        if (isAngry())
        {
            return 1.5393804F;
        }
        else
        {
            return isTamed() ? (0.55F - (getMaxHealth() - dataManager.get(DATA_HEALTH_ID).floatValue()) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
        }
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isWolfsFavoriteMeat();
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 8;
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry()
    {
        return (dataManager.get(EntityTameable.TAMED).byteValue() & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean angry)
    {
        byte b0 = dataManager.get(EntityTameable.TAMED).byteValue();

        if (angry)
        {
            dataManager.set(EntityTameable.TAMED, Byte.valueOf((byte)(b0 | 2)));
        }
        else
        {
            dataManager.set(EntityTameable.TAMED, Byte.valueOf((byte)(b0 & -3)));
        }
    }

    public EnumDyeColor getCollarColor()
    {
        return EnumDyeColor.byDyeDamage(dataManager.get(COLLAR_COLOR).intValue() & 15);
    }

    public void setCollarColor(EnumDyeColor collarcolor)
    {
        dataManager.set(COLLAR_COLOR, Integer.valueOf(collarcolor.getDyeDamage()));
    }

    public EntityWolf createChild(EntityAgeable ageable)
    {
        EntityWolf entitywolf = new EntityWolf(world);
        UUID uuid = getOwnerId();

        if (uuid != null)
        {
            entitywolf.setOwnerId(uuid);
            entitywolf.setTamed(true);
        }

        return entitywolf;
    }

    public void setBegging(boolean beg)
    {
        dataManager.set(BEGGING, Boolean.valueOf(beg));
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!isTamed())
        {
            return false;
        }
        else if (!(otherAnimal instanceof EntityWolf))
        {
            return false;
        }
        else
        {
            EntityWolf entitywolf = (EntityWolf)otherAnimal;

            if (!entitywolf.isTamed())
            {
                return false;
            }
            else if (entitywolf.isSitting())
            {
                return false;
            }
            else
            {
                return isInLove() && entitywolf.isInLove();
            }
        }
    }

    public boolean isBegging()
    {
        return dataManager.get(BEGGING).booleanValue();
    }

    public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_)
    {
        if (!(p_142018_1_ instanceof EntityCreeper) && !(p_142018_1_ instanceof EntityGhast))
        {
            if (p_142018_1_ instanceof EntityWolf)
            {
                EntityWolf entitywolf = (EntityWolf)p_142018_1_;

                if (entitywolf.isTamed() && entitywolf.getOwner() == p_142018_2_)
                {
                    return false;
                }
            }

            if (p_142018_1_ instanceof EntityPlayer && p_142018_2_ instanceof EntityPlayer && !((EntityPlayer)p_142018_2_).canAttackPlayer((EntityPlayer)p_142018_1_))
            {
                return false;
            }
            else
            {
                return !(p_142018_1_ instanceof AbstractHorse) || !((AbstractHorse)p_142018_1_).isTame();
            }
        }
        else
        {
            return false;
        }
    }

    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return !isAngry() && super.canBeLeashedTo(player);
    }

    class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T>
    {
        private final EntityWolf field_190856_d;

        public AIAvoidEntity(EntityWolf p_i47251_2_, Class<T> p_i47251_3_, float p_i47251_4_, double p_i47251_5_, double p_i47251_7_)
        {
            super(p_i47251_2_, p_i47251_3_, p_i47251_4_, p_i47251_5_, p_i47251_7_);
            field_190856_d = p_i47251_2_;
        }

        public boolean shouldExecute()
        {
            if (super.shouldExecute() && closestLivingEntity instanceof EntityLlama)
            {
                return !field_190856_d.isTamed() && func_190854_a((EntityLlama) closestLivingEntity);
            }
            else
            {
                return false;
            }
        }

        private boolean func_190854_a(EntityLlama p_190854_1_)
        {
            return p_190854_1_.func_190707_dL() >= rand.nextInt(5);
        }

        public void startExecuting()
        {
            setAttackTarget(null);
            super.startExecuting();
        }

        public void updateTask()
        {
            setAttackTarget(null);
            super.updateTask();
        }
    }
}
