package org.moonware.client.cmd;

import org.moonware.client.cmd.impl.*;
import org.moonware.client.cmd.impl.SunClip.SunriseClipCommand;
import org.moonware.client.event.EventManager;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        commands.add(new FakeHackCommand());
        commands.add(new BindCommand());
        commands.add(new ClipCommand());
        commands.add(new AesCommand());
        commands.add(new FriendCommand());
        commands.add(new MacroCommand());
        commands.add(new ConfigCommand());
        commands.add(new HelpCommand());
        commands.add(new XrayCommand());
        commands.add(new BrushCommand());
        commands.add(new GPSCommand());
        commands.add(new SunriseClipCommand());
    }

    public List<Command> getCommands() {
        return commands;
    }

    public boolean execute(String args) {
        String noPrefix = args.substring(1);
        String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            for (Command command : commands) {
                CommandAbstract abstractCommand = (CommandAbstract) command;
                String[] commandAliases = abstractCommand.getAliases();
                for (String alias : commandAliases) {
                    if (split[0].equalsIgnoreCase(alias)) {
                        abstractCommand.execute(split);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}