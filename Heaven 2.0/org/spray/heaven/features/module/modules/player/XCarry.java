package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.client.CPacketCloseWindow;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "XCarry", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class XCarry extends Module {

	@Override
	public void onDisable() {
		if (mc.world != null)
			mc.player.connection.sendPacket(new CPacketCloseWindow(mc.player.inventoryContainer.windowId));
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof CPacketCloseWindow
					&& ((CPacketCloseWindow) event.getPacket()).getWindowId() == mc.player.inventoryContainer.windowId)
				event.cancel();

		}
	}

}
