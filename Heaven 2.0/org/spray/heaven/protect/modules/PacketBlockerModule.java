package org.spray.heaven.protect.modules;

import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.protect.ProtectModule;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

public class PacketBlockerModule extends ProtectModule {

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.SEND))
			event.cancel();
		if (event.getType().equals(EventType.RECIEVE))
			event.cancel();
	}

}
