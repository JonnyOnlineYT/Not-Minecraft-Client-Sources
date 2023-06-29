package ua.apraxia.modules.impl.player;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.packet.EventReceivePacket;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ModeSetting;

public class Velocity extends Module {
    public ModeSetting velocityMode = new ModeSetting("Velocity Mode",  "Packet");


    public Velocity() {
        super("Velocity", Categories.Player);
        addSetting(velocityMode);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (velocityMode.is("Packet")) {
            if ((event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) &&
                    ((SPacketEntityVelocity)event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                event.setCancelled(true);
            }
            // } if (velocityMode.is("Matrix")) {
            //     if (mc.player.hurtTime > 8) {
            //         mc.player.onGround = true;
            //    }
        }
    }
}