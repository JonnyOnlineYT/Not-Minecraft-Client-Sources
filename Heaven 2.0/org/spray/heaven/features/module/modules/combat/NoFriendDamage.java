package org.spray.heaven.features.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "NoFriendDamage", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class NoFriendDamage extends Module {

	@EventTarget
	public void onSendPacket(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof CPacketUseEntity) {
				CPacketUseEntity cu = (CPacketUseEntity) event.getPacket();
				Entity target = cu.getEntityFromWorld(mc.world);
				if (target instanceof EntityPlayer && Wrapper.getFriend().contains(target.getName()))
					event.cancel();
			}
		}
	}

}
