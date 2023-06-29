package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombie extends EntityMob
{
    /**
     * The attribute which determines the chance that this mob will spawn reinforcements
     */
    protected static final IAttribute SPAWN_REINFORCEMENTS_CHANCE = (new RangedAttribute(null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
    private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VILLAGER_TYPE = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
    private boolean isBreakDoorsTaskSet;

    /** The width of the entity */
    private float zombieWidth = -1.0F;

    /** The height of the the entity. */
    private float zombieHeight;

    public EntityZombie(World worldIn)
    {
        super(worldIn);
        setSize(0.6F, 1.95F);
    }

    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
        tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        applyEntityAI();
    }

    protected void applyEntityAI()
    {
        tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPigZombie.class));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(rand.nextDouble() * 0.10000000149011612D);
    }

    protected void entityInit()
    {
        super.entityInit();
        getDataManager().register(IS_CHILD, Boolean.valueOf(false));
        getDataManager().register(VILLAGER_TYPE, Integer.valueOf(0));
        getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
    }

    public void setArmsRaised(boolean armsRaised)
    {
        getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
    }

    public boolean isArmsRaised()
    {
        return getDataManager().get(ARMS_RAISED).booleanValue();
    }

    public boolean isBreakDoorsTaskSet()
    {
        return isBreakDoorsTaskSet;
    }

    /**
     * Sets or removes EntityAIBreakDoor task
     */
    public void setBreakDoorsAItask(boolean enabled)
    {
        if (isBreakDoorsTaskSet != enabled)
        {
            isBreakDoorsTaskSet = enabled;
            ((PathNavigateGround) getNavigator()).setBreakDoors(enabled);

            if (enabled)
            {
                tasks.addTask(1, breakDoor);
            }
            else
            {
                tasks.removeTask(breakDoor);
            }
        }
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return getDataManager().get(IS_CHILD).booleanValue();
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        if (isChild())
        {
            experienceValue = (int)((float) experienceValue * 2.5F);
        }

        return super.getExperiencePoints(player);
    }

    /**
     * Set whether this zombie is a child.
     */
    public void setChild(boolean childZombie)
    {
        getDataManager().set(IS_CHILD, Boolean.valueOf(childZombie));

        if (world != null && !world.isRemote)
        {
            IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(BABY_SPEED_BOOST);

            if (childZombie)
            {
                iattributeinstance.applyModifier(BABY_SPEED_BOOST);
            }
        }

        setChildSize(childZombie);
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (IS_CHILD.equals(key))
        {
            setChildSize(isChild());
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (world.isDaytime() && !world.isRemote && !isChild() && func_190730_o())
        {
            float f = getBrightness();

            if (f > 0.5F && rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && world.canSeeSky(new BlockPos(posX, posY + (double) getEyeHeight(), posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (!itemstack.isEmpty())
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamage() + rand.nextInt(2));

                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
                        {
                            renderBrokenItemStack(itemstack);
                            setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    setFire(8);
                }
            }
        }

        super.onLivingUpdate();
    }

    protected boolean func_190730_o()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (super.attackEntityFrom(source, amount))
        {
            EntityLivingBase entitylivingbase = getAttackTarget();

            if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase)
            {
                entitylivingbase = (EntityLivingBase)source.getEntity();
            }

            if (entitylivingbase != null && world.getDifficulty() == EnumDifficulty.HARD && (double) rand.nextFloat() < getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue() && world.getGameRules().getBoolean("doMobSpawning"))
            {
                int i = MathHelper.floor(posX);
                int j = MathHelper.floor(posY);
                int k = MathHelper.floor(posZ);
                EntityZombie entityzombie = new EntityZombie(world);

                for (int l = 0; l < 50; ++l)
                {
                    int i1 = i + MathHelper.getInt(rand, 7, 40) * MathHelper.getInt(rand, -1, 1);
                    int j1 = j + MathHelper.getInt(rand, 7, 40) * MathHelper.getInt(rand, -1, 1);
                    int k1 = k + MathHelper.getInt(rand, 7, 40) * MathHelper.getInt(rand, -1, 1);

                    if (world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isFullyOpaque() && world.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10)
                    {
                        entityzombie.setPosition(i1, j1, k1);

                        if (!world.isAnyPlayerWithinRangeAt(i1, j1, k1, 7.0D) && world.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && world.getCollisionBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entityzombie.getEntityBoundingBox()))
                        {
                            world.spawnEntityInWorld(entityzombie);
                            entityzombie.setAttackTarget(entitylivingbase);
                            entityzombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityzombie)), null);
                            getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
                            entityzombie.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
                            break;
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = super.attackEntityAsMob(entityIn);

        if (flag)
        {
            float f = world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

            if (getHeldItemMainhand().isEmpty() && isBurning() && rand.nextFloat() < f * 0.3F)
            {
                entityIn.setFire(2 * (int)f);
            }
        }

        return flag;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected SoundEvent func_190731_di()
    {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        playSound(func_190731_di(), 0.15F, 1.0F);
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.ENTITIES_ZOMBIE;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        super.setEquipmentBasedOnDifficulty(difficulty);

        if (rand.nextFloat() < (world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F))
        {
            int i = rand.nextInt(3);

            if (i == 0)
            {
                setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            else
            {
                setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }

    public static void registerFixesZombie(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityZombie.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (isChild())
        {
            compound.setBoolean("IsBaby", true);
        }

        compound.setBoolean("CanBreakDoors", isBreakDoorsTaskSet());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.getBoolean("IsBaby"))
        {
            setChild(true);
        }

        setBreakDoorsAItask(compound.getBoolean("CanBreakDoors"));
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn)
    {
        super.onKillEntity(entityLivingIn);

        if ((world.getDifficulty() == EnumDifficulty.NORMAL || world.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager)
        {
            if (world.getDifficulty() != EnumDifficulty.HARD && rand.nextBoolean())
            {
                return;
            }

            EntityVillager entityvillager = (EntityVillager)entityLivingIn;
            EntityZombieVillager entityzombievillager = new EntityZombieVillager(world);
            entityzombievillager.copyLocationAndAnglesFrom(entityvillager);
            world.removeEntity(entityvillager);
            entityzombievillager.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityzombievillager)), new EntityZombie.GroupData(false));
            entityzombievillager.func_190733_a(entityvillager.getProfession());
            entityzombievillager.setChild(entityvillager.isChild());
            entityzombievillager.setNoAI(entityvillager.isAIDisabled());

            if (entityvillager.hasCustomName())
            {
                entityzombievillager.setCustomNameTag(entityvillager.getCustomNameTag());
                entityzombievillager.setAlwaysRenderNameTag(entityvillager.getAlwaysRenderNameTag());
            }

            world.spawnEntityInWorld(entityzombievillager);
            world.playEvent(null, 1026, new BlockPos(this), 0);
        }
    }

    public float getEyeHeight()
    {
        float f = 1.74F;

        if (isChild())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }

    protected boolean canEquipItem(ItemStack stack)
    {
        return (stack.getItem() != Items.EGG || !isChild() || !isRiding()) && super.canEquipItem(stack);
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        float f = difficulty.getClampedAdditionalDifficulty();
        setCanPickUpLoot(rand.nextFloat() < 0.55F * f);

        if (livingdata == null)
        {
            livingdata = new EntityZombie.GroupData(world.rand.nextFloat() < 0.05F);
        }

        if (livingdata instanceof EntityZombie.GroupData)
        {
            EntityZombie.GroupData entityzombie$groupdata = (EntityZombie.GroupData)livingdata;

            if (entityzombie$groupdata.isChild)
            {
                setChild(true);

                if ((double) world.rand.nextFloat() < 0.05D)
                {
                    List<EntityChicken> list = world.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);

                    if (!list.isEmpty())
                    {
                        EntityChicken entitychicken = list.get(0);
                        entitychicken.setChickenJockey(true);
                        startRiding(entitychicken);
                    }
                }
                else if ((double) world.rand.nextFloat() < 0.05D)
                {
                    EntityChicken entitychicken1 = new EntityChicken(world);
                    entitychicken1.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    entitychicken1.onInitialSpawn(difficulty, null);
                    entitychicken1.setChickenJockey(true);
                    world.spawnEntityInWorld(entitychicken1);
                    startRiding(entitychicken1);
                }
            }
        }

        setBreakDoorsAItask(rand.nextFloat() < f * 0.1F);
        setEquipmentBasedOnDifficulty(difficulty);
        setEnchantmentBasedOnDifficulty(difficulty);

        if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
        {
            Calendar calendar = world.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F)
            {
                setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextDouble() * 0.05000000074505806D, 0));
        double d0 = rand.nextDouble() * 1.5D * (double)f;

        if (d0 > 1.0D)
        {
            getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (rand.nextFloat() < f * 0.05F)
        {
            getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 0.25D + 0.5D, 0));
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 3.0D + 1.0D, 2));
            setBreakDoorsAItask(true);
        }

        return livingdata;
    }

    /**
     * sets the size of the entity to be half of its current size if true.
     */
    public void setChildSize(boolean isChild)
    {
        multiplySize(isChild ? 0.5F : 1.0F);
    }

    /**
     * Sets the width and height of the entity.
     */
    protected final void setSize(float width, float height)
    {
        boolean flag = zombieWidth > 0.0F && zombieHeight > 0.0F;
        zombieWidth = width;
        zombieHeight = height;

        if (!flag)
        {
            multiplySize(1.0F);
        }
    }

    /**
     * Multiplies the height and width by the provided float.
     */
    protected final void multiplySize(float size)
    {
        super.setSize(zombieWidth * size, zombieHeight * size);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return isChild() ? 0.0D : -0.45D;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (cause.getEntity() instanceof EntityCreeper)
        {
            EntityCreeper entitycreeper = (EntityCreeper)cause.getEntity();

            if (entitycreeper.getPowered() && entitycreeper.isAIEnabled())
            {
                entitycreeper.incrementDroppedSkulls();
                ItemStack itemstack = func_190732_dj();

                if (!itemstack.isEmpty())
                {
                    entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    protected ItemStack func_190732_dj()
    {
        return new ItemStack(Items.SKULL, 1, 2);
    }

    class GroupData implements IEntityLivingData
    {
        public boolean isChild;

        private GroupData(boolean p_i47328_2_)
        {
            isChild = p_i47328_2_;
        }
    }
}
