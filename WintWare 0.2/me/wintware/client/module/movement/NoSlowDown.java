package me.wintware.client.module.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class NoSlowDown extends Module {
   public static Setting percentage;

   public NoSlowDown() {
      super("NoSlowDown", Category.Movement);
      Main.instance.setmgr.rSetting(percentage = new Setting("Percentage", this, 100.0D, 0.0D, 100.0D, false));
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      double а = percentage.getValDouble();
      this.setDisplayName("NoSlowDown " + ChatFormatting.GRAY + а);
   }
}
