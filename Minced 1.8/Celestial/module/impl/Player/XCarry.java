package Celestial.module.impl.Player;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.packet.EventSendPacket;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import net.minecraft.network.play.client.CPacketCloseWindow;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry", ModuleCategory.Player);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            event.setCancelled(true);
        }
    }
}