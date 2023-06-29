package ru.rockstar.client.features.impl.player;

import net.minecraft.network.play.client.CPacketCloseWindow;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventSendPacket;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class XCarry extends Feature {

    public XCarry() {
        super("XCarry", "Позволяет хранить предметы в слотах для крафта",0, Category.PLAYER);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            event.setCancelled(true);
        }
    }
}
