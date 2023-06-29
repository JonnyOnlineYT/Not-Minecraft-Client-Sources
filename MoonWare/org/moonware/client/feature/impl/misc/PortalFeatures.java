package org.moonware.client.feature.impl.misc;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;

public class PortalFeatures extends Feature {

    public static BooleanSetting chat = new BooleanSetting("Chat", true, () -> true);
    public static BooleanSetting cancelTeleport = new BooleanSetting("Cancel Teleport", true, () -> true);

    public PortalFeatures() {
        super("PortalFeatures", "Позволяет открыть чат в портале", Type.Other);
        addSettings(chat, cancelTeleport);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport && cancelTeleport.getBoolValue()) {
            event.setCancelled(true);
        }
    }
}
