package me.wintware.client.module.player;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class NoWeb extends Module {
   public NoWeb() {
      super("NoWeb", Category.Player);
      ArrayList<String> web = new ArrayList();
      web.add("NCP");
      web.add("Matrix");
      Main.instance.setmgr.rSetting(new Setting("NoWeb Mode", this, "Matrix", web));
   }

   @EventTarget
   public void onPreMotion(EventUpdate event) {
      if (this.getState()) {
         String mode = Main.instance.setmgr.getSettingByName("NoWeb Mode").getValString();
         if (mode.equalsIgnoreCase("Matrix")) {
            mc.player.isInWeb = false;
         }

         if (mode.equalsIgnoreCase("NCP") && mc.player.isInWeb) {
            mc.player.isInWeb = false;
            float yaw = mc.player.rotationYaw + (float)(mc.player.moveForward < 0.0F ? 180 : 0);
            float lol;
            float xd;
            if (mc.player.moveStrafing > 0.0F) {
               if (mc.player.moveForward < 0.0F) {
                  xd = -0.5F;
               } else {
                  xd = mc.player.moveForward > 0.0F ? 0.4F : 1.0F;
               }

               lol = -90.0F * xd;
            } else {
               lol = 0.0F;
            }

            xd = yaw + lol;
            float xz = (float)Math.cos((double)(xd + 90.0F) * 3.141592653589793D / 180.0D);
            float var7 = (float)Math.sin((double)(xd + 90.0F) * 3.141592653589793D / 180.0D);
         }
      }

   }
}
