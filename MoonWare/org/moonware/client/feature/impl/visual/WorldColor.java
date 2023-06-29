package org.moonware.client.feature.impl.visual;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;

import java.awt.*;

public class WorldColor extends Feature {

    public static ListSetting worldColorMode = new ListSetting("LightMap Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
    public static ColorSetting lightMapColor;
    public static BooleanSetting customSky = new BooleanSetting("Custom Sky",true);
    public static ColorSetting customSkyColor = new ColorSetting("CustomSky Color",Color.WHITE.getRGB(), customSky::getBoolValue);

    public WorldColor() {
        super("WorldColor", "Меняет цвет игры", Type.Visuals);
        lightMapColor = new ColorSetting("LightMap Color", Color.WHITE.getRGB(), () -> worldColorMode.currentMode.equals("Custom"));
        addSettings(worldColorMode, lightMapColor, customSky, customSkyColor);
    }
}
