package ru.rockstar.client.features.impl.display;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class Notifications extends Feature {
    public static ListSetting notifMode;

    public static BooleanSetting state;

    public Notifications() {
        super("Notifications", "Показывает необходимую информацию о модулях",0, Category.DISPLAY);
        state = new BooleanSetting("Module State", true, () -> true);
        notifMode = new ListSetting("Notification Mode", "Rect", () -> true, "Rect", "Chat", "System");
        addSettings(notifMode,state);
    }
}