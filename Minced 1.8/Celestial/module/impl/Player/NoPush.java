package Celestial.module.impl.Player;

import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.BooleanSetting;

public class NoPush extends Module {
    public static BooleanSetting water = new BooleanSetting("Water", true, () -> true);
    public static BooleanSetting players = new BooleanSetting("Entity", true, () -> true);
    public static BooleanSetting blocks = new BooleanSetting("Blocks", true, () -> true);

    public NoPush() {
        super("NoPush", "Вы не отталкиваетесь", ModuleCategory.Player);
        addSettings(players, water, blocks);
    }
}
