package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.*;
import net.minecraft.init.MobEffects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.EnumSkyBlock;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.event.events.impl.render.EventRenderEntity;
import org.moonware.client.event.events.impl.render.EventRenderWorldLight;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.BooleanSetting;

public class NoRender extends Feature {

    public static BooleanSetting rain;
    public static BooleanSetting hurt;
    public static BooleanSetting pumpkin;
    public static BooleanSetting armor;
    public static BooleanSetting totem;
    public static BooleanSetting blindness;
    public static BooleanSetting cameraSmooth;
    public static BooleanSetting cameraBounds;
    public static BooleanSetting fire;
    public static BooleanSetting light;
    public static BooleanSetting fog;
    public static BooleanSetting armorStand;
    public static BooleanSetting bossBar;
    public static BooleanSetting tnt;
    public static BooleanSetting crystal;
    public static BooleanSetting fireworks;
    public static BooleanSetting swing;
    public static BooleanSetting sign;
    public static BooleanSetting frame;
    public static BooleanSetting banner;
    public static BooleanSetting glintEffect;
    public static BooleanSetting chatRect;

    public NoRender() {
        super("NoOverlay", "Убирает опредленные элементы рендера в игре", Type.Visuals);
        rain = new BooleanSetting("Rain", true, () -> true);
        hurt = new BooleanSetting("HurtCamera", true, () -> true);
        pumpkin = new BooleanSetting("Pumpkin", true, () -> true);
        armor = new BooleanSetting("Armor", false, () -> true);
        totem = new BooleanSetting("Totem", true, () -> true);
        blindness = new BooleanSetting("Blindness", true, () -> true);
        cameraSmooth = new BooleanSetting("Camera Smooth", true, () -> true);
        cameraBounds = new BooleanSetting("Camera Bounds", false, () -> true);
        fire = new BooleanSetting("Fire", true, () -> true);
        light = new BooleanSetting("Light", false, () -> true);
        fog = new BooleanSetting("Fog", false, () -> true);
        armorStand = new BooleanSetting("Armor Stand", false, () -> true);
        bossBar = new BooleanSetting("Boss Bar", true, () -> true);
        tnt = new BooleanSetting("Tnt", false, () -> true);
        crystal = new BooleanSetting("Crystal", false, () -> true);
        fireworks = new BooleanSetting("FireWorks", false, () -> true);
        swing = new BooleanSetting("Swing", false, () -> true);
        sign = new BooleanSetting("Sign", false, () -> true);
        frame = new BooleanSetting("Frame", false, () -> true);
        banner = new BooleanSetting("Banner", false, () -> true);
        glintEffect = new BooleanSetting("Glint Effect", false, () -> true);
        chatRect = new BooleanSetting("Chat Rect", false, () -> false);
        addSettings(rain, hurt, pumpkin, armor, totem, blindness, cameraSmooth, fire, light, fog, armorStand, bossBar, tnt, crystal, fireworks, swing, sign, frame, banner, glintEffect);
    }

    @EventTarget
    public void onEntityRenderer(EventRenderEntity event) {
        if (!getState())
            return;
        if (event.getEntity() != null) {
            if (fireworks.getBoolValue() && event.getEntity() instanceof EntityFireworkRocket) {
                event.setCancelled(true);
            } else if (crystal.getBoolValue() && event.getEntity() instanceof EntityEnderCrystal) {
                event.setCancelled(true);
            } else if (tnt.getBoolValue() && event.getEntity() instanceof EntityTNTPrimed) {
                event.setCancelled(true);
            } else if (armorStand.getBoolValue() && event.getEntity() instanceof EntityArmorStand) {
                event.setCancelled(true);
            } else if (frame.getBoolValue() && event.getEntity() instanceof EntityItemFrame) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (banner.getBoolValue()) {
            for (TileEntity e : Minecraft.world.loadedTileEntityList) {
                if (e instanceof TileEntityBanner) {
                    Minecraft.world.removeTileEntity(e.getPos());
                }
            }
        }
        if (cameraSmooth.getBoolValue()) {
            Minecraft.gameSettings.smoothCamera = false;
        }
        if (rain.getBoolValue() && Minecraft.world.isRaining()) {
            Minecraft.world.setRainStrength(0);
            Minecraft.world.setThunderStrength(0);
        }
        if (blindness.getBoolValue() && Minecraft.player.isPotionActive(MobEffects.BLINDNESS) || Minecraft.player.isPotionActive(MobEffects.NAUSEA)) {
            Minecraft.player.removePotionEffect(MobEffects.NAUSEA);
            Minecraft.player.removePotionEffect(MobEffects.BLINDNESS);
        }
    }

    @EventTarget
    public void onWorldLight(EventRenderWorldLight event) {
        if (!getState())
            return;
        if (light.getBoolValue()) {
            if (event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
                event.setCancelled(true);
            }
        }
    }
}