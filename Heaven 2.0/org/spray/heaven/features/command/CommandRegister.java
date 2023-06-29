package org.spray.heaven.features.command;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.Arrays;
import java.util.List;

import org.spray.heaven.features.command.commands.*;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.protect.events.ClientInitializeEvent;

public class CommandRegister {
	
	private final List<Command> commands = Arrays.asList(
			new BindCmd(),
			new HelpCmd(),
			new FriendCmd(),
			new MacrosCmd(),
			new VClipCmd(),
			new HClipCmd(),
			new ConfigCmd()
			);
	
	public CommandRegister(ClientInitializeEvent event) {
		event.check();
	}
	
	public List<Command> getCommands() {
		return commands;
	}
	
	public void call(String input) {
		String[] split = input.split(" ", -1);
		String command = split[0];
		String args = input.substring(command.length()).trim();
		for (Command c : getCommands()) {
			if (c.getName().equalsIgnoreCase(command)) {
				try {
					c.command(args.split(" "), input);
				} catch (Exception e) {
					c.error();
					e.printStackTrace();
				}
				return;
			}
		}
		Wrapper.message(ChatFormatting.GRAY + "Command not found. " + ChatFormatting.WHITE + "Please see "
				+ ChatFormatting.LIGHT_PURPLE + Command.PREFIX + "help");
	}

}
