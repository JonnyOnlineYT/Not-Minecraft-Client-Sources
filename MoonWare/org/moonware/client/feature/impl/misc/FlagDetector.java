package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.KillAura;
import org.moonware.client.feature.impl.combat.TargetStrafe;
import org.moonware.client.feature.impl.movement.*;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

public class FlagDetector extends Feature {

    public static BooleanSetting disable = new BooleanSetting("Disable on flag",true,() -> true);
    public FlagDetector() {
        super("FlagDetector", "Автоматически выключает модуль при его детекте", Type.Other);
        addSettings(disable);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (getState()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                if (MoonWare.featureManager.getFeatureByClass(Speed.class).getState()) {
                    featureAlert("Speed");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(Speed.class).toggle();

                } else if (MoonWare.featureManager.getFeatureByClass(Flight.class).getState()) {
                    featureAlert("Flight");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(Flight.class).toggle();
                } else if (MoonWare.featureManager.getFeatureByClass(FastClimb.class).getState() && Minecraft.player.isOnLadder() && !Minecraft.player.isUsingItem()) {
                    featureAlert("FastClimb");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(FastClimb.class).toggle();
                }else if (MoonWare.featureManager.getFeatureByClass(WallClimb.class).getState() && Minecraft.player.isCollidedHorizontally) {
                    featureAlert("Spider");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(WallClimb.class).toggle();
                }else if (MoonWare.featureManager.getFeatureByClass(TargetStrafe.class).getState() && KillAura.target != null) {
                    featureAlert("TargetStrafe");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(TargetStrafe.class).toggle();

                } else if (MoonWare.featureManager.getFeatureByClass(LongJump.class).getState()) {
                    featureAlert("LongJump");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(LongJump.class).toggle();

                } else if (MoonWare.featureManager.getFeatureByClass(LiquidWalk.class).getState() && Minecraft.player.isInLiquid()) {
                    featureAlert("LiquidWalk");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(LiquidWalk.class).toggle();

                } else if (MoonWare.featureManager.getFeatureByClass(timer.class).getState()) {
                    featureAlert("Timer");
                    if (disable.get())
                        MoonWare.featureManager.getFeatureByClass(timer.class).toggle();
                }
            }
        }
    }

    public void featureAlert(String feature) {
        NotificationManager.publicity("Flag Detector Debug", "Module " + feature  + " was flagged" + (Minecraft.player.isInWater() ? " on water" : "") + (Minecraft.player.isInLava() ? " in lava" : "") + "!", 3, NotificationType.WARNING);
    }
}