package me.wintware.client.module.player;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class VClip extends Module {
   public VClip() {
      super("VClip", Category.Player);
      Main.instance.setmgr.rSetting(new Setting("Y", this, 50.0D, 1.0D, 200.0D, false));
   }

   @EventTarget
   public void onUpdate(EventPreMotionUpdate event) {
      if (mc.player != null || mc.world != null) {
         float tp = Main.instance.setmgr.getSettingByName("Y").getValFloat();

         for(int i = 0; i < 1; ++i) {
            mc.player.setEntityBoundingBox(mc.player.getEntityBoundingBox().offset(0.0D, (double)(-tp), 0.0D));
            this.toggle();
         }

      }
   }
}
