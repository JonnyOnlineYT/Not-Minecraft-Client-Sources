package ua.apraxia.modules.impl.world;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.utility.Utility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.MobEffects;

public class NoRenderOverlay extends Module {
    public static BooleanSetting rain = new BooleanSetting("Rain", true);
    public static BooleanSetting noHurtCam = new BooleanSetting("Hurt Cam", true);
    public static BooleanSetting antiTotem = new BooleanSetting("Anti Totem Animation", false);
    public static BooleanSetting noFire = new BooleanSetting("No Fire Overlay", false);
    public static BooleanSetting noBossBar = new BooleanSetting("No Boss Bar", false);
    public static BooleanSetting noArmorStand = new BooleanSetting("Armor Stand", false);
    public static BooleanSetting blindness = new BooleanSetting("Blindness", true);
    public static BooleanSetting cameraClip = new BooleanSetting("Camera Clip", true);
    public NoRenderOverlay() {
        super("NoRenderOverlay", Categories.World);
        addSetting(rain, noArmorStand, noHurtCam, antiTotem, noFire, blindness, noBossBar, cameraClip);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (rain.value && Utility.mc.world.isRaining()) {
            Utility.mc.world.setRainStrength(0);
            Utility.mc.world.setThunderStrength(0);
        }
        if (blindness.value && Utility.mc.player.isPotionActive(MobEffects.BLINDNESS) || Utility.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            Utility.mc.player.removePotionEffect(MobEffects.NAUSEA);
            Utility.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (noArmorStand.value) {
            if (Utility.mc.player == null || Utility.mc.world == null) {
                return;
            }
            for (Entity entity : Utility.mc.world.loadedEntityList) {
                if (entity == null || !(entity instanceof EntityArmorStand)) continue;
                Utility.mc.world.removeEntity(entity);
            }
        }
    }

}
