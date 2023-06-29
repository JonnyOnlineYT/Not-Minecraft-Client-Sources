package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class HighJump extends Module {
   public HighJump() {
      super("WaterJump", Category.Movement);
      Main.instance.setmgr.rSetting(new Setting("Motion", this, 1.0D, 0.0D, 25.0D, false));
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player.isInWater() && mc.player.motionY < 0.0D) {
         mc.player.jump();
         mc.player.motionY = Main.instance.setmgr.getSettingByName("Motion").getValDouble();
         mc.player.jumpMovementFactor = 0.5F;
      }

   }
}
