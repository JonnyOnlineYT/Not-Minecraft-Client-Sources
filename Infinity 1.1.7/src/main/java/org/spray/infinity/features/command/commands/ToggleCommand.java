package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import net.minecraft.util.Formatting;

@CommandInfo(name = "toggle", desc = "enable / disable module")
public class ToggleCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		org.spray.infinity.features.Module module = Infinity.getModuleManager().getByName(args[0]);
		
		if (module.getCategory() != Category.HIDDEN)
		module.enable();
		
		if (module.isEnabled()) {
			Helper.message(Formatting.AQUA + module.getName() + Formatting.WHITE + " module -" + Formatting.GREEN + " enabled");
		} else {
			Helper.message(Formatting.AQUA + module.getName() + Formatting.WHITE + " module -" + Formatting.RED + " disabled");
		}
	}

	@Override
	public void error() {
		send(Formatting.GRAY + "Please use" + Formatting.WHITE + ":");
		send(Formatting.WHITE + prefix + "toggle " + Formatting.GRAY + "<" + Formatting.AQUA + "module" + Formatting.GRAY
				+ ">");

	}

}
