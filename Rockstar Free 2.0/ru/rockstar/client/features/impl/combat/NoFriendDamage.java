package ru.rockstar.client.features.impl.combat;

import net.minecraft.network.play.client.CPacketUseEntity;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPacket;
import ru.rockstar.api.event.event.EventSendPacket;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class NoFriendDamage extends Feature {

    public NoFriendDamage() {
        super("NoFriendDamage", "Не даёт ударить друга",0, Category.COMBAT);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
    	if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cpacketUseEntity = (CPacketUseEntity) event.getPacket();
            if (cpacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && Main.instance.friendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                event.setCancelled(true);
            }
        }
    }
}
