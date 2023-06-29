package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class NoRotate extends Feature {

    public NoRotate() {
        super("NoServerRotation", "Убирает ротацию со стороны сервера", Type.Other);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = Minecraft.player.rotationYaw;
            packet.pitch = Minecraft.player.rotationPitch;
        }
    }
}
