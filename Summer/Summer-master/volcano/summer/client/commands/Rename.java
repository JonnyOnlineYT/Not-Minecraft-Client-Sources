package volcano.summer.client.commands;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.base.manager.module.Module;

public class Rename extends Command {

	public Rename() {
		super("Rename", "<module> <newname>");
	}

	@Override
	public void run(String message) {
		String modName = "";
		String newName = "";
		if (message.split(" ").length > 1) {
			modName = message.split(" ")[1];
			if (message.split(" ").length > 2) {
				newName = message.split(" ")[2];
			}
		}
		Module module = Summer.moduleManager.getModuleByString(modName);
		if (module.name.equalsIgnoreCase("null")) {
			Summer.tellPlayer("Module Wasn't Found.");
			return;
		}
		if (newName == "") {
			Summer.tellPlayer("The Module Name " + String.valueOf(module.name) + " ? Has Been Reset.");
			module.name = module.realName;
			return;
		}
		module.name = newName;
		Summer.tellPlayer(String.valueOf(module.name) + " was renamed to " + newName);
	}
}