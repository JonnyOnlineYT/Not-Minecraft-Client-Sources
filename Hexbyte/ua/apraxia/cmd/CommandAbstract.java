package ua.apraxia.cmd;


import ua.apraxia.utility.other.ChatUtility;
import ua.apraxia.utility.Utility;


public abstract class CommandAbstract implements Command, Utility {


    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public CommandAbstract(String name, String description, String usage, String ... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public void usage() {
        ChatUtility.addChatMessage("§cInvalid usage, try: " + this.usage + " or .help");
    }

    public String getUsage() {
        return this.usage;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}

