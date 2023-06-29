package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatList
{
    protected static final Map<String, StatBase> ID_TO_STAT_MAP = Maps.newHashMap();
    public static final List<StatBase> ALL_STATS = Lists.newArrayList();
    public static final List<StatBase> BASIC_STATS = Lists.newArrayList();
    public static final List<StatCrafting> USE_ITEM_STATS = Lists.newArrayList();
    public static final List<StatCrafting> MINE_BLOCK_STATS = Lists.newArrayList();

    /** number of times you've left a game */
    public static final StatBase LEAVE_GAME = (new StatBasic("stat.leaveGame", new TranslatableComponent("stat.leaveGame"))).initIndependentStat().registerStat();
    public static final StatBase PLAY_ONE_MINUTE = (new StatBasic("stat.playOneMinute", new TranslatableComponent("stat.playOneMinute"), StatBase.timeStatType)).initIndependentStat().registerStat();
    public static final StatBase TIME_SINCE_DEATH = (new StatBasic("stat.timeSinceDeath", new TranslatableComponent("stat.timeSinceDeath"), StatBase.timeStatType)).initIndependentStat().registerStat();
    public static final StatBase SNEAK_TIME = (new StatBasic("stat.sneakTime", new TranslatableComponent("stat.sneakTime"), StatBase.timeStatType)).initIndependentStat().registerStat();
    public static final StatBase WALK_ONE_CM = (new StatBasic("stat.walkOneCm", new TranslatableComponent("stat.walkOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase CROUCH_ONE_CM = (new StatBasic("stat.crouchOneCm", new TranslatableComponent("stat.crouchOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase SPRINT_ONE_CM = (new StatBasic("stat.sprintOneCm", new TranslatableComponent("stat.sprintOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** distance you have swam */
    public static final StatBase SWIM_ONE_CM = (new StatBasic("stat.swimOneCm", new TranslatableComponent("stat.swimOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the distance you have fallen */
    public static final StatBase FALL_ONE_CM = (new StatBasic("stat.fallOneCm", new TranslatableComponent("stat.fallOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase CLIMB_ONE_CM = (new StatBasic("stat.climbOneCm", new TranslatableComponent("stat.climbOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase FLY_ONE_CM = (new StatBasic("stat.flyOneCm", new TranslatableComponent("stat.flyOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase DIVE_ONE_CM = (new StatBasic("stat.diveOneCm", new TranslatableComponent("stat.diveOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase MINECART_ONE_CM = (new StatBasic("stat.minecartOneCm", new TranslatableComponent("stat.minecartOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase BOAT_ONE_CM = (new StatBasic("stat.boatOneCm", new TranslatableComponent("stat.boatOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase PIG_ONE_CM = (new StatBasic("stat.pigOneCm", new TranslatableComponent("stat.pigOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase HORSE_ONE_CM = (new StatBasic("stat.horseOneCm", new TranslatableComponent("stat.horseOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();
    public static final StatBase AVIATE_ONE_CM = (new StatBasic("stat.aviateOneCm", new TranslatableComponent("stat.aviateOneCm"), StatBase.distanceStatType)).initIndependentStat().registerStat();

    /** the times you've jumped */
    public static final StatBase JUMP = (new StatBasic("stat.jump", new TranslatableComponent("stat.jump"))).initIndependentStat().registerStat();

    /** the distance you've dropped (or times you've fallen?) */
    public static final StatBase DROP = (new StatBasic("stat.drop", new TranslatableComponent("stat.drop"))).initIndependentStat().registerStat();
    public static final StatBase DAMAGE_DEALT = (new StatBasic("stat.damageDealt", new TranslatableComponent("stat.damageDealt"), StatBase.divideByTen)).registerStat();
    public static final StatBase DAMAGE_TAKEN = (new StatBasic("stat.damageTaken", new TranslatableComponent("stat.damageTaken"), StatBase.divideByTen)).registerStat();
    public static final StatBase DEATHS = (new StatBasic("stat.deaths", new TranslatableComponent("stat.deaths"))).registerStat();
    public static final StatBase MOB_KILLS = (new StatBasic("stat.mobKills", new TranslatableComponent("stat.mobKills"))).registerStat();

    /** the number of animals you have bred */
    public static final StatBase ANIMALS_BRED = (new StatBasic("stat.animalsBred", new TranslatableComponent("stat.animalsBred"))).registerStat();

    /** counts the number of times you've killed a player */
    public static final StatBase PLAYER_KILLS = (new StatBasic("stat.playerKills", new TranslatableComponent("stat.playerKills"))).registerStat();
    public static final StatBase FISH_CAUGHT = (new StatBasic("stat.fishCaught", new TranslatableComponent("stat.fishCaught"))).registerStat();
    public static final StatBase TALKED_TO_VILLAGER = (new StatBasic("stat.talkedToVillager", new TranslatableComponent("stat.talkedToVillager"))).registerStat();
    public static final StatBase TRADED_WITH_VILLAGER = (new StatBasic("stat.tradedWithVillager", new TranslatableComponent("stat.tradedWithVillager"))).registerStat();
    public static final StatBase CAKE_SLICES_EATEN = (new StatBasic("stat.cakeSlicesEaten", new TranslatableComponent("stat.cakeSlicesEaten"))).registerStat();
    public static final StatBase CAULDRON_FILLED = (new StatBasic("stat.cauldronFilled", new TranslatableComponent("stat.cauldronFilled"))).registerStat();
    public static final StatBase CAULDRON_USED = (new StatBasic("stat.cauldronUsed", new TranslatableComponent("stat.cauldronUsed"))).registerStat();
    public static final StatBase ARMOR_CLEANED = (new StatBasic("stat.armorCleaned", new TranslatableComponent("stat.armorCleaned"))).registerStat();
    public static final StatBase BANNER_CLEANED = (new StatBasic("stat.bannerCleaned", new TranslatableComponent("stat.bannerCleaned"))).registerStat();
    public static final StatBase BREWINGSTAND_INTERACTION = (new StatBasic("stat.brewingstandInteraction", new TranslatableComponent("stat.brewingstandInteraction"))).registerStat();
    public static final StatBase BEACON_INTERACTION = (new StatBasic("stat.beaconInteraction", new TranslatableComponent("stat.beaconInteraction"))).registerStat();
    public static final StatBase DROPPER_INSPECTED = (new StatBasic("stat.dropperInspected", new TranslatableComponent("stat.dropperInspected"))).registerStat();
    public static final StatBase HOPPER_INSPECTED = (new StatBasic("stat.hopperInspected", new TranslatableComponent("stat.hopperInspected"))).registerStat();
    public static final StatBase DISPENSER_INSPECTED = (new StatBasic("stat.dispenserInspected", new TranslatableComponent("stat.dispenserInspected"))).registerStat();
    public static final StatBase NOTEBLOCK_PLAYED = (new StatBasic("stat.noteblockPlayed", new TranslatableComponent("stat.noteblockPlayed"))).registerStat();
    public static final StatBase NOTEBLOCK_TUNED = (new StatBasic("stat.noteblockTuned", new TranslatableComponent("stat.noteblockTuned"))).registerStat();
    public static final StatBase FLOWER_POTTED = (new StatBasic("stat.flowerPotted", new TranslatableComponent("stat.flowerPotted"))).registerStat();
    public static final StatBase TRAPPED_CHEST_TRIGGERED = (new StatBasic("stat.trappedChestTriggered", new TranslatableComponent("stat.trappedChestTriggered"))).registerStat();
    public static final StatBase ENDERCHEST_OPENED = (new StatBasic("stat.enderchestOpened", new TranslatableComponent("stat.enderchestOpened"))).registerStat();
    public static final StatBase ITEM_ENCHANTED = (new StatBasic("stat.itemEnchanted", new TranslatableComponent("stat.itemEnchanted"))).registerStat();
    public static final StatBase RECORD_PLAYED = (new StatBasic("stat.recordPlayed", new TranslatableComponent("stat.recordPlayed"))).registerStat();
    public static final StatBase FURNACE_INTERACTION = (new StatBasic("stat.furnaceInteraction", new TranslatableComponent("stat.furnaceInteraction"))).registerStat();
    public static final StatBase CRAFTING_TABLE_INTERACTION = (new StatBasic("stat.craftingTableInteraction", new TranslatableComponent("stat.workbenchInteraction"))).registerStat();
    public static final StatBase CHEST_OPENED = (new StatBasic("stat.chestOpened", new TranslatableComponent("stat.chestOpened"))).registerStat();
    public static final StatBase SLEEP_IN_BED = (new StatBasic("stat.sleepInBed", new TranslatableComponent("stat.sleepInBed"))).registerStat();
    public static final StatBase field_191272_ae = (new StatBasic("stat.shulkerBoxOpened", new TranslatableComponent("stat.shulkerBoxOpened"))).registerStat();
    private static final StatBase[] BLOCKS_STATS = new StatBase[4096];
    private static final StatBase[] CRAFTS_STATS = new StatBase[32000];

    /** Tracks the number of times a given block or item has been used. */
    private static final StatBase[] OBJECT_USE_STATS = new StatBase[32000];

    /** Tracks the number of times a given block or item has been broken. */
    private static final StatBase[] OBJECT_BREAK_STATS = new StatBase[32000];
    private static final StatBase[] OBJECTS_PICKED_UP_STATS = new StatBase[32000];
    private static final StatBase[] OBJECTS_DROPPED_STATS = new StatBase[32000];

    @Nullable
    public static StatBase getBlockStats(Block blockIn)
    {
        return BLOCKS_STATS[Block.getIdFromBlock(blockIn)];
    }

    @Nullable
    public static StatBase getCraftStats(Item itemIn)
    {
        return CRAFTS_STATS[Item.getIdFromItem(itemIn)];
    }

    @Nullable
    public static StatBase getObjectUseStats(Item itemIn)
    {
        return OBJECT_USE_STATS[Item.getIdFromItem(itemIn)];
    }

    @Nullable
    public static StatBase getObjectBreakStats(Item itemIn)
    {
        return OBJECT_BREAK_STATS[Item.getIdFromItem(itemIn)];
    }

    @Nullable
    public static StatBase getObjectsPickedUpStats(Item itemIn)
    {
        return OBJECTS_PICKED_UP_STATS[Item.getIdFromItem(itemIn)];
    }

    @Nullable
    public static StatBase getDroppedObjectStats(Item itemIn)
    {
        return OBJECTS_DROPPED_STATS[Item.getIdFromItem(itemIn)];
    }

    public static void init()
    {
        initMiningStats();
        initStats();
        initItemDepleteStats();
        initCraftableStats();
        initPickedUpAndDroppedStats();
    }

    /**
     * Initializes statistics related to craftable items. Is only called after both block and item stats have been
     * initialized.
     */
    private static void initCraftableStats()
    {
        Set<Item> set = Sets.newHashSet();

        for (IRecipe irecipe : CraftingManager.field_193380_a)
        {
            ItemStack itemstack = irecipe.getRecipeOutput();

            if (!itemstack.isEmpty())
            {
                set.add(irecipe.getRecipeOutput().getItem());
            }
        }

        for (ItemStack itemstack1 : FurnaceRecipes.instance().getSmeltingList().values())
        {
            set.add(itemstack1.getItem());
        }

        for (Item item : set)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null)
                {
                    CRAFTS_STATS[i] = (new StatCrafting("stat.craftItem.", s, new TranslatableComponent("stat.craftItem", (new ItemStack(item)).getTextComponent()), item)).registerStat();
                }
            }
        }

        replaceAllSimilarBlocks(CRAFTS_STATS);
    }

    private static void initMiningStats()
    {
        for (Block block : Block.REGISTRY)
        {
            Item item = Item.getItemFromBlock(block);

            if (item != Items.field_190931_a)
            {
                int i = Block.getIdFromBlock(block);
                String s = getItemName(item);

                if (s != null && block.getEnableStats())
                {
                    BLOCKS_STATS[i] = (new StatCrafting("stat.mineBlock.", s, new TranslatableComponent("stat.mineBlock", (new ItemStack(block)).getTextComponent()), item)).registerStat();
                    MINE_BLOCK_STATS.add((StatCrafting) BLOCKS_STATS[i]);
                }
            }
        }

        replaceAllSimilarBlocks(BLOCKS_STATS);
    }

    private static void initStats()
    {
        for (Item item : Item.REGISTRY)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null)
                {
                    OBJECT_USE_STATS[i] = (new StatCrafting("stat.useItem.", s, new TranslatableComponent("stat.useItem", (new ItemStack(item)).getTextComponent()), item)).registerStat();

                    if (!(item instanceof ItemBlock))
                    {
                        USE_ITEM_STATS.add((StatCrafting) OBJECT_USE_STATS[i]);
                    }
                }
            }
        }

        replaceAllSimilarBlocks(OBJECT_USE_STATS);
    }

    private static void initItemDepleteStats()
    {
        for (Item item : Item.REGISTRY)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null && item.isDamageable())
                {
                    OBJECT_BREAK_STATS[i] = (new StatCrafting("stat.breakItem.", s, new TranslatableComponent("stat.breakItem", (new ItemStack(item)).getTextComponent()), item)).registerStat();
                }
            }
        }

        replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
    }

    private static void initPickedUpAndDroppedStats()
    {
        for (Item item : Item.REGISTRY)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = getItemName(item);

                if (s != null)
                {
                    OBJECTS_PICKED_UP_STATS[i] = (new StatCrafting("stat.pickup.", s, new TranslatableComponent("stat.pickup", (new ItemStack(item)).getTextComponent()), item)).registerStat();
                    OBJECTS_DROPPED_STATS[i] = (new StatCrafting("stat.drop.", s, new TranslatableComponent("stat.drop", (new ItemStack(item)).getTextComponent()), item)).registerStat();
                }
            }
        }

        replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
    }

    private static String getItemName(Item itemIn)
    {
        Namespaced resourcelocation = Item.REGISTRY.getNameForObject(itemIn);
        return resourcelocation != null ? resourcelocation.toString().replace(':', '.') : null;
    }

    /**
     * Forces all dual blocks to count for each other on the stats list
     */
    private static void replaceAllSimilarBlocks(StatBase[] stat)
    {
        mergeStatBases(stat, Blocks.WATER, Blocks.FLOWING_WATER);
        mergeStatBases(stat, Blocks.LAVA, Blocks.FLOWING_LAVA);
        mergeStatBases(stat, Blocks.LIT_PUMPKIN, Blocks.PUMPKIN);
        mergeStatBases(stat, Blocks.LIT_FURNACE, Blocks.FURNACE);
        mergeStatBases(stat, Blocks.LIT_REDSTONE_ORE, Blocks.REDSTONE_ORE);
        mergeStatBases(stat, Blocks.POWERED_REPEATER, Blocks.UNPOWERED_REPEATER);
        mergeStatBases(stat, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_COMPARATOR);
        mergeStatBases(stat, Blocks.REDSTONE_TORCH, Blocks.UNLIT_REDSTONE_TORCH);
        mergeStatBases(stat, Blocks.LIT_REDSTONE_LAMP, Blocks.REDSTONE_LAMP);
        mergeStatBases(stat, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB);
        mergeStatBases(stat, Blocks.DOUBLE_WOODEN_SLAB, Blocks.WOODEN_SLAB);
        mergeStatBases(stat, Blocks.DOUBLE_STONE_SLAB2, Blocks.STONE_SLAB2);
        mergeStatBases(stat, Blocks.GRASS, Blocks.DIRT);
        mergeStatBases(stat, Blocks.FARMLAND, Blocks.DIRT);
    }

    /**
     * Merge {@link StatBase} object references for similar blocks
     */
    private static void mergeStatBases(StatBase[] statBaseIn, Block block1, Block block2)
    {
        int i = Block.getIdFromBlock(block1);
        int j = Block.getIdFromBlock(block2);

        if (statBaseIn[i] != null && statBaseIn[j] == null)
        {
            statBaseIn[j] = statBaseIn[i];
        }
        else
        {
            ALL_STATS.remove(statBaseIn[i]);
            MINE_BLOCK_STATS.remove(statBaseIn[i]);
            BASIC_STATS.remove(statBaseIn[i]);
            statBaseIn[i] = statBaseIn[j];
        }
    }

    public static StatBase getStatKillEntity(EntityList.EntityEggInfo eggInfo)
    {
        String s = EntityList.func_191302_a(eggInfo.spawnedID);
        return s == null ? null : (new StatBase("stat.killEntity." + s, new TranslatableComponent("stat.entityKill", new TranslatableComponent("entity." + s + ".name")))).registerStat();
    }

    public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo eggInfo)
    {
        String s = EntityList.func_191302_a(eggInfo.spawnedID);
        return s == null ? null : (new StatBase("stat.entityKilledBy." + s, new TranslatableComponent("stat.entityKilledBy", new TranslatableComponent("entity." + s + ".name")))).registerStat();
    }

    @Nullable
    public static StatBase getOneShotStat(String statName)
    {
        return ID_TO_STAT_MAP.get(statName);
    }
}
