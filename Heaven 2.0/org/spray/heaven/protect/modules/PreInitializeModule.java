package org.spray.heaven.protect.modules;

import org.spray.heaven.font.IFont;
import org.spray.heaven.protect.AntiAgent;
import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.util.file.FileManager;
import org.spray.heaven.via.ViaMCP;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class PreInitializeModule extends ProtectModule {

	@EventTarget
	public void onClientInitialize(ClientInitializeEvent event) {
		if (event.getType().equals(EventType.PRE)) {
			FileManager.initFiles();
			IFont.initFonts();
//			SizeChecker.check();
//			AntiAgent.antiArgs();
			ViaMCP.getInstance().start();

			disable();
		}
	}

}
