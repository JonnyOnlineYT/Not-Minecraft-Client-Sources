package me.wintware.client.module.movement;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;

public class Speed extends Module {
   public Speed() {
      super("Speed", Category.Movement);
      ArrayList<String> speed = new ArrayList();
      speed.add("Matrix 6.0.4");
      speed.add("OnGround");
      Main.instance.setmgr.rSetting(new Setting("Speed Mode", this, "Matrix 6.0.4", speed));
   }

   @EventTarget
   public void onPreMotion(EventPreMotionUpdate event) {
      if (this.getState()) {
         String mode = Main.instance.setmgr.getSettingByName("Speed Mode").getValString();
         if (mode.equalsIgnoreCase("Matrix 6.0.4")) {
            if (mc.player.onGround) {
               mc.player.jump();
            } else {
               if (mc.player.ticksExisted % 5 == 0) {
                  mc.player.jumpMovementFactor = 0.0F;
                  mc.timer.timerSpeed = 0.6F;
               }

               if (mc.player.ticksExisted % 5 == 0) {
                  mc.player.jumpMovementFactor = 0.28F;
                  mc.timer.timerSpeed = 1.0F;
               }

               if (mc.player.ticksExisted % 10 == 0) {
                  mc.player.jumpMovementFactor = 0.38F;
               }

               if (mc.player.ticksExisted % 20 == 0) {
                  mc.player.jumpMovementFactor = 0.35F;
                  mc.timer.timerSpeed = 1.1F;
               }
            }
         }

         if (mode.equalsIgnoreCase("OnGround")) {
            MovementUtil.setSpeed(9.0D);
         }
      }

   }
}
