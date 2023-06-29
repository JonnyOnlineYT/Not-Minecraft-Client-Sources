package me.wintware.client.module.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.util.MovementInput;

public class Strafe extends Module {
   public static Setting speed;

   public Strafe() {
      super("Strafe", Category.Movement);
      Main.instance.setmgr.rSetting(speed = new Setting("Speed", this, 50.0D, 20.0D, 100.0D, false));
   }

   @EventTarget
   public void onUpdate(EventPreMotionUpdate e) {
      double speed1 = speed.getValDouble();
      this.setDisplayName("Strafe " + ChatFormatting.WHITE + speed1 + "%");
      if (!mc.player.isInWater()) {
         MovementInput movementInput = mc.player.movementInput;
         float forward = MovementInput.moveForward;
         float strafe = MovementInput.moveStrafe;
         float dir = mc.player.rotationYaw;
         if (forward == 0.0F && strafe == 0.0F) {
            mc.player.motionX = 0.0D;
            mc.player.motionZ = 0.0D;
         } else if (forward != 0.0F) {
            if (strafe >= 1.0F) {
               dir += (float)(forward > 0.0F ? -45 : 45);
               strafe = 0.0F;
            } else if (strafe <= -1.0F) {
               dir += (float)(forward > 0.0F ? 45 : -45);
               strafe = 0.0F;
            }

            if (forward > 0.0F) {
               forward = 1.0F;
            } else if (forward < 0.0F) {
               forward = -1.0F;
            }
         }

         double da = 0.004079999999999999D * speed.getValDouble();
         if (mc.player.isSprinting()) {
            da *= 1.3190000119209289D;
         }

         if (mc.player.isSneaking()) {
            da *= 0.3D;
         }

         if (mc.player.onGround) {
            da *= 0.011000000000000001D * speed.getValDouble();
         }

         float var9 = (float)((double)((float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * da);
         float zD = (float)((double)((float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * da);
         if ((mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed) && !mc.gameSettings.keyBindBack.pressed) {
            mc.player.motionX = (double)var9;
            mc.player.motionZ = (double)zD;
         }
      }

   }
}
