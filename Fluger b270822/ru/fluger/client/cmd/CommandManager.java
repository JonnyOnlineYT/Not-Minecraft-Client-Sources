/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.cmd;

import java.util.ArrayList;
import java.util.List;
import ru.fluger.client.cmd.Command;
import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.cmd.CommandHandler;
import ru.fluger.client.cmd.impl.BindCommand;
import ru.fluger.client.cmd.impl.ClipCommand;
import ru.fluger.client.cmd.impl.ConfigCommand;
import ru.fluger.client.cmd.impl.FriendCommand;
import ru.fluger.client.cmd.impl.GPSCommand;
import ru.fluger.client.cmd.impl.HelpCommand;
import ru.fluger.client.cmd.impl.MacroCommand;
import ru.fluger.client.cmd.impl.PanicCommand;
import ru.fluger.client.event.EventManager;

public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        this.commands.add(new ConfigCommand());
        this.commands.add(new MacroCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new PanicCommand());
        this.commands.add(new ClipCommand());
        this.commands.add(new BindCommand());
        this.commands.add(new GPSCommand());
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public boolean execute(String args) {
        String noPrefix = args.substring(1);
        String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            for (Command command : this.commands) {
                String[] commandAliases;
                CommandAbstract abstractCommand = (CommandAbstract)command;
                for (String alias : commandAliases = abstractCommand.getAliases()) {
                    if (!split[0].equalsIgnoreCase(alias)) continue;
                    abstractCommand.execute(split);
                    return true;
                }
            }
        }
        return false;
    }
}

