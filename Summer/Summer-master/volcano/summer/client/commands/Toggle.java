package volcano.summer.client.commands;

import com.mojang.realmsclient.gui.ChatFormatting;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.base.manager.module.Module;

public class Toggle extends Command {

	public Toggle() {
		super("t", "<Module>");
	}

	@Override
	public void run(final String message) {
		String modName = "";
		if (message.length() > 1) {
			modName = message.split(" ")[1];
		}
		Module module = Summer.moduleManager.getModuleByString(modName);
		if (module.name.equalsIgnoreCase("null")) {
			Summer.tellPlayer("Invalid Module.");
			return;
		}
		module.toggleMod();
		Summer.tellPlayer(module.name + " Has Been: "
				+ (module.state ? ChatFormatting.GREEN + "Enabled" : ChatFormatting.RED + "Disabled"));
	}
}
