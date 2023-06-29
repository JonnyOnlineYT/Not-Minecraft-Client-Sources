package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.ListSetting;

public class NoFall extends Feature {

    public static ListSetting noFallMode;
    public TimerHelper timerHelper = new TimerHelper();

    public NoFall() {
        super("NoFall", "Позволяет получить меньший дамаг при падении", Type.Other);
        noFallMode = new ListSetting("NoFall Mode", "Vanilla", () -> true, "Vanilla", "GroundCancel", "Spartan", "AAC-Flags", "Matrix", "Hypixel");
        addSettings(noFallMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = noFallMode.getOptions();
        setSuffix(mode);
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (Minecraft.player.fallDistance > 3) {
                event.setOnGround(true);
                Minecraft.player.connection.sendPacket(new CPacketPlayer(true));
            }
        } else if (mode.equalsIgnoreCase("Spartan")) {
            if (Minecraft.player.fallDistance > 3.5f) {
                if (timerHelper.hasReached(150L)) {
                    Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, true));
                    timerHelper.reset();
                } else {
                    Minecraft.player.onGround = false;
                }
            }
        } else if (mode.equalsIgnoreCase("AAC-Flags")) {
            Minecraft.player.motionY -= 0.1;
            event.setOnGround(true);
            Minecraft.player.capabilities.disableDamage = true;
        } else if (mode.equalsIgnoreCase("Hypixel")) {
            if (Minecraft.player.fallDistance > 3.4) {
                event.setOnGround(Minecraft.player.ticksExisted % 2 == 0);
            }
        } else if (mode.equalsIgnoreCase("Matrix")) {
            if (Minecraft.player.fallDistance > 3) {
                Minecraft.player.fallDistance = (float) (Math.random() * 1.0E-12);
                Minecraft.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, true));
                Minecraft.player.fallDistance = 0;
            }
        } else if (mode.equalsIgnoreCase("GroundCancel")) {
            event.setOnGround(false);
        }
    }
}