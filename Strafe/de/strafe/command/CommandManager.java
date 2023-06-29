package de.strafe.command;

import com.google.common.collect.Lists;

import java.util.List;

public class CommandManager {
    public final List<Command> commands = Lists.newArrayList();

    public CommandManager() {
        commands.add(new BindCommand());
    }
    public Command getCommand(String name) {
        return commands.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    public void callCommand(String name) {

    }

}
