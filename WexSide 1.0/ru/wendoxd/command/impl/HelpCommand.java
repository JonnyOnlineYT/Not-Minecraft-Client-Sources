package ru.wendoxd.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import ru.wendoxd.WexSide;
import ru.wendoxd.command.Command;
import ru.wendoxd.command.CommandInfo;

@CommandInfo(name = "help", desc = "w")
public class HelpCommand extends Command {

	@Override
	public void execute(String[] args) throws Exception {
		for (Command command : WexSide.commandManager.getCommands()) {
			if (!(command instanceof HelpCommand)) {
				sendMessage(ChatFormatting.WHITE + PrefixCommand.prefix +  command.name + ChatFormatting.GRAY + " ("
						+ ChatFormatting.WHITE + command.desc + ChatFormatting.GRAY + ")");
			}
		}
	}

	@Override
	public void error() {
		sendMessage(ChatFormatting.RED + "WTF");
	}
}
