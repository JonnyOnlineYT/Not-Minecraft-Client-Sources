package me.wintware.client.module.visual;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class EnchantEffect extends Module {
   public static float hue = 0.0F;

   public EnchantEffect() {
      super("EnchantEffect", Category.Visuals);
      ArrayList<String> color = new ArrayList();
      color.add("Rgb");
      color.add("Custom");
      Main.instance.setmgr.rSetting(new Setting("Enchant color", this, "Rgb", color));
   }

   @EventTarget
   public void Render2d(Event2D e) {
      hue += 0.2F;
      if (hue > 255.0F) {
         hue = 0.0F;
      }

   }
}
