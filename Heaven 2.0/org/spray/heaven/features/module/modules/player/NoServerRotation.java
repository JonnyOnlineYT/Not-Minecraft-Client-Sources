package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "NoServerRotation", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class NoServerRotation extends Module {

	@EventTarget
	public void onReceivePacket(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE)) {
			if (event.getPacket() instanceof SPacketPlayerPosLook) {
				SPacketPlayerPosLook sp = (SPacketPlayerPosLook) event.getPacket();
				sp.setYaw(mc.player.rotationYaw);
				sp.setPitch(mc.player.rotationPitch);
			}
		}
	}
	
}
