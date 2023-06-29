package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoClip extends Module {
   private float gamma;

   public NoClip() {
      super("NoClip", Category.Player);
   }

   @EventTarget
   public void onLivingUpdate(EventUpdateLiving event) {
      EntityPlayerSP var10000;
      if (mc.player != null) {
         mc.player.noClip = true;
         mc.player.motionY = 0.0D;
         mc.player.onGround = false;
         mc.player.capabilities.isFlying = false;
         if (mc.gameSettings.keyBindJump.isKeyDown()) {
            var10000 = mc.player;
            var10000.motionY += 0.5D;
         }

         if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            var10000 = mc.player;
            var10000.motionY -= 0.5D;
         }
      } else {
         float f = mc.player.rotationYaw;
         if (mc.player.moveForward < 0.0F) {
            f += 180.0F;
         }

         if (mc.player.moveStrafing > 0.0F) {
            f -= 90.0F * (mc.player.moveForward < 0.0F ? -0.5F : (mc.player.moveForward > 0.0F ? 0.5F : 1.0F));
         }

         if (mc.player.moveStrafing < 0.0F) {
            float var7 = f + 90.0F * (mc.player.moveForward < 0.0F ? -0.5F : (mc.player.moveForward > 0.0F ? 0.5F : 1.0F));
         }

         double d = (double)mc.player.getHorizontalFacing().getDirectionVec().getX() * 1.273197475E-15D;
         double d2 = (double)mc.player.getHorizontalFacing().getDirectionVec().getZ() * 1.273197475E-15D;
         mc.player.motionY = 0.0D;
         if (mc.gameSettings.keyBindJump.isKeyDown()) {
            var10000 = mc.player;
            var10000.motionY += 4.24399158E-15D;
         }

         if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            var10000 = mc.player;
            var10000.motionY -= 4.24399158E-15D;
         }

         if (mc.player.isCollidedVertically) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.273197475E-14D, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + d * 0.0D, mc.player.posY, mc.player.posZ + d2 * 0.0D, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
         }
      }

   }

   public void onDisable() {
      super.onDisable();
      mc.player.onGround = false;
   }
}
