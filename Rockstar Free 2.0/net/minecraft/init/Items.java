package net.minecraft.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmorStand;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemShears;
import net.minecraft.util.ResourceLocation;

public class Items {
    private static Item getRegisteredItem(String name) {
        Item item = (Item)Item.REGISTRY.getObject(new ResourceLocation(name));
        if (item == null)
            throw new IllegalStateException("Invalid Item requested: " + name);
        return item;
    }

    static {
        if (!Bootstrap.isRegistered())
            throw new RuntimeException("Accessed Items before Bootstrap!");
    }

    public static final Item field_190931_a = getRegisteredItem("air");

    public static final Item IRON_SHOVEL = getRegisteredItem("iron_shovel");

    public static final Item IRON_PICKAXE = getRegisteredItem("iron_pickaxe");

    public static final Item IRON_AXE = getRegisteredItem("iron_axe");

    public static final Item FLINT_AND_STEEL = getRegisteredItem("flint_and_steel");

    public static final Item APPLE = getRegisteredItem("apple");

    public static final ItemBow BOW = (ItemBow)getRegisteredItem("bow");

    public static final Item ARROW = getRegisteredItem("arrow");

    public static final Item SPECTRAL_ARROW = getRegisteredItem("spectral_arrow");

    public static final Item TIPPED_ARROW = getRegisteredItem("tipped_arrow");

    public static final Item COAL = getRegisteredItem("coal");

    public static final Item DIAMOND = getRegisteredItem("diamond");

    public static final Item IRON_INGOT = getRegisteredItem("iron_ingot");

    public static final Item GOLD_INGOT = getRegisteredItem("gold_ingot");

    public static final Item IRON_SWORD = getRegisteredItem("iron_sword");

    public static final Item WOODEN_SWORD = getRegisteredItem("wooden_sword");

    public static final Item WOODEN_SHOVEL = getRegisteredItem("wooden_shovel");

    public static final Item WOODEN_PICKAXE = getRegisteredItem("wooden_pickaxe");

    public static final Item WOODEN_AXE = getRegisteredItem("wooden_axe");

    public static final Item STONE_SWORD = getRegisteredItem("stone_sword");

    public static final Item STONE_SHOVEL = getRegisteredItem("stone_shovel");

    public static final Item STONE_PICKAXE = getRegisteredItem("stone_pickaxe");

    public static final Item STONE_AXE = getRegisteredItem("stone_axe");

    public static final Item DIAMOND_SWORD = getRegisteredItem("diamond_sword");

    public static final Item DIAMOND_SHOVEL = getRegisteredItem("diamond_shovel");

    public static final Item DIAMOND_PICKAXE = getRegisteredItem("diamond_pickaxe");

    public static final Item DIAMOND_AXE = getRegisteredItem("diamond_axe");

    public static final Item STICK = getRegisteredItem("stick");

    public static final Item BOWL = getRegisteredItem("bowl");

    public static final Item MUSHROOM_STEW = getRegisteredItem("mushroom_stew");

    public static final Item GOLDEN_SWORD = getRegisteredItem("golden_sword");

    public static final Item GOLDEN_SHOVEL = getRegisteredItem("golden_shovel");

    public static final Item GOLDEN_PICKAXE = getRegisteredItem("golden_pickaxe");

    public static final Item GOLDEN_AXE = getRegisteredItem("golden_axe");

    public static final Item STRING = getRegisteredItem("string");

    public static final Item FEATHER = getRegisteredItem("feather");

    public static final Item GUNPOWDER = getRegisteredItem("gunpowder");

    public static final Item WOODEN_HOE = getRegisteredItem("wooden_hoe");

    public static final Item STONE_HOE = getRegisteredItem("stone_hoe");

    public static final Item IRON_HOE = getRegisteredItem("iron_hoe");

    public static final Item DIAMOND_HOE = getRegisteredItem("diamond_hoe");

    public static final Item GOLDEN_HOE = getRegisteredItem("golden_hoe");

    public static final Item WHEAT_SEEDS = getRegisteredItem("wheat_seeds");

    public static final Item WHEAT = getRegisteredItem("wheat");

    public static final Item BREAD = getRegisteredItem("bread");

    public static final ItemArmor LEATHER_HELMET = (ItemArmor)getRegisteredItem("leather_helmet");

    public static final ItemArmor LEATHER_CHESTPLATE = (ItemArmor)getRegisteredItem("leather_chestplate");

    public static final ItemArmor LEATHER_LEGGINGS = (ItemArmor)getRegisteredItem("leather_leggings");

    public static final ItemArmor LEATHER_BOOTS = (ItemArmor)getRegisteredItem("leather_boots");

    public static final ItemArmor CHAINMAIL_HELMET = (ItemArmor)getRegisteredItem("chainmail_helmet");

    public static final ItemArmor CHAINMAIL_CHESTPLATE = (ItemArmor)getRegisteredItem("chainmail_chestplate");

    public static final ItemArmor CHAINMAIL_LEGGINGS = (ItemArmor)getRegisteredItem("chainmail_leggings");

    public static final ItemArmor CHAINMAIL_BOOTS = (ItemArmor)getRegisteredItem("chainmail_boots");

    public static final ItemArmor IRON_HELMET = (ItemArmor)getRegisteredItem("iron_helmet");

    public static final ItemArmor IRON_CHESTPLATE = (ItemArmor)getRegisteredItem("iron_chestplate");

    public static final ItemArmor IRON_LEGGINGS = (ItemArmor)getRegisteredItem("iron_leggings");

    public static final ItemArmor IRON_BOOTS = (ItemArmor)getRegisteredItem("iron_boots");

    public static final ItemArmor DIAMOND_HELMET = (ItemArmor)getRegisteredItem("diamond_helmet");

    public static final ItemArmor DIAMOND_CHESTPLATE = (ItemArmor)getRegisteredItem("diamond_chestplate");

    public static final ItemArmor DIAMOND_LEGGINGS = (ItemArmor)getRegisteredItem("diamond_leggings");

    public static final ItemArmor DIAMOND_BOOTS = (ItemArmor)getRegisteredItem("diamond_boots");

    public static final ItemArmor GOLDEN_HELMET = (ItemArmor)getRegisteredItem("golden_helmet");

    public static final ItemArmor GOLDEN_CHESTPLATE = (ItemArmor)getRegisteredItem("golden_chestplate");

    public static final ItemArmor GOLDEN_LEGGINGS = (ItemArmor)getRegisteredItem("golden_leggings");

    public static final ItemArmor GOLDEN_BOOTS = (ItemArmor)getRegisteredItem("golden_boots");

    public static final Item FLINT = getRegisteredItem("flint");

    public static final Item PORKCHOP = getRegisteredItem("porkchop");

    public static final Item COOKED_PORKCHOP = getRegisteredItem("cooked_porkchop");

    public static final Item PAINTING = getRegisteredItem("painting");

    public static final Item GOLDEN_APPLE = getRegisteredItem("golden_apple");

    public static final Item SIGN = getRegisteredItem("sign");

    public static final Item OAK_DOOR = getRegisteredItem("wooden_door");

    public static final Item SPRUCE_DOOR = getRegisteredItem("spruce_door");

    public static final Item BIRCH_DOOR = getRegisteredItem("birch_door");

    public static final Item JUNGLE_DOOR = getRegisteredItem("jungle_door");

    public static final Item ACACIA_DOOR = getRegisteredItem("acacia_door");

    public static final Item DARK_OAK_DOOR = getRegisteredItem("dark_oak_door");

    public static final Item BUCKET = getRegisteredItem("bucket");

    public static final Item WATER_BUCKET = getRegisteredItem("water_bucket");

    public static final Item LAVA_BUCKET = getRegisteredItem("lava_bucket");

    public static final Item MINECART = getRegisteredItem("minecart");

    public static final Item SADDLE = getRegisteredItem("saddle");

    public static final Item IRON_DOOR = getRegisteredItem("iron_door");

    public static final Item REDSTONE = getRegisteredItem("redstone");

    public static final Item SNOWBALL = getRegisteredItem("snowball");

    public static final Item BOAT = getRegisteredItem("boat");

    public static final Item SPRUCE_BOAT = getRegisteredItem("spruce_boat");

    public static final Item BIRCH_BOAT = getRegisteredItem("birch_boat");

    public static final Item JUNGLE_BOAT = getRegisteredItem("jungle_boat");

    public static final Item ACACIA_BOAT = getRegisteredItem("acacia_boat");

    public static final Item DARK_OAK_BOAT = getRegisteredItem("dark_oak_boat");

    public static final Item LEATHER = getRegisteredItem("leather");

    public static final Item MILK_BUCKET = getRegisteredItem("milk_bucket");

    public static final Item BRICK = getRegisteredItem("brick");

    public static final Item CLAY_BALL = getRegisteredItem("clay_ball");

    public static final Item REEDS = getRegisteredItem("reeds");

    public static final Item PAPER = getRegisteredItem("paper");

    public static final Item BOOK = getRegisteredItem("book");

    public static final Item SLIME_BALL = getRegisteredItem("slime_ball");

    public static final Item CHEST_MINECART = getRegisteredItem("chest_minecart");

    public static final Item FURNACE_MINECART = getRegisteredItem("furnace_minecart");

    public static final Item EGG = getRegisteredItem("egg");

    public static final Item COMPASS = getRegisteredItem("compass");

    public static final ItemFishingRod FISHING_ROD = (ItemFishingRod)getRegisteredItem("fishing_rod");

    public static final Item CLOCK = getRegisteredItem("clock");

    public static final Item GLOWSTONE_DUST = getRegisteredItem("glowstone_dust");

    public static final Item FISH = getRegisteredItem("fish");

    public static final Item COOKED_FISH = getRegisteredItem("cooked_fish");

    public static final Item DYE = getRegisteredItem("dye");

    public static final Item BONE = getRegisteredItem("bone");

    public static final Item SUGAR = getRegisteredItem("sugar");

    public static final Item CAKE = getRegisteredItem("cake");

    public static final Item BED = getRegisteredItem("bed");

    public static final Item REPEATER = getRegisteredItem("repeater");

    public static final Item COOKIE = getRegisteredItem("cookie");

    public static final ItemMap FILLED_MAP = (ItemMap)getRegisteredItem("filled_map");

    public static final ItemShears SHEARS = (ItemShears)getRegisteredItem("shears");

    public static final Item MELON = getRegisteredItem("melon");

    public static final Item PUMPKIN_SEEDS = getRegisteredItem("pumpkin_seeds");

    public static final Item MELON_SEEDS = getRegisteredItem("melon_seeds");

    public static final Item BEEF = getRegisteredItem("beef");

    public static final Item COOKED_BEEF = getRegisteredItem("cooked_beef");

    public static final Item CHICKEN = getRegisteredItem("chicken");

    public static final Item COOKED_CHICKEN = getRegisteredItem("cooked_chicken");

    public static final Item MUTTON = getRegisteredItem("mutton");

    public static final Item COOKED_MUTTON = getRegisteredItem("cooked_mutton");

    public static final Item RABBIT = getRegisteredItem("rabbit");

    public static final Item COOKED_RABBIT = getRegisteredItem("cooked_rabbit");

    public static final Item RABBIT_STEW = getRegisteredItem("rabbit_stew");

    public static final Item RABBIT_FOOT = getRegisteredItem("rabbit_foot");

    public static final Item RABBIT_HIDE = getRegisteredItem("rabbit_hide");

    public static final Item ROTTEN_FLESH = getRegisteredItem("rotten_flesh");

    public static final Item ENDER_PEARL = getRegisteredItem("ender_pearl");

    public static final Item BLAZE_ROD = getRegisteredItem("blaze_rod");

    public static final Item GHAST_TEAR = getRegisteredItem("ghast_tear");

    public static final Item GOLD_NUGGET = getRegisteredItem("gold_nugget");

    public static final Item NETHER_WART = getRegisteredItem("nether_wart");

    public static final ItemPotion POTIONITEM = (ItemPotion)getRegisteredItem("potion");

    public static final ItemPotion SPLASH_POTION = (ItemPotion)getRegisteredItem("splash_potion");

    public static final ItemPotion LINGERING_POTION = (ItemPotion)getRegisteredItem("lingering_potion");

    public static final Item GLASS_BOTTLE = getRegisteredItem("glass_bottle");

    public static final Item DRAGON_BREATH = getRegisteredItem("dragon_breath");

    public static final Item SPIDER_EYE = getRegisteredItem("spider_eye");

    public static final Item FERMENTED_SPIDER_EYE = getRegisteredItem("fermented_spider_eye");

    public static final Item BLAZE_POWDER = getRegisteredItem("blaze_powder");

    public static final Item MAGMA_CREAM = getRegisteredItem("magma_cream");

    public static final Item BREWING_STAND = getRegisteredItem("brewing_stand");

    public static final Item CAULDRON = getRegisteredItem("cauldron");

    public static final Item ENDER_EYE = getRegisteredItem("ender_eye");

    public static final Item SPECKLED_MELON = getRegisteredItem("speckled_melon");

    public static final Item SPAWN_EGG = getRegisteredItem("spawn_egg");

    public static final Item EXPERIENCE_BOTTLE = getRegisteredItem("experience_bottle");

    public static final Item FIRE_CHARGE = getRegisteredItem("fire_charge");

    public static final Item WRITABLE_BOOK = getRegisteredItem("writable_book");

    public static final Item WRITTEN_BOOK = getRegisteredItem("written_book");

    public static final Item EMERALD = getRegisteredItem("emerald");

    public static final Item ITEM_FRAME = getRegisteredItem("item_frame");

    public static final Item FLOWER_POT = getRegisteredItem("flower_pot");

    public static final Item CARROT = getRegisteredItem("carrot");

    public static final Item POTATO = getRegisteredItem("potato");

    public static final Item BAKED_POTATO = getRegisteredItem("baked_potato");

    public static final Item POISONOUS_POTATO = getRegisteredItem("poisonous_potato");

    public static final ItemEmptyMap MAP = (ItemEmptyMap)getRegisteredItem("map");

    public static final Item GOLDEN_CARROT = getRegisteredItem("golden_carrot");

    public static final Item SKULL = getRegisteredItem("skull");

    public static final Item CARROT_ON_A_STICK = getRegisteredItem("carrot_on_a_stick");

    public static final Item NETHER_STAR = getRegisteredItem("nether_star");

    public static final Item PUMPKIN_PIE = getRegisteredItem("pumpkin_pie");

    public static final Item FIREWORKS = getRegisteredItem("fireworks");

    public static final Item FIREWORK_CHARGE = getRegisteredItem("firework_charge");

    public static final Item ENCHANTED_BOOK = getRegisteredItem("enchanted_book");

    public static final Item COMPARATOR = getRegisteredItem("comparator");

    public static final Item NETHERBRICK = getRegisteredItem("netherbrick");

    public static final Item QUARTZ = getRegisteredItem("quartz");

    public static final Item TNT_MINECART = getRegisteredItem("tnt_minecart");

    public static final Item HOPPER_MINECART = getRegisteredItem("hopper_minecart");

    public static final ItemArmorStand ARMOR_STAND = (ItemArmorStand)getRegisteredItem("armor_stand");

    public static final Item IRON_HORSE_ARMOR = getRegisteredItem("iron_horse_armor");

    public static final Item GOLDEN_HORSE_ARMOR = getRegisteredItem("golden_horse_armor");

    public static final Item DIAMOND_HORSE_ARMOR = getRegisteredItem("diamond_horse_armor");

    public static final Item LEAD = getRegisteredItem("lead");

    public static final Item NAME_TAG = getRegisteredItem("name_tag");

    public static final Item COMMAND_BLOCK_MINECART = getRegisteredItem("command_block_minecart");

    public static final Item RECORD_13 = getRegisteredItem("record_13");

    public static final Item RECORD_CAT = getRegisteredItem("record_cat");

    public static final Item RECORD_BLOCKS = getRegisteredItem("record_blocks");

    public static final Item RECORD_CHIRP = getRegisteredItem("record_chirp");

    public static final Item RECORD_FAR = getRegisteredItem("record_far");

    public static final Item RECORD_MALL = getRegisteredItem("record_mall");

    public static final Item RECORD_MELLOHI = getRegisteredItem("record_mellohi");

    public static final Item RECORD_STAL = getRegisteredItem("record_stal");

    public static final Item RECORD_STRAD = getRegisteredItem("record_strad");

    public static final Item RECORD_WARD = getRegisteredItem("record_ward");

    public static final Item RECORD_11 = getRegisteredItem("record_11");

    public static final Item RECORD_WAIT = getRegisteredItem("record_wait");

    public static final Item PRISMARINE_SHARD = getRegisteredItem("prismarine_shard");

    public static final Item PRISMARINE_CRYSTALS = getRegisteredItem("prismarine_crystals");

    public static final Item BANNER = getRegisteredItem("banner");

    public static final Item END_CRYSTAL = getRegisteredItem("end_crystal");

    public static final Item SHIELD = getRegisteredItem("shield");

    public static final Item ELYTRA = getRegisteredItem("elytra");

    public static final Item CHORUS_FRUIT = getRegisteredItem("chorus_fruit");

    public static final Item CHORUS_FRUIT_POPPED = getRegisteredItem("chorus_fruit_popped");

    public static final Item BEETROOT_SEEDS = getRegisteredItem("beetroot_seeds");

    public static final Item BEETROOT = getRegisteredItem("beetroot");

    public static final Item BEETROOT_SOUP = getRegisteredItem("beetroot_soup");

    public static final Item TOTEM_OF_UNDYING = getRegisteredItem("totem_of_undying");

    public static final Item field_190930_cZ = getRegisteredItem("shulker_shell");

    public static final Item field_191525_da = getRegisteredItem("iron_nugget");

    public static final Item field_192397_db = getRegisteredItem("knowledge_book");
}
