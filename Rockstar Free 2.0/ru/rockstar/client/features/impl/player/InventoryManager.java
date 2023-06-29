package ru.rockstar.client.features.impl.player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class InventoryManager extends Feature
{
    public static NumberSetting delay1;
    public static BooleanSetting cleaner;
    public static BooleanSetting openinv;
    public static BooleanSetting dropBlocks;
    public static BooleanSetting nomoveswap;
    public static int weaponSlot;
    public static int pickaxeSlot;
    public static int axeSlot;
    public static int shovelSlot;
    public static List<Block> invalidBlocks;
    private final TimerHelper timer;
    
    public InventoryManager() {
        super("InventoryManager", "\u0427\u0438\u0441\u0442\u0438\u0442, \u0441\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c \u0437\u0430 \u0432\u0430\u0441",0, Category.PLAYER);
        this.timer = new TimerHelper();
        InventoryManager.delay1 = new NumberSetting("Sort Delay", 1.0f, 0.0f, 10.0f, 0.1f, () -> true);
        InventoryManager.cleaner = new BooleanSetting("Cleaner", true, () -> true);
        InventoryManager.openinv = new BooleanSetting("Open Inv", true, () -> true);
        InventoryManager.nomoveswap = new BooleanSetting("No Moving Swap", false, () -> true);
        InventoryManager.dropBlocks = new BooleanSetting("Drop Blocks", false, () -> InventoryManager.cleaner.getBoolValue());
        this.addSettings(InventoryManager.delay1, InventoryManager.cleaner, InventoryManager.dropBlocks, InventoryManager.openinv, InventoryManager.nomoveswap);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion eventPre) {
        final long delay = (long)InventoryManager.delay1.getNumberValue() * 50L;
        if (!(InventoryManager.mc.currentScreen instanceof GuiInventory) && InventoryManager.openinv.getBoolValue()) {
            return;
        }
        if (MovementHelper.isMoving() && InventoryManager.nomoveswap.getBoolValue()) {
            return;
        }
        if (InventoryManager.mc.currentScreen == null || InventoryManager.mc.currentScreen instanceof GuiInventory || InventoryManager.mc.currentScreen instanceof GuiChat) {
            if (this.timer.hasReached((double)delay) && InventoryManager.weaponSlot >= 36) {
                if (!InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.weaponSlot).getHasStack()) {
                    this.getBestWeapon(InventoryManager.weaponSlot);
                }
                else if (!this.isBestWeapon(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.weaponSlot).getStack())) {
                    this.getBestWeapon(InventoryManager.weaponSlot);
                }
            }
            if (this.timer.hasReached((double)delay) && InventoryManager.axeSlot >= 36) {
                this.getBestAxe();
            }
            if (this.timer.hasReached((double)delay) && InventoryManager.cleaner.getBoolValue()) {
                for (int i = 9; i < 45; ++i) {
                    if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                        final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                        if (this.shouldDrop(is, i)) {
                            this.drop(i);
                            if (delay == 0L) {
                                InventoryManager.mc.player.closeScreen();
                            }
                            this.timer.reset();
                            if (delay > 0L) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void swap(final int slot1, final int hotbarSlot) {
        InventoryManager.mc.playerController.windowClick(InventoryManager.mc.player.inventoryContainer.windowId, slot1, hotbarSlot, ClickType.SWAP, InventoryManager.mc.player);
    }
    
    public void drop(final int slot) {
        InventoryManager.mc.playerController.windowClick(InventoryManager.mc.player.inventoryContainer.windowId, slot, 1, ClickType.THROW, InventoryManager.mc.player);
    }
    
    public boolean isBestWeapon(final ItemStack stack) {
        final float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.getDamage(is) > damage && is.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        return stack.getItem() instanceof ItemSword;
    }
    
    public void getBestWeapon(final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.isBestWeapon(is) && this.getDamage(is) > 0.0f && is.getItem() instanceof ItemSword) {
                    this.swap(i, slot - 36);
                    this.timer.reset();
                    break;
                }
            }
        }
    }
    
    private float getDamage(final ItemStack stack) {
        float damage = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemTool) {
            final ItemTool tool = (ItemTool)item;
            damage += tool.getDamageVsEntity();
        }
        if (item instanceof ItemSword) {
            final ItemSword sword = (ItemSword)item;
            damage += sword.getDamageVsEntity();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), stack) * 1.25f + EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), stack) * 0.01f;
        return damage;
    }
    
    public boolean shouldDrop(final ItemStack stack, final int slot) {
        if (stack.getDisplayName().toLowerCase().contains("/")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("\u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("§k||")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("kit")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("\u043b\u043e\u0431\u0431\u0438")) {
            return false;
        }
        if ((slot == InventoryManager.weaponSlot && this.isBestWeapon(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.weaponSlot).getStack())) || (slot == InventoryManager.pickaxeSlot && this.isBestPickaxe(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.pickaxeSlot).getStack()) && InventoryManager.pickaxeSlot >= 0) || (slot == InventoryManager.axeSlot && this.isBestAxe(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.axeSlot).getStack()) && InventoryManager.axeSlot >= 0) || (slot == InventoryManager.shovelSlot && this.isBestShovel(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.shovelSlot).getStack()) && InventoryManager.shovelSlot >= 0)) {
            return false;
        }
        if (stack.getItem() instanceof ItemArmor) {
            for (int type = 1; type < 5; ++type) {
                if (InventoryManager.mc.player.inventoryContainer.getSlot(4 + type).getHasStack()) {
                    final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(4 + type).getStack();
                    if (InventoryHelper.isBestArmor(is, type)) {
                        continue;
                    }
                }
                if (InventoryHelper.isBestArmor(stack, type)) {
                    return false;
                }
            }
        }
        return (InventoryManager.dropBlocks.getBoolValue() && stack.getItem() instanceof ItemBlock) || (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) || ((!(stack.getItem() instanceof ItemFood) || stack.getItem() instanceof ItemAppleGold) && (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor || (!(stack.getItem() instanceof ItemBow) && !stack.getItem().getUnlocalizedName().contains("arrow") && (stack.getItem().getUnlocalizedName().contains("tnt") || stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("bed") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || (stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("wool") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston")))));
    }
    
    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !InventoryManager.invalidBlocks.contains(((ItemBlock)item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }
    
    private void getBestPickaxe() {
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.isBestPickaxe(is) && InventoryManager.pickaxeSlot != i && !this.isBestWeapon(is)) {
                    if (!InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.pickaxeSlot).getHasStack()) {
                        this.swap(i, InventoryManager.pickaxeSlot - 36);
                        this.timer.reset();
                        if (InventoryManager.delay1.getNumberValue() > 0.0f) {
                            return;
                        }
                    }
                    else if (!this.isBestPickaxe(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.pickaxeSlot).getStack())) {
                        this.swap(i, InventoryManager.pickaxeSlot - 36);
                        this.timer.reset();
                        if (InventoryManager.delay1.getNumberValue() > 0.0f) {
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void getBestShovel() {
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.isBestShovel(is) && InventoryManager.shovelSlot != i && !this.isBestWeapon(is)) {
                    if (!InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.shovelSlot).getHasStack()) {
                        this.swap(i, InventoryManager.shovelSlot - 36);
                        this.timer.reset();
                        if (InventoryManager.delay1.getNumberValue() > 0.0f) {
                            return;
                        }
                    }
                    else if (!this.isBestShovel(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.shovelSlot).getStack())) {
                        this.swap(i, InventoryManager.shovelSlot - 36);
                        this.timer.reset();
                        if (InventoryManager.delay1.getNumberValue() > 0.0f) {
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void getBestAxe() {
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.isBestAxe(is) && InventoryManager.axeSlot != i && !this.isBestWeapon(is)) {
                    if (!InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.axeSlot).getHasStack()) {
                        this.swap(i, InventoryManager.axeSlot - 36);
                        this.timer.reset();
                        if (InventoryManager.delay1.getNumberValue() > 0.0f) {
                            return;
                        }
                    }
                    else if (!this.isBestAxe(InventoryManager.mc.player.inventoryContainer.getSlot(InventoryManager.axeSlot).getStack())) {
                        this.swap(i, InventoryManager.axeSlot - 36);
                        this.timer.reset();
                        if (InventoryManager.delay1.getNumberValue() > 0.0f) {
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private boolean isBestPickaxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestShovel(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestAxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (InventoryManager.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InventoryManager.mc.player.inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !this.isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private float getToolEffect(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        final String name = item.getUnlocalizedName();
        final ItemTool tool = (ItemTool)item;
        float value;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.STONE.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.DIRT.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            value = tool.getStrVsBlock(stack, Blocks.LOG.getDefaultState());
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(32)), stack) * 0.0075);
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), stack) / 100.0);
        return value;
    }
    
    private boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            for (final PotionEffect o : PotionUtils.getEffectsFromStack(stack)) {
                if (o.getPotion() == Potion.getPotionById(19) || o.getPotion() == Potion.getPotionById(7) || o.getPotion() == Potion.getPotionById(2) || o.getPotion() == Potion.getPotionById(18)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static {
        InventoryManager.weaponSlot = 36;
        InventoryManager.pickaxeSlot = 37;
        InventoryManager.axeSlot = 38;
        InventoryManager.shovelSlot = 39;
        InventoryManager.invalidBlocks = Arrays.asList(Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA, Blocks.SAND, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.STONE_SLAB, Blocks.WOODEN_SLAB, Blocks.STONE_SLAB2, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, Blocks.CACTUS, Blocks.LADDER, Blocks.WEB);
    }
}
