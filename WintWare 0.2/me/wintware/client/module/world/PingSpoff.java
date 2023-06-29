package me.wintware.client.module.world;

import java.util.ArrayList;
import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.client.CPacketKeepAlive;

public class PingSpoff extends Module {
   ArrayList<CPacketKeepAlive> packets;

   public PingSpoff() {
      super("PingSpoof", Category.World);
      Main.instance.setmgr.rSetting(new Setting("Ping", this, 15000.0D, 0.0D, 30000.0D, false));
   }

   @EventTarget
   public void onSendPacket(EventSendPacket e) {
      int ping = (int)Main.instance.setmgr.getSettingByName("Ping").getValDouble();
      if (e.getPacket() instanceof CPacketKeepAlive && mc.player.isEntityAlive()) {
         e.setCancelled(true);
         if (!this.packets.isEmpty()) {
            e.setCancelled(false);
            Runnable run = () -> {
               try {
                  Iterator var3 = this.packets.iterator();

                  while(var3.hasNext()) {
                     CPacketKeepAlive packetAye = (CPacketKeepAlive)var3.next();
                     Thread.sleep((long)ping);
                     mc.getConnection().sendPacket(packetAye);
                     this.packets.remove(packetAye);
                  }

                  this.packets.clear();
                  e.setCancelled(true);
               } catch (Exception var5) {
               }

            };
            Thread thread = new Thread(run, "ping");
            thread.start();
         }

         Runnable run = () -> {
            try {
               Thread.sleep(20L);
            } catch (InterruptedException var2) {
            }

            if (mc.player != null) {
               mc.player.connection.sendPacket(e.getPacket());
               e.setCancelled(true);
            }

         };
         Thread thread = new Thread(run, "toSave");
         thread.start();
      }

   }
}
