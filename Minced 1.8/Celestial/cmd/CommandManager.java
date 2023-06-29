/*
 * Decompiled with CFR 0.150.
 */
package Celestial.cmd;


import Celestial.cmd.impl.*;

import Celestial.event.EventManager;

import java.util.ArrayList;
import java.util.List;




public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        this.commands.add(new ConfigCommand());
        this.commands.add(new MacroCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new FakeNameCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ParseCommand());
        this.commands.add(new PanicCommand());
        this.commands.add(new GPSCommand());
        this.commands.add(new ClipCommand());
        this.commands.add(new BindCommand());
        this.commands.add(new eclipCommand());
        this.commands.add(new TpCommand());
        this.commands.add(new TPaCommand());
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
                String[] arrstring = commandAliases = abstractCommand.getAliases();
                int n = commandAliases.length;
                for (int i = 0; i < n; ++i) {
                    String alias = arrstring[i];
                    if (!split[0].equalsIgnoreCase(alias)) continue;
                    abstractCommand.execute(split);
                    return true;
                }
            }
        }
        return false;
    }
}

