package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class SpeedMine extends Module {
   public SpeedMine() {
      super("SpeedMine", Category.Player);
   }

   @EventTarget
   public void onUpdate(EventPreMotionUpdate event) {
      if (mc.playerController.curBlockDamageMP >= 0.7F) {
         mc.playerController.curBlockDamageMP = 1.0F;
      }

   }
}
