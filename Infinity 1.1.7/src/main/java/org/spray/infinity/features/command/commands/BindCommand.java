package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.main.Infinity;

import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;

@CommandInfo(name = "bind", desc = "Bind a module")
public class BindCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		org.spray.infinity.features.Module module = Infinity.getModuleManager().getByName(args[1]);
		if (args[0].equalsIgnoreCase("add")) {
			module.setKey(InputUtil.fromTranslationKey("key.keyboard." + args[2].toLowerCase()).getCode());
			send(Formatting.GRAY + "[Feature] " + Formatting.WHITE + module.getName() + Formatting.GRAY + " binded to "
					+ Formatting.AQUA + args[2]);
		} else if (args[0].equalsIgnoreCase("del")) {
			module.setKey(-2);
			send(Formatting.GRAY + "[Feature] " + Formatting.WHITE + module.getName() + Formatting.GRAY + " removed binds");
		} else if (args[0].equalsIgnoreCase("clear")) {
			for (org.spray.infinity.features.Module m : Infinity.getModuleManager().getModules())
				m.setKey(-2);

			send(Formatting.GRAY + "Binds cleared");
		}
	}

	@Override
	public void error() {
		send(Formatting.GRAY + "Please use" + Formatting.WHITE + ":");
		send(Formatting.WHITE + prefix + "bind add " + Formatting.AQUA + "module" + Formatting.GRAY + "<"
				+ Formatting.AQUA + "key" + Formatting.GRAY + ">");
		send(Formatting.WHITE + prefix + "bind del " + Formatting.AQUA + "module");
		send(Formatting.WHITE + prefix + "bind clear");
	}

}
