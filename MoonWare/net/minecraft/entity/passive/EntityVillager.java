package net.minecraft.entity.passive;

import java.util.Locale;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant
{
    private static final Logger field_190674_bx = LogManager.getLogger();
    private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityVillager.class, DataSerializers.VARINT);
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;
    @Nullable

    /** This villager's current customer. */
    private EntityPlayer buyingPlayer;
    @Nullable

    /** Initialises the MerchantRecipeList.java */
    private MerchantRecipeList buyingList;
    private int timeUntilReset;

    /** addDefaultEquipmentAndRecipies is called if this is true */
    private boolean needsInitilization;
    private boolean isWillingToMate;
    private int wealth;

    /** Last player to trade with this villager, used for aggressivity. */
    private String lastBuyingPlayer;
    private int careerId;

    /** This is the EntityVillager's career level value */
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private final InventoryBasic villagerInventory;

    /**
     * A multi-dimensional array mapping the various professions, careers and career levels that a Villager may offer
     */
    private static final EntityVillager.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = {{{{new EmeraldForItems(Items.WHEAT, new PriceInfo(18, 22)), new EmeraldForItems(Items.POTATO, new PriceInfo(15, 19)), new EmeraldForItems(Items.CARROT, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.BREAD, new PriceInfo(-4, -2))}, {new EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.PUMPKIN_PIE, new PriceInfo(-3, -2))}, {new EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.APPLE, new PriceInfo(-7, -5))}, {new ListItemForEmeralds(Items.COOKIE, new PriceInfo(-10, -6)), new ListItemForEmeralds(Items.CAKE, new PriceInfo(1, 1))}}, {{new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.FISH, new PriceInfo(6, 6), Items.COOKED_FISH, new PriceInfo(6, 6))}, {new ListEnchantedItemForEmeralds(Items.FISHING_ROD, new PriceInfo(7, 8))}}, {{new EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new PriceInfo(16, 22)), new ListItemForEmeralds(Items.SHEARS, new PriceInfo(3, 4))}, {new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new PriceInfo(1, 2))}}, {{new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.ARROW, new PriceInfo(-12, -8))}, {new ListItemForEmeralds(Items.BOW, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new PriceInfo(10, 10), Items.FLINT, new PriceInfo(6, 10))}}}, {{{new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds()}, {new EmeraldForItems(Items.BOOK, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.COMPASS, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new PriceInfo(3, 4))}, {new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.CLOCK, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new PriceInfo(-5, -3))}, {new ListEnchantedBookForEmeralds()}, {new ListEnchantedBookForEmeralds()}, {new ListItemForEmeralds(Items.NAME_TAG, new PriceInfo(20, 22))}}, {{new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36))}, {new EmeraldForItems(Items.COMPASS, new PriceInfo(1, 1))}, {new ListItemForEmeralds(Items.MAP, new PriceInfo(7, 11))}, {new TreasureMapForEmeralds(new PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT), new TreasureMapForEmeralds(new PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION)}}}, {{{new EmeraldForItems(Items.ROTTEN_FLESH, new PriceInfo(36, 40)), new EmeraldForItems(Items.GOLD_INGOT, new PriceInfo(8, 10))}, {new ListItemForEmeralds(Items.REDSTONE, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1))}, {new ListItemForEmeralds(Items.ENDER_PEARL, new PriceInfo(4, 7)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new PriceInfo(-3, -1))}, {new ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new PriceInfo(3, 11))}}}, {{{new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.IRON_HELMET, new PriceInfo(4, 6))}, {new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListItemForEmeralds(Items.IRON_CHESTPLATE, new PriceInfo(10, 14))}, {new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new PriceInfo(16, 19))}, {new ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new PriceInfo(9, 11)), new ListItemForEmeralds(Items.CHAINMAIL_HELMET, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new PriceInfo(11, 15))}}, {{new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.IRON_AXE, new PriceInfo(6, 8))}, {new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_SWORD, new PriceInfo(9, 10))}, {new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new PriceInfo(9, 12))}}, {{new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new PriceInfo(5, 7))}, {new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new PriceInfo(9, 11))}, {new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new PriceInfo(12, 15))}}}, {{{new EmeraldForItems(Items.PORKCHOP, new PriceInfo(14, 18)), new EmeraldForItems(Items.CHICKEN, new PriceInfo(14, 18))}, {new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.COOKED_PORKCHOP, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.COOKED_CHICKEN, new PriceInfo(-8, -6))}}, {{new EmeraldForItems(Items.LEATHER, new PriceInfo(9, 12)), new ListItemForEmeralds(Items.LEATHER_LEGGINGS, new PriceInfo(2, 4))}, {new ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new PriceInfo(7, 12))}, {new ListItemForEmeralds(Items.SADDLE, new PriceInfo(8, 10))}}}, {new ITradeList[0][]}};

    public EntityVillager(World worldIn)
    {
        this(worldIn, 0);
    }

    public EntityVillager(World worldIn, int professionId)
    {
        super(worldIn);
        villagerInventory = new InventoryBasic("Items", false, 8);
        setProfession(professionId);
        setSize(0.6F, 1.95F);
        ((PathNavigateGround) getNavigator()).setBreakDoors(true);
        setCanPickUpLoot(true);
    }

    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        tasks.addTask(1, new EntityAITradePlayer(this));
        tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        tasks.addTask(2, new EntityAIMoveIndoors(this));
        tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        tasks.addTask(6, new EntityAIVillagerMate(this));
        tasks.addTask(7, new EntityAIFollowGolem(this));
        tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIVillagerInteract(this));
        tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    private void setAdditionalAItasks()
    {
        if (!areAdditionalTasksSet)
        {
            areAdditionalTasksSet = true;

            if (isChild())
            {
                tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            }
            else if (getProfession() == 0)
            {
                tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
            }
        }
    }

    /**
     * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as
     * an adult)
     */
    protected void onGrowingAdult()
    {
        if (getProfession() == 0)
        {
            tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6D));
        }

        super.onGrowingAdult();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    protected void updateAITasks()
    {
        if (--randomTickDivider <= 0)
        {
            BlockPos blockpos = new BlockPos(this);
            world.getVillageCollection().addToVillagerPositionList(blockpos);
            randomTickDivider = 70 + rand.nextInt(50);
            villageObj = world.getVillageCollection().getNearestVillage(blockpos, 32);

            if (villageObj == null)
            {
                detachHome();
            }
            else
            {
                BlockPos blockpos1 = villageObj.getCenter();
                setHomePosAndDistance(blockpos1, villageObj.getVillageRadius());

                if (isLookingForHome)
                {
                    isLookingForHome = false;
                    villageObj.setDefaultPlayerReputation(5);
                }
            }
        }

        if (!isTrading() && timeUntilReset > 0)
        {
            --timeUntilReset;

            if (timeUntilReset <= 0)
            {
                if (needsInitilization)
                {
                    for (MerchantRecipe merchantrecipe : buyingList)
                    {
                        if (merchantrecipe.isRecipeDisabled())
                        {
                            merchantrecipe.increaseMaxTradeUses(rand.nextInt(6) + rand.nextInt(6) + 2);
                        }
                    }

                    populateBuyingList();
                    needsInitilization = false;

                    if (villageObj != null && lastBuyingPlayer != null)
                    {
                        world.setEntityState(this, (byte)14);
                        villageObj.modifyPlayerReputation(lastBuyingPlayer, 1);
                    }
                }

                addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
            }
        }

        super.updateAITasks();
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag)
        {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        else if (!func_190669_a(itemstack, getClass()) && isEntityAlive() && !isTrading() && !isChild())
        {
            if (buyingList == null)
            {
                populateBuyingList();
            }

            if (hand == EnumHand.MAIN_HAND)
            {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }

            if (!world.isRemote && !buyingList.isEmpty())
            {
                setCustomer(player);
                player.displayVillagerTradeGui(this);
            }
            else if (buyingList.isEmpty())
            {
                return super.processInteract(player, hand);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(PROFESSION, Integer.valueOf(0));
    }

    public static void registerFixesVillager(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityVillager.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityVillager.class, "Inventory"));
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker()
        {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
            {
                if (EntityList.getKey(EntityVillager.class).equals(new Namespaced(compound.getString("id"))) && compound.hasKey("Offers", 10))
                {
                    NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");

                    if (nbttagcompound.hasKey("Recipes", 9))
                    {
                        NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 10);

                        for (int i = 0; i < nbttaglist.tagCount(); ++i)
                        {
                            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                            DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buy");
                            DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buyB");
                            DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "sell");
                            nbttaglist.set(i, nbttagcompound1);
                        }
                    }
                }

                return compound;
            }
        });
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", getProfession());
        compound.setInteger("Riches", wealth);
        compound.setInteger("Career", careerId);
        compound.setInteger("CareerLevel", careerLevel);
        compound.setBoolean("Willing", isWillingToMate);

        if (buyingList != null)
        {
            compound.setTag("Offers", buyingList.getRecipiesAsTags());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }

        compound.setTag("Inventory", nbttaglist);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setProfession(compound.getInteger("Profession"));
        wealth = compound.getInteger("Riches");
        careerId = compound.getInteger("Career");
        careerLevel = compound.getInteger("CareerLevel");
        isWillingToMate = compound.getBoolean("Willing");

        if (compound.hasKey("Offers", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
            buyingList = new MerchantRecipeList(nbttagcompound);
        }

        NBTTagList nbttaglist = compound.getTagList("Inventory", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if (!itemstack.isEmpty())
            {
                villagerInventory.addItem(itemstack);
            }
        }

        setCanPickUpLoot(true);
        setAdditionalAItasks();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Nullable
    protected Namespaced getLootTable()
    {
        return LootTableList.field_191184_at;
    }

    public void setProfession(int professionId)
    {
        dataManager.set(PROFESSION, Integer.valueOf(professionId));
    }

    public int getProfession()
    {
        return Math.max(dataManager.get(PROFESSION).intValue() % 6, 0);
    }

    public boolean isMating()
    {
        return isMating;
    }

    public void setMating(boolean mating)
    {
        isMating = mating;
    }

    public void setPlaying(boolean playing)
    {
        isPlaying = playing;
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public void setRevengeTarget(@Nullable EntityLivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (villageObj != null && livingBase != null)
        {
            villageObj.addOrRenewAgressor(livingBase);

            if (livingBase instanceof EntityPlayer)
            {
                int i = -1;

                if (isChild())
                {
                    i = -3;
                }

                villageObj.modifyPlayerReputation(livingBase.getName(), i);

                if (isEntityAlive())
                {
                    world.setEntityState(this, (byte)13);
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        if (villageObj != null)
        {
            Entity entity = cause.getEntity();

            if (entity != null)
            {
                if (entity instanceof EntityPlayer)
                {
                    villageObj.modifyPlayerReputation(entity.getName(), -2);
                }
                else if (entity instanceof IMob)
                {
                    villageObj.endMatingSeason();
                }
            }
            else
            {
                EntityPlayer entityplayer = world.getClosestPlayerToEntity(this, 16.0D);

                if (entityplayer != null)
                {
                    villageObj.endMatingSeason();
                }
            }
        }

        super.onDeath(cause);
    }

    public void setCustomer(@Nullable EntityPlayer player)
    {
        buyingPlayer = player;
    }

    @Nullable
    public EntityPlayer getCustomer()
    {
        return buyingPlayer;
    }

    public boolean isTrading()
    {
        return buyingPlayer != null;
    }

    /**
     * Returns current or updated value of {@link #isWillingToMate}
     */
    public boolean getIsWillingToMate(boolean updateFirst)
    {
        if (!isWillingToMate && updateFirst && hasEnoughFoodToBreed())
        {
            boolean flag = false;

            for (int i = 0; i < villagerInventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = villagerInventory.getStackInSlot(i);

                if (!itemstack.isEmpty())
                {
                    if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3)
                    {
                        flag = true;
                        villagerInventory.decrStackSize(i, 3);
                    }
                    else if ((itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT) && itemstack.getCount() >= 12)
                    {
                        flag = true;
                        villagerInventory.decrStackSize(i, 12);
                    }
                }

                if (flag)
                {
                    world.setEntityState(this, (byte)18);
                    isWillingToMate = true;
                    break;
                }
            }
        }

        return isWillingToMate;
    }

    public void setIsWillingToMate(boolean isWillingToMate)
    {
        this.isWillingToMate = isWillingToMate;
    }

    public void useRecipe(MerchantRecipe recipe)
    {
        recipe.incrementToolUses();
        livingSoundTime = -getTalkInterval();
        playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
        int i = 3 + rand.nextInt(4);

        if (recipe.getToolUses() == 1 || rand.nextInt(5) == 0)
        {
            timeUntilReset = 40;
            needsInitilization = true;
            isWillingToMate = true;

            if (buyingPlayer != null)
            {
                lastBuyingPlayer = buyingPlayer.getName();
            }
            else
            {
                lastBuyingPlayer = null;
            }

            i += 5;
        }

        if (recipe.getItemToBuy().getItem() == Items.EMERALD)
        {
            wealth += recipe.getItemToBuy().getCount();
        }

        if (recipe.getRewardsExp())
        {
            world.spawnEntityInWorld(new EntityXPOrb(world, posX, posY + 0.5D, posZ, i));
        }

        if (buyingPlayer instanceof EntityPlayerMP)
        {
            CriteriaTriggers.field_192138_r.func_192234_a((EntityPlayerMP) buyingPlayer, this, recipe.getItemToSell());
        }
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack)
    {
        if (!world.isRemote && livingSoundTime > -getTalkInterval() + 20)
        {
            livingSoundTime = -getTalkInterval();
            playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
        }
    }

    @Nullable
    public MerchantRecipeList getRecipes(EntityPlayer player)
    {
        if (buyingList == null)
        {
            populateBuyingList();
        }

        return buyingList;
    }

    private void populateBuyingList()
    {
        EntityVillager.ITradeList[][][] aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP[getProfession()];

        if (careerId != 0 && careerLevel != 0)
        {
            ++careerLevel;
        }
        else
        {
            careerId = rand.nextInt(aentityvillager$itradelist.length) + 1;
            careerLevel = 1;
        }

        if (buyingList == null)
        {
            buyingList = new MerchantRecipeList();
        }

        int i = careerId - 1;
        int j = careerLevel - 1;

        if (i >= 0 && i < aentityvillager$itradelist.length)
        {
            EntityVillager.ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];

            if (j >= 0 && j < aentityvillager$itradelist1.length)
            {
                EntityVillager.ITradeList[] aentityvillager$itradelist2 = aentityvillager$itradelist1[j];

                for (EntityVillager.ITradeList entityvillager$itradelist : aentityvillager$itradelist2)
                {
                    entityvillager$itradelist.func_190888_a(this, buyingList, rand);
                }
            }
        }
    }

    public void setRecipes(@Nullable MerchantRecipeList recipeList)
    {
    }

    public World func_190670_t_()
    {
        return world;
    }

    public BlockPos func_190671_u_()
    {
        return new BlockPos(this);
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public Component getDisplayName()
    {
        Team team = getTeam();
        String s = getCustomNameTag();

        if (s != null && !s.isEmpty())
        {
            TextComponent textcomponentstring = new TextComponent(ScorePlayerTeam.formatPlayerName(team, s));
            textcomponentstring.getStyle().setHoverEvent(getHoverEvent());
            textcomponentstring.getStyle().setInsertion(getCachedUniqueIdString());
            return textcomponentstring;
        }
        else
        {
            if (buyingList == null)
            {
                populateBuyingList();
            }

            String s1 = null;

            switch (getProfession())
            {
                case 0:
                    if (careerId == 1)
                    {
                        s1 = "farmer";
                    }
                    else if (careerId == 2)
                    {
                        s1 = "fisherman";
                    }
                    else if (careerId == 3)
                    {
                        s1 = "shepherd";
                    }
                    else if (careerId == 4)
                    {
                        s1 = "fletcher";
                    }

                    break;

                case 1:
                    if (careerId == 1)
                    {
                        s1 = "librarian";
                    }
                    else if (careerId == 2)
                    {
                        s1 = "cartographer";
                    }

                    break;

                case 2:
                    s1 = "cleric";
                    break;

                case 3:
                    if (careerId == 1)
                    {
                        s1 = "armor";
                    }
                    else if (careerId == 2)
                    {
                        s1 = "weapon";
                    }
                    else if (careerId == 3)
                    {
                        s1 = "tool";
                    }

                    break;

                case 4:
                    if (careerId == 1)
                    {
                        s1 = "butcher";
                    }
                    else if (careerId == 2)
                    {
                        s1 = "leather";
                    }

                    break;

                case 5:
                    s1 = "nitwit";
            }

            if (s1 != null)
            {
                Component itextcomponent = new TranslatableComponent("entity.Villager." + s1);
                itextcomponent.getStyle().setHoverEvent(getHoverEvent());
                itextcomponent.getStyle().setInsertion(getCachedUniqueIdString());

                if (team != null)
                {
                    itextcomponent.getStyle().setColor(team.getChatFormat());
                }

                return itextcomponent;
            }
            else
            {
                return super.getDisplayName();
            }
        }
    }

    public float getEyeHeight()
    {
        return isChild() ? 0.81F : 1.62F;
    }

    public void handleStatusUpdate(byte id)
    {
        if (id == 12)
        {
            spawnParticles(EnumParticleTypes.HEART);
        }
        else if (id == 13)
        {
            spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14)
        {
            spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    private void spawnParticles(EnumParticleTypes particleType)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            world.spawnParticle(particleType, posX + (double)(rand.nextFloat() * width * 2.0F) - (double) width, posY + 1.0D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double) width, d0, d1, d2);
        }
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        return func_190672_a(difficulty, livingdata, true);
    }

    public IEntityLivingData func_190672_a(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_)
    {
        p_190672_2_ = super.onInitialSpawn(p_190672_1_, p_190672_2_);

        if (p_190672_3_)
        {
            setProfession(world.rand.nextInt(6));
        }

        setAdditionalAItasks();
        populateBuyingList();
        return p_190672_2_;
    }

    public void setLookingForHome()
    {
        isLookingForHome = true;
    }

    public EntityVillager createChild(EntityAgeable ageable)
    {
        EntityVillager entityvillager = new EntityVillager(world);
        entityvillager.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), null);
        return entityvillager;
    }

    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return false;
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        if (!world.isRemote && !isDead)
        {
            EntityWitch entitywitch = new EntityWitch(world);
            entitywitch.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            entitywitch.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitywitch)), null);
            entitywitch.setNoAI(isAIDisabled());

            if (hasCustomName())
            {
                entitywitch.setCustomNameTag(getCustomNameTag());
                entitywitch.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
            }

            world.spawnEntityInWorld(entitywitch);
            setDead();
        }
    }

    public InventoryBasic getVillagerInventory()
    {
        return villagerInventory;
    }

    /**
     * Tests if this entity should pickup a weapon or an armor. Entity drops current weapon or armor if the new one is
     * better.
     */
    protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getEntityItem();
        Item item = itemstack.getItem();

        if (canVillagerPickupItem(item))
        {
            ItemStack itemstack1 = villagerInventory.addItem(itemstack);

            if (itemstack1.isEmpty())
            {
                itemEntity.setDead();
            }
            else
            {
                itemstack.func_190920_e(itemstack1.getCount());
            }
        }
    }

    private boolean canVillagerPickupItem(Item itemIn)
    {
        return itemIn == Items.BREAD || itemIn == Items.POTATO || itemIn == Items.CARROT || itemIn == Items.WHEAT || itemIn == Items.WHEAT_SEEDS || itemIn == Items.BEETROOT || itemIn == Items.BEETROOT_SEEDS;
    }

    public boolean hasEnoughFoodToBreed()
    {
        return hasEnoughItems(1);
    }

    /**
     * Used by {@link net.minecraft.entity.ai.EntityAIVillagerInteract EntityAIVillagerInteract} to check if the
     * villager can give some items from an inventory to another villager.
     */
    public boolean canAbondonItems()
    {
        return hasEnoughItems(2);
    }

    public boolean wantsMoreFood()
    {
        boolean flag = getProfession() == 0;

        if (flag)
        {
            return !hasEnoughItems(5);
        }
        else
        {
            return !hasEnoughItems(1);
        }
    }

    /**
     * Returns true if villager has enough items in inventory
     */
    private boolean hasEnoughItems(int multiplier)
    {
        boolean flag = getProfession() == 0;

        for (int i = 0; i < villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * multiplier || itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.BEETROOT && itemstack.getCount() >= 12 * multiplier)
                {
                    return true;
                }

                if (flag && itemstack.getItem() == Items.WHEAT && itemstack.getCount() >= 9 * multiplier)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if villager has seeds, potatoes or carrots in inventory
     */
    public boolean isFarmItemInInventory()
    {
        for (int i = 0; i < villagerInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = villagerInventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT || itemstack.getItem() == Items.BEETROOT_SEEDS))
            {
                return true;
            }
        }

        return false;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn))
        {
            return true;
        }
        else
        {
            int i = inventorySlot - 300;

            if (i >= 0 && i < villagerInventory.getSizeInventory())
            {
                villagerInventory.setInventorySlotContents(i, itemStackIn);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    static class EmeraldForItems implements EntityVillager.ITradeList
    {
        public Item buyingItem;
        public EntityVillager.PriceInfo price;

        public EmeraldForItems(Item itemIn, EntityVillager.PriceInfo priceIn)
        {
            buyingItem = itemIn;
            price = priceIn;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = 1;

            if (price != null)
            {
                i = price.getPrice(p_190888_3_);
            }

            p_190888_2_.add(new MerchantRecipe(new ItemStack(buyingItem, i, 0), Items.EMERALD));
        }
    }

    interface ITradeList
    {
        void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_);
    }

    static class ItemAndEmeraldToItem implements EntityVillager.ITradeList
    {
        public ItemStack buyingItemStack;
        public EntityVillager.PriceInfo buyingPriceInfo;
        public ItemStack sellingItemstack;
        public EntityVillager.PriceInfo sellingPriceInfo;

        public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_)
        {
            buyingItemStack = new ItemStack(p_i45813_1_);
            buyingPriceInfo = p_i45813_2_;
            sellingItemstack = new ItemStack(p_i45813_3_);
            sellingPriceInfo = p_i45813_4_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = buyingPriceInfo.getPrice(p_190888_3_);
            int j = sellingPriceInfo.getPrice(p_190888_3_);
            p_190888_2_.add(new MerchantRecipe(new ItemStack(buyingItemStack.getItem(), i, buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD), new ItemStack(sellingItemstack.getItem(), j, sellingItemstack.getMetadata())));
        }
    }

    static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList
    {
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            Enchantment enchantment = Enchantment.REGISTRY.getRandomObject(p_190888_3_);
            int i = MathHelper.getInt(p_190888_3_, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + p_190888_3_.nextInt(5 + i * 10) + 3 * i;

            if (enchantment.isTreasureEnchantment())
            {
                j *= 2;
            }

            if (j > 64)
            {
                j = 64;
            }

            p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
        }
    }

    static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList
    {
        public ItemStack enchantedItemStack;
        public EntityVillager.PriceInfo priceInfo;

        public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_)
        {
            enchantedItemStack = new ItemStack(p_i45814_1_);
            priceInfo = p_i45814_2_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = 1;

            if (priceInfo != null)
            {
                i = priceInfo.getPrice(p_190888_3_);
            }

            ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
            ItemStack itemstack1 = EnchantmentHelper.addRandomEnchantment(p_190888_3_, new ItemStack(enchantedItemStack.getItem(), 1, enchantedItemStack.getMetadata()), 5 + p_190888_3_.nextInt(15), false);
            p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class ListItemForEmeralds implements EntityVillager.ITradeList
    {
        public ItemStack itemToBuy;
        public EntityVillager.PriceInfo priceInfo;

        public ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo)
        {
            itemToBuy = new ItemStack(par1Item);
            this.priceInfo = priceInfo;
        }

        public ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo)
        {
            itemToBuy = stack;
            this.priceInfo = priceInfo;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = 1;

            if (priceInfo != null)
            {
                i = priceInfo.getPrice(p_190888_3_);
            }

            ItemStack itemstack;
            ItemStack itemstack1;

            if (i < 0)
            {
                itemstack = new ItemStack(Items.EMERALD);
                itemstack1 = new ItemStack(itemToBuy.getItem(), -i, itemToBuy.getMetadata());
            }
            else
            {
                itemstack = new ItemStack(Items.EMERALD, i, 0);
                itemstack1 = new ItemStack(itemToBuy.getItem(), 1, itemToBuy.getMetadata());
            }

            p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class PriceInfo extends Tuple<Integer, Integer>
    {
        public PriceInfo(int p_i45810_1_, int p_i45810_2_)
        {
            super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));

            if (p_i45810_2_ < p_i45810_1_)
            {
                field_190674_bx.warn("PriceRange({}, {}) invalid, {} smaller than {}", Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_1_));
            }
        }

        public int getPrice(Random rand)
        {
            return getFirst().intValue() >= getSecond().intValue() ? getFirst().intValue() : getFirst().intValue() + rand.nextInt(getSecond().intValue() - getFirst().intValue() + 1);
        }
    }

    static class TreasureMapForEmeralds implements EntityVillager.ITradeList
    {
        public EntityVillager.PriceInfo field_190889_a;
        public String field_190890_b;
        public MapDecoration.Type field_190891_c;

        public TreasureMapForEmeralds(EntityVillager.PriceInfo p_i47340_1_, String p_i47340_2_, MapDecoration.Type p_i47340_3_)
        {
            field_190889_a = p_i47340_1_;
            field_190890_b = p_i47340_2_;
            field_190891_c = p_i47340_3_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = field_190889_a.getPrice(p_190888_3_);
            World world = p_190888_1_.func_190670_t_();
            BlockPos blockpos = world.func_190528_a(field_190890_b, p_190888_1_.func_190671_u_(), true);

            if (blockpos != null)
            {
                ItemStack itemstack = ItemMap.func_190906_a(world, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
                ItemMap.func_190905_a(world, itemstack);
                MapData.func_191094_a(itemstack, blockpos, "+", field_190891_c);
                itemstack.func_190924_f("filled_map." + field_190890_b.toLowerCase(Locale.ROOT));
                p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack));
            }
        }
    }
}
