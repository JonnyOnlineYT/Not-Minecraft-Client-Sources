package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.ListSetting;

public class ClientFont extends Feature {
   public static ListSetting font;

   public ClientFont() {
      super("Client Font", "��������� ������� ��������� ������ � ����", Type.Hud);
      font = new ListSetting("Font Type", "Ubuntu", new String[]{"RobotoRegular", "Rubik", "SF UI", "Luxora", "Calibri", "Verdana", "Comfortaa", "LucidaConsole", "Lato", "RaleWay", "Product Sans", "Open Sans", "Kollektif", "Ubuntu", "Bebas Book"});
      this.addSettings(new Setting[]{font});
   }

   public void onEnable() {
      this.toggle();
      super.onEnable();
   }
}
