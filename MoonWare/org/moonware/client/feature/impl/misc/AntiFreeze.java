package org.moonware.client.feature.impl.misc;

import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventReceivePacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;

public class AntiFreeze extends Feature {

    public static BooleanSetting clearFlag = new BooleanSetting("Clear flags", "Убирает почти все флаги ( вас так же может кикать )", true, () -> true);
    public AntiFreeze() {
        super("Anti Freeze", "Убирает заморозку игрока", Type.Other);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketEntityTeleport) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (clearFlag.getBoolValue()) {
                event.setCancelled(true);
            }
        }
        if (event.getPacket() instanceof SPacketOpenWindow) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {

        }

    }
}
