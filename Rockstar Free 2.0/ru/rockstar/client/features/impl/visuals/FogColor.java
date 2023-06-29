package ru.rockstar.client.features.impl.visuals;

import java.awt.*;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventFogColor;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class FogColor extends Feature {

    public static NumberSetting distance;
    public ListSetting colorMode;
    public ColorSetting customColor;

    public FogColor() {
        super("FogColor", "Меняет цвет тумана", 0, Category.VISUALS);
        colorMode = new ListSetting("Fog Color", "Rainbow", () -> true, "Rainbow", "Client", "Custom");
        distance = new NumberSetting("Distance", 0.10F, 0.001F, 2, 0.01F, () -> true);
        customColor = new ColorSetting("Custom Fog", new Color(0xAB31CB).getRGB(), () -> colorMode.currentMode.equals("Custom"));
        addSettings(colorMode, distance, customColor);
    }

    @EventTarget
    public void onFogColor(EventFogColor event) {
        String colorModeValue = colorMode.getOptions();
        if (colorModeValue.equalsIgnoreCase("Rainbow")) {
            Color color = DrawHelper.rainbow(1, 1, 1);
            event.setRed(color.getRed());
            event.setGreen(color.getGreen());
            event.setBlue(color.getBlue());
        } else if (colorModeValue.equalsIgnoreCase("Client")) {
            Color clientColor = ClientHelper.getClientColor();
            event.setRed(clientColor.getRed());
            event.setGreen(clientColor.getGreen());
            event.setBlue(clientColor.getBlue());
        } else if (colorModeValue.equalsIgnoreCase("Custom")) {
            Color customColorValue = new Color(customColor.getColorValue());
            event.setRed(customColorValue.getRed());
            event.setGreen(customColorValue.getGreen());
            event.setBlue(customColorValue.getBlue());
        }
    }
}
