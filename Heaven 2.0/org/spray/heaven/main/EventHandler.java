package org.spray.heaven.main;

import org.spray.heaven.events.HudRenderEvent;
import org.spray.heaven.events.KeyEvent;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.events.UpdateEvent;
import org.spray.heaven.features.command.Command;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.client.CPacketChatMessage;

public class EventHandler {
	
	public EventHandler() {
		Wrapper.getModule().get("DiscordRPC").enable();
		Wrapper.getModule().get("Arraylist").enable();
		Wrapper.getModule().get("Watermark").enable();
		Wrapper.getConfig().loadDefault();
	}

	@EventTarget
	public void onRender(HudRenderEvent event) {
		if (!Wrapper.MC.gameSettings.showDebugInfo) {
			Wrapper.getModule().onRender(event.getWidth(), event.getHeight(), event.getTickDelta());
			
			Wrapper.getNotification().render(event.getHeight());
		}
	}

	@EventTarget
	public void onKey(KeyEvent event) {
		Wrapper.getModule().onKeyPressed(event.getKey());
		Wrapper.getMacros().onKeyPressed(event.getKey());
	}

	@EventTarget
	public void onUpdate(UpdateEvent event) {
		Wrapper.getModule().onUpdate();
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (!event.getType().equals(EventType.SEND))
			return;

		if (event.getPacket() instanceof CPacketChatMessage) {
			CPacketChatMessage cm = (CPacketChatMessage) event.getPacket();

			if (cm.getMessage().startsWith(Command.PREFIX)) {
				Wrapper.getCommand().call(cm.getMessage().substring(Command.PREFIX.length()));
				event.cancel();
			}
		}
	}

}
