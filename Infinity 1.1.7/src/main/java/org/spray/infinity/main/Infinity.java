package org.spray.infinity.main;

import java.io.File;

import org.spray.infinity.features.ModuleManager;
import org.spray.infinity.features.command.CommandManager;
import org.spray.infinity.features.component.cape.Capes;
import org.spray.infinity.features.component.friends.Friend;
import org.spray.infinity.features.component.macro.MacroManager;
import org.spray.infinity.ui.account.main.AccountManager;
import org.spray.infinity.ui.menu.ClickMenu;
import org.spray.infinity.utils.file.ClientSettings;
import org.spray.infinity.utils.file.config.ConfigManager;

public class Infinity extends Initialize {
	
	public static Infinity INSTANCE;

	public static final String NAME = "Infinity";
	public static final String VERSION = "1.0.7";

	private ClientSettings SETTINGS;

	public static ClickMenu MENU;

	/* Minecraft timer */
	public static float TIMER = 1.0f;

	@Override
	protected void start() {
		MENU = new ClickMenu();
		SETTINGS = new ClientSettings();
		SETTINGS.load();
	}

	@Override
	public void shutdown() {
		SETTINGS.save();
	}

	public static File getDirection() {
		return direction;
	}

	public static ModuleManager getModuleManager() {
		return moduleManager;
	}

	public static ConfigManager getConfigManager() {
		return configManager;
	}

	public static AccountManager getAccountManager() {
		return accountManager;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static MacroManager getMacroManager() {
		return macroManager;
	}

	public static Friend getFriend() {
		return friend;
	}

	public static Capes getCape() {
		return cape;
	}

	public static void resetTimer() {
		TIMER = 1.0f;
	}
}