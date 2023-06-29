package Celestial.module.impl.Render;

import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ListSetting;

public class Notifications extends Module {
    public static ListSetting notifMode;

    public static BooleanSetting state;

    public Notifications() {
        super("Notifications", "Показывает информацию о модуле.", ModuleCategory.Render);
        state = new BooleanSetting("Module State", true, () -> true);
        notifMode = new ListSetting("Notification Mode", "Rect", () -> true, "Rect", "Chat");
        addSettings(notifMode,state);
    }
}