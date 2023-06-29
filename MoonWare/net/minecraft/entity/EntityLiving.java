package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import optifine.Config;
import optifine.Reflector;

public abstract class EntityLiving extends EntityLivingBase
{
    private static final DataParameter<Byte> AI_FLAGS = EntityDataManager.createKey(EntityLiving.class, DataSerializers.BYTE);

    /** Number of ticks since this EntityLiving last produced its sound */
    public int livingSoundTime;

    /** The experience points the Entity gives. */
    protected int experienceValue;
    private final EntityLookHelper lookHelper;
    protected EntityMoveHelper moveHelper;

    /** Entity jumping helper */
    protected EntityJumpHelper jumpHelper;
    private final EntityBodyHelper bodyHelper;
    protected PathNavigate navigator;

    /** Passive tasks (wandering, look, idle, ...) */
    protected final EntityAITasks tasks;

    /** Fighting tasks (used by monsters, wolves, ocelots) */
    protected final EntityAITasks targetTasks;

    /** The active target the Task system uses for tracking */
    private EntityLivingBase attackTarget;
    private final EntitySenses senses;
    private final NonNullList<ItemStack> inventoryHands = NonNullList.func_191197_a(2, ItemStack.EMPTY);

    /** Chances for equipment in hands dropping when this entity dies. */
    protected float[] inventoryHandsDropChances = new float[2];
    private final NonNullList<ItemStack> inventoryArmor = NonNullList.func_191197_a(4, ItemStack.EMPTY);

    /** Chances for armor dropping when this entity dies. */
    protected float[] inventoryArmorDropChances = new float[4];

    /** Whether this entity can pick up items from the ground. */
    private boolean canPickUpLoot;

    /** Whether this entity should NOT despawn. */
    private boolean persistenceRequired;
    private final Map<PathNodeType, Float> mapPathPriority = Maps.newEnumMap(PathNodeType.class);
    private Namespaced deathLootTable;
    private long deathLootTableSeed;
    private boolean isLeashed;
    private Entity leashedToEntity;
    private NBTTagCompound leashNBTTag;
    public int randomMobsId;
    public Biome spawnBiome;
    public BlockPos spawnPosition;
    private UUID teamUuid;
    private String teamUuidString;

    public EntityLiving(World worldIn)
    {
        super(worldIn);
        tasks = new EntityAITasks(worldIn != null && worldIn.theProfiler != null ? worldIn.theProfiler : null);
        targetTasks = new EntityAITasks(worldIn != null && worldIn.theProfiler != null ? worldIn.theProfiler : null);
        lookHelper = new EntityLookHelper(this);
        moveHelper = new EntityMoveHelper(this);
        jumpHelper = new EntityJumpHelper(this);
        bodyHelper = createBodyHelper();
        navigator = getNewNavigator(worldIn);
        senses = new EntitySenses(this);
        Arrays.fill(inventoryArmorDropChances, 0.085F);
        Arrays.fill(inventoryHandsDropChances, 0.085F);

        if (worldIn != null && !worldIn.isRemote)
        {
            initEntityAI();
        }

        UUID uuid = getUniqueID();
        long i = uuid.getLeastSignificantBits();
        randomMobsId = (int)(i & 2147483647L);
    }

    protected void initEntityAI()
    {
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigate getNewNavigator(World worldIn)
    {
        return new PathNavigateGround(this, worldIn);
    }

    public float getPathPriority(PathNodeType nodeType)
    {
        Float f = mapPathPriority.get(nodeType);
        return f == null ? nodeType.getPriority() : f.floatValue();
    }

    public void setPathPriority(PathNodeType nodeType, float priority)
    {
        mapPathPriority.put(nodeType, Float.valueOf(priority));
    }

    protected EntityBodyHelper createBodyHelper()
    {
        return new EntityBodyHelper(this);
    }

    public EntityLookHelper getLookHelper()
    {
        return lookHelper;
    }

    public EntityMoveHelper getMoveHelper()
    {
        return moveHelper;
    }

    public EntityJumpHelper getJumpHelper()
    {
        return jumpHelper;
    }

    public PathNavigate getNavigator()
    {
        return navigator;
    }

    /**
     * returns the EntitySenses Object for the EntityLiving
     */
    public EntitySenses getEntitySenses()
    {
        return senses;
    }

    @Nullable

    /**
     * Gets the active target the Task system uses for tracking
     */
    public EntityLivingBase getAttackTarget()
    {
        return attackTarget;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        attackTarget = entitylivingbaseIn;
        Reflector.callVoid(Reflector.ForgeHooks_onLivingSetAttackTarget, this, entitylivingbaseIn);
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    public boolean canAttackClass(Class <? extends EntityLivingBase > cls)
    {
        return cls != EntityGhast.class;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus()
    {
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(AI_FLAGS, Byte.valueOf((byte)0));
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 80;
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound()
    {
        SoundEvent soundevent = getAmbientSound();

        if (soundevent != null)
        {
            playSound(soundevent, getSoundVolume(), getSoundPitch());
        }
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        world.theProfiler.startSection("mobBaseTick");

        if (isEntityAlive() && rand.nextInt(1000) < livingSoundTime++)
        {
            applyEntityAI();
            playLivingSound();
        }

        world.theProfiler.endSection();
    }

    protected void playHurtSound(DamageSource source)
    {
        applyEntityAI();
        super.playHurtSound(source);
    }

    private void applyEntityAI()
    {
        livingSoundTime = -getTalkInterval();
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        if (experienceValue > 0)
        {
            int i = experienceValue;

            for (int j = 0; j < inventoryArmor.size(); ++j)
            {
                if (!inventoryArmor.get(j).isEmpty() && inventoryArmorDropChances[j] <= 1.0F)
                {
                    i += 1 + rand.nextInt(3);
                }
            }

            for (int k = 0; k < inventoryHands.size(); ++k)
            {
                if (!inventoryHands.get(k).isEmpty() && inventoryHandsDropChances[k] <= 1.0F)
                {
                    i += 1 + rand.nextInt(3);
                }
            }

            return i;
        }
        else
        {
            return experienceValue;
        }
    }

    /**
     * Spawns an explosion particle around the Entity's location
     */
    public void spawnExplosionParticle()
    {
        if (world.isRemote)
        {
            for (int i = 0; i < 20; ++i)
            {
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                double d3 = 10.0D;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width - d0 * 10.0D, posY + (double)(rand.nextFloat() * height) - d1 * 10.0D, posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width - d2 * 10.0D, d0, d1, d2);
            }
        }
        else
        {
            world.setEntityState(this, (byte)20);
        }
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 20)
        {
            spawnExplosionParticle();
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (Config.isSmoothWorld() && canSkipUpdate())
        {
            onUpdateMinimal();
        }
        else
        {
            super.onUpdate();

            if (!world.isRemote)
            {
                updateLeashedState();

                if (ticksExisted % 5 == 0)
                {
                    boolean flag = !(getControllingPassenger() instanceof EntityLiving);
                    boolean flag1 = !(getRidingEntity() instanceof EntityBoat);
                    tasks.setControlFlag(1, flag);
                    tasks.setControlFlag(4, flag && flag1);
                    tasks.setControlFlag(2, flag);
                }
            }
        }
    }

    protected float updateDistance(float p_110146_1_, float p_110146_2_)
    {
        bodyHelper.updateRenderAngles();
        return p_110146_2_;
    }

    @Nullable
    protected SoundEvent getAmbientSound()
    {
        return null;
    }

    @Nullable
    protected Item getDropItem()
    {
        return null;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        Item item = getDropItem();

        if (item != null)
        {
            int i = rand.nextInt(3);

            if (lootingModifier > 0)
            {
                i += rand.nextInt(lootingModifier + 1);
            }

            for (int j = 0; j < i; ++j)
            {
                dropItem(item, 1);
            }
        }
    }

    public static void registerFixesMob(DataFixer fixer, Class<?> name)
    {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(name, "ArmorItems", "HandItems"));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("CanPickUpLoot", canPickUpLoot());
        compound.setBoolean("PersistenceRequired", persistenceRequired);
        NBTTagList nbttaglist = new NBTTagList();

        for (ItemStack itemstack : inventoryArmor)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if (!itemstack.isEmpty())
            {
                itemstack.writeToNBT(nbttagcompound);
            }

            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (ItemStack itemstack1 : inventoryHands)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            if (!itemstack1.isEmpty())
            {
                itemstack1.writeToNBT(nbttagcompound1);
            }

            nbttaglist1.appendTag(nbttagcompound1);
        }

        compound.setTag("HandItems", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        for (float f : inventoryArmorDropChances)
        {
            nbttaglist2.appendTag(new NBTTagFloat(f));
        }

        compound.setTag("ArmorDropChances", nbttaglist2);
        NBTTagList nbttaglist3 = new NBTTagList();

        for (float f1 : inventoryHandsDropChances)
        {
            nbttaglist3.appendTag(new NBTTagFloat(f1));
        }

        compound.setTag("HandDropChances", nbttaglist3);
        compound.setBoolean("Leashed", isLeashed);

        if (leashedToEntity != null)
        {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();

            if (leashedToEntity instanceof EntityLivingBase)
            {
                UUID uuid = leashedToEntity.getUniqueID();
                nbttagcompound2.setUniqueId("UUID", uuid);
            }
            else if (leashedToEntity instanceof EntityHanging)
            {
                BlockPos blockpos = ((EntityHanging) leashedToEntity).getHangingPosition();
                nbttagcompound2.setInteger("X", blockpos.getX());
                nbttagcompound2.setInteger("Y", blockpos.getY());
                nbttagcompound2.setInteger("Z", blockpos.getZ());
            }

            compound.setTag("Leash", nbttagcompound2);
        }

        compound.setBoolean("LeftHanded", isLeftHanded());

        if (deathLootTable != null)
        {
            compound.setString("DeathLootTable", deathLootTable.toString());

            if (deathLootTableSeed != 0L)
            {
                compound.setLong("DeathLootTableSeed", deathLootTableSeed);
            }
        }

        if (isAIDisabled())
        {
            compound.setBoolean("NoAI", isAIDisabled());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("CanPickUpLoot", 1))
        {
            setCanPickUpLoot(compound.getBoolean("CanPickUpLoot"));
        }

        persistenceRequired = compound.getBoolean("PersistenceRequired");

        if (compound.hasKey("ArmorItems", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("ArmorItems", 10);

            for (int i = 0; i < inventoryArmor.size(); ++i)
            {
                inventoryArmor.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (compound.hasKey("HandItems", 9))
        {
            NBTTagList nbttaglist1 = compound.getTagList("HandItems", 10);

            for (int j = 0; j < inventoryHands.size(); ++j)
            {
                inventoryHands.set(j, new ItemStack(nbttaglist1.getCompoundTagAt(j)));
            }
        }

        if (compound.hasKey("ArmorDropChances", 9))
        {
            NBTTagList nbttaglist2 = compound.getTagList("ArmorDropChances", 5);

            for (int k = 0; k < nbttaglist2.tagCount(); ++k)
            {
                inventoryArmorDropChances[k] = nbttaglist2.getFloatAt(k);
            }
        }

        if (compound.hasKey("HandDropChances", 9))
        {
            NBTTagList nbttaglist3 = compound.getTagList("HandDropChances", 5);

            for (int l = 0; l < nbttaglist3.tagCount(); ++l)
            {
                inventoryHandsDropChances[l] = nbttaglist3.getFloatAt(l);
            }
        }

        isLeashed = compound.getBoolean("Leashed");

        if (isLeashed && compound.hasKey("Leash", 10))
        {
            leashNBTTag = compound.getCompoundTag("Leash");
        }

        setLeftHanded(compound.getBoolean("LeftHanded"));

        if (compound.hasKey("DeathLootTable", 8))
        {
            deathLootTable = new Namespaced(compound.getString("DeathLootTable"));
            deathLootTableSeed = compound.getLong("DeathLootTableSeed");
        }

        setNoAI(compound.getBoolean("NoAI"));
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return null;
    }

    /**
     * drops the loot of this entity upon death
     */
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        Namespaced resourcelocation = deathLootTable;

        if (resourcelocation == null)
        {
            resourcelocation = getLootTable();
        }

        if (resourcelocation != null)
        {
            LootTable loottable = world.getLootTableManager().getLootTableFromLocation(resourcelocation);
            deathLootTable = null;
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) world)).withLootedEntity(this).withDamageSource(source);

            if (wasRecentlyHit && attackingPlayer != null)
            {
                lootcontext$builder = lootcontext$builder.withPlayer(attackingPlayer).withLuck(attackingPlayer.getLuck());
            }

            for (ItemStack itemstack : loottable.generateLootForPools(deathLootTableSeed == 0L ? rand : new Random(deathLootTableSeed), lootcontext$builder.build()))
            {
                entityDropItem(itemstack, 0.0F);
            }

            dropEquipment(wasRecentlyHit, lootingModifier);
        }
        else
        {
            super.dropLoot(wasRecentlyHit, lootingModifier, source);
        }
    }

    public void func_191989_p(float p_191989_1_)
    {
        field_191988_bg = p_191989_1_;
    }

    public void setMoveForward(float amount)
    {
        moveForward = amount;
    }

    public void setMoveStrafing(float amount)
    {
        moveStrafing = amount;
    }

    /**
     * set the movespeed used for the new AI system
     */
    public void setAIMoveSpeed(float speedIn)
    {
        super.setAIMoveSpeed(speedIn);
        func_191989_p(speedIn);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        world.theProfiler.startSection("looting");

        if (!world.isRemote && canPickUpLoot() && !dead && world.getGameRules().getBoolean("mobGriefing"))
        {
            for (EntityItem entityitem : world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D)))
            {
                if (!entityitem.isDead && !entityitem.getEntityItem().isEmpty() && !entityitem.cannotPickup())
                {
                    updateEquipmentIfNeeded(entityitem);
                }
            }
        }

        world.theProfiler.endSection();
    }

    /**
     * Tests if this entity should pickup a weapon or an armor. Entity drops current weapon or armor if the new one is
     * better.
     */
    protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getEntityItem();
        EntityEquipmentSlot entityequipmentslot = getSlotForItemStack(itemstack);
        boolean flag = true;
        ItemStack itemstack1 = getItemStackFromSlot(entityequipmentslot);

        if (!itemstack1.isEmpty())
        {
            if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND)
            {
                if (itemstack.getItem() instanceof ItemSword && !(itemstack1.getItem() instanceof ItemSword))
                {
                    flag = true;
                }
                else if (itemstack.getItem() instanceof ItemSword && itemstack1.getItem() instanceof ItemSword)
                {
                    ItemSword itemsword = (ItemSword)itemstack.getItem();
                    ItemSword itemsword1 = (ItemSword)itemstack1.getItem();

                    if (itemsword.getDamageVsEntity() == itemsword1.getDamageVsEntity())
                    {
                        flag = itemstack.getMetadata() > itemstack1.getMetadata() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                    }
                    else
                    {
                        flag = itemsword.getDamageVsEntity() > itemsword1.getDamageVsEntity();
                    }
                }
                else if (itemstack.getItem() instanceof ItemBow && itemstack1.getItem() instanceof ItemBow)
                {
                    flag = itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                }
                else
                {
                    flag = false;
                }
            }
            else if (itemstack.getItem() instanceof ItemArmor && !(itemstack1.getItem() instanceof ItemArmor))
            {
                flag = true;
            }
            else if (itemstack.getItem() instanceof ItemArmor && itemstack1.getItem() instanceof ItemArmor && !EnchantmentHelper.func_190938_b(itemstack1))
            {
                ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                ItemArmor itemarmor1 = (ItemArmor)itemstack1.getItem();

                if (itemarmor.damageReduceAmount == itemarmor1.damageReduceAmount)
                {
                    flag = itemstack.getMetadata() > itemstack1.getMetadata() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                }
                else
                {
                    flag = itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
                }
            }
            else
            {
                flag = false;
            }
        }

        if (flag && canEquipItem(itemstack))
        {
            double d0;

            switch (entityequipmentslot.getSlotType())
            {
                case HAND:
                    d0 = inventoryHandsDropChances[entityequipmentslot.getIndex()];
                    break;

                case ARMOR:
                    d0 = inventoryArmorDropChances[entityequipmentslot.getIndex()];
                    break;

                default:
                    d0 = 0.0D;
            }

            if (!itemstack1.isEmpty() && (double)(rand.nextFloat() - 0.1F) < d0)
            {
                entityDropItem(itemstack1, 0.0F);
            }

            setItemStackToSlot(entityequipmentslot, itemstack);

            switch (entityequipmentslot.getSlotType())
            {
                case HAND:
                    inventoryHandsDropChances[entityequipmentslot.getIndex()] = 2.0F;
                    break;

                case ARMOR:
                    inventoryArmorDropChances[entityequipmentslot.getIndex()] = 2.0F;
            }

            persistenceRequired = true;
            onItemPickup(itemEntity, itemstack.getCount());
            itemEntity.setDead();
        }
    }

    protected boolean canEquipItem(ItemStack stack)
    {
        return true;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return true;
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    protected void despawnEntity()
    {
        Object object = null;
        Object object1 = Reflector.getFieldValue(Reflector.Event_Result_DEFAULT);
        Object object2 = Reflector.getFieldValue(Reflector.Event_Result_DENY);

        if (persistenceRequired)
        {
            entityAge = 0;
        }
        else if ((entityAge & 31) == 31 && (object = Reflector.call(Reflector.ForgeEventFactory_canEntityDespawn, this)) != object1)
        {
            if (object == object2)
            {
                entityAge = 0;
            }
            else
            {
                setDead();
            }
        }
        else
        {
            Entity entity = world.getClosestPlayerToEntity(this, -1.0D);

            if (entity != null)
            {
                double d0 = entity.posX - posX;
                double d1 = entity.posY - posY;
                double d2 = entity.posZ - posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (canDespawn() && d3 > 16384.0D)
                {
                    setDead();
                }

                if (entityAge > 600 && rand.nextInt(800) == 0 && d3 > 1024.0D && canDespawn())
                {
                    setDead();
                }
                else if (d3 < 1024.0D)
                {
                    entityAge = 0;
                }
            }
        }
    }

    protected final void updateEntityActionState()
    {
        ++entityAge;
        world.theProfiler.startSection("checkDespawn");
        despawnEntity();
        world.theProfiler.endSection();
        world.theProfiler.startSection("sensing");
        senses.clearSensingCache();
        world.theProfiler.endSection();
        world.theProfiler.startSection("targetSelector");
        targetTasks.onUpdateTasks();
        world.theProfiler.endSection();
        world.theProfiler.startSection("goalSelector");
        tasks.onUpdateTasks();
        world.theProfiler.endSection();
        world.theProfiler.startSection("navigation");
        navigator.onUpdateNavigation();
        world.theProfiler.endSection();
        world.theProfiler.startSection("mob tick");
        updateAITasks();
        world.theProfiler.endSection();

        if (isRiding() && getRidingEntity() instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving) getRidingEntity();
            entityliving.getNavigator().setPath(getNavigator().getPath(), 1.5D);
            entityliving.getMoveHelper().read(getMoveHelper());
        }

        world.theProfiler.startSection("controls");
        world.theProfiler.startSection("move");
        moveHelper.onUpdateMoveHelper();
        world.theProfiler.endStartSection("look");
        lookHelper.onUpdateLook();
        world.theProfiler.endStartSection("jump");
        jumpHelper.doJump();
        world.theProfiler.endSection();
        world.theProfiler.endSection();
    }

    protected void updateAITasks()
    {
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 40;
    }

    public int getHorizontalFaceSpeed()
    {
        return 10;
    }

    /**
     * Changes pitch and yaw so that the entity calling the function is facing the entity provided as an argument.
     */
    public void faceEntity(Entity entityIn, float maxYawIncrease, float maxPitchIncrease)
    {
        double d0 = entityIn.posX - posX;
        double d1 = entityIn.posZ - posZ;
        double d2;

        if (entityIn instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            d2 = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (posY + (double) getEyeHeight());
        }
        else
        {
            d2 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (posY + (double) getEyeHeight());
        }

        double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f = (float)(MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
        float f1 = (float)(-(MathHelper.atan2(d2, d3) * (180D / Math.PI)));
        rotationPitch = updateRotation(rotationPitch, f1, maxPitchIncrease);
        rotationYaw = updateRotation(rotationYaw, f, maxYawIncrease);
    }

    /**
     * Arguments: current rotation, intended rotation, max increment.
     */
    private float updateRotation(float angle, float targetAngle, float maxIncrease)
    {
        float f = MathHelper.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease)
        {
            f = maxIncrease;
        }

        if (f < -maxIncrease)
        {
            f = -maxIncrease;
        }

        return angle + f;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        IBlockState iblockstate = world.getBlockState((new BlockPos(this)).down());
        return iblockstate.canEntitySpawn(this);
    }

    /**
     * Checks that the entity is not colliding with any blocks / liquids
     */
    public boolean isNotColliding()
    {
        return !world.containsAnyLiquid(getEntityBoundingBox()) && world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && world.checkNoEntityCollision(getEntityBoundingBox(), this);
    }

    /**
     * Returns render size modifier
     */
    public float getRenderSizeModifier()
    {
        return 1.0F;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 4;
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight()
    {
        if (getAttackTarget() == null)
        {
            return 3;
        }
        else
        {
            int i = (int)(getHealth() - getMaxHealth() * 0.33F);
            i = i - (3 - world.getDifficulty().getDifficultyId()) * 4;

            if (i < 0)
            {
                i = 0;
            }

            return i + 3;
        }
    }

    public Iterable<ItemStack> getHeldEquipment()
    {
        return inventoryHands;
    }

    public Iterable<ItemStack> getArmorInventoryList()
    {
        return inventoryArmor;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
    {
        switch (slotIn.getSlotType())
        {
            case HAND:
                return inventoryHands.get(slotIn.getIndex());

            case ARMOR:
                return inventoryArmor.get(slotIn.getIndex());

            default:
                return ItemStack.EMPTY;
        }
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
    {
        switch (slotIn.getSlotType())
        {
            case HAND:
                inventoryHands.set(slotIn.getIndex(), stack);
                break;

            case ARMOR:
                inventoryArmor.set(slotIn.getIndex(), stack);
        }
    }

    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
    {
        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
        {
            ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
            double d0;

            switch (entityequipmentslot.getSlotType())
            {
                case HAND:
                    d0 = inventoryHandsDropChances[entityequipmentslot.getIndex()];
                    break;

                case ARMOR:
                    d0 = inventoryArmorDropChances[entityequipmentslot.getIndex()];
                    break;

                default:
                    d0 = 0.0D;
            }

            boolean flag = d0 > 1.0D;

            if (!itemstack.isEmpty() && !EnchantmentHelper.func_190939_c(itemstack) && (wasRecentlyHit || flag) && (double)(rand.nextFloat() - (float)lootingModifier * 0.01F) < d0)
            {
                if (!flag && itemstack.isItemStackDamageable())
                {
                    itemstack.setItemDamage(itemstack.getMaxDamage() - rand.nextInt(1 + rand.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
                }

                entityDropItem(itemstack, 0.0F);
            }
        }
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        if (rand.nextFloat() < 0.15F * difficulty.getClampedAdditionalDifficulty())
        {
            int i = rand.nextInt(2);
            float f = world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            if (rand.nextFloat() < 0.095F)
            {
                ++i;
            }

            boolean flag = true;

            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
            {
                if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
                {
                    ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);

                    if (!flag && rand.nextFloat() < f)
                    {
                        break;
                    }

                    flag = false;

                    if (itemstack.isEmpty())
                    {
                        Item item = getArmorByChance(entityequipmentslot, i);

                        if (item != null)
                        {
                            setItemStackToSlot(entityequipmentslot, new ItemStack(item));
                        }
                    }
                }
            }
        }
    }

    public static EntityEquipmentSlot getSlotForItemStack(ItemStack stack)
    {
        if (stack.getItem() != Item.getItemFromBlock(Blocks.PUMPKIN) && stack.getItem() != Items.SKULL)
        {
            if (stack.getItem() instanceof ItemArmor)
            {
                return ((ItemArmor)stack.getItem()).armorType;
            }
            else if (stack.getItem() == Items.ELYTRA)
            {
                return EntityEquipmentSlot.CHEST;
            }
            else
            {
                boolean flag = stack.getItem() == Items.SHIELD;

                if (Reflector.ForgeItem_isShield.exists())
                {
                    flag = Reflector.callBoolean(stack.getItem(), Reflector.ForgeItem_isShield, stack, null);
                }

                return flag ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND;
            }
        }
        else
        {
            return EntityEquipmentSlot.HEAD;
        }
    }

    @Nullable
    public static Item getArmorByChance(EntityEquipmentSlot slotIn, int chance)
    {
        switch (slotIn)
        {
            case HEAD:
                if (chance == 0)
                {
                    return Items.LEATHER_HELMET;
                }
                else if (chance == 1)
                {
                    return Items.GOLDEN_HELMET;
                }
                else if (chance == 2)
                {
                    return Items.CHAINMAIL_HELMET;
                }
                else if (chance == 3)
                {
                    return Items.IRON_HELMET;
                }
                else if (chance == 4)
                {
                    return Items.DIAMOND_HELMET;
                }

            case CHEST:
                if (chance == 0)
                {
                    return Items.LEATHER_CHESTPLATE;
                }
                else if (chance == 1)
                {
                    return Items.GOLDEN_CHESTPLATE;
                }
                else if (chance == 2)
                {
                    return Items.CHAINMAIL_CHESTPLATE;
                }
                else if (chance == 3)
                {
                    return Items.IRON_CHESTPLATE;
                }
                else if (chance == 4)
                {
                    return Items.DIAMOND_CHESTPLATE;
                }

            case LEGS:
                if (chance == 0)
                {
                    return Items.LEATHER_LEGGINGS;
                }
                else if (chance == 1)
                {
                    return Items.GOLDEN_LEGGINGS;
                }
                else if (chance == 2)
                {
                    return Items.CHAINMAIL_LEGGINGS;
                }
                else if (chance == 3)
                {
                    return Items.IRON_LEGGINGS;
                }
                else if (chance == 4)
                {
                    return Items.DIAMOND_LEGGINGS;
                }

            case FEET:
                if (chance == 0)
                {
                    return Items.LEATHER_BOOTS;
                }
                else if (chance == 1)
                {
                    return Items.GOLDEN_BOOTS;
                }
                else if (chance == 2)
                {
                    return Items.CHAINMAIL_BOOTS;
                }
                else if (chance == 3)
                {
                    return Items.IRON_BOOTS;
                }
                else if (chance == 4)
                {
                    return Items.DIAMOND_BOOTS;
                }

            default:
                return null;
        }
    }

    /**
     * Enchants Entity's current equipments based on given DifficultyInstance
     */
    protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        float f = difficulty.getClampedAdditionalDifficulty();

        if (!getHeldItemMainhand().isEmpty() && rand.nextFloat() < 0.25F * f)
        {
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(rand, getHeldItemMainhand(), (int)(5.0F + f * (float) rand.nextInt(18)), false));
        }

        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
        {
            if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
            {
                ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);

                if (!itemstack.isEmpty() && rand.nextFloat() < 0.5F * f)
                {
                    setItemStackToSlot(entityequipmentslot, EnchantmentHelper.addRandomEnchantment(rand, itemstack, (int)(5.0F + f * (float) rand.nextInt(18)), false));
                }
            }
        }
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextGaussian() * 0.05D, 1));

        setLeftHanded(rand.nextFloat() < 0.05F);

        return livingdata;
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
        return false;
    }

    /**
     * Enable the Entity persistence
     */
    public void enablePersistence()
    {
        persistenceRequired = true;
    }

    public void setDropChance(EntityEquipmentSlot slotIn, float chance)
    {
        switch (slotIn.getSlotType())
        {
            case HAND:
                inventoryHandsDropChances[slotIn.getIndex()] = chance;
                break;

            case ARMOR:
                inventoryArmorDropChances[slotIn.getIndex()] = chance;
        }
    }

    public boolean canPickUpLoot()
    {
        return canPickUpLoot;
    }

    public void setCanPickUpLoot(boolean canPickup)
    {
        canPickUpLoot = canPickup;
    }

    /**
     * Return the persistenceRequired field (whether this entity is allowed to naturally despawn)
     */
    public boolean isNoDespawnRequired()
    {
        return persistenceRequired;
    }

    public final boolean processInitialInteract(EntityPlayer player, EnumHand stack)
    {
        if (getLeashed() && getLeashedToEntity() == player)
        {
            clearLeashed(true, !player.capabilities.isCreativeMode);
            return true;
        }
        else
        {
            ItemStack itemstack = player.getHeldItem(stack);

            if (itemstack.getItem() == Items.LEAD && canBeLeashedTo(player))
            {
                setLeashedToEntity(player, true);
                itemstack.func_190918_g(1);
                return true;
            }
            else
            {
                return processInteract(player, stack) || super.processInitialInteract(player, stack);
            }
        }
    }

    protected boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        return false;
    }

    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    protected void updateLeashedState()
    {
        if (leashNBTTag != null)
        {
            recreateLeash();
        }

        if (isLeashed)
        {
            if (!isEntityAlive())
            {
                clearLeashed(true, true);
            }

            if (leashedToEntity == null || leashedToEntity.isDead)
            {
                clearLeashed(true, true);
            }
        }
    }

    /**
     * Removes the leash from this entity
     */
    public void clearLeashed(boolean sendPacket, boolean dropLead)
    {
        if (isLeashed)
        {
            isLeashed = false;
            leashedToEntity = null;

            if (!world.isRemote && dropLead)
            {
                dropItem(Items.LEAD, 1);
            }

            if (!world.isRemote && sendPacket && world instanceof WorldServer)
            {
                ((WorldServer) world).getEntityTracker().sendToAllTrackingEntity(this, new SPacketEntityAttach(this, null));
            }
        }
    }

    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return !getLeashed() && !(this instanceof IMob);
    }

    public boolean getLeashed()
    {
        return isLeashed;
    }

    public Entity getLeashedToEntity()
    {
        return leashedToEntity;
    }

    /**
     * Sets the entity to be leashed to.
     */
    public void setLeashedToEntity(Entity entityIn, boolean sendAttachNotification)
    {
        isLeashed = true;
        leashedToEntity = entityIn;

        if (!world.isRemote && sendAttachNotification && world instanceof WorldServer)
        {
            ((WorldServer) world).getEntityTracker().sendToAllTrackingEntity(this, new SPacketEntityAttach(this, leashedToEntity));
        }

        if (isRiding())
        {
            dismountRidingEntity();
        }
    }

    public boolean startRiding(Entity entityIn, boolean force)
    {
        boolean flag = super.startRiding(entityIn, force);

        if (flag && getLeashed())
        {
            clearLeashed(true, true);
        }

        return flag;
    }

    private void recreateLeash()
    {
        if (isLeashed && leashNBTTag != null)
        {
            if (leashNBTTag.hasUniqueId("UUID"))
            {
                UUID uuid = leashNBTTag.getUniqueId("UUID");

                for (EntityLivingBase entitylivingbase : world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expandXyz(10.0D)))
                {
                    if (entitylivingbase.getUniqueID().equals(uuid))
                    {
                        setLeashedToEntity(entitylivingbase, true);
                        break;
                    }
                }
            }
            else if (leashNBTTag.hasKey("X", 99) && leashNBTTag.hasKey("Y", 99) && leashNBTTag.hasKey("Z", 99))
            {
                BlockPos blockpos = new BlockPos(leashNBTTag.getInteger("X"), leashNBTTag.getInteger("Y"), leashNBTTag.getInteger("Z"));
                EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(world, blockpos);

                if (entityleashknot == null)
                {
                    entityleashknot = EntityLeashKnot.createKnot(world, blockpos);
                }

                setLeashedToEntity(entityleashknot, true);
            }
            else
            {
                clearLeashed(false, true);
            }
        }

        leashNBTTag = null;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        EntityEquipmentSlot entityequipmentslot;

        if (inventorySlot == 98)
        {
            entityequipmentslot = EntityEquipmentSlot.MAINHAND;
        }
        else if (inventorySlot == 99)
        {
            entityequipmentslot = EntityEquipmentSlot.OFFHAND;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex())
        {
            entityequipmentslot = EntityEquipmentSlot.HEAD;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex())
        {
            entityequipmentslot = EntityEquipmentSlot.CHEST;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex())
        {
            entityequipmentslot = EntityEquipmentSlot.LEGS;
        }
        else
        {
            if (inventorySlot != 100 + EntityEquipmentSlot.FEET.getIndex())
            {
                return false;
            }

            entityequipmentslot = EntityEquipmentSlot.FEET;
        }

        if (!itemStackIn.isEmpty() && !isItemStackInSlot(entityequipmentslot, itemStackIn) && entityequipmentslot != EntityEquipmentSlot.HEAD)
        {
            return false;
        }
        else
        {
            setItemStackToSlot(entityequipmentslot, itemStackIn);
            return true;
        }
    }

    public boolean canPassengerSteer()
    {
        return canBeSteered() && super.canPassengerSteer();
    }

    public static boolean isItemStackInSlot(EntityEquipmentSlot slotIn, ItemStack stack)
    {
        EntityEquipmentSlot entityequipmentslot = getSlotForItemStack(stack);
        return entityequipmentslot == slotIn || entityequipmentslot == EntityEquipmentSlot.MAINHAND && slotIn == EntityEquipmentSlot.OFFHAND || entityequipmentslot == EntityEquipmentSlot.OFFHAND && slotIn == EntityEquipmentSlot.MAINHAND;
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld()
    {
        return super.isServerWorld() && !isAIDisabled();
    }

    /**
     * Set whether this Entity's AI is disabled
     */
    public void setNoAI(boolean disable)
    {
        byte b0 = dataManager.get(AI_FLAGS).byteValue();
        dataManager.set(AI_FLAGS, Byte.valueOf(disable ? (byte)(b0 | 1) : (byte)(b0 & -2)));
    }

    public void setLeftHanded(boolean disable)
    {
        byte b0 = dataManager.get(AI_FLAGS).byteValue();
        dataManager.set(AI_FLAGS, Byte.valueOf(disable ? (byte)(b0 | 2) : (byte)(b0 & -3)));
    }

    /**
     * Get whether this Entity's AI is disabled
     */
    public boolean isAIDisabled()
    {
        return (dataManager.get(AI_FLAGS).byteValue() & 1) != 0;
    }

    public boolean isLeftHanded()
    {
        return (dataManager.get(AI_FLAGS).byteValue() & 2) != 0;
    }

    public EnumHandSide getPrimaryHand()
    {
        return isLeftHanded() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    private boolean canSkipUpdate()
    {
        if (isChild())
        {
            return false;
        }
        else if (hurtTime > 0)
        {
            return false;
        }
        else if (ticksExisted < 20)
        {
            return false;
        }
        else
        {
            World world = getEntityWorld();

            if (world == null)
            {
                return false;
            }
            else if (world.playerEntities.size() != 1)
            {
                return false;
            }
            else
            {
                Entity entity = world.playerEntities.get(0);
                double d0 = Math.max(Math.abs(posX - entity.posX) - 16.0D, 0.0D);
                double d1 = Math.max(Math.abs(posZ - entity.posZ) - 16.0D, 0.0D);
                double d2 = d0 * d0 + d1 * d1;
                return !isInRangeToRenderDist(d2);
            }
        }
    }

    private void onUpdateMinimal()
    {
        ++entityAge;

        if (this instanceof EntityMob)
        {
            float f = getBrightness();

            if (f > 0.5F)
            {
                entityAge += 2;
            }
        }

        despawnEntity();
    }

    public Team getTeam()
    {
        UUID uuid = getUniqueID();

        if (teamUuid != uuid)
        {
            teamUuid = uuid;
            teamUuidString = uuid.toString();
        }

        return world.getScoreboard().getPlayersTeam(teamUuidString);
    }

    public enum SpawnPlacementType
    {
        ON_GROUND,
        IN_AIR,
        IN_WATER
    }
}
