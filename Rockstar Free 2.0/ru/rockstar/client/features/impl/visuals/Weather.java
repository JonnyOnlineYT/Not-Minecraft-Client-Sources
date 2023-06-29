package ru.rockstar.client.features.impl.visuals;

import java.awt.*;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ColorSetting;

public class Weather extends Feature {
    public static ColorSetting weatherColor;
    public Weather() {
        super("Weather", "Добавляет частички снега в мир", 0, Category.VISUALS);
        weatherColor = new ColorSetting("Weather",new Color(0xFFFFFF).getRGB(), () -> true);
        addSettings(weatherColor);
    }
}
