package org.spray.heaven.protect.modules;

import org.spray.heaven.events.TickEvent;
import org.spray.heaven.main.EventHandler;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.protect.AntiAgent;
import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.Provider;
import org.spray.keyauth.user.UserData;
import org.spray.keyauth.user.UserData.UserStatus;
import org.spray.keyauth.util.ShutDown;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class ReloginProcess extends ProtectModule {

	@EventTarget
	public void onTick(TickEvent event) {
		if (Wrapper.getWorld() == null)
			return;

		if (Provider.getKeyAuth().relogincheck || !Provider.getChecker().getResult().get().equalsIgnoreCase("true")) {
			ShutDown.run();
			Wrapper.MC.displayCrashReport(null);
			Provider.getProtectRegister().enableList();
			return;
		}
		if (Provider.getKeyAuth().relogin) {
			UserData user = Provider.getKeyAuth().getApi().getUserData();
			String key = user.getKey();

//			AntiAgent.antiArgs();

			if (Provider.getKeyAuth().getApi().license(key) != null && user.getStatus().equals(UserStatus.SUCCESS)) {
				Provider.getKeyAuth().relogin = false;
				EventManager.register(new EventHandler());
//				SizeChecker.check();
				disable();
			} else {
				ShutDown.run();
				Wrapper.MC.displayCrashReport(null);
				Provider.getProtectRegister().enableList();
				Provider.getKeyAuth().relogincheck = true;
			}
		}
	}

}
