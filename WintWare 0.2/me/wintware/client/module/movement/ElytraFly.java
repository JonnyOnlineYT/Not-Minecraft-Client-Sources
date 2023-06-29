package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ElytraFly extends Module {
   Setting motion;

   public ElytraFly() {
      super("ElytraFly", Category.Movement);
      Main.instance.setmgr.rSetting(this.motion = new Setting("Speed", this, 30.0D, 20.0D, 100.0D, false));
   }

   @EventTarget
   public void onUpdateLiving(EventUpdateLiving e) {
      ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
      if (chest != null && chest.getItem() == Items.ELYTRA) {
         if (mc.player.isElytraFlying()) {
            mc.player.motionY = 0.0D;
            mc.player.capabilities.isFlying = true;
            mc.player.capabilities.setFlySpeed(0.6F);
            MovementUtil.setSpeed(0.4D);
            float yaw;
            EntityPlayerSP player5;
            EntityPlayerSP player6;
            if (mc.gameSettings.keyBindForward.pressed && mc.player.getPosition().getY() < 256) {
               yaw = (float)Math.toRadians((double)mc.player.rotationYaw);
               player5 = mc.player;
               player5.motionX -= Math.sin((double)yaw) * 0.05000000074505806D / 100.0D * this.motion.getValDouble();
               player6 = mc.player;
               player6.motionZ += Math.cos((double)yaw) * 0.05000000074505806D / 100.0D * this.motion.getValDouble();
            } else if (mc.gameSettings.keyBindBack.pressed && mc.player.getPosition().getY() < 256) {
               yaw = (float)Math.toRadians((double)mc.player.rotationYaw);
               player5 = mc.player;
               player5.motionX += Math.sin((double)yaw) * 0.05000000074505806D;
               player6 = mc.player;
               player6.motionZ -= Math.cos((double)yaw) * 0.05000000074505806D;
            }
         }

      }
   }
}
