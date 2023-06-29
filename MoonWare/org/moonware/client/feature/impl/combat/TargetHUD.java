package org.moonware.client.feature.impl.combat;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;

import java.awt.*;

public class TargetHUD extends Feature {
    public static BooleanSetting targetHud;
    public static ColorSetting targetHudColor;
    public static ListSetting targetHudMode;
    public static BooleanSetting targetHudGradient;
    public static BooleanSetting targetHudGlow;
    public static BooleanSetting targetHudAnimation;
    public static ListSetting targetHudAnimationMode;
    public static ListSetting thBackgroundMode= new ListSetting("Background mode","Gradient",() -> targetHudMode.currentMode.equalsIgnoreCase("Up-Gradient") && targetHud.get(),"Gradient","transparent","blur");
    public static ListSetting thHealthMode= new ListSetting("Health mode","Gradient",() -> targetHudMode.currentMode.equalsIgnoreCase("Up-Gradient") && targetHud.get(),"Gradient","transparent");
    public static ColorSetting left_up_color = new ColorSetting("FourColor", Color.BLUE.getRGB(),()-> targetHudMode.currentMode.equals("Up-Gradient") && thBackgroundMode.currentMode.equalsIgnoreCase("Gradient"));
    public static ColorSetting left_down_color = new ColorSetting("ThreeColor",Color.BLUE.getRGB(),()-> targetHudMode.currentMode.equals("Up-Gradient") && thBackgroundMode.currentMode.equalsIgnoreCase("Gradient"));
    public static ColorSetting right_up_color = new ColorSetting("OneColor",Color.BLUE.getRGB(),()-> targetHudMode.currentMode.equals("Up-Gradient"));
    public static ColorSetting right_down_color = new ColorSetting("TwoColor",Color.BLUE.getRGB(),()-> targetHudMode.currentMode.equals("Up-Gradient") && thBackgroundMode.currentMode.equalsIgnoreCase("Gradient"));


    public TargetHUD() {
        super("TargetHud", "Отображает информацию о таргете на экране", Type.Hud);
        targetHud = new BooleanSetting("tHud", false, () -> true);
        //targetHudColor = new ColorSetting("TargetHUD Color", new Color(200, 78, 205, 25).brighter().getRGB(), () -> targetHud.getCurrentValue() && (KillAura.targetHudMode.currentMode.equals("Astolfo") || KillAura.targetHudMode.currentMode.equals("Glow") || KillAura.targetHudMode.currentMode.equals("Novoline Old") || KillAura.targetHudMode.currentMode.equals("Novoline New")));
        targetHudMode = new ListSetting("THud Mode", "Type-1", () -> targetHud.getBoolValue(),  "Type-2", "Type-1", "Type-3" , "Type4");
        /*
        Types:
        1 - default, not info and health
        2 - xz
        3 - info in left and right and hurttime,dst , health
        4 - info in left and health :
         */
        targetHudGlow = new BooleanSetting("Outline Glow", true,()-> targetHudMode.getCurrentMode().equalsIgnoreCase("Bordered"));
        targetHudGradient = new BooleanSetting("THud Gradient",true,()-> targetHudMode.getCurrentMode().equalsIgnoreCase("Bordered"));
        targetHudAnimation = new BooleanSetting("THudAnimation", true, () -> targetHud.getBoolValue());
        targetHudAnimationMode = new ListSetting("THudAnimation Mode", "Translate",()-> targetHudAnimation.getBoolValue(),"Translate","Scale");
        addSettings(targetHud, thBackgroundMode, right_up_color, right_down_color, left_down_color, left_up_color, targetHudGlow, targetHudGradient, targetHudAnimation, targetHudAnimationMode, targetHudMode);
    }


}
