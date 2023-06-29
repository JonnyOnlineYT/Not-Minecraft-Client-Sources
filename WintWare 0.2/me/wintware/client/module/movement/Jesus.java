package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;

public class Jesus extends Module {
   public Jesus() {
      super("Jesus", Category.Movement);
   }

   @EventTarget
   public void onPreMotion(EventPreMotionUpdate event) {
      if (mc.player.isInWater() && mc.player.motionY < 0.0D) {
         mc.player.jump();
         MovementUtil.setSpeed(0.3D);
      }

   }
}
