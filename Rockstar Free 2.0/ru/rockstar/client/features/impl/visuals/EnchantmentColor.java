package ru.rockstar.client.features.impl.visuals;

import java.awt.*;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class EnchantmentColor extends Feature {

    public static ListSetting colorMode;
    public static ColorSetting customColor;

    public EnchantmentColor() {
        super("EnchantmentColor", "Изменяет цвет зачарований",0, Category.VISUALS);
        colorMode = new ListSetting("Crumbs Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
        customColor = new ColorSetting("Custom Enchantment", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        addSettings(colorMode, customColor);
    }
}
