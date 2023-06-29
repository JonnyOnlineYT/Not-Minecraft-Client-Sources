package Celestial.module.impl.Util;

import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.BooleanSetting;

public class NameProtect extends Module {
    public static BooleanSetting myName = new BooleanSetting("My Name", false, () -> true);
    public static BooleanSetting friends = new BooleanSetting("Friends Spoof", true, () -> true);
    public static BooleanSetting otherName = new BooleanSetting("Other Names", true, () -> true);
    public static BooleanSetting tabSpoof = new BooleanSetting("Tab Spoof", true, () -> true);
    public static BooleanSetting scoreboardSpoof = new BooleanSetting("Scoreboard Spoof", true, () -> true);

    public NameProtect() {
        super("NameProtect", "Скрывает ваш ник и др.", ModuleCategory.Util);
        addSettings(otherName, friends, tabSpoof, scoreboardSpoof);
    }
}
