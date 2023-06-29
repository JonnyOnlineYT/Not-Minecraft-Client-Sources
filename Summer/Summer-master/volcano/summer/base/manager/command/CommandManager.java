package volcano.summer.base.manager.command;

import java.util.ArrayList;

import volcano.summer.client.commands.Bind;
import volcano.summer.client.commands.Config;
import volcano.summer.client.commands.Connect;
import volcano.summer.client.commands.Damage;
import volcano.summer.client.commands.Friend;
import volcano.summer.client.commands.Hclip;
import volcano.summer.client.commands.Help;
import volcano.summer.client.commands.Prefix;
import volcano.summer.client.commands.Rename;
import volcano.summer.client.commands.SS;
import volcano.summer.client.commands.Settings;
import volcano.summer.client.commands.Toggle;
import volcano.summer.client.commands.Vclip;

public class CommandManager {

	public static ArrayList<Command> commands = new ArrayList<Command>();
	public static String prefix = ".";

	public static void add(Command cmd) {
		CommandManager.commands.add(cmd);
	}

	public Command getCommandByClass(Class command) {
		for (Command command2 : commands)
			if (command2.getClass().equals(command))
				return command2;
		return null;
	}

	public void loadCommands() {
		try {
			commands.add(new Toggle());
			commands.add(new Bind());
			commands.add(new Damage());
			commands.add(new Friend());
			commands.add(new Help());
			commands.add(new Prefix());
			commands.add(new Vclip());
			commands.add(new Connect());
			commands.add(new Hclip());
			commands.add(new Rename());
			// commands.add(new Info());
			commands.add(new SS());
			commands.add(new Settings());
			commands.add(new Config());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setPrefix(String prefix) {
		CommandManager.prefix = prefix;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

}
