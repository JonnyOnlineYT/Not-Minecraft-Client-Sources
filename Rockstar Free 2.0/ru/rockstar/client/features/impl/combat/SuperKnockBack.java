package ru.rockstar.client.features.impl.combat;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventAttackPacket;
import ru.rockstar.api.event.event.EventSendPacket;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class SuperKnockBack extends Feature {
    public SuperKnockBack() {
        super("ExtendedKnockBack", "Вы откидываете противника дальше",0, Category.COMBAT);
    }
    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                mc.player.setSprinting(false);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                mc.player.setSprinting(true);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            }
        }
    }

}
