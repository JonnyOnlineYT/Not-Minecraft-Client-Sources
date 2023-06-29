package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventStep;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class Step extends Module {
   boolean isStep = false;
   double jumpGround = 0.0D;
   double stepY = 0.0D;
   double stepX = 0.0D;
   double stepZ = 0.0D;
   int ticks = 2;

   public Step() {
      super("Step", Category.Movement);
      Main.instance.setmgr.rSetting(new Setting("Height", this, 1.0D, 1.0D, 3.0D, false));
      Main.instance.setmgr.rSetting(new Setting("Timer", this, 1.0D, 0.01D, 1.0D, false));
   }

   @EventTarget
   public void onEventStep(EventStep event) {
      mc.player.stepHeight = 0.5F;
      event.setStepHeight((float)Main.instance.setmgr.getSettingByName("Height").getValDouble());
      if (this.ticks == 1) {
         mc.timer.timerSpeed = 1.0F;
         this.ticks = 2;
      }

      if ((double)event.getStepHeight() > 0.625D) {
         this.isStep = true;
         this.stepY = mc.player.posY;
         this.stepX = mc.player.posX;
         this.stepZ = mc.player.posZ;
      }

   }

   @EventTarget
   public void onStepConfirm(EventStep event) {
      if (mc.player.getEntityBoundingBox().minY - this.stepY > 0.625D) {
         this.fakeJump();
         mc.getConnection().sendPacket(new CPacketPlayer.Position(this.stepX, this.stepY + 0.41999998688698D, this.stepZ, false));
         mc.getConnection().sendPacket(new CPacketPlayer.Position(this.stepX, this.stepY + 0.7531999805212D, this.stepZ, false));
         this.isStep = false;
         mc.timer.timerSpeed = (float)Main.instance.setmgr.getSettingByName("Timer").getValDouble();
         this.ticks = 1;
      }

   }

   public void fakeJump() {
   }

   public void onDisable() {
      super.onDisable();
      mc.player.stepHeight = 0.625F;
   }
}
