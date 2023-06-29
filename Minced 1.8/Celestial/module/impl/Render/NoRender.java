package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.ui.settings.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.MobEffects;

public class NoRender extends Module {
    public static BooleanSetting rain = new BooleanSetting("Rain", true);
    public static BooleanSetting noHurtCam = new BooleanSetting("HurtCam", true);
    public static BooleanSetting cameraClip = new BooleanSetting("Camera Clip", true);
    public static BooleanSetting antiTotem = new BooleanSetting("AntiTotemAnimation", false);
    public static BooleanSetting noFire = new BooleanSetting("NoFireOverlay", false);
    public static BooleanSetting noBossBar = new BooleanSetting("NoBossBar", false);
    public static BooleanSetting noArmorStand = new BooleanSetting("ArmorStand", false);
    public static BooleanSetting blindness = new BooleanSetting("Blindness", true);

    public NoRender() {
        super("NoRender", "Убирает рендер некоторых энтити или эффектов в игре", ModuleCategory.Render);
        addSettings(rain, noArmorStand, noHurtCam, cameraClip, antiTotem, noFire, blindness, noBossBar);

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (rain.getCurrentValue() && Helper.mc.world.isRaining()) {
            Helper.mc.world.setRainStrength(0);
            Helper.mc.world.setThunderStrength(0);
        }
        if (blindness.getCurrentValue() && Helper.mc.player.isPotionActive(MobEffects.BLINDNESS) || Helper.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            Helper.mc.player.removePotionEffect(MobEffects.NAUSEA);
            Helper.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (noArmorStand.getCurrentValue()) {
            if (Helper.mc.player == null || Helper.mc.world == null) {
                return;
            }
            for (Entity entity : Helper.mc.world.loadedEntityList) {
                if (entity == null || !(entity instanceof EntityArmorStand)) continue;
                Helper.mc.world.removeEntity(entity);
            }
        }
    }

}
