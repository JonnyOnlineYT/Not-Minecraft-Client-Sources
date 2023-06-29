package org.spray.heaven.protect.modules;

import org.spray.heaven.protect.Crasher;
import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.events.ServerConnectionEvent;

import com.darkmagician6.eventapi.EventTarget;

public class ServerConnectionBlocker extends ProtectModule {
	
	@EventTarget
	public void onConnection(ServerConnectionEvent event) {
		Crasher.crash();
	}

}
