package me.wintware.client.module.combat;

import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.combat.RotationUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class AimAssist extends Module {
   Setting range;
   public float[] facing;

   public AimAssist() {
      super("AimAssist", Category.Combat);
      Main.instance.setmgr.rSetting(this.range = new Setting("Range", this, 4.0D, 3.0D, 7.0D, false));
   }

   @EventTarget
   public void onPreMotion(EventPreMotionUpdate event) {
      Iterator var2 = mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Object theObject = var2.next();
         if (theObject instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)theObject;
            if (!(entity instanceof EntityPlayerSP)) {
               this.facing = faceTarget(entity, 360.0F, 360.0F, false);
               if ((double)mc.player.getDistanceToEntity(entity) < this.range.getValDouble()) {
                  float f = this.facing[0];
                  float f2 = this.facing[1];
                  event.setYaw(f);
                  mc.player.rotationYaw = f;
                  mc.player.rotationPitch = f2 - -15.0F;
                  EntityPlayerSP var10000;
                  if (mc.player.rotationYaw < f) {
                     var10000 = mc.player;
                     var10000.rotationYaw += 0.5F;
                  }

                  if (mc.player.rotationYaw > f) {
                     var10000 = mc.player;
                     var10000.rotationYaw -= 0.5F;
                  }

                  if (mc.player.rotationPitch < f2) {
                     var10000 = mc.player;
                     var10000.rotationPitch += 0.5F;
                  }

                  if (mc.player.rotationPitch > f2) {
                     var10000 = mc.player;
                     var10000.rotationPitch -= 0.5F;
                  }
               }
            }
         }
      }

   }

   public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
      double var4 = target.posX - mc.player.posX;
      double var5 = target.posZ - mc.player.posZ;
      double var7;
      if (target instanceof EntityLivingBase) {
         EntityLivingBase var6 = (EntityLivingBase)target;
         var7 = var6.posY + (double)var6.getEyeHeight() - (mc.player.posY + (double)mc.player.getEyeHeight());
      } else {
         var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0D - (mc.player.posY + (double)mc.player.getEyeHeight());
      }

      double var8 = (double)MathHelper.sqrt(var4 * var4 + var5 * var5);
      float var9 = (float)(Math.atan2(var5, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(Math.atan2(var7 - (target instanceof EntityPlayer ? 0.25D : 0.0D), var8) * 180.0D / 3.141592653589793D));
      float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
      float gcd = f * f * f * 1.2F;
      float pitch = RotationUtil.updateRotation(mc.player.rotationPitch, var10, p_706253);
      float yaw = RotationUtil.updateRotation(mc.player.rotationYaw, var9, p_706252);
      yaw -= yaw % gcd;
      pitch -= pitch % gcd;
      return new float[]{yaw, pitch};
   }
}
