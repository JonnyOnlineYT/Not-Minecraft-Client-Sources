package ru.fluger.client.feature.impl.hud;

import java.awt.Color;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;

public class ArrayList extends Feature {
   public static ListSetting mode = new ListSetting("Mode", "Shader", () -> {
      return true;
   }, new String[]{"Shader", "Flat"});
   public ListSetting colorMode = new ListSetting("Color Mode", "Custom", () -> {
      return mode.index == 1;
   }, new String[]{"Rainbow", "Custom"});
   public ColorSetting firstColor = new ColorSetting("Color", (new Color(14606046)).getRGB(), () -> {
      return this.colorMode.currentMode.equals("Custom");
   });
   public static BooleanSetting glowing = new BooleanSetting("Glowing", true, () -> {
      return mode.index == 1;
   });
   public NumberSetting rainbowSpeed = new NumberSetting("Rainbow speed", "", 10.0F, 1.0F, 20.0F, 1.0F, () -> {
      return this.colorMode.getCurrentMode().equalsIgnoreCase("Rainbow");
   });
   public NumberSetting saturation = new NumberSetting("Rainbow Saturation", "", 0.5F, 0.1F, 1.0F, 0.01F, () -> {
      return this.colorMode.getCurrentMode().equalsIgnoreCase("Rainbow");
   });
   public NumberSetting glow_strength = new NumberSetting("Glow strength", "", 30.0F, 10.0F, 50.0F, 1.0F, () -> {
      return glowing.getCurrentValue();
   });

   public ArrayList() {
      super("ArrayList", "", Type.Hud);
      this.addSettings(new Setting[]{this.firstColor, mode, this.rainbowSpeed, this.saturation, glowing, this.colorMode, this.glow_strength});
   }
}
