package org.moonware.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class XCarry extends Feature {

    public XCarry() {
        super("XCarry", "Позволяет хранить предметы в слотах для крафта", Type.Other);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            event.setCancelled(true);
        }
    }
}
