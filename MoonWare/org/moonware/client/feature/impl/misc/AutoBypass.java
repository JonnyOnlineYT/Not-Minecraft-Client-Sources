/* 3eLeHyy#0089 */

package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class AutoBypass extends Feature {

    public static NumberSetting delay;
    private final List<Packet<?>> packets = new CopyOnWriteArrayList<>();
    private final TimerHelper timerHelper = new TimerHelper();
    public long StopDecompilingClientFaggot;
    public ListSetting mode = new ListSetting("Disabler Mode", "PingFreeze", () -> true, "PingFreeze","MatrixTest", "Destruction", "LimitNet");

    public AutoBypass() {
        super("Auto Bypass", "Отключает некоторые чеки анти читов", Type.Other);
        delay = new NumberSetting("PingFreezeDelay", 1000, 0, 3000, 0.1F, () -> mode.currentMode.equals("PingFreeze"), NumberSetting.NumberType.MS);
        addSettings(delay);
        addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        setSuffix(mode.currentMode);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mode.currentMode.equals("MatrixTest")) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook posLook = new SPacketPlayerPosLook();
                posLook.yaw = 90;
            }
        } else if (mode.currentMode.equals("PingFreeze")) {
            if (Minecraft.player.ticksExisted % 100 != 0) {
                if (event.getPacket() instanceof SPacketKeepAlive) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof SPacketCustomPayload) {
                    event.setCancelled(true);
                }
            } else if (mode.currentMode.equals("LimitNet")) {
                StopDecompilingClientFaggot = new Random().nextInt(10);
                if (event.getPacket() instanceof SPacketKeepAlive)
                    try {
                        Thread.sleep(150L * StopDecompilingClientFaggot);
                    } catch (Exception exception) {

                    }
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (timerHelper.hasReached(delay.getNumberValue()) && mode.currentMode.equals("PingFreeze")) {
            if (event.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer cPacketPlayer = (CPacketPlayer) event.getPacket();
                cPacketPlayer.x = Minecraft.player.posX;
                cPacketPlayer.y = Minecraft.player.posY;
                cPacketPlayer.z = Minecraft.player.posZ;
                Minecraft.player.posX = Minecraft.player.prevPosX;
                Minecraft.player.posY = Minecraft.player.prevPosY;
                Minecraft.player.posZ = Minecraft.player.prevPosZ;
                timerHelper.reset();
                if (Minecraft.player.ticksExisted % 100 != 0) {
                    if (event.getPacket() instanceof C00Handshake) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof CPacketClientSettings) {
                        event.setCancelled(true);
                    }
                } else if (mode.currentMode.equals("Destruction")) {
                    if (event.getPacket() instanceof C00Handshake) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof SPacketKeepAlive) {
                        event.setCancelled(true);
                    }
                    if (event.getPacket() instanceof CPacketClientSettings) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}