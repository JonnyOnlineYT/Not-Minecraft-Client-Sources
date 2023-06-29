package org.spray.heaven.features.module.modules.misc;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.Timer;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.server.SPacketChat;

@ModuleInfo(name = "AutoTPAccept", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class AutoTPAccept extends Module {

	private final Setting onlyFriends = register(new Setting("Only Friends", false));
	private final Setting delay = register(new Setting("Delay", 200, 0, 1000));

	private final Timer timer = new Timer();

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType() != EventType.RECIEVE)
			return;

		if (event.getPacket() instanceof SPacketChat) {
			SPacketChat packet = (SPacketChat) event.getPacket();
			String msg = packet.getChatComponent().getFormattedText();
			if (msg.contains("телепортироваться")) {
				timer.reset();
				if (onlyFriends.isToggle()) {
					for (String friend : Wrapper.getFriend().getFriends().keySet()) {
						if (msg.contains(friend)) {
							accept();
							break;
						}
					}
					return;
				}
				accept();
			}
		}
	}

	private void accept() {
		if (timer.hasReached(delay.getValue())) {
			mc.player.sendChatMessage("/tpaccept");
		}
	}
}
