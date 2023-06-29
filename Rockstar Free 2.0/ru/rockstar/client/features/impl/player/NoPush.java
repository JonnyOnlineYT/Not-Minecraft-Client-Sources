package ru.rockstar.client.features.impl.player;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;


public class NoPush extends Feature {
    public static BooleanSetting pushplayers = new BooleanSetting("Players", true, () -> true);
    public static BooleanSetting pushblocks = new BooleanSetting("Blocks", true, () -> true);
    public static BooleanSetting pushwater = new BooleanSetting("Water", true, () -> true);
    public NoPush() {
        super("NoPush", "Не отталкивает вас от воды,блоков,игроков",0, Category.PLAYER);
        addSettings(pushblocks,pushplayers,pushwater);

    }
}

