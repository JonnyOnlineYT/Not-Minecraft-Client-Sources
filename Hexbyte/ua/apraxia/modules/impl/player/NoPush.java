package ua.apraxia.modules.impl.player;


import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;

public class NoPush extends Module {
    public static BooleanSetting water = new BooleanSetting("Water", true);
    public static BooleanSetting players = new BooleanSetting("Entity", true);
    public static BooleanSetting blocks = new BooleanSetting("Blocks", true);

    public NoPush() {
        super("NoPush", Categories.Player);
        addSetting(players, water, blocks);
    }
}
