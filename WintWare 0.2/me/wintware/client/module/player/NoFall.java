package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {
   public NoFall() {
      super("NoFall", Category.Player);
   }

   @EventTarget
   public void onPacket(EventUpdate event) {
      if (mc.player.isAirBorne && mc.player.fallDistance > 3.0F) {
         EntityPlayerSP var10000 = mc.player;
         var10000.motionY -= 0.9649999737739563D;
         mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
      }

   }
}
