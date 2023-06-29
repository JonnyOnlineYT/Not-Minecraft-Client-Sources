package me.wintware.client.module.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class ArreyList extends Module {
   public ArreyList() {
      super("ArrayList", Category.Hud);
      ArrayList<String> color = new ArrayList();
      color.add("Rainbow");
      color.add("Rainbow2");
      color.add("Category");
      color.add("Pulsing");
      color.add("White");
      color.add("BluePurple");
      color.add("Test");
      color.add("GreenWhite");
      color.add("RedWhite");
      color.add("YellowAstolfo");
      color.add("Gold");
      Main.instance.setmgr.rSetting(new Setting("ArrayList Color", this, "Pulsing", color));
      Main.instance.setmgr.rSetting(new Setting("NoBackground", this, false));
      Main.instance.setmgr.rSetting(new Setting("NoBorder", this, false));
      Main.instance.setmgr.rSetting(new Setting("Sort", this, true));
      Main.instance.setmgr.rSetting(new Setting("Opacity", this, 1.0D, 0.0D, 1.0D, false));
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      String mode = Main.instance.setmgr.getSettingByName("ArrayList Color").getValString();
      this.setDisplayName("ArrayList " + ChatFormatting.GRAY + mode);
   }

   @EventTarget
   public void onRender2D(Event2D e) {
      HUD hud = new HUD();
      hud.renderArrayList();
   }
}
