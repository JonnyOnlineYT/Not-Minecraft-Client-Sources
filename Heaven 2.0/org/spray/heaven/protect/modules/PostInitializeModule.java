package org.spray.heaven.protect.modules;

import org.spray.heaven.features.command.CommandRegister;
import org.spray.heaven.features.misc.FriendManager;
import org.spray.heaven.features.misc.MacrosManager;
import org.spray.heaven.features.module.ModuleRegister;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.NotificationManager;
import org.spray.heaven.protect.Crasher;
import org.spray.heaven.protect.ProtectModule;
import org.spray.heaven.protect.Provider;
import org.spray.heaven.protect.events.ClientInitializeEvent;
import org.spray.heaven.protect.ui.LoadingUI;
import org.spray.heaven.ui.clickui.ClickUI;
import org.spray.heaven.ui.draggable.DragManager;
import org.spray.heaven.ui.menu.MenuScreen;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.file.ClientConfig;
import org.spray.heaven.util.file.config.ConfigRegister;
import org.spray.keyauth.util.ShutDown;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import by.radioegor146.nativeobfuscator.Native;
import net.minecraft.client.gui.GuiMainMenu;

@Native
public class PostInitializeModule extends ProtectModule {

	@EventTarget
	public void onClientInitialize(ClientInitializeEvent event) {
		if (event.getType().equals(EventType.POST)) {
			if (!Provider.getKeyAuth().getApi().getUserData().getUsername()
					.equalsIgnoreCase(Provider.getKeyAuth().getApi().getUserData().getKey())
					|| Provider.getKeyAuth().getApi().getUserData().getKey() == null
					|| !Provider.getKeyAuth().relogincheck) {
				Crasher.crash();
				return;
			}

			if (Boolean.parseBoolean(Provider.getChecker().getResult().get()) != true)
				ShutDown.run();

			Provider.getKeyAuth().relogincheck = false;

			try {
				Wrapper.setModule(new ModuleRegister(event));
			} catch (Exception ignored) {
			}

//			AntiAgent.antiArgs();

			Wrapper.setCommand(new CommandRegister(event));
			Wrapper.setConfig(new ConfigRegister(event));
			Wrapper.setFriend(new FriendManager(event));
			Wrapper.setMacros(new MacrosManager(event));
			Wrapper.setNotification(new NotificationManager(event));

			DragManager.loadDragData();
			Wrapper.getMacros().load();
			Wrapper.getFriend().load();
			ClientConfig.load();
			Provider.getProtectRegister().disableList();

			Wrapper.UI = new ClickUI(event);
			Wrapper.MENU = new MenuScreen();

			Provider.LOGGED = true;

			Wrapper.MC.displayGuiScreen(new LoadingUI(new GuiMainMenu(), (int) MathUtil.random(100, 150)));
		}
	}
}
