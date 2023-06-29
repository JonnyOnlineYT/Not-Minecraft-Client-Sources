package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class Timer extends Module {
   public Timer() {
      super("Timer", Category.Movement);
      Main.instance.setmgr.rSetting(new Setting("Timer", this, 1.0D, 0.1D, 10.0D, false));
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (this.getState()) {
         mc.timer.timerSpeed = Main.instance.setmgr.getSettingByName("Timer").getValFloat();
      }

   }

   public void onDisable() {
      super.onDisable();
      mc.timer.timerSpeed = 1.0F;
   }
}
