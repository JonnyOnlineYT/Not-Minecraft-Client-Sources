package org.spray.heaven.protect.modules;

import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.events.OpenScreenEvent;
import org.spray.heaven.protect.ui.AuthUI;
import org.spray.heaven.protect.ui.LoadingUI;

import com.darkmagician6.eventapi.EventTarget;

public class OpenScreenBlocker extends ProtectModule {

	@EventTarget
	public void onOpenScreen(OpenScreenEvent event) {
		if (event.getScreen() instanceof AuthUI || event.getScreen() instanceof LoadingUI)
			return;

		event.cancel();
	}

}
