package ru.wendoxd.managers;

import ru.wendoxd.command.Command;
import ru.wendoxd.command.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

	private final List<Command> commandList = new ArrayList<>();

	public CommandManager() {
		registerCommand(new FriendCommand(), new MacroCommand(), new HelpCommand(), new VClipCommand(),
				new HClipCommand(), new TPCommand(), new GPSCommand(), new BindCommand(), new ConfigCommand(),
				new PrefixCommand(), new EClipCommand(), new PlayerFinderCommand());
	}

	private void registerCommand(Command... commands) {
		commandList.addAll(Arrays.asList(commands));
	}

	public List<Command> getCommands() {
		return commandList;
	}
}