package org.moonware.client.feature.impl.hud;

import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;

public class SessionStaticks extends Feature {
    public static BooleanSetting glow = new BooleanSetting("Glow","Свечение,если лагает,багается - выключите",true);
    public static ColorSetting left_up_color = new ColorSetting("Left Up Color", Color.BLUE.getRGB());
    public static ColorSetting left_down_color = new ColorSetting("Left Down Color",Color.BLUE.getRGB());
    public static ColorSetting right_up_color = new ColorSetting("Right Up Color",Color.BLUE.getRGB());
    public static ColorSetting right_down_color = new ColorSetting("Right Down Color",Color.BLUE.getRGB());
    public static ColorSetting glowColor = new ColorSetting("Glow Color",Color.BLUE.getRGB(),()-> glow.get());
    public static NumberSetting glowRange = new NumberSetting("Glow Range",13,1,50,1,()-> glow.get());
    public SessionStaticks() {
        super("SessionStats","Показывает статистику о сессии на экране", Type.Hud);
        addSettings(glow, glowColor, glowRange, right_up_color, right_down_color, left_down_color, left_up_color);
    }
}
