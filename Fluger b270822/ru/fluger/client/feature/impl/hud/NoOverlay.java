package ru.fluger.client.feature.impl.hud;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.MobEffects;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;

public class NoOverlay extends Feature {
   public static BooleanSetting rain = new BooleanSetting("Rain", true, () -> {
      return true;
   });
   public static BooleanSetting noHurtCam = new BooleanSetting("HurtCam", true, () -> {
      return true;
   });
   public static BooleanSetting cameraClip = new BooleanSetting("Camera Clip", true, () -> {
      return true;
   });
   public static BooleanSetting antiTotem = new BooleanSetting("AntiTotemAnimation", false, () -> {
      return true;
   });
   public static BooleanSetting noFire = new BooleanSetting("NoFireOverlay", false, () -> {
      return true;
   });
   public static BooleanSetting noBossBar = new BooleanSetting("NoBossBar", false, () -> {
      return true;
   });
   public static BooleanSetting noArmorStand = new BooleanSetting("ArmorStand", false, () -> {
      return true;
   });
   public static BooleanSetting blindness = new BooleanSetting("Blindness", true, () -> {
      return true;
   });

   public NoOverlay() {
      super("NoRender", "������� ����������� �������� ������� � ����", Type.Hud);
      this.addSettings(new Setting[]{rain, noArmorStand, noHurtCam, cameraClip, antiTotem, noFire, blindness, noBossBar});
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (rain.getCurrentValue() && mc.world.isRaining()) {
         mc.world.setRainStrength(0.0F);
         mc.world.setThunderStrength(0.0F);
      }

      if (blindness.getCurrentValue() && mc.player.isPotionActive(MobEffects.BLINDNESS) || mc.player.isPotionActive(MobEffects.NAUSEA)) {
         mc.player.removePotionEffect(MobEffects.NAUSEA);
         mc.player.removePotionEffect(MobEffects.BLINDNESS);
      }

      if (noArmorStand.getCurrentValue()) {
         if (mc.player == null || mc.world == null) {
            return;
         }

         Iterator var2 = mc.world.loadedEntityList.iterator();

         while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if (entity != null && entity instanceof EntityArmorStand) {
               mc.world.removeEntity(entity);
            }
         }
      }

   }
}
