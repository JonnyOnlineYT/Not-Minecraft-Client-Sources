package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class StatList {
   protected static final Map<String, StatBase> ID_TO_STAT_MAP = Maps.newHashMap();
   public static final List<StatBase> ALL_STATS = Lists.newArrayList();
   public static final List<StatBase> BASIC_STATS = Lists.newArrayList();
   public static final List<StatCrafting> USE_ITEM_STATS = Lists.newArrayList();
   public static final List<StatCrafting> MINE_BLOCK_STATS = Lists.newArrayList();
   public static final StatBase LEAVE_GAME = (new StatBasic("stat.leaveGame", new TextComponentTranslation("stat.leaveGame", new Object[0]))).initIndependentStat().registerStat();
   public static final StatBase PLAY_ONE_MINUTE;
   public static final StatBase TIME_SINCE_DEATH;
   public static final StatBase SNEAK_TIME;
   public static final StatBase WALK_ONE_CM;
   public static final StatBase CROUCH_ONE_CM;
   public static final StatBase SPRINT_ONE_CM;
   public static final StatBase SWIM_ONE_CM;
   public static final StatBase FALL_ONE_CM;
   public static final StatBase CLIMB_ONE_CM;
   public static final StatBase FLY_ONE_CM;
   public static final StatBase DIVE_ONE_CM;
   public static final StatBase MINECART_ONE_CM;
   public static final StatBase BOAT_ONE_CM;
   public static final StatBase PIG_ONE_CM;
   public static final StatBase HORSE_ONE_CM;
   public static final StatBase AVIATE_ONE_CM;
   public static final StatBase JUMP;
   public static final StatBase DROP;
   public static final StatBase DAMAGE_DEALT;
   public static final StatBase DAMAGE_TAKEN;
   public static final StatBase DEATHS;
   public static final StatBase MOB_KILLS;
   public static final StatBase ANIMALS_BRED;
   public static final StatBase PLAYER_KILLS;
   public static final StatBase FISH_CAUGHT;
   public static final StatBase TALKED_TO_VILLAGER;
   public static final StatBase TRADED_WITH_VILLAGER;
   public static final StatBase CAKE_SLICES_EATEN;
   public static final StatBase CAULDRON_FILLED;
   public static final StatBase CAULDRON_USED;
   public static final StatBase ARMOR_CLEANED;
   public static final StatBase BANNER_CLEANED;
   public static final StatBase BREWINGSTAND_INTERACTION;
   public static final StatBase BEACON_INTERACTION;
   public static final StatBase DROPPER_INSPECTED;
   public static final StatBase HOPPER_INSPECTED;
   public static final StatBase DISPENSER_INSPECTED;
   public static final StatBase NOTEBLOCK_PLAYED;
   public static final StatBase NOTEBLOCK_TUNED;
   public static final StatBase FLOWER_POTTED;
   public static final StatBase TRAPPED_CHEST_TRIGGERED;
   public static final StatBase ENDERCHEST_OPENED;
   public static final StatBase ITEM_ENCHANTED;
   public static final StatBase RECORD_PLAYED;
   public static final StatBase FURNACE_INTERACTION;
   public static final StatBase CRAFTING_TABLE_INTERACTION;
   public static final StatBase CHEST_OPENED;
   public static final StatBase SLEEP_IN_BED;
   public static final StatBase field_191272_ae;
   private static final StatBase[] BLOCKS_STATS;
   private static final StatBase[] CRAFTS_STATS;
   private static final StatBase[] OBJECT_USE_STATS;
   private static final StatBase[] OBJECT_BREAK_STATS;
   private static final StatBase[] OBJECTS_PICKED_UP_STATS;
   private static final StatBase[] OBJECTS_DROPPED_STATS;

   @Nullable
   public static StatBase getBlockStats(Block blockIn) {
      return BLOCKS_STATS[Block.getIdFromBlock(blockIn)];
   }

   @Nullable
   public static StatBase getCraftStats(Item itemIn) {
      return CRAFTS_STATS[Item.getIdFromItem(itemIn)];
   }

   @Nullable
   public static StatBase getObjectUseStats(Item itemIn) {
      return OBJECT_USE_STATS[Item.getIdFromItem(itemIn)];
   }

   @Nullable
   public static StatBase getObjectBreakStats(Item itemIn) {
      return OBJECT_BREAK_STATS[Item.getIdFromItem(itemIn)];
   }

   @Nullable
   public static StatBase getObjectsPickedUpStats(Item itemIn) {
      return OBJECTS_PICKED_UP_STATS[Item.getIdFromItem(itemIn)];
   }

   @Nullable
   public static StatBase getDroppedObjectStats(Item itemIn) {
      return OBJECTS_DROPPED_STATS[Item.getIdFromItem(itemIn)];
   }

   public static void init() {
      initMiningStats();
      initStats();
      initItemDepleteStats();
      initCraftableStats();
      initPickedUpAndDroppedStats();
   }

   private static void initCraftableStats() {
      Set<Item> set = Sets.newHashSet();
      Iterator var1 = CraftingManager.field_193380_a.iterator();

      while(var1.hasNext()) {
         IRecipe irecipe = (IRecipe)var1.next();
         ItemStack itemstack = irecipe.getRecipeOutput();
         if (!itemstack.func_190926_b()) {
            set.add(irecipe.getRecipeOutput().getItem());
         }
      }

      var1 = FurnaceRecipes.instance().getSmeltingList().values().iterator();

      while(var1.hasNext()) {
         ItemStack itemstack1 = (ItemStack)var1.next();
         set.add(itemstack1.getItem());
      }

      var1 = set.iterator();

      while(var1.hasNext()) {
         Item item = (Item)var1.next();
         if (item != null) {
            int i = Item.getIdFromItem(item);
            String s = getItemName(item);
            if (s != null) {
               CRAFTS_STATS[i] = (new StatCrafting("stat.craftItem.", s, new TextComponentTranslation("stat.craftItem", new Object[]{(new ItemStack(item)).getTextComponent()}), item)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(CRAFTS_STATS);
   }

   private static void initMiningStats() {
      Iterator var0 = Block.REGISTRY.iterator();

      while(var0.hasNext()) {
         Block block = (Block)var0.next();
         Item item = Item.getItemFromBlock(block);
         if (item != Items.field_190931_a) {
            int i = Block.getIdFromBlock(block);
            String s = getItemName(item);
            if (s != null && block.getEnableStats()) {
               BLOCKS_STATS[i] = (new StatCrafting("stat.mineBlock.", s, new TextComponentTranslation("stat.mineBlock", new Object[]{(new ItemStack(block)).getTextComponent()}), item)).registerStat();
               MINE_BLOCK_STATS.add((StatCrafting)BLOCKS_STATS[i]);
            }
         }
      }

      replaceAllSimilarBlocks(BLOCKS_STATS);
   }

   private static void initStats() {
      Iterator var0 = Item.REGISTRY.iterator();

      while(var0.hasNext()) {
         Item item = (Item)var0.next();
         if (item != null) {
            int i = Item.getIdFromItem(item);
            String s = getItemName(item);
            if (s != null) {
               OBJECT_USE_STATS[i] = (new StatCrafting("stat.useItem.", s, new TextComponentTranslation("stat.useItem", new Object[]{(new ItemStack(item)).getTextComponent()}), item)).registerStat();
               if (!(item instanceof ItemBlock)) {
                  USE_ITEM_STATS.add((StatCrafting)OBJECT_USE_STATS[i]);
               }
            }
         }
      }

      replaceAllSimilarBlocks(OBJECT_USE_STATS);
   }

   private static void initItemDepleteStats() {
      Iterator var0 = Item.REGISTRY.iterator();

      while(var0.hasNext()) {
         Item item = (Item)var0.next();
         if (item != null) {
            int i = Item.getIdFromItem(item);
            String s = getItemName(item);
            if (s != null && item.isDamageable()) {
               OBJECT_BREAK_STATS[i] = (new StatCrafting("stat.breakItem.", s, new TextComponentTranslation("stat.breakItem", new Object[]{(new ItemStack(item)).getTextComponent()}), item)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
   }

   private static void initPickedUpAndDroppedStats() {
      Iterator var0 = Item.REGISTRY.iterator();

      while(var0.hasNext()) {
         Item item = (Item)var0.next();
         if (item != null) {
            int i = Item.getIdFromItem(item);
            String s = getItemName(item);
            if (s != null) {
               OBJECTS_PICKED_UP_STATS[i] = (new StatCrafting("stat.pickup.", s, new TextComponentTranslation("stat.pickup", new Object[]{(new ItemStack(item)).getTextComponent()}), item)).registerStat();
               OBJECTS_DROPPED_STATS[i] = (new StatCrafting("stat.drop.", s, new TextComponentTranslation("stat.drop", new Object[]{(new ItemStack(item)).getTextComponent()}), item)).registerStat();
            }
         }
      }

      replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
   }

   private static String getItemName(Item itemIn) {
      ResourceLocation resourcelocation = (ResourceLocation)Item.REGISTRY.getNameForObject(itemIn);
      return resourcelocation != null ? resourcelocation.toString().replace(':', '.') : null;
   }

   private static void replaceAllSimilarBlocks(StatBase[] stat) {
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

   private static void mergeStatBases(StatBase[] statBaseIn, Block block1, Block block2) {
      int i = Block.getIdFromBlock(block1);
      int j = Block.getIdFromBlock(block2);
      if (statBaseIn[i] != null && statBaseIn[j] == null) {
         statBaseIn[j] = statBaseIn[i];
      } else {
         ALL_STATS.remove(statBaseIn[i]);
         MINE_BLOCK_STATS.remove(statBaseIn[i]);
         BASIC_STATS.remove(statBaseIn[i]);
         statBaseIn[i] = statBaseIn[j];
      }

   }

   public static StatBase getStatKillEntity(EntityList.EntityEggInfo eggInfo) {
      String s = EntityList.func_191302_a(eggInfo.spawnedID);
      return s == null ? null : (new StatBase("stat.killEntity." + s, new TextComponentTranslation("stat.entityKill", new Object[]{new TextComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
   }

   public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo eggInfo) {
      String s = EntityList.func_191302_a(eggInfo.spawnedID);
      return s == null ? null : (new StatBase("stat.entityKilledBy." + s, new TextComponentTranslation("stat.entityKilledBy", new Object[]{new TextComponentTranslation("entity." + s + ".name", new Object[0])}))).registerStat();
   }

   @Nullable
   public static StatBase getOneShotStat(String statName) {
      return (StatBase)ID_TO_STAT_MAP.get(statName);
   }

   static {
      PLAY_ONE_MINUTE = (new StatBasic("stat.playOneMinute", new TextComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
      TIME_SINCE_DEATH = (new StatBasic("stat.timeSinceDeath", new TextComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
      SNEAK_TIME = (new StatBasic("stat.sneakTime", new TextComponentTranslation("stat.sneakTime", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
      WALK_ONE_CM = (new StatBasic("stat.walkOneCm", new TextComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      CROUCH_ONE_CM = (new StatBasic("stat.crouchOneCm", new TextComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      SPRINT_ONE_CM = (new StatBasic("stat.sprintOneCm", new TextComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      SWIM_ONE_CM = (new StatBasic("stat.swimOneCm", new TextComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      FALL_ONE_CM = (new StatBasic("stat.fallOneCm", new TextComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      CLIMB_ONE_CM = (new StatBasic("stat.climbOneCm", new TextComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      FLY_ONE_CM = (new StatBasic("stat.flyOneCm", new TextComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      DIVE_ONE_CM = (new StatBasic("stat.diveOneCm", new TextComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      MINECART_ONE_CM = (new StatBasic("stat.minecartOneCm", new TextComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      BOAT_ONE_CM = (new StatBasic("stat.boatOneCm", new TextComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      PIG_ONE_CM = (new StatBasic("stat.pigOneCm", new TextComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      HORSE_ONE_CM = (new StatBasic("stat.horseOneCm", new TextComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      AVIATE_ONE_CM = (new StatBasic("stat.aviateOneCm", new TextComponentTranslation("stat.aviateOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
      JUMP = (new StatBasic("stat.jump", new TextComponentTranslation("stat.jump", new Object[0]))).initIndependentStat().registerStat();
      DROP = (new StatBasic("stat.drop", new TextComponentTranslation("stat.drop", new Object[0]))).initIndependentStat().registerStat();
      DAMAGE_DEALT = (new StatBasic("stat.damageDealt", new TextComponentTranslation("stat.damageDealt", new Object[0]), StatBase.divideByTen)).registerStat();
      DAMAGE_TAKEN = (new StatBasic("stat.damageTaken", new TextComponentTranslation("stat.damageTaken", new Object[0]), StatBase.divideByTen)).registerStat();
      DEATHS = (new StatBasic("stat.deaths", new TextComponentTranslation("stat.deaths", new Object[0]))).registerStat();
      MOB_KILLS = (new StatBasic("stat.mobKills", new TextComponentTranslation("stat.mobKills", new Object[0]))).registerStat();
      ANIMALS_BRED = (new StatBasic("stat.animalsBred", new TextComponentTranslation("stat.animalsBred", new Object[0]))).registerStat();
      PLAYER_KILLS = (new StatBasic("stat.playerKills", new TextComponentTranslation("stat.playerKills", new Object[0]))).registerStat();
      FISH_CAUGHT = (new StatBasic("stat.fishCaught", new TextComponentTranslation("stat.fishCaught", new Object[0]))).registerStat();
      TALKED_TO_VILLAGER = (new StatBasic("stat.talkedToVillager", new TextComponentTranslation("stat.talkedToVillager", new Object[0]))).registerStat();
      TRADED_WITH_VILLAGER = (new StatBasic("stat.tradedWithVillager", new TextComponentTranslation("stat.tradedWithVillager", new Object[0]))).registerStat();
      CAKE_SLICES_EATEN = (new StatBasic("stat.cakeSlicesEaten", new TextComponentTranslation("stat.cakeSlicesEaten", new Object[0]))).registerStat();
      CAULDRON_FILLED = (new StatBasic("stat.cauldronFilled", new TextComponentTranslation("stat.cauldronFilled", new Object[0]))).registerStat();
      CAULDRON_USED = (new StatBasic("stat.cauldronUsed", new TextComponentTranslation("stat.cauldronUsed", new Object[0]))).registerStat();
      ARMOR_CLEANED = (new StatBasic("stat.armorCleaned", new TextComponentTranslation("stat.armorCleaned", new Object[0]))).registerStat();
      BANNER_CLEANED = (new StatBasic("stat.bannerCleaned", new TextComponentTranslation("stat.bannerCleaned", new Object[0]))).registerStat();
      BREWINGSTAND_INTERACTION = (new StatBasic("stat.brewingstandInteraction", new TextComponentTranslation("stat.brewingstandInteraction", new Object[0]))).registerStat();
      BEACON_INTERACTION = (new StatBasic("stat.beaconInteraction", new TextComponentTranslation("stat.beaconInteraction", new Object[0]))).registerStat();
      DROPPER_INSPECTED = (new StatBasic("stat.dropperInspected", new TextComponentTranslation("stat.dropperInspected", new Object[0]))).registerStat();
      HOPPER_INSPECTED = (new StatBasic("stat.hopperInspected", new TextComponentTranslation("stat.hopperInspected", new Object[0]))).registerStat();
      DISPENSER_INSPECTED = (new StatBasic("stat.dispenserInspected", new TextComponentTranslation("stat.dispenserInspected", new Object[0]))).registerStat();
      NOTEBLOCK_PLAYED = (new StatBasic("stat.noteblockPlayed", new TextComponentTranslation("stat.noteblockPlayed", new Object[0]))).registerStat();
      NOTEBLOCK_TUNED = (new StatBasic("stat.noteblockTuned", new TextComponentTranslation("stat.noteblockTuned", new Object[0]))).registerStat();
      FLOWER_POTTED = (new StatBasic("stat.flowerPotted", new TextComponentTranslation("stat.flowerPotted", new Object[0]))).registerStat();
      TRAPPED_CHEST_TRIGGERED = (new StatBasic("stat.trappedChestTriggered", new TextComponentTranslation("stat.trappedChestTriggered", new Object[0]))).registerStat();
      ENDERCHEST_OPENED = (new StatBasic("stat.enderchestOpened", new TextComponentTranslation("stat.enderchestOpened", new Object[0]))).registerStat();
      ITEM_ENCHANTED = (new StatBasic("stat.itemEnchanted", new TextComponentTranslation("stat.itemEnchanted", new Object[0]))).registerStat();
      RECORD_PLAYED = (new StatBasic("stat.recordPlayed", new TextComponentTranslation("stat.recordPlayed", new Object[0]))).registerStat();
      FURNACE_INTERACTION = (new StatBasic("stat.furnaceInteraction", new TextComponentTranslation("stat.furnaceInteraction", new Object[0]))).registerStat();
      CRAFTING_TABLE_INTERACTION = (new StatBasic("stat.craftingTableInteraction", new TextComponentTranslation("stat.workbenchInteraction", new Object[0]))).registerStat();
      CHEST_OPENED = (new StatBasic("stat.chestOpened", new TextComponentTranslation("stat.chestOpened", new Object[0]))).registerStat();
      SLEEP_IN_BED = (new StatBasic("stat.sleepInBed", new TextComponentTranslation("stat.sleepInBed", new Object[0]))).registerStat();
      field_191272_ae = (new StatBasic("stat.shulkerBoxOpened", new TextComponentTranslation("stat.shulkerBoxOpened", new Object[0]))).registerStat();
      BLOCKS_STATS = new StatBase[4096];
      CRAFTS_STATS = new StatBase[32000];
      OBJECT_USE_STATS = new StatBase[32000];
      OBJECT_BREAK_STATS = new StatBase[32000];
      OBJECTS_PICKED_UP_STATS = new StatBase[32000];
      OBJECTS_DROPPED_STATS = new StatBase[32000];
   }
}
