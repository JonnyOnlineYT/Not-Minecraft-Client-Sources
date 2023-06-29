package me.wintware.client.module.movement;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.entity.EntityPlayerSP;

public class Fly extends Module {
   Setting speed;

   public Fly() {
      super("Fly", Category.Movement);
      ArrayList<String> mode = new ArrayList();
      mode.add("Vanilla");
      mode.add("Motion");
      Main.instance.setmgr.rSetting(new Setting("Fly Mode", this, "Vanilla", mode));
      Main.instance.setmgr.rSetting(this.speed = new Setting("Speed", this, 1.0D, 0.1D, 15.0D, false));
   }

   public void onDisable() {
      super.onDisable();
      mc.timer.timerSpeed = 1.0F;
      mc.player.capabilities.isFlying = false;
   }

   public void onEnable() {
      super.onEnable();
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      String mode = Main.instance.setmgr.getSettingByName("Fly Mode").getValString();
      if (mode.equalsIgnoreCase("Motion")) {
         mc.player.fallDistance = 0.0F;
         mc.player.motionX = 0.0D;
         mc.player.motionY = 0.0D;
         mc.player.motionZ = 0.0D;
         EntityPlayerSP thePlayer3 = mc.player;
         thePlayer3.posY += 0.1D;
         EntityPlayerSP thePlayer4 = mc.player;
         thePlayer4.posY -= 0.1D;
         MovementUtil.setSpeed(this.speed.getValDouble());
         EntityPlayerSP thePlayer6;
         if (mc.gameSettings.keyBindJump.isKeyDown()) {
            thePlayer6 = mc.player;
            thePlayer6.motionY += 0.5D;
         }

         if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            thePlayer6 = mc.player;
            thePlayer6.motionY -= 0.5D;
         }
      }

      if (mode.equalsIgnoreCase("Vanilla")) {
         mc.player.capabilities.isFlying = true;
         mc.player.capabilities.setFlySpeed((float)this.speed.getValDouble());
      }

   }
}
