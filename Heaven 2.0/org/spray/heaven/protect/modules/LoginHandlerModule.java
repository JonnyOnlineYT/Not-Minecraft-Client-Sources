package org.spray.heaven.protect.modules;

import org.spray.heaven.main.Wrapper;
import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.Provider;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.protect.events.LoginHandlerEvent;
import org.spray.keyauth.user.UserData;
import org.spray.keyauth.user.UserData.UserStatus;
import org.spray.keyauth.util.ShutDown;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

public class LoginHandlerModule extends ProtectModule {

	@EventTarget
	public void onLogin(LoginHandlerEvent event) {
		UserData user = Provider.getKeyAuth().getApi().getUserData();
		if (user.getExpiry() == null || user.getSubscription() == null || user.getUsername() == null
				|| user.getKey() == null || user.getKey().isEmpty())
			Wrapper.MC.displayCrashReport(null);
		
		if (!user.getStatus().equals(UserStatus.SUCCESS))
			ShutDown.run();

		if (!Provider.getKeyAuth().getApi().isInitialized())
			Wrapper.MC.displayCrashReport(null);

		if (Provider.getKeyAuth().getApi().license(user.getKey()) != null)
			EventManager.call(new ClientInitializeEvent(EventType.POST));
	}

}
