package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;

public class WaterSpeed extends Module {
   public int ticks = 0;
   public Setting speed;

   public WaterSpeed() {
      super("WaterSpeed", Category.Movement);
      Main.instance.setmgr.rSetting(this.speed = new Setting("Speed", this, 1.0D, 0.10000000149011612D, 5.0D, false));
   }

   @EventTarget
   public void onPreMotion(EventPreMotionUpdate event) {
      if (mc.player.isInWater()) {
         ++this.ticks;
         if (this.ticks == 4) {
            MovementUtil.setSpeed(this.speed.getValDouble());
         }

         if (this.ticks >= 5) {
            MovementUtil.setSpeed(this.speed.getValDouble());
            this.ticks = 0;
         }

         MovementUtil.setSpeed(this.speed.getValDouble());
      }

   }
}
