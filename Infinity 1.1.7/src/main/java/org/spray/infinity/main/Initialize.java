package org.spray.infinity.main;

import java.io.File;

import org.spray.infinity.features.ModuleManager;
import org.spray.infinity.features.command.CommandManager;
import org.spray.infinity.features.component.cape.Capes;
import org.spray.infinity.features.component.friends.Friend;
import org.spray.infinity.features.component.macro.MacroManager;
import org.spray.infinity.features.module.misc.DiscordRPCMod;
import org.spray.infinity.features.module.visual.HUD;
import org.spray.infinity.ui.account.main.AccountManager;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.file.config.ConfigManager;
import org.spray.infinity.via.ViaFabric;

public abstract class Initialize {

	protected static File direction;

	protected static CommandManager commandManager;
	protected static ConfigManager configManager;
	protected static ModuleManager moduleManager;
	protected static AccountManager accountManager;
	protected static MacroManager macroManager;
	protected static Friend friend;
	protected static Capes cape;

	public Initialize() {
		direction = new File(Helper.MC.runDirectory, "Infinity-1.17");

		if (!direction.exists())
			direction.mkdirs();

		moduleManager = new ModuleManager();
		accountManager = new AccountManager();
		commandManager = new CommandManager();
		configManager = new ConfigManager();
		macroManager = new MacroManager();
		friend = new Friend();
		cape = new Capes();

		new ViaFabric().onInitialize();

		configManager.loadConfigs();
		macroManager.load();
		friend.load();
		accountManager.load();
		cape.initCapes();

		Infinity.getModuleManager().get(DiscordRPCMod.class).enable();
		moduleManager.get(HUD.class).enable();

		start();
	}

	protected abstract void start();

	public abstract void shutdown();

}
