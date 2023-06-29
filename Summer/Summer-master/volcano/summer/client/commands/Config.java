package volcano.summer.client.commands;

import java.util.concurrent.CopyOnWriteArrayList;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;

public class Config extends Command {

	public Config() {
		super("Config", "<list/save/load/remove> <name>");
	}

	@Override
	public void run(String message) {
		if (message.split(" ").length == 2) {
			if (message.split(" ")[1].equalsIgnoreCase("list")) {
				CopyOnWriteArrayList<volcano.summer.base.manager.config.Config> configs = Summer.configManager.getConfigs();
				if (configs.size() == 0) {
					Summer.tellPlayer("There are currently no configs.");
				} else {
					Summer.tellPlayer("Here are the list of Configs:");
					for (volcano.summer.base.manager.config.Config c : configs) {
						Summer.tellPlayer(c.getName());
					}
				}
			}
		} else if (message.split(" ").length == 3) {
			String c = message.split(" ")[1];
			String s = message.split(" ")[2];
			if (c.equalsIgnoreCase("save")) {
				Summer.configManager.addConfig(s.toLowerCase());
			} else if (c.equalsIgnoreCase("remove")) {
				Summer.configManager.removeConfig(s.toLowerCase());
				Summer.tellPlayer("Config '" + s.toLowerCase() + "' removed.");
			} else if (c.equalsIgnoreCase("load")) {
				volcano.summer.base.manager.config.Config config = Summer.configManager.getConfig(s.toLowerCase());
				if (config != null) {
					config.load();
				} else {
					Summer.tellPlayer("Config does not exist.");
				}
			}
			Summer.configManager.saveConfigs();
		} else {
			Summer.tellPlayer("Incorrect Syntax! Config <list/save/load/remove> <name>");
		}
	}
}
