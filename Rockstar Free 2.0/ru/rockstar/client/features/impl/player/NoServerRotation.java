package ru.rockstar.client.features.impl.player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class NoServerRotation extends Feature {

        public NoServerRotation() {
            super("NoServerRotation", "Убирает ротацию со стороны сервера",0, Category.PLAYER);
        }

        @EventTarget
        public void onReceivePacket(EventReceivePacket event) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                packet.yaw = mc.player.rotationYaw;
                packet.pitch = mc.player.rotationPitch;
            }
        }
    }
