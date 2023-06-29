package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.Fluger;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;

public class ClickGui extends Feature {
   public static BooleanSetting glow;
   public static BooleanSetting backGroundBlur;
   public static NumberSetting backGroundBlurStrength;
   public static ColorSetting color;
   public static BooleanSetting girl;
   public static ListSetting girlMode = new ListSetting("Girl Mode", "Girl1", () -> {
      return girl.getCurrentValue();
   }, new String[]{"Girl1", "Girl2", "Girl3", "Girl4", "Animated"});

   public ClickGui() {
      super("ClickGui", "��������� ���� ��� ����", Type.Hud);
      this.setBind(54);
      glow = new BooleanSetting("Glow", true, () -> {
         return true;
      });
      backGroundBlur = new BooleanSetting("Background Blur", true, () -> {
         return true;
      });
      backGroundBlurStrength = new NumberSetting("Blur Strength", 15.0F, 5.0F, 30.0F, 5.0F, () -> {
         return backGroundBlur.getCurrentValue();
      });
      girl = new BooleanSetting("Girl", true, () -> {
         return false;
      });
      this.addSettings(new Setting[]{glow, backGroundBlur, backGroundBlurStrength, girl, girlMode});
   }

   public void onEnable() {
      mc.displayGuiScreen(Fluger.instance.csgoGUi);
      Fluger.instance.featureManager.getFeatureByClass(ClickGui.class).setState(false);
      super.onEnable();
   }
}
