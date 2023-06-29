package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.client.CPacketUseEntity;

public class AutoShift extends Module {
   public AutoShift() {
      super("AutoShift", Category.Combat);
   }

   @EventTarget
   public void onSendPacket(EventSendPacket event) {
      if (event.getPacket() instanceof CPacketUseEntity) {
         CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
         if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && mc.gameSettings.keyBindSneak.pressed) {
         }
      }

   }
}
