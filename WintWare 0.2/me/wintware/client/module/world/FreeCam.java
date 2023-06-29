package me.wintware.client.module.world;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdateLiving;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.GameSettings;

public class FreeCam extends Module {
   private float yaw;
   private float pitch;
   private float yawHead;
   private float gamma;
   private EntityOtherPlayerMP other;
   private float old;
   public Setting speed;

   public FreeCam() {
      super("FreeCam", Category.World);
      Main.instance.setmgr.rSetting(this.speed = new Setting("Speed", this, 0.1D, 0.01D, 10.0D, false));
   }

   public void onDisable() {
      mc.player.capabilities.isFlying = false;
      mc.player.capabilities.setFlySpeed(this.old);
      mc.player.setPosition(this.other.posX, this.other.posY, this.other.posZ);
      mc.player.rotationPitch = this.pitch;
      mc.player.rotationYaw = this.yaw;
      mc.world.removeEntityFromWorld(-1);
      mc.player.noClip = false;
      mc.renderGlobal.loadRenderers();
      mc.gameSettings.gammaSetting = this.gamma;
      super.onDisable();
   }

   public void onEnable() {
      this.old = mc.player.capabilities.getFlySpeed();
      mc.player.capabilities.isFlying = true;
      if (mc.world != null && mc.player != null) {
         EntityOtherPlayerMP player = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
         this.yaw = mc.player.rotationYaw;
         this.pitch = mc.player.rotationPitch;
         this.yawHead = mc.player.rotationYawHead;
         player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
         player.rotationYaw = mc.player.rotationYaw;
         player.rotationPitch = mc.player.rotationPitch;
         player.rotationYawHead = mc.player.rotationYawHead;
         player.setSneaking(mc.player.isSneaking());
         player.inventory.copyInventory(mc.player.inventory);
         mc.world.addEntityToWorld(-1, player);
         this.other = player;
         this.gamma = mc.gameSettings.gammaSetting;
         super.onEnable();
      }

   }

   @EventTarget
   public void g(EventUpdateLiving e) {
      mc.player.noClip = true;
      mc.player.onGround = false;
      mc.player.capabilities.setFlySpeed((float)this.speed.getValDouble());
      mc.player.capabilities.isFlying = true;
      if (!mc.player.isInsideOfMaterial(Material.AIR) && !mc.player.isInsideOfMaterial(Material.LAVA) && !mc.player.isInsideOfMaterial(Material.WATER)) {
         if (mc.gameSettings.gammaSetting < 100.0F) {
            GameSettings var10000 = mc.gameSettings;
            var10000.gammaSetting += 0.08F;
         }
      } else {
         mc.gameSettings.gammaSetting = this.gamma;
      }

   }
}
