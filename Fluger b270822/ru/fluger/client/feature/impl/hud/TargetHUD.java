package ru.fluger.client.feature.impl.hud;

import java.awt.Color;
import net.minecraft.entity.EntityLivingBase;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;

public class TargetHUD extends Feature {
   private double scale = 0.0;
   private static EntityLivingBase curTarget = null;
   public static TimerHelper thudTimer = new TimerHelper();
   private float healthBarWidth;
   public static ListSetting thudColorMode = new ListSetting("TargetHUD Color", "Astolfo", () -> {
      return true;
   }, new String[]{"Astolfo", "Rainbow", "Client", "Custom"});
   public static ColorSetting targetHudColor;

   public TargetHUD() {
      super("TargetHUD", "���������� ����� �� ������", Type.Hud);
      this.addSettings(new Setting[]{thudColorMode, targetHudColor});
   }

   public void onEnable() {
      if (mc.gameSettings.ofFastRender) {
         mc.gameSettings.ofFastRender = false;
      }

      super.onEnable();
   }

   static {
      targetHudColor = new ColorSetting("THUD Color", Color.PINK.getRGB(), () -> {
         return thudColorMode.currentMode.equals("Custom");
      });
   }
}
