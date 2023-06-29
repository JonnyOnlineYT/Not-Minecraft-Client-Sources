package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventReceivePacket;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class NoServerRotations extends Module {

    public NoServerRotations() {
        super("NoRotate", "Отключает ротации сервера", ModuleCategory.Player);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = Helper.mc.player.rotationYaw;
            packet.pitch = Helper.mc.player.rotationPitch;
        }
    }
}