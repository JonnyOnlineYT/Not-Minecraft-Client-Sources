package ru.rockstar.client.features.impl.visuals;

import java.awt.*;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ColorSetting;

public class NightMode extends Feature {
    public static ColorSetting worldColor = new ColorSetting("World Color", Color.RED.getRGB(), () -> true);

    public NightMode() {
        super("NightMode", "Меняет цвет мира", 0, Category.VISUALS);
        addSettings(worldColor);

    }
}