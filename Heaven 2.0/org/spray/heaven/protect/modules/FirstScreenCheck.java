package org.spray.heaven.protect.modules;

import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.Provider;
import org.spray.heaven.protect.events.OpenScreenEvent;
import org.spray.heaven.protect.ui.AuthUI;
import org.spray.heaven.protect.ui.LoadingUI;
import org.spray.keyauth.user.UserData.UserStatus;
import org.spray.keyauth.util.ShutDown;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.GuiMainMenu;

public class FirstScreenCheck extends ProtectModule {

	@EventTarget
	public void onOpenScreen(OpenScreenEvent event) {
		if (event.getScreen() instanceof AuthUI || event.getScreen() instanceof LoadingUI
				|| event.getScreen() instanceof GuiMainMenu)
			return;

		if (Provider.getKeyAuth().getApi().getUserData().getStatus().equals(UserStatus.SUCCESS)
				&& Provider.getKeyAuth().getApi().getUserData().getUsername() != null
				&& Provider.getChecker().getResult().get().equalsIgnoreCase("true")) {
			disable();
		} else {
			ShutDown.run();
			Provider.getProtectRegister().enableList();
		}
	}

}
