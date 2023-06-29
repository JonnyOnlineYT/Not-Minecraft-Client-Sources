package ru.rockstar.client.features.impl.player;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;

public class NoInteract extends Feature {
    public static BooleanSetting armorStands;
    public static BooleanSetting craftTable = new BooleanSetting("Craft Table", true, () -> true);
    public static BooleanSetting standing = new BooleanSetting("Standing Sign", true, () -> true);
    public static BooleanSetting door = new BooleanSetting("Door", true, () -> true);
    public static BooleanSetting hopper = new BooleanSetting("Hopper", true, () -> true);
    public static BooleanSetting furnace = new BooleanSetting("Furnace", true, () -> true);
    public static BooleanSetting dispenser = new BooleanSetting("Dispenser", true, () -> true);
    public static BooleanSetting anvil = new BooleanSetting("Furnace", true, () -> true);
    public static BooleanSetting woodenslab = new BooleanSetting("Wooden Slab", true, () -> true);
    public static BooleanSetting lever = new BooleanSetting("Lever", true, () -> true);

    public NoInteract() {
        super("NoInteract", "Позволяет не нажимать ПКМ по верстакам, печкам и т.д", 0, Category.PLAYER);
        armorStands = new BooleanSetting("Armor Stand", true, () -> true);
        addSettings(armorStands, craftTable, standing, door, hopper, furnace, dispenser, anvil, woodenslab, lever);
    }
}