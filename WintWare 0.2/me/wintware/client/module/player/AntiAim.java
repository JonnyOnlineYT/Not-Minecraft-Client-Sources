package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class AntiAim extends Module {
   private float pitch;

   public AntiAim() {
      super("AntiAim", Category.Player);
   }

   @EventTarget
   public void onPreMotion(EventPreMotionUpdate event) {
      this.pitch += 30.0F;
      if (this.pitch > 90.0F) {
         this.pitch = -90.0F;
      } else if (this.pitch < -90.0F) {
         this.pitch = 90.0F;
      }

      this.pitch = (float)(mc.player.ticksExisted % 2 == 0 ? 90 : -90);
      event.setPitch(this.pitch);
      event.setYaw(this.pitch);
      this.pitch -= 22.0F;
      if (this.pitch <= -180.0F) {
         this.pitch = 180.0F;
         mc.player.renderYawOffset = this.pitch;
         mc.player.rotationYawHead = this.pitch;
         mc.player.connection.sendPacket(new CPacketPlayer.Rotation(this.pitch, mc.player.rotationPitch, false));
         event.setYaw(this.pitch);
      }

   }
}
