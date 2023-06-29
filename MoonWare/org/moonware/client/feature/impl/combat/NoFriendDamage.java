package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.packet.EventSendPacket;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;

public class NoFriendDamage extends Feature {

    public NoFriendDamage() {
        super("NoFriendDamage", "Не даёт ударить друга", Type.Combat);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cpacketUseEntity = (CPacketUseEntity) event.getPacket();
            if (cpacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && MoonWare.friendManager.isFriend(Minecraft.objectMouseOver.entityHit.getName())) {
                event.setCancelled(true);
            }
        }
    }
}
