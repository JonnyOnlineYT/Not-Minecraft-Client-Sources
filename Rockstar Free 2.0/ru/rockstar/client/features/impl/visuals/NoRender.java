package ru.rockstar.client.features.impl.visuals;

import net.minecraft.init.MobEffects;
import net.minecraft.world.EnumSkyBlock;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRenderWorldLight;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;

public class NoRender extends Feature {
    public static BooleanSetting hurtcam;
    public static BooleanSetting cameraClip;
    public static BooleanSetting antiTotem;
    public static BooleanSetting noFire;
    public static BooleanSetting noPotion;
    public static BooleanSetting noExp;
    public static BooleanSetting noPumpkin;
    public static BooleanSetting chatRect;
    public static BooleanSetting rain;
    public static BooleanSetting fog;
    public static BooleanSetting noBoss;
    public static BooleanSetting light;
    public static BooleanSetting blindness;
    public static BooleanSetting noArrow;
    public static BooleanSetting noArmor;
    public static BooleanSetting glintEffect;
    public static BooleanSetting itemGui;

    public NoRender() {
        super("NoRender", "Убирает опредленные элементы рендера в игре", 0, Category.VISUALS);
        rain = new BooleanSetting("Rain", true, () -> true);
        hurtcam = new BooleanSetting("HurtCam", true, () -> true);
        cameraClip = new BooleanSetting("Camera Clip", true, () -> true);
        antiTotem = new BooleanSetting("AntiTotemAnimation", false, () -> true);
        noFire = new BooleanSetting("NoFireOverlay", false, () -> true);
        noPotion = new BooleanSetting("NoPotionDebug", false, () -> true);
        noExp = new BooleanSetting("NoExpBar", false, () -> true);
        fog = new BooleanSetting("Fog", false, () -> true);
        noPumpkin = new BooleanSetting("NoPumpkinOverlay", false, () -> true);
        chatRect = new BooleanSetting("Chat Rect", false, () -> true);
        light = new BooleanSetting("Light", false, () -> true);
        blindness = new BooleanSetting("Blindness", true, () -> true);
        noBoss = new BooleanSetting("NoBossBar", false, () -> true);
        glintEffect = new BooleanSetting("Glint Effect", false, () -> true);
        noArrow = new BooleanSetting("NoArrowInPlayer", false, () -> true);
        noArmor = new BooleanSetting("NoArmor", false, () -> true);
        itemGui = new BooleanSetting("Item Gui", false, () -> true);
        addSettings(rain, fog, hurtcam, cameraClip, antiTotem, noArmor, noFire,chatRect, blindness, light, noPotion, noExp, noPumpkin, noBoss,glintEffect, noArrow, itemGui);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        if (blindness.getBoolValue() && mc.player.isPotionActive(MobEffects.BLINDNESS) || mc.player.isPotionActive(MobEffects.NAUSEA)) {
            mc.player.removePotionEffect(MobEffects.NAUSEA);
            mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
    }

    @EventTarget
    public void onWorldLight(EventRenderWorldLight event) {
        if (!isToggled())
            return;
        if (light.getBoolValue()) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.setCancelled(true);
            }
        }
    }
}
