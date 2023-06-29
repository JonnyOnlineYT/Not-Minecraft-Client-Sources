package org.spray.heaven.protect;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.spray.heaven.protect.modules.FirstScreenCheck;
import org.spray.heaven.protect.modules.LoginHandlerModule;
import org.spray.heaven.protect.modules.LoginStartModule;
import org.spray.heaven.protect.modules.OpenScreenBlocker;
import org.spray.heaven.protect.modules.PacketBlockerModule;
import org.spray.heaven.protect.modules.PostInitializeModule;
import org.spray.heaven.protect.modules.PreInitializeModule;
import org.spray.heaven.protect.modules.ReloginProcess;
import org.spray.heaven.protect.modules.ServerConnectionBlocker;
import org.spray.heaven.protect.modules.StartRegisterModule;

import by.radioegor146.nativeobfuscator.Native;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class ProtectRegister {

	private List<ProtectModule> protectList;
	public boolean screenCheck;

	/**
	 * 
	 * Initialized in the LanguageManager.java :D
	 * 
	 */
	public ProtectRegister() {
		protectList = Arrays.asList(new OpenScreenBlocker().enable(), new StartRegisterModule().enable(),
				new LoginStartModule().enable(), new LoginHandlerModule().enable(), new PreInitializeModule().enable(),
				new PostInitializeModule().enable(), new ServerConnectionBlocker().enable(),
				new PacketBlockerModule().enable());

		new ReloginProcess().enable();
		new FirstScreenCheck().enable();
	}

	public ProtectModule get(Class<?> clas) {
		Iterator<ProtectModule> iteratorMod = protectList.iterator();
		ProtectModule module;
		do {
			if (!iteratorMod.hasNext()) {
				return null;
			}
			module = (ProtectModule) iteratorMod.next();
		} while (module.getClass() != clas);
		return module;
	}

	public List<ProtectModule> getProtectList() {
		return protectList;
	}

	public void disableList() {
		getProtectList().forEach(protectModule -> protectModule.disable());
	}

	public void enableList() {
		getProtectList().forEach(protectModule -> protectModule.enable());
	}

}
