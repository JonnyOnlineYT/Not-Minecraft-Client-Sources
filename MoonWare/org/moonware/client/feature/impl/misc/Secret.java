/* 3eLeHyy#0089 */

package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.server.SPacketKeepAlive;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;

import java.util.Random;

public class Secret extends Feature {
    public long StopDecompilingClientFaggot;
    public BooleanSetting matrixDestruction = new BooleanSetting("Matrix Destruction", false, () -> true);

    public Secret() {
        super("Secret", "Test", Type.Other);
        addSettings(matrixDestruction);
    }

    public void xyesos(double e) {
        for (int i = 0; i < i + 1; i++)
            xyesos(e * Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)), Math.pow(Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), Math.pow(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)))));
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {

    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        Minecraft.player.connection.sendPacket(new CPacketKeepAlive(0));
        if (Minecraft.player.ticksExisted % 3 == 0) {
            Minecraft.player.connection.sendPacket(new CPacketInput());
        }
        if (matrixDestruction.getBoolValue()) {
            if (Minecraft.player.ticksExisted % 6 == 0) {
                Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        StopDecompilingClientFaggot = new Random().nextInt(10);
        if (event.getPacket() instanceof SPacketKeepAlive)
            try {
                Thread.sleep(50L * StopDecompilingClientFaggot);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C00Handshake) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketClientSettings) {
            event.setCancelled(true);
        }
    }
}
