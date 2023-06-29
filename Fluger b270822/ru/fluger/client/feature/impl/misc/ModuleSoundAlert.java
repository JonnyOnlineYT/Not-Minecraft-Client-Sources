package ru.fluger.client.feature.impl.misc;

import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;

public class ModuleSoundAlert extends Feature {
   public static ListSetting soundMode;
   public static NumberSetting volume;
   public static NumberSetting pitch;

   public ModuleSoundAlert() {
      super("ModuleSoundAlert", "������������� ����� ���������/���������� ������", Type.Misc);
      soundMode = new ListSetting("Sound Mode", "Wav", () -> {
         return true;
      }, new String[]{"Wav", "Button"});
      volume = new NumberSetting("Volume", 50.0F, 1.0F, 100.0F, 1.0F, () -> {
         return true;
      });
      pitch = new NumberSetting("Pitch", 2.0F, 0.5F, 2.0F, 0.1F, () -> {
         return soundMode.currentMode.equals("Button");
      });
      this.addSettings(new Setting[]{soundMode, volume, pitch});
   }
}
