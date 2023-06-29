package ru.rockstar.client.features.impl.visuals;

import java.awt.*;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class Chams extends Feature {

    public static ColorSetting colorChams;
    public static BooleanSetting clientColor;
    public static ListSetting chamsMode;

    public Chams() {
        super("Chams", "Подсвечивает игроков",0, Category.VISUALS);
        chamsMode = new ListSetting("Chams Mode", "Fill", () -> true, "Fill", "Walls");
        clientColor = new BooleanSetting("Client Colored", false, () -> !chamsMode.currentMode.equals("Walls"));
        colorChams = new ColorSetting("Chams Color", new Color(0xFFFFFF).getRGB(), () -> !chamsMode.currentMode.equals("Walls") && !clientColor.getBoolValue());
        addSettings(chamsMode, colorChams, clientColor);
    }
}