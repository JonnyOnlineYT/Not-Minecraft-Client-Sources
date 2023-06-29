package org.moonware.client.feature.impl.hud;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class Notifications extends Feature {

    public static BooleanSetting state;
    public static BooleanSetting timePeriod;
    public static ListSetting position = new ListSetting("Position","Left-down","Right-down", "Left-down","Left-up");
    public static ListSetting startposition = new ListSetting("Start Position","Left-up","Right-down","Left-down","Left-up");
    public static ListSetting backGroundMode;
    public static NumberSetting timetooff;
    public static BooleanSetting testAnim = new BooleanSetting("Test anim", false);

    public Notifications() {
        super("Notifications", "Показывает необходимую информацию о модулях", Type.Hud);
        state = new BooleanSetting("Module State", true, () -> true);
        timetooff = new NumberSetting("Info Time", 2,0.5F,6,0.1F, () -> true);
        timePeriod = new BooleanSetting("Time Period", false, () -> false);
        addSettings(state, testAnim, position, startposition, timePeriod, timetooff, backGroundMode);
    }

    public static boolean isRightPosition() {
        return position.currentMode.equalsIgnoreCase("Right");
    }
    static {
        backGroundMode = new ListSetting("Background Mode", "Rect",()->true, "Rect","Gradient");
    }
}