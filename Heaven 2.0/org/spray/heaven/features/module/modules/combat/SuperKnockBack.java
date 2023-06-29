package org.spray.heaven.features.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;


@ModuleInfo(name = "SuperKnockBack", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class SuperKnockBack extends Module {

    @EventTarget
    public void onSendPacket(PacketEvent event) {
        if (event.getType() == EventType.SEND) {
            if (event.getPacket() instanceof CPacketUseEntity) {
                CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
                if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                    mc.player.setSprinting(false);
                    mc.player.connection.sendPacket(
                            new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    mc.player.setSprinting(true);
                    mc.player.connection.sendPacket(
                            new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }
            }
        }
    }

}
