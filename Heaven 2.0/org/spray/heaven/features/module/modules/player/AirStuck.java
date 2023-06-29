package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "AirStuck", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AirStuck extends Module {
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.player.motionX = 0;
        mc.player.motionY = 0;
        mc.player.motionZ = 0;
    }

    @EventTarget
    public void onReceivePacket(PacketEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            event.setCancelled(true);
        }
    }


    @EventTarget
    public void onSendPacket(PacketEvent event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer.Position) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction) {
            event.setCancelled(true);
        }
    }
}
