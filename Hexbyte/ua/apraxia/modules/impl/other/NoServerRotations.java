package ua.apraxia.modules.impl.other;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.packet.EventReceivePacket;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.utility.Utility;

public class NoServerRotations extends Module {

    public NoServerRotations() {
        super("NoRotate", Categories.Other);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = Utility.mc.player.rotationYaw;
            packet.pitch = Utility.mc.player.rotationPitch;
        }
    }
}