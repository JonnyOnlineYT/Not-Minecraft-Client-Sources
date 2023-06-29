package org.moonware.client.feature.impl.misc;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;

public class WeatherColor extends Feature {
    public static ColorSetting weatherColor;
    public static BooleanSetting rain;
    public static NumberSetting rainStrengh;
    public WeatherColor() {
        super("CustomWeather", "изменяет цвет погоды", Type.Other);
        weatherColor = new ColorSetting("Color",new Color(0xFFFFFF).getRGB(), () -> true);
        rain = new BooleanSetting("AlwaysRain", true, () -> true);
        rainStrengh = new NumberSetting("Rain Strengh",1,1,1,1,() -> true);
        addSettings(weatherColor);
    }
}
