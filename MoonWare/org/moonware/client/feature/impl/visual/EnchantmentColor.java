package org.moonware.client.feature.impl.visual;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;

import java.awt.*;

public class EnchantmentColor extends Feature {

    public static ListSetting colorMode;
    public static ColorSetting customColor;

    public EnchantmentColor() {
        super("EnchantColor", "Изменяет цвет зачарований", Type.Visuals);
        colorMode = new ListSetting("Crumbs Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
        customColor = new ColorSetting("Custom Enchantment", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        addSettings(colorMode, customColor);
    }
}
