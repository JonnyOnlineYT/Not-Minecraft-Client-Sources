package me.wintware.client.module.combat;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
   Setting percentage;

   public Velocity() {
      super("Velocity", Category.Combat);
      ArrayList<String> mode = new ArrayList();
      mode.add("Packet");
      mode.add("Motion");
      Main.instance.setmgr.rSetting(this.percentage = new Setting("Percentage", this, 0.0D, 0.0D, 100.0D, false));
      Main.instance.setmgr.rSetting(this.percentage = new Setting("Velocity mode", this, "Packet", mode));
   }

   @EventTarget
   public void velocity(EventReceivePacket e) {
      String mode = Main.instance.setmgr.getSettingByName("Velocity Mode").getValString();
      if (mode.equalsIgnoreCase("Packet")) {
         Packet packet = e.getPacket();
         if (packet instanceof SPacketEntityVelocity || packet instanceof SPacketExplosion) {
            e.setCancelled(true);
            if (Main.instance.setmgr.getSettingByName("Percentage").getValDouble() < 3.0D) {
               double da = Main.instance.setmgr.getSettingByName("Percentage").getValDouble() / 100.0D;
               SPacketEntityVelocity.motionX = (int)(100.0D * da);
               SPacketEntityVelocity.motionY = (int)(100.0D * da);
               SPacketEntityVelocity.motionZ = (int)(100.0D * da);
            }
         }
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      String mode = Main.instance.setmgr.getSettingByName("Velocity Mode").getValString();
      if (mode.equalsIgnoreCase("Motion")) {
         double da = Main.instance.setmgr.getSettingByName("Percentage").getValDouble() / 100.0D;
         if (mc.player.hurtTime > 0) {
            mc.player.motionX = (double)((int)(100.0D * da));
            mc.player.motionZ = (double)((int)(100.0D * da));
            mc.player.motionY = (double)((int)(100.0D * da));
         }
      }

   }
}
