package org.spray.heaven.protect.modules;

import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.Provider;
import org.spray.heaven.protect.events.LoginActionEvent;
import org.spray.heaven.protect.events.LoginHandlerEvent;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

public class LoginStartModule extends ProtectModule {

	@EventTarget
	public void onLoginAttempt(LoginActionEvent event) {
		if (Provider.getKeyAuth().getApi().getUserData() != null
				&& Provider.getKeyAuth().getApi().getSessionId() != null
				&& !Provider.getKeyAuth().getApi().getSessionId().isEmpty()) {
			EventManager.call(new LoginHandlerEvent());
		} else
			throw new NullPointerException();

	}

}
