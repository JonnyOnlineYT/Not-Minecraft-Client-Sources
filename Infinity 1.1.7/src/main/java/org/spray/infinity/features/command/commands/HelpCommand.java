package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.main.Infinity;

import net.minecraft.util.Formatting;

@CommandInfo(name = "help", desc = "See commands")
public class HelpCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		for (Command commands : Infinity.getCommandManager().getCommands()) {
			send(commands.getName() + " - " + Formatting.GRAY + commands.getDesc());
		}
	}

	@Override
	public void error() {
		send("Please use: " + Formatting.AQUA + prefix + "help");
	}

}
