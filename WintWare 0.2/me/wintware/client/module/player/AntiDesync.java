package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class AntiDesync extends Module {
   public AntiDesync() {
      super("Anti Desync", Category.Player);
   }

   @EventTarget
   public void onPacket(EventReceivePacket event) {
      if (event.getPacket() instanceof SPacketPlayerPosLook) {
         SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
         packet.setYaw(mc.player.rotationYaw);
         packet.setPitch(mc.player.rotationPitch);
      }

   }
}
